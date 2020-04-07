package kz.aspan.awesomechat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.PrimaryKey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.aspan.awesomechat.db.AppDb;
import kz.aspan.awesomechat.db.dao.ContactsDao;
import kz.aspan.awesomechat.db.entities.Contact;
import kz.aspan.awesomechat.dialogs.AddContactDialogFragment;

public class ActiveChatsFragment extends BaseFragment implements AddContactDialogFragment.ContactAddListener, AdapterView.OnItemClickListener {

    private final String KEY_ID = "id";
    private final String KEY_CAPTION = "caption";
    private final String KEY_PHONE = "phone";

    private FloatingActionButton fab;
    private ListView listView;
    private TextView textView;

    private SimpleAdapter adapter;
    private List<Map<String, Object>> contacts;
    private ContactsDao contactsDao;

    //======

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_chats_fragmnet, container, false);
        fab = view.findViewById(R.id.fab);
        listView = view.findViewById(R.id.listView);
        textView = view.findViewById(R.id.emptyText);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactDialogFragment dialogFragment = new AddContactDialogFragment(ActiveChatsFragment.this);
                dialogFragment.show(getFragmentManager(), "AddContactDialogFragment");
            }
        });

        contactsDao = AppDb.getInstance(getContext()).getContactDao();
        initContacts();
    }

    @Override
    public void onContactAdded(String name, String phone) {
        Contact contact = new Contact(name, phone);
        contactsDao.insert(contact);

        contacts.add(0, asMap(contact));
        adapter.notifyDataSetChanged();

        textView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);


    }


    private void initContacts() {
        contacts = getContacts();

        adapter = new SimpleAdapter(
                getContext(),
                contacts,
                R.layout.listitem_contact,
                new String[]{KEY_CAPTION, KEY_PHONE},
                new int[]{R.id.captionField, R.id.descriptionField}
        );

        if (contacts.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    private List<Map<String, Object>> getContacts() {
        List<Contact> dbContacts = contactsDao.getAll();

        ArrayList<Map<String, Object>> contacts = new ArrayList<>(dbContacts.size());
        for (Contact contact : dbContacts) {
            contacts.add(asMap(contact));
        }

        return contacts;
    }

    private Map<String, Object> asMap(Contact contact) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ID, contact.getId());
        item.put(KEY_CAPTION, contact.getCaption());
        item.put(KEY_PHONE, contact.getPhone());
        return item;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> item = contacts.get(position);
        String phone = (String) item.get(KEY_PHONE);
        changeFragment(new ChatFragment(phone));
    }
}



























