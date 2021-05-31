package com.example.shopx.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopx.Model.ProductInfo;
import com.example.shopx.ProductsFragment.ProductsAdapter;
import com.example.shopx.ProductsFragment.ProductsFragment;
import com.example.shopx.Model.Product;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.SearchFragment.SearchFragment;
import com.example.shopx.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ProductsAdapter.onItemClickListener {

    private FragmentHomeBinding binding;
    private Repository repository;
    private ProductsAdapter adapter;

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
        setupBestDealsRecycler();
        setupMobilesRecycler();
        binding.homeSearchView.setOnClickListener(this);
        binding.categories.categoryMobiles.setOnClickListener(this);
        binding.categories.categoryLaptops.setOnClickListener(this);
    }


    private void setupBestDealsRecycler() {
        binding.rvBestDeals.setHasFixedSize(true);
        binding.rvBestDeals.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvBestDeals.setNestedScrollingEnabled(false);

    }

    private void setupMobilesRecycler() {
        binding.rvMobiles.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvMobiles.setHasFixedSize(true);
        binding.rvMobiles.setAdapter(adapter);
        binding.rvMobiles.setNestedScrollingEnabled(false);
        binding.rvMobiles.setHasFixedSize(true);
        binding.mobilesProgressBar.setVisibility(View.VISIBLE);
        repository.getProducts("Mobiles").observe(this, mobiles -> {
                binding.mobilesProgressBar.setVisibility(View.GONE);
                adapter.setProducts(mobiles);
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
                Toast.makeText(getActivity(), "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_beauty:
                Toast.makeText(getActivity(), "Beauty", Toast.LENGTH_SHORT).show();
                break;
            case R.id.category_see_more:
                Toast.makeText(getActivity(), "See more", Toast.LENGTH_SHORT).show();
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

    }
}