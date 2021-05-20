package com.example.shopx.LoginFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    private TextInputEditText emailTxt;
    private TextInputEditText passwordTxt;
    private MaterialButton signInBtn;
    private MaterialButton signUpBtn;
    private MaterialButton forgotPasswordBtn;

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

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener.showBottomNavigation(false);
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

    }
}