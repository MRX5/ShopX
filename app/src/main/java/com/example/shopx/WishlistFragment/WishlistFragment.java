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

import com.example.shopx.R;
import com.example.shopx.Repository;


public class WishlistFragment extends Fragment {

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
        adapter=new WishlistAdapter();
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
        repository.getMobiles().observe(this,items->
        {
            for(int i=0;i<items.size();i++)
            {
                Log.d("mmm", items.get(i).getId());
            }
        });
    }

}