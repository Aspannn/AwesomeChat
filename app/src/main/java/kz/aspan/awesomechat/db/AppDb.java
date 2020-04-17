package kz.aspan.awesomechat.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import kz.aspan.awesomechat.db.dao.ContactsDao;
import kz.aspan.awesomechat.db.dao.MessagesDao;
import kz.aspan.awesomechat.db.entities.Contact;
import kz.aspan.awesomechat.db.entities.Message;

@Database(entities = {Contact.class, Message.class}, version = 4)
public abstract class AppDb extends RoomDatabase {

    private static AppDb database;

    public abstract ContactsDao getContactDao();

    public abstract MessagesDao getMessagesDao();

    public static AppDb getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDb.class, "mca")
                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_3_4)
                    .build();
        }
        return database;
    }

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE messages ADD COLUMN status INTEGER NOT NULL DEFAULT " + Message.STATUS_NEW);
        }
    };

}
