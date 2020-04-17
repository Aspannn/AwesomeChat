package kz.aspan.awesomechat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainFragment extends BaseFragment {

    public static final int PERMISSION_READ_CONTACT = 0;

    private FloatingActionButton fab;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        fab = view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewpager();
        initFab();
    }

    private void initViewpager() {
        ChatsListAdapter adapter = new ChatsListAdapter(this);

        ViewPager2 pager = requireView().findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabLayout = requireView().findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Личные");
                        break;

                    case 1:
                        tab.setText("Групповые");
                        break;
                }
            }
        }).attach();
    }

    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTab = tabLayout.getSelectedTabPosition();
                if (currentTab == 0) {
                    int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);

                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        changeFragment(new ContactsFramgent());
                    } else {
                        //permission is not granted
                        final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};

                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                            new MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("Need permission")
                                    .setMessage("Give a permission")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissions, PERMISSION_READ_CONTACT);
                                        }
                                    })
                                    .show();
                        } else {
                            requestPermissions(permissions, PERMISSION_READ_CONTACT);
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_CONTACT:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    changeFragment(new ContactsFramgent());
                } else {
                    Toast.makeText(requireContext(), "No permission to read contacts", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
