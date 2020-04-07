package kz.aspan.awesomechat;

import kz.aspan.awesomechat.db.entities.Message;

public interface ChatManager {

    void sendMessage(Message message);
}
