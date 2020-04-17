package kz.aspan.awesomechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kz.aspan.awesomechat.auth.AuthActivity;
import kz.aspan.awesomechat.broadcasts.MessageReceiver;
import kz.aspan.awesomechat.db.AppDb;
import kz.aspan.awesomechat.db.dao.ContactsDao;
import kz.aspan.awesomechat.db.dao.MessagesDao;
import kz.aspan.awesomechat.db.entities.Contact;
import kz.aspan.awesomechat.db.entities.Message;

public class MainActivity extends AppCompatActivity implements ChatManager {

    private String sender;

    private FirebaseDatabase firebaseDb;
    private DatabaseReference messageRef;

    private MessageListener messageListener;

    private MessagesDao messagesDao;
    private ContactsDao contactsDao;

    private NotificationManager notyManager;

    private static ChatManager chatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        sender = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        initFirebase();

//        ActiveChatsFragment fragment = new ActiveChatsFragment();
        MainFragment fragment = new MainFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        messagesDao = AppDb.getInstance(this).getMessagesDao();
        contactsDao = AppDb.getInstance(this).getContactDao();

        chatManager = this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String phone = intent.getStringExtra(KEY_PHONE);
        String name = intent.getStringExtra(KEY_NAME);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChatFragment(phone, name))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof ChatFragment) {
            messageListener = (MessageListener) fragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                return true;

            //uakytsha test ushin
            case R.id.send:
                long timesTamp = System.currentTimeMillis();

                Message message = new Message("+77074807466", sender, "Hello World");
                message.setTimesTamp(timesTamp);

                messageRef.child(message.getRecipient()).push().setValue(message, timesTamp);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFirebase() {
        firebaseDb = FirebaseDatabase.getInstance();
        messageRef = firebaseDb.getReference("message");

        messageRef.child(sender).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String previousId) {
                Log.v("Firebase", "Added: " + ds.getKey() + " => " + ds.getValue() + "; " + previousId);
                Message message = ds.getValue(Message.class);
                message.setExternal(true);

                long newId = messagesDao.insert(message);
                ds.getRef().setValue(null);
                message.setId(newId);

                Contact contact = contactsDao.getByPhone(message.getSender());
                message.setSenderName(contact.getCaption());


                if (messageListener != null) {
                    messageListener.onMessageReceived(message);
                } else {
                    showIncomingMessageNoty(contact, message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot ds, @Nullable String previousId) {
                Log.v("Firebase", "Changed: " + ds.getKey() + " => " + ds.getValue() + "; " + previousId);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot ds) {
                Log.v("Firebase", "Removed: " + ds.getKey() + " => " + ds.getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot ds, @Nullable String previousId) {
                Log.v("Firebase", "Moved: " + ds.getKey() + " => " + ds.getValue() + "; " + previousId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError ds) {
                Log.v("Firebase", "Cancelled: " + ds.getCode() + " => " + ds.getMessage());
            }
        });
    }

    @Override
    public void sendMessage(Message message) {
        long timesTamp = System.currentTimeMillis();

        messagesDao.insert(message);

        messageRef.child(message.getRecipient()).push().setValue(message, timesTamp);

        if (messageListener != null) {
            messageListener.onMessageReceived(message);
        }
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void showIncomingMessageNoty(Contact sender, Message message) {
        int chatId = (int) sender.getId();

        //show notification
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(KEY_CHAT_ID, chatId);

        PendingIntent pendingIntent = createBroadcastIntent(chatId, intent);

        Notification.Builder builder = createNotificationBuilder()
                .setContentTitle(sender.getCaption())
                .setContentText(message.getContent())
                .setContentIntent(pendingIntent)
                .setLargeIcon(getAvatar())
                .addAction(createMarkAsReadAction(chatId, message.getId()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.addAction(createReplyToAction(chatId, message.getId()));
        }

        Notification noty = builder.build();
        notyManager.notify(chatId, noty);

    }

    public static ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public void startChat(String name, String phone) {
        Contact contact = new Contact(name, phone);
        contactsDao.insert(contact);
        replaceFragment(new ChatFragment(phone, name), false);

    }

    public void replaceFragment(Fragment fragment, boolean addToStack) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .replace(R.id.container, fragment);
        if (addToStack) {
            trx.addToBackStack(null);
        }

        trx.commit();
    }

    private PendingIntent createBroadcastIntent(int chatId, Intent intent) {
        return PendingIntent.getBroadcast(
                getApplicationContext(),
                chatId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification.Action createMarkAsReadAction(int chatId, long messageId) {
        Intent intent = new Intent(getApplicationContext(), MessageReceiver.class);
        intent.setAction(MessageReceiver.BROADCAST_MARK_AS_READ);
        intent.putExtra(KEY_CHAT_ID, chatId);
        intent.putExtra(KEY_MESSAGE_ID, messageId);

        PendingIntent pendingIntent = createBroadcastIntent(chatId, intent);

        return new Notification.Action.Builder(R.drawable.ic_message,
                getString(R.string.action_mark_read), pendingIntent).build();
    }

    private Notification.Action createReplyToAction(int chatId, long messageId) {
        Intent intent = new Intent(getApplicationContext(), MessageReceiver.class);
        intent.setAction(MessageReceiver.BROADCAST_REPLY);
        intent.putExtra(KEY_CHAT_ID, chatId);
        intent.putExtra(KEY_MESSAGE_ID, messageId);

        PendingIntent pendingIntent = createBroadcastIntent(chatId, intent);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(getString(R.string.action_reply))
                .build();

        return new Notification.Action.Builder(R.drawable.ic_message,
                getString(R.string.action_reply), pendingIntent)
                .addRemoteInput(remoteInput)
                .build();
    }

    private Notification.Builder createNotificationBuilder() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    BuildConfig.APPLICATION_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notyManager.createNotificationChannel(channel);
            builder = new Notification.Builder(MainActivity.this, BuildConfig.APPLICATION_ID);
        } else {
            builder = new Notification.Builder(MainActivity.this);
        }

        builder.setSmallIcon(R.drawable.ic_message);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(true);

        return builder;
    }

    private Bitmap getAvatar() {
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_account_circle);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
