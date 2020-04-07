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

public class CodeVerificationFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout codeField;
    private MaterialButton signUp;

    private CodeVerificationListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verefication, container, false);

        codeField = view.findViewById(R.id.codeField);
        signUp = view.findViewById(R.id.signUpButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                String code = codeField.getEditText().getText().toString();
                check(code);

                if (listener != null) {

                    listener.onCodeEntered(code);
                }
                break;
        }
    }

    private void check(String code) {
        if (!code.isEmpty()) {
            //todo
        } else {
            codeField.setError("Enter code");
            codeField.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        codeField.setError(null);
                    } else {
                        codeField.setError("Enter code");
                    }
                }
            });
        }
    }

    public void setCodeVerificationListener(CodeVerificationListener listener) {
        this.listener = listener;
    }

    interface CodeVerificationListener {
        void onCodeEntered(String code);
    }

}
