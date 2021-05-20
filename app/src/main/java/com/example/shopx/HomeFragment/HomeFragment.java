package com.example.shopx.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopx.MobilesFragment.MobilesFragment;
import com.example.shopx.R;
import com.example.shopx.SearchFragment.SearchFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private SearchView searchView;
    private CardView category_mobiles;
    private CardView category_laptops;
    private CardView category_fashion;
    private CardView category_home;
    private CardView category_beauty;
    private RecyclerView rv_best_deals;
    private RecyclerView rv_mobiles;
    private TextView seeMoreCategories;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView=view.findViewById(R.id.home_search_view);
        category_mobiles=view.findViewById(R.id.category_mobiles);
        category_laptops=view.findViewById(R.id.category_laptops);
        category_fashion=view.findViewById(R.id.category_fashion);
        category_home=view.findViewById(R.id.category_home);
        category_beauty=view.findViewById(R.id.category_beauty);
        seeMoreCategories=view.findViewById(R.id.category_see_more);
        rv_best_deals=view.findViewById(R.id.rv_best_deals);
        rv_mobiles=view.findViewById(R.id.rv_mobiles);
        setupBestDealsRecycler();
        setupMobilesRecycler();
        searchView.setOnClickListener(this);
        category_mobiles.setOnClickListener(this);
    }


    private void setupBestDealsRecycler() {
        rv_best_deals.setHasFixedSize(true);
        rv_best_deals.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
    private void setupMobilesRecycler() {
        rv_mobiles.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mobiles.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        switch (viewId)
        {
            case R.id.home_search_view:
                SearchFragment searchFragment=SearchFragment.newInstance("mostafa","");
                loadFragment(searchFragment);
                break;

            case R.id.category_mobiles:
                MobilesFragment mobilesFragment=MobilesFragment.newInstance();
                loadFragment(mobilesFragment);
                break;

            case R.id.category_laptops:
                Toast.makeText(getActivity(),"Laptops",Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_fashion:
                Toast.makeText(getActivity(),"Fashion",Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_home:
                Toast.makeText(getActivity(),"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_beauty:
                Toast.makeText(getActivity(),"Beauty",Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_see_more:
                Toast.makeText(getActivity(),"See more",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void loadFragment(Fragment fragment)
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}