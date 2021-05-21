package com.example.shopx.SignUpFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopx.MainActivity.MainActivity;
import com.example.shopx.R;
import com.example.shopx.RegisterActivity.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private TextInputEditText usernameTxt,emailTxt,passwordTxt;
    private MaterialButton signUpBtn;

    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTxt=view.findViewById(R.id.user_name_txt);
        emailTxt=view.findViewById(R.id.email_txt);
        passwordTxt=view.findViewById(R.id.password_txt);
        signUpBtn=view.findViewById(R.id.sign_up_button);

        signUpBtn.setOnClickListener(v -> checkInputs());
    }

    private void checkInputs() {
        String username=usernameTxt.getText().toString().trim();
        String email=emailTxt.getText().toString().trim();
        String password=passwordTxt.getText().toString().trim();

        boolean error=false;

        if(TextUtils.isEmpty(username))
        {
            usernameTxt.setError("Enter username");
            error=true;
        }
        if(TextUtils.isEmpty(email))
        {
            emailTxt.setError("Enter Email");
            error=true;
        }
        if(TextUtils.isEmpty(password)||password.length()<8)
        {
            passwordTxt.setError("Passwords must be at least 8 characters long");
            error=true;
        }

        if(!error)
        {
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            saveUserData(username,email);
                            launchMainActivity();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }


    private void saveUserData(String username,String email) {
        HashMap<String,Object>mp=new HashMap<>();
        mp.put("username",username);
        mp.put("email",email);
        mFirestore.collection("Users")
                .add(mp)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void launchMainActivity() {
        Intent intent=new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}