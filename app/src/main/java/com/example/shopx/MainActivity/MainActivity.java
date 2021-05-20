package com.example.shopx.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shopx.HomeFragment.HomeFragment;
import com.example.shopx.LoginFragment.LoginFragment;
import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchViewClickListener {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        LoginFragment home_fragment= LoginFragment.newInstance();
        loadFragment(home_fragment);
    }

    public void loadFragment(Fragment fragment)
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container,fragment);
        ft.commit();
    }


    @Override
    public void showBottomNavigation(boolean state) {
        if(state) {bottomNavigationView.setVisibility(View.VISIBLE);}
        else{bottomNavigationView.setVisibility(View.GONE);}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,""+getSupportFragmentManager().getBackStackEntryCount(),Toast.LENGTH_SHORT).show();
        if(bottomNavigationView.getVisibility()==View.GONE)
        {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}