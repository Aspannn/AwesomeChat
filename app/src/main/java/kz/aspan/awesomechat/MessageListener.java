package kz.aspan.awesomechat;

import kz.aspan.awesomechat.db.entities.Message;

public interface MessageListener {
    void onMessageReceived(Message message);

//    //test
//    void onMessageReceived(SenderMessage message);
}
