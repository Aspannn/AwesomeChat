package kz.aspan.awesomechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.aspan.awesomechat.db.entities.Message;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Message> messages = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private TextView senderField;
        private TextView contentField;
        private TextView dateField;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.itemView);
            senderField = itemView.findViewById(R.id.senderField);
            contentField = itemView.findViewById(R.id.contentField);
            dateField = itemView.findViewById(R.id.dateField);
        }

        public void bind(Message message) {


            senderField.setText(message.getSender());
            contentField.setText(message.getContent());


            String time = formatter.format(new Date(message.getTimesTamp()));
            dateField.setText(time);

            System.out.println(message.getContent() + " " + message.getExternal());


            int marginBig = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.margin_big);
            int marginSmall = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.padding_horz);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();

            if (message.getExternal()) {
                params.setMarginStart(marginSmall);
                params.setMarginEnd(marginBig);

            } else {
                params.setMarginStart(marginBig);
                params.setMarginEnd(marginSmall);

            }
        }
    }

}
