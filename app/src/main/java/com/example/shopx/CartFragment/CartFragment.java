package com.example.shopx.CartFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.SearchFragment.SearchFragment;
import com.example.shopx.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartAdapterInterface {

    private FragmentCartBinding binding;
    private SearchFragment.BottomNavigationListener listener;
    private CartAdapter adapter;
    private SharedViewModel viewModel;
    private List<ProductInfo> mobiles;
    private boolean flag = false;
    private Repository repository;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.BottomNavigationListener) {
            listener = (SearchFragment.BottomNavigationListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement searchProductListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener.showBottomNavigation(false);
        adapter = new CartAdapter(this);
        repository = new Repository();
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel.mobiles.observe(this, results -> {
            mobiles = results;
        });


        initializeViews();

        loadCart();
    }

    private void initializeViews() {
        Toolbar toolbar = binding.myToolbar.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.cart));
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvCart.setAdapter(adapter);
        binding.rvCart.setHasFixedSize(true);
    }

    private void loadCart() {
        binding.progressBar.setVisibility(View.VISIBLE);
        List<ProductInfo> cartList = new ArrayList<>();
        repository.getCartList().observe(this, items ->
        {
            for (int i = 0; i < items.size(); i++) {
                repository.getCartProduct(items.get(i).getCategory(), items.get(i).getProductId())
                        .observe(this, product -> {
                            cartList.add(product);
                            if (cartList.size() == items.size())  // when all responses received -> set items
                            {
                                binding.progressBar.setVisibility(View.GONE);
                                adapter.setItems(cartList);
                            }
                        });
            }
        });
    }

    @Override
    public void onRemoveIconClick(ProductInfo product) {
        repository.removeFormCart(product);

        for (int i = 0; i < mobiles.size(); i++) {
            if (product.getId().equals(mobiles.get(i).getId())) {
                mobiles.get(i).setInCart(false);
                viewModel.sendMobiles(mobiles);
                flag = true;
                break;
            }
        }
    }

    @Override
    public void getTotalPrice(double total) {
        binding.totalPrice.setText(total + "");
    }

    @Override
    public void onDestroy() {
        if (!flag) {
            viewModel.sendMobiles(mobiles);
        }
        super.onDestroy();
    }
}