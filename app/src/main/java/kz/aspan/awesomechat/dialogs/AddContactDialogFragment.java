package kz.aspan.awesomechat.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import kz.aspan.awesomechat.R;

public class AddContactDialogFragment extends DialogFragment implements View.OnClickListener {
    private ContactAddListener listener;

    private TextInputLayout nameField;
    private TextInputLayout phoneField;

    public AddContactDialogFragment(ContactAddListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_add_contact, null, false);

        nameField = view.findViewById(R.id.nameField);
        phoneField = view.findViewById(R.id.phoneField);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.add_contact)
                .setView(view)
                .setPositiveButton(R.string.action_add, null)
                .setNegativeButton(R.string.action_cancel, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(AddContactDialogFragment.this);

            }
        });
        return dialog;
    }

    @Override
    public void onClick(View v) {
        String name = nameField.getEditText().getText().toString();
        String phone = phoneField.getEditText().getText().toString();

        //todo
        if (listener != null) {

            listener.onContactAdded(name, phone);
        }
        dismiss();
    }


    public interface ContactAddListener {
        void onContactAdded(String name, String phone);
    }
}
