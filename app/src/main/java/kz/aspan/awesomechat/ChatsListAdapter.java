package kz.aspan.awesomechat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ChatsListAdapter extends FragmentStateAdapter {

    public ChatsListAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ActiveChatsFragment();
        } else {
            return new GroupChatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
