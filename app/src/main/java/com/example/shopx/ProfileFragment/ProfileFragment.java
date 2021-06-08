package com.example.shopx.ProfileFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopx.MainActivity.BottomNavigationListener;
import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.R;
import com.example.shopx.RegisterActivity.RegisterActivity;
import com.example.shopx.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private BottomNavigationListener listener;
    private FirebaseFirestore db;
    private FragmentProfileBinding binding;
    private SharedViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof BottomNavigationListener) {
            listener = (BottomNavigationListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement searchProductListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener.showBottomNavigation(false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();
        db.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        binding.userNameTxt.setText(documentSnapshot.getString("username"));
                        binding.profileUsername.setText(documentSnapshot.getString("username"));
                        binding.emailTxt.setText(documentSnapshot.getString("email"));
                        binding.profileEmail.setText(documentSnapshot.getString("email"));
                    }
                });

        binding.logOutButton.setOnClickListener(v -> {
            mAuth.signOut();
            launchRegisterActivity();
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.profileToolbar.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void launchRegisterActivity() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        if (viewModel.mobiles != null) {
            viewModel.mobiles.observe(this, results -> {
                viewModel.sendMobiles(results);
            });
        }
        super.onDestroy();
    }
}