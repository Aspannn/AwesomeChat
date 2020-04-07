package kz.aspan.awesomechat.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.aspan.awesomechat.db.entities.Message;
import kz.aspan.awesomechat.R;


public class TestChatFragment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_SENDER = 0;
    private final int TYPE_RECEIVER = 1;

    private List<Message> messages = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            default:
            case TYPE_SENDER:
                View view = inflater.inflate(R.layout.listitem_message_test, parent, false);
                return new SenderMessageViewHolder(view);
            case TYPE_RECEIVER:
                view = inflater.inflate(R.layout.listitem_message, parent, false);
                return new ReceiverMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReceiverMessageViewHolder) {
            ((ReceiverMessageViewHolder) holder).receiverBind((Message) messages.get(position));
        } else if (holder instanceof SenderMessageViewHolder) {
            ((SenderMessageViewHolder) holder).senderBind((Message) messages.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message obj = messages.get(position);
        if (obj.getSender().equals(Util.SENDER)) {
            return TYPE_SENDER;
//        } else if (obj.getSender().equals(Util.RECEIVER)) {
//            return TYPE_RECEIVER;
        } else
            return TYPE_RECEIVER;
    }

    class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView senderField;
        private TextView contentField;
        private TextView dateField;

        public ReceiverMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderField = itemView.findViewById(R.id.senderField);
            contentField = itemView.findViewById(R.id.contentField);
            dateField = itemView.findViewById(R.id.dateField);
        }

        public void receiverBind(Message message) {
            senderField.setText(message.getSender());
            contentField.setText(message.getContent());

            String time = formatter.format(new Date(message.getTimesTamp()));
            dateField.setText(time);
        }
    }

    class SenderMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView senderField1;
        private TextView contentField1;
        private TextView dateField1;

        public SenderMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderField1 = itemView.findViewById(R.id.senderField1);
            contentField1 = itemView.findViewById(R.id.contentField1);
            dateField1 = itemView.findViewById(R.id.dateField1);
        }

        public void senderBind(Message message) {
            senderField1.setText(message.getSender());
            contentField1.setText(message.getContent());

            String time = formatter.format(new Date(message.getTimesTamp()));
            dateField1.setText(time);
        }
    }


}
