package com.example.shopx.SignInFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopx.MainActivity.MainActivity;
import com.example.shopx.R;
import com.example.shopx.SignUpFragment.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText emailTxt;
    private TextInputEditText passwordTxt;
    private MaterialButton signInBtn;
    private MaterialButton signUpBtn;
    private MaterialButton forgotPasswordBtn;
    private FirebaseAuth mAuth;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailTxt = view.findViewById(R.id.email_txt);
        passwordTxt = view.findViewById(R.id.password_txt);
        signInBtn = view.findViewById(R.id.sign_in_button);
        signUpBtn = view.findViewById(R.id.sign_up_button);
        forgotPasswordBtn = view.findViewById(R.id.forgot_password_button);

        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_up_button:
                SignUpFragment signUpFragment = SignUpFragment.newInstance();
                loadFragment(signUpFragment);
                break;
            case R.id.forgot_password_button:
                forgotPassword();
        }
    }

    private void forgotPassword() {
        mAuth.sendPasswordResetEmail(emailTxt.getText().toString())

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.register_frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }


    private void signIn() {
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        launchMainActivity();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void checkInfo(String email,String password)
    {
    }

    private void launchMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}