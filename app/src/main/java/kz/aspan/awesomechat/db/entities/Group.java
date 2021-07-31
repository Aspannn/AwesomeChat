package kz.aspan.awesomechat.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "groups")
public class Group {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @TypeConverters({ContactsConverter.class})
    private ArrayList<Contact> contacts;

    private String groupName;


    public Group() {
    }

    public Group(ArrayList<Contact> contacts, String groupName) {
        this.contacts = contacts;
        this.groupName = groupName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
