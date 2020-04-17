package kz.aspan.awesomechat;

import kz.aspan.awesomechat.db.entities.Message;

public interface ChatManager {


    String KEY_CHAT_ID = "chat_id";
    String KEY_PHONE = "phone";
    String KEY_NAME = "name";
    String KEY_MESSAGE_ID = "message_id";
    String KEY_TEXT_REPLY = "key_text_reply";

    void sendMessage(Message message);

    void startChat(String name, String phone);
}
