package kz.aspan.awesomechat.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

import kz.aspan.awesomechat.db.entities.ContactsConverter;
import kz.aspan.awesomechat.db.entities.Group;

@Dao
@TypeConverters({ContactsConverter.class})
public interface GroupDao {

    @Query("SELECT * FROM groups")
    List<Group> getAll();

    @Insert
    long insert(Group group);
}
