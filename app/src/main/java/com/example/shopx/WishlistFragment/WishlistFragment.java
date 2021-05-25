package com.example.shopx.WishlistFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.Model.Wishlist;
import com.example.shopx.R;
import com.example.shopx.Repository;

import java.util.ArrayList;
import java.util.List;


public class WishlistFragment extends Fragment implements WishlistAdapter.onFavouriteIconClickListener{

    private RecyclerView recyclerView;
    private WishlistAdapter adapter;
    private Repository repository;

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
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        loadWishlist();

    }

    private void initializeViews(View view) {
        Toolbar toolbar = view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.wishlist));

        recyclerView = view.findViewById(R.id.rv_wishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void loadWishlist() {
        List<Wishlist>favouriteList=new ArrayList<>();
        repository.getWishlist().observe(this,items->
        {
            Log.d("aaa", ""+ items.size());
            for(int i=0;i<items.size();i++)
            {
                repository.getProduct(items.get(i).getCategory(),items.get(i).getProductId())
                .observe(this, product -> {
                    favouriteList.add(product);
                    if(favouriteList.size()==items.size())  // when all responses complete -> set items
                    {
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