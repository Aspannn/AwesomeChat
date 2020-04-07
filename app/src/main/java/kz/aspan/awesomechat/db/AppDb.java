package kz.aspan.awesomechat.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kz.aspan.awesomechat.db.dao.ContactsDao;
import kz.aspan.awesomechat.db.dao.MessagesDao;
import kz.aspan.awesomechat.db.entities.Contact;
import kz.aspan.awesomechat.db.entities.Message;

@Database(entities = {Contact.class, Message.class}, version = 2)
public abstract class AppDb extends RoomDatabase {

    private static AppDb database;

    public abstract ContactsDao getContactDao();

    public abstract MessagesDao getMessagesDao();

    public static AppDb getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDb.class, "mca")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

}
