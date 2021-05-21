package com.example.shopx.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shopx.HomeFragment.HomeFragment;
import com.example.shopx.ProfileFragment.ProfileFragment;
import com.example.shopx.RegisterActivity.RegisterActivity;
import com.example.shopx.SignInFragment.SignInFragment;
import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchViewClickListener
    , BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent=new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        Toast.makeText(this,mAuth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        HomeFragment homeFragment = HomeFragment.newInstance();
        loadFragment(homeFragment);

    }
    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment)
        .addToBackStack(null)
        .commit();
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }
        super.onBackPressed();
        Toast.makeText(this,""+getSupportFragmentManager().getBackStackEntryCount(),Toast.LENGTH_SHORT).show();
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
        int itemId=item.getItemId();

        switch (itemId){
            case R.id.action_home:
                HomeFragment homeFragment=HomeFragment.newInstance();
                loadFragment(homeFragment);
                return true;
            case R.id.action_categories:
                return true;
            case R.id.action_cart:
                return true;
            case R.id.action_favourite:
                return true;
            case R.id.action_profile:
                ProfileFragment profileFragment=ProfileFragment.newInstance();
                loadFragment(profileFragment);
                return true;
        }
        return false;
    }
}