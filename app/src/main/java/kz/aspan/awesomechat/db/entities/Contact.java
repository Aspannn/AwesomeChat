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

    public Contact(@NonNull String caption, @NonNull String phone) {
        this.caption = caption;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getCaption() {
        return caption;
    }

    public void setCaption(@NonNull String caption) {
        this.caption = caption;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }
}
