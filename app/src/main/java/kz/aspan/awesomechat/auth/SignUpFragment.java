package kz.aspan.awesomechat.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import kz.aspan.awesomechat.R;
import kz.aspan.awesomechat.utils.EditTextMask;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout phoneNumberField;
    private MaterialButton nextButton;
    private SignUpListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        phoneNumberField = view.findViewById(R.id.phoneNumberField);
        nextButton = view.findViewById(R.id.nextButton);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // +7 (707) 309 85 83
        final EditText phoneNumber = phoneNumberField.getEditText();
        final MaskImpl mask = MaskImpl.createTerminated(EditTextMask.KZ_PHONE_NUMBER);
        mask.setShowingEmptySlots(true);
        mask.setPlaceholder('-');
        mask.insertFront("");
        mask.toString();

        MaskFormatWatcher formatter = new MaskFormatWatcher(mask);
//        formatter.installOn(phoneNumberField.getEditText());
        formatter.installOnAndFill(phoneNumber);

        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditTextMask.applySelection(phoneNumber, mask.getPlaceholder(), true);
                }
            }
        });

        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextMask.applySelection(phoneNumber, mask.getPlaceholder(), true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumberField.getEditText().getText().toString();

                number = EditTextMask.getNumber(mask, mask.getPlaceholder(), number);
                System.out.println(number);
                EditTextMask.check(phoneNumberField, number, 12);

                if (listener != null) {
                    listener.onVerifyPhoneNumber(number);
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:

                break;
        }
    }

    public void setSignUpListener(SignUpListener listener) {
        this.listener = listener;
    }

    interface SignUpListener {
        void onVerifyPhoneNumber(String phoneNumber);
    }
}


