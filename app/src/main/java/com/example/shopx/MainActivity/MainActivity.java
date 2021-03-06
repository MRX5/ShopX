package com.example.shopx.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shopx.CartFragment.CartFragment;
import com.example.shopx.HomeFragment.HomeFragment;
import com.example.shopx.ProfileFragment.ProfileFragment;
import com.example.shopx.RegisterActivity.RegisterActivity;
import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;
import com.example.shopx.WishlistFragment.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , BottomNavigationListener {

    private final int HOME_FRAGMENT = 1;
    private final int CART_FRAGMENT = 2;
    private final int WISHLIST_FRAGMENT = 3;
    private final int PROFILE_FRAGMENT = 4;
    private int curr_fragment = HOME_FRAGMENT;
    private FragmentManager fragmentManager = getSupportFragmentManager();

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

        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        if (curr_fragment != HOME_FRAGMENT) {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
            return;
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
        super.onBackPressed();
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
                    curr_fragment = HOME_FRAGMENT;
                    clearBackStack();
                    /*HomeFragment homeFragment = HomeFragment.newInstance();
                    loadFragment(homeFragment);*/
                }
                return true;
            case R.id.action_cart:
                if (curr_fragment != CART_FRAGMENT) {
                    curr_fragment = CART_FRAGMENT;
                    clearBackStack();
                    CartFragment cartFragment = CartFragment.newInstance();
                    loadFragment(cartFragment);
                }
                return true;
            case R.id.action_favourite:
                if (curr_fragment != WISHLIST_FRAGMENT) {
                    curr_fragment = WISHLIST_FRAGMENT;
                    clearBackStack();
                    WishlistFragment wishlistFragment = WishlistFragment.newInstance();
                    loadFragment(wishlistFragment);
                }
                return true;
            case R.id.action_profile:

                if (curr_fragment != PROFILE_FRAGMENT) {
                    curr_fragment = PROFILE_FRAGMENT;
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

    public void clearBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount() - 1; i++)
            fragmentManager.popBackStack();
    }

}