package com.example.shopx.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shopx.HomeFragment.HomeFragment;
import com.example.shopx.ProfileFragment.ProfileFragment;
import com.example.shopx.RegisterActivity.RegisterActivity;
import com.example.shopx.Repository;
import com.example.shopx.SignInFragment.SignInFragment;
import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchViewClickListener
        , BottomNavigationView.OnNavigationItemSelectedListener {

    private final int HOME_FRAGMENT = 1;
    private final int CATEGORIES_FRAGMENT = 2;
    private final int CART_FRAGMENT = 3;
    private final int WISHLIST_FRAGMENT = 4;
    private final int PROFILE_FRAGMENT = 5;
    private int curr_fragment = HOME_FRAGMENT;
    private FragmentManager fragmentManager=getSupportFragmentManager();

    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        HomeFragment homeFragment = HomeFragment.newInstance();
        loadFragment(homeFragment);

    }

    @Override
    public void showBottomNavigation(boolean state) {
        if (state) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if(curr_fragment!=HOME_FRAGMENT)
        {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
            return;
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
        super.onBackPressed();
        Toast.makeText(this, "" + getSupportFragmentManager().getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_home:
                if (curr_fragment != HOME_FRAGMENT) {
                    curr_fragment=HOME_FRAGMENT;
                    clearBackStack();
                    HomeFragment homeFragment = HomeFragment.newInstance();
                    loadFragment(homeFragment);
                }
                return true;
            case R.id.action_categories:
                return true;
            case R.id.action_cart:
                return true;
            case R.id.action_favourite:
                return true;
            case R.id.action_profile:

                if (curr_fragment != PROFILE_FRAGMENT) {
                    curr_fragment=PROFILE_FRAGMENT;
                    clearBackStack();
                    ProfileFragment profileFragment = ProfileFragment.newInstance();
                    loadFragment(profileFragment);
                }
                return true;

        }
        return false;
    }

    public void loadFragment(Fragment fragment) {

         fragmentManager.beginTransaction()
         .replace(R.id.main_container, fragment)
                 .addToBackStack(null)
                .commit();
    }

    public void clearBackStack()
    {
        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++)
            fragmentManager.popBackStack();
    }
}