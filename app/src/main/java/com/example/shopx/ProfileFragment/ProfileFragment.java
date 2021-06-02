package com.example.shopx.ProfileFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.R;
import com.example.shopx.RegisterActivity.RegisterActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private MaterialButton signOutBtn;
    private TextView usernameTxt,emailTxt;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedViewModel viewModel;

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
        db=FirebaseFirestore.getInstance();
        viewModel= ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
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
        emailTxt=view.findViewById(R.id.email_txt);
        signOutBtn=view.findViewById(R.id.sign_out_button);
        if(mAuth.getCurrentUser()!=null)
        {
            usernameTxt.setVisibility(View.VISIBLE);
            db.collection("Users")
                    .document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            usernameTxt.setText(documentSnapshot.getString("username"));
                            emailTxt.setText(documentSnapshot.getString("email"));
                        }
                    });

        }
        else
        {
            usernameTxt.setVisibility(View.GONE);
        }

        signOutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            launchRegisterActivity();
        });
    }

    private void launchRegisterActivity() {
        Intent intent=new Intent(getActivity(),RegisterActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        if(viewModel.mobiles!=null) {
            viewModel.mobiles.observe(this,results->{
                viewModel.sendMobiles(results);
            });
        }
        super.onDestroy();
    }
}