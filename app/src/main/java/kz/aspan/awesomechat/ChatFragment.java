package kz.aspan.awesomechat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import kz.aspan.awesomechat.db.AppDb;
import kz.aspan.awesomechat.db.entities.Message;

public class ChatFragment extends BaseFragment implements View.OnClickListener, MessageListener {

    private EditText messagesField;
    private ImageView sendButton;
    private RecyclerView messagesView;

    private ChatAdapter adapter;

    private String sender;
    private String recipient;
    private String recipientName;


    public ChatFragment(String recipient, String recipientName) {
        this.recipient = recipient;
        this.recipientName = recipientName;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).setMessageListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).setMessageListener(null);
    }

    //test
//    private TestChatFragment adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messagesField = view.findViewById(R.id.messageFiled);
        sendButton = view.findViewById(R.id.sendButton);
        messagesView = view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(recipientName);

        sendButton.setOnClickListener(this);
        adapter = new ChatAdapter();
        //test
//        adapter = new TestChatFragment();

        messagesView.setAdapter(adapter);

        sender = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        List<Message> messages = AppDb.getInstance(getContext()).getMessagesDao().getAll(sender, recipient);
        adapter.addMessages(messages);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                String text = messagesField.getText().toString();

                Message message = new Message(sender, recipient, text);
                getChatManager().sendMessage(message);
                messagesField.setText("");
                break;
        }
    }


    @Override
    public void onMessageReceived(Message message) {
        adapter.addMessage(message);
    }
}
