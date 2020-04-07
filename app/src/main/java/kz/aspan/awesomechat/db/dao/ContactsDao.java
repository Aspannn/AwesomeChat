package kz.aspan.awesomechat.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kz.aspan.awesomechat.db.entities.Contact;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM contacts")
    List<Contact> getAll();

    @Insert
    long insert(Contact contact);
}
