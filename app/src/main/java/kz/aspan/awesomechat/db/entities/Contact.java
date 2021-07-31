package kz.aspan.awesomechat.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts", indices = {@Index(value = "phone", unique = true)})
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String caption;

    @NonNull
    private String phone;

    public Contact() {
    }

    public Contact(String caption, String phone) {
        this.caption = caption;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(@NonNull String caption) {
        this.caption = caption;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return caption + ',' + phone;
    }
}
