package kz.aspan.awesomechat.db.entities;

import android.os.Build;

import androidx.room.TypeConverter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContactsConverter {

    @TypeConverter
    public String fromContact(ArrayList<Contact> contacts) {
        if (contacts == null) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder();
            for (Contact c : contacts) {
                builder.append(c).append("!");
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();

        }
    }

    @TypeConverter
    public ArrayList<Contact> toContact(String data) {
        ArrayList<Contact> contacts = new ArrayList<>();
        String[] strings = data.split("!");
        for (String str : strings) {
            String[] a = str.split(",");
            Contact contact = new Contact(a[0], a[1]);
            contacts.add(contact);
        }
        return contacts;
    }
}
