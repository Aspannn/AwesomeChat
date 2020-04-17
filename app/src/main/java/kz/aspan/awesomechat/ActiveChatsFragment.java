package kz.aspan.awesomechat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.aspan.awesomechat.db.AppDb;
import kz.aspan.awesomechat.db.dao.ContactsDao;
import kz.aspan.awesomechat.db.entities.Contact;
import kz.aspan.awesomechat.dialogs.AddContactDialogFragment;
import kz.aspan.awesomechat.profile.ProfileFragment;

public class ActiveChatsFragment extends BaseFragment implements AddContactDialogFragment.ContactAddListener, AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    private final String KEY_ID = "id";
    private final String KEY_CAPTION = "caption";
    private final String KEY_PHONE = "phone";


    private ListView listView;
    private TextView textView;

    private SimpleAdapter adapter;
    private List<Map<String, Object>> contacts;
    private ContactsDao contactsDao;

    //======


    private DrawerLayout drawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_chats_fragmnet, container, false);
        listView = view.findViewById(R.id.listView);
        textView = view.findViewById(R.id.emptyText);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(getString(R.string.app_name));

        //toolbar
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        drawer = getView().findViewById(R.id.drawer_layout);

        NavigationView navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //==================

//

        contactsDao = AppDb.getInstance(getContext()).getContactDao();
        initContacts();
    }


//================================

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
        String caption = (String) item.get(KEY_CAPTION);
        changeFragment(new ChatFragment(phone, caption));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_menu:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new ProfileFragment())
                        .addToBackStack(null)
                        .commit();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//        @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
}



























