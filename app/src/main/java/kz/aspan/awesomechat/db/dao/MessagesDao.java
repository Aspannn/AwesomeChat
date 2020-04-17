package kz.aspan.awesomechat.db.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kz.aspan.awesomechat.db.entities.Message;

@Dao
public interface MessagesDao {
    @Insert
    long insert(Message message);

    @Query("SELECT * FROM messages WHERE (sender = :sender AND recipient = :recipient) OR" +
            "(sender = :recipient AND recipient = :sender)")
    List<Message> getAll(String sender, String recipient);

    @Query("SELECT * FROM messages WHERE id = :id")
    Message getById(long id);
}
