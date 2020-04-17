package kz.aspan.awesomechat;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {


    public ChatManager getChatManager() {
        return (ChatManager) getActivity();
    }

    public void changeFragment(Fragment fragment) {
        ((MainActivity) requireActivity()).replaceFragment(fragment, true);
    }

    public void setTitle(String title) {
        requireActivity().setTitle(title);
    }
}
