package com.example.shopx.WishlistFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.Model.Wishlist;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.databinding.FragmentWishlistBinding;

import java.util.ArrayList;
import java.util.List;


public class WishlistFragment extends Fragment implements WishlistAdapter.onFavouriteIconClickListener{

    private WishlistAdapter adapter;
    private Repository repository;
    private FragmentWishlistBinding binding;

    public WishlistFragment() {
        // Required empty public constructor
    }

    public static WishlistFragment newInstance() {
        WishlistFragment fragment = new WishlistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new WishlistAdapter(this);
        repository=new Repository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentWishlistBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews();

        loadWishlist();

    }

    private void initializeViews() {
        Toolbar toolbar = binding.myToolbar.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.wishlist));

        binding.rvWishlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvWishlist.setAdapter(adapter);
    }

    private void loadWishlist() {
        binding.progressBar.setVisibility(View.VISIBLE);
        List<Wishlist>favouriteList=new ArrayList<>();
        repository.getWishlist().observe(this,items->
        {
            for(int i=0;i<items.size();i++)
            {
                repository.getWishlistProduct(items.get(i).getCategory(),items.get(i).getProductId())
                .observe(this, product -> {
                    favouriteList.add(product);
                    if(favouriteList.size()==items.size())  // when all responses received -> set items
                    {
                        binding.progressBar.setVisibility(View.GONE);
                        adapter.setItems(favouriteList);
                    }
                });
            }
        });
    }

    @Override
    public void onIconClick(Wishlist product) {
        repository.removeFormWishlist(product);
    }
}