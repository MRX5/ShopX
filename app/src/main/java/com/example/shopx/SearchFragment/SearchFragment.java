package com.example.shopx.SearchFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.shopx.MainActivity.BottomNavigationListener;
import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.ProductDetailsFragment.ProductDetails;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.Utils.GridSpacingItemDecoration;
import com.example.shopx.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, SearchAdapter.onItemClickListener
        , SearchAdapterCards.onItemClickListener {

    private BottomNavigationListener listener;
    private FragmentSearchBinding binding;
    private Repository repository;
    private SearchAdapter adapter;
    private SearchAdapterCards adapterCards;
    private SharedViewModel viewModel;
    private int siz;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationListener) {
            listener = (BottomNavigationListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement searchProductListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener.showBottomNavigation(false);
        repository = new Repository();
        adapter = new SearchAdapter(this);
        adapterCards = new SearchAdapterCards(this, getActivity());
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.resultsNumber.setVisibility(View.INVISIBLE);
        binding.filterBtn.setVisibility(View.INVISIBLE);
        binding.searchView.setOnQueryTextListener(this);
        binding.cancelBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        setupRecycler();
    }

    private void setupRecycler() {
        binding.rvSearchResults.setHasFixedSize(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        binding.rvSearchResults.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvSearchResults.addItemDecoration(new GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(getContext(), 2), true));
        binding.searchView.clearFocus();
        binding.rvSearchResults.setAdapter(adapterCards);

        if (query.length() > 0) {
            repository.searchForProduct().observe(this, results -> {
                List<ProductInfo> products = new ArrayList<>();
                siz = results.size();
                for (ProductInfo product : results) {
                    if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                        repository.getInWish_and_InCart(product.getId()).observe(this, result -> {
                            product.setInCart(result.isInCart());
                            product.setInWish(result.isInWish());
                            products.add(product);
                            if (products.size() == siz) {
                                showFilterIcon(true);
                                binding.resultsNumber.setText("Filter " + siz + " results");
                                adapterCards.setItems(products);
                            }
                        });
                    } else siz--;
                }
            });
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listener.showBottomNavigation(true);
        showFilterIcon(false);

        binding.rvSearchResults.setAdapter(adapter);
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        if (newText.length() > 0)
            repository.searchForProduct()
                    .observe(this, results ->
                    {
                        List<ProductInfo> searchResults = new ArrayList<>();
                        for (int i = 0; i < results.size(); i++) {
                            if (results.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                                searchResults.add(results.get(i));
                            }
                        }
                        adapter.setItems(searchResults);
                    });
        return false;
    }

    private void showFilterIcon(Boolean state) {
        if (state) {
            binding.resultsNumber.setVisibility(View.VISIBLE);
            binding.filterBtn.setVisibility(View.VISIBLE);
        } else {
            binding.resultsNumber.setVisibility(View.INVISIBLE);
            binding.filterBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(ProductInfo product) {
        ProductDetails fragment = ProductDetails.newInstance(product.getId(), product.getCategory());
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}