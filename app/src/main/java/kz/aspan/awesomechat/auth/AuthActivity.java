package kz.aspan.awesomechat.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import kz.aspan.awesomechat.MainActivity;
import kz.aspan.awesomechat.R;

public class AuthActivity extends AppCompatActivity implements SignUpFragment.SignUpListener, CodeVerificationFragment.CodeVerificationListener {

    private String verificationId;
    private static String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new SignUpFragment())
                .commit();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof CodeVerificationFragment) {
            ((CodeVerificationFragment) fragment).setCodeVerificationListener(this);
        } else if (fragment instanceof SignUpFragment) {
            ((SignUpFragment) fragment).setSignUpListener(this);
        }
    }

    @Override
    public void onVerifyPhoneNumber(String phoneNumber) {

        PhoneAuthProvider authProvider = PhoneAuthProvider.getInstance();
        authProvider.verifyPhoneNumber(
                phoneNumber,
                60, TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //todo
                        Log.d(TAG, "onVerificationCompleted" + phoneAuthCredential.getSmsCode());

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e(TAG, "onVerificationFailed" + e.getMessage());

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            //todo

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            //todo
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);

                        Log.d(TAG, "onCodeSent" + verificationId);

                        AuthActivity.this.verificationId = verificationId;


                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new CodeVerificationFragment())
                                .addToBackStack(null)
                                .commit();

                    }
                }
        );
    }

    @Override
    public void onCodeEntered(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Log.d();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure" + task.getException().getMessage());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // the
                            }
                        }
                    }
                });
    }
}
