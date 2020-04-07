package kz.aspan.awesomechat;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public ChatManager getChatManager() {
        return (ChatManager) getActivity();
    }

    public void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
