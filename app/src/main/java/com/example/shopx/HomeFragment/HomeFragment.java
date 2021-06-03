package com.example.shopx.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.ProductDetailsFragment.ProductDetails;
import com.example.shopx.ProductsFragment.ProductsAdapter;
import com.example.shopx.ProductsFragment.ProductsFragment;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.SearchFragment.SearchFragment;
import com.example.shopx.databinding.FragmentHomeBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, ProductsAdapter.onItemClickListener {
    private FragmentHomeBinding binding;
    private Repository repository;
    private ProductsAdapter adapter;
    private SharedViewModel viewModel;
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
        adapter = new ProductsAdapter(getContext(), this);
        viewModel= ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        repository = new Repository(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMobilesRecycler();
        binding.homeSearchView.setOnClickListener(this);
        binding.categories.categoryMobiles.setOnClickListener(this);
        binding.categories.categoryLaptops.setOnClickListener(this);
        binding.categories.categoryHome.setOnClickListener(this);
        binding.mobilesSeeMore.setOnClickListener(this);
    }


    private void setupMobilesRecycler() {
        binding.rvMobiles.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvMobiles.setHasFixedSize(true);
        binding.rvMobiles.setAdapter(adapter);
        binding.rvMobiles.setNestedScrollingEnabled(false);
        binding.rvMobiles.setHasFixedSize(true);
        binding.mobilesProgressBar.setVisibility(View.VISIBLE);

        if(viewModel.mobiles !=null)
        {
            viewModel.mobiles.observe(this, products-> {
                binding.mobilesProgressBar.setVisibility(View.GONE);
                adapter.setProducts(products);
            });
        }
        else {
            getMobiles();

        }
    }

    private void getMobiles() {
        repository.getProducts("Mobiles").observe(this, mobiles -> {
            binding.mobilesProgressBar.setVisibility(View.GONE);
            adapter.setProducts(mobiles);
            viewModel.sendMobiles(mobiles);
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.home_search_view:
                SearchFragment searchFragment = SearchFragment.newInstance();
                loadFragment(searchFragment);
                break;

            case R.id.category_mobiles:
                ProductsFragment mobiles = ProductsFragment.newInstance("Mobiles");
                loadFragment(mobiles);
                break;

            case R.id.category_laptops:
                ProductsFragment laptops = ProductsFragment.newInstance("Laptops");
                loadFragment(laptops);
                break;
            case R.id.category_fashion:
                Toast.makeText(getActivity(), "Fashion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_home:
                ProductsFragment home = ProductsFragment.newInstance("Home");
                loadFragment(home);
                break;
            case R.id.mobiles_see_more:
                ProductsFragment fragment = ProductsFragment.newInstance("Mobiles");
                loadFragment(fragment);
                break;
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onItemClick(ProductInfo mobile) {
        ProductDetails fragment=ProductDetails.newInstance(mobile.getId(),mobile.getCategory());
        loadFragment(fragment);
    }

    @Override
    public void onButtonClick(String productId, boolean inWish, boolean inCart) {

    }

}