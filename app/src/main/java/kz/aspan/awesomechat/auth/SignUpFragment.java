package kz.aspan.awesomechat.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import kz.aspan.awesomechat.R;

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

        nextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                String number = phoneNumberField.getEditText().getText().toString();

                check(number);

                if (listener != null) {
                    listener.onVerifyPhoneNumber("+7" + number);
                }
                break;
        }

    }

    private void check(String number) {
        if (number.length() == 10) {
        } else {
            System.out.println(number.length());
            phoneNumberField.setError("Field number");
            phoneNumberField.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        phoneNumberField.setError(null);
                    } else {
                        phoneNumberField.setError("Field number");
                    }
                }
            });
        }
    }

    public void setSignUpListener(SignUpListener listener) {
        this.listener = listener;
    }

    interface SignUpListener {
        void onVerifyPhoneNumber(String phoneNumber);
    }


}
