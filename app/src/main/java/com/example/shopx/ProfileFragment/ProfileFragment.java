package com.example.shopx.ProfileFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopx.R;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {

    private Button signOutBtn;
    private TextView usernameTxt;
    private FirebaseAuth mAuth;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameTxt=view.findViewById(R.id.user_name_txt);
        signOutBtn=view.findViewById(R.id.sign_out_button);
        if(mAuth.getCurrentUser()!=null)
        {
            usernameTxt.setVisibility(View.VISIBLE);
            usernameTxt.setText(mAuth.getCurrentUser().getEmail());
        }
        else
        {
            usernameTxt.setVisibility(View.GONE);
        }

        signOutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            usernameTxt.setVisibility(View.GONE);
        });
    }


}