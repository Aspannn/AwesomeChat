package kz.aspan.awesomechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import kz.aspan.awesomechat.db.AppDb;
import kz.aspan.awesomechat.db.dao.ContactsDao;
import kz.aspan.awesomechat.db.dao.MessagesDao;
import kz.aspan.awesomechat.db.entities.Message;

public class MainActivity extends AppCompatActivity implements ChatManager {

    private String sender;

    private FirebaseDatabase firebaseDb;
    private DatabaseReference messageRef;

    private MessageListener messageListener;

    private MessagesDao messagesDao;
    //test
    private ContactsDao contactsDao;

    private NotificationManager notyManager;
    private Notification.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNotyManager();

        sender = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        initFirebase();

        ActiveChatsFragment fragment = new ActiveChatsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        messagesDao = AppDb.getInstance(this).getMessagesDao();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String phone = intent.getStringExtra("chat");


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

                messagesDao.insert(message);
                ds.getRef().setValue(null);


                if (messageListener != null) {
                    messageListener.onMessageReceived(message);
                } else {
                    //show noty
                    builder.setContentTitle(message.getSender());
                    builder.setContentText(message.getContent());

                    Notification noty = builder.build();
                    notyManager.notify(1, noty);
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

        message.setTimesTamp(timesTamp);
        messageRef.child(message.getRecipient()).push().setValue(message, timesTamp);

        messageListener.onMessageReceived(message);
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    private void initNotyManager() {
        notyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "1234", "Chat",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notyManager.createNotificationChannel(channel);
            builder = new Notification.Builder(MainActivity.this, "1234");

        } else {
            builder = new Notification.Builder(MainActivity.this);
        }
        builder.setSmallIcon(R.drawable.ic_message);


    }
}
