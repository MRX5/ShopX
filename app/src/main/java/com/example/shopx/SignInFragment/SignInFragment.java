package com.example.shopx.SignInFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopx.HomeFragment.HomeFragment;
import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
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

    private SearchFragment.OnSearchViewClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragment.OnSearchViewClickListener)
        {
            listener=(SearchFragment.OnSearchViewClickListener)context;
        }
        else
        {
            throw new ClassCastException(context.toString()
                    + " must implement searchProductListener");
        }
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
        listener.showBottomNavigation(false);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailTxt=view.findViewById(R.id.email_txt);
        passwordTxt=view.findViewById(R.id.password_txt);
        signInBtn=view.findViewById(R.id.sign_in_button);
        signUpBtn=view.findViewById(R.id.sign_up_button);
        forgotPasswordBtn=view.findViewById(R.id.forgot_password_button);

        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();

        switch (viewId)
        {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_up_button:

                break;
            case R.id.forgot_password_button:
                break;
        }
    }


    private void signIn() {
        String email=emailTxt.getText().toString().trim();
        String password=passwordTxt.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            HomeFragment homeFragment=HomeFragment.newInstance();
                            loadFragment(homeFragment);
                        }
                        else
                        {
                            String error=task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loadFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction()
                .remove(this)
                .replace(R.id.main_container,fragment)
                .addToBackStack(null)
                .commit();
    }
}