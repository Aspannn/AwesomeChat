package kz.aspan.awesomechat.broadcasts;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import kz.aspan.awesomechat.BuildConfig;
import kz.aspan.awesomechat.ChatManager;
import kz.aspan.awesomechat.MainActivity;
import kz.aspan.awesomechat.db.AppDb;
import kz.aspan.awesomechat.db.entities.Message;

public class MessageReceiver extends BroadcastReceiver {

    public static final String BROADCAST_MARK_AS_READ = BuildConfig.APPLICATION_ID + ".mark_as_read";
    public static final String BROADCAST_REPLY = BuildConfig.APPLICATION_ID + ".reply";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive " + intent);

        NotificationManager notyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String action = intent.getAction();
        if (action != null) {

            Bundle bundle = intent.getExtras();
            int chatID = bundle.getInt(ChatManager.KEY_CHAT_ID);
            long messageId = bundle.getLong(ChatManager.KEY_MESSAGE_ID);

            switch (action) {
                case BROADCAST_MARK_AS_READ:
                    System.out.println("sssssssssssssssss");
                    //todo
                    break;

                case BROADCAST_REPLY:

                    System.out.println("hi");

                    Bundle remoteBundle = RemoteInput.getResultsFromIntent(intent);
                    String replyText = remoteBundle.getString(ChatManager.KEY_TEXT_REPLY);

                    ChatManager chatManager = MainActivity.getChatManager();
                    Message message = AppDb.getInstance(context).getMessagesDao().getById(messageId);

                    Message newMessage = new Message(
                            message.getRecipient(),
                            message.getSender(),
                            replyText
                    );

                    System.out.println(newMessage.getContent());
                    chatManager.sendMessage(newMessage);
                    notyManager.cancel(chatID);
                    break;
            }
        }
    }
}
