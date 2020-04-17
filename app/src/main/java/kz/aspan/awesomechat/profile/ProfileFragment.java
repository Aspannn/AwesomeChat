package kz.aspan.awesomechat.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import kz.aspan.awesomechat.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollapsingToolbarLayout collapsingToolbarLayout = getView().findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle("Account name");

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("List item" + (i + 1));
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, data);

        ListView listView = getView().findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

    }
}
