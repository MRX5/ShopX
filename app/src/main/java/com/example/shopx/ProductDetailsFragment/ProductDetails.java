package com.example.shopx.ProductDetailsFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.Mobile;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.SearchFragment.SearchFragment;
import com.example.shopx.databinding.FragmentProductDetailsBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetails extends Fragment {

    private FragmentProductDetailsBinding binding;
    private static final String PRODUCT_ID = "product_id";
    private static final String IN_WISHLIST = "in_wishlist";
    private static final String IN_CART = "in_cart";
    private static final String CATEGORY = "category";

    private SearchFragment.BottomNavigationListener listener;

    private String productID;
    private boolean inCart;
    private boolean inWishlist;
    private String category;

    private Repository repository;

    private SharedViewModel viewModel;

    private List<Mobile> mobiles;

    private int currProductIndex;

    public ProductDetails() {
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

    public static ProductDetails newInstance(String param1, boolean inWishlist, boolean inCart, String category) {
        ProductDetails fragment = new ProductDetails();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, param1);
        args.putBoolean(IN_WISHLIST, inWishlist);
        args.putBoolean(IN_CART, inCart);
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        listener.showBottomNavigation(false);

        repository = new Repository();

        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        if (getArguments() != null) {
            productID = getArguments().getString(PRODUCT_ID);
            inCart = getArguments().getBoolean(IN_CART);
            inWishlist = getArguments().getBoolean(IN_WISHLIST);
            category = getArguments().getString(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeToolbar();

        updateUi();
    }

    private void initializeToolbar() {
        Toolbar toolbar = binding.myToolbar.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void updateUi() {
        viewModel.mobiles.observe(this, mobiles -> {
            this.mobiles = mobiles;

            for (int i = 0; i < mobiles.size(); i++) {
                if (mobiles.get(i).getId().equals(productID)) {
                    currProductIndex = i;
                    binding.productName.setText(mobiles.get(currProductIndex).getName());
                    binding.productPrice.setText(mobiles.get(currProductIndex).getPrice());
                    break;
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.product_details_menu, menu);
        if (inCart)
            menu.findItem(R.id.action_add_cart).setIcon(R.drawable.icon_add_cart_green);

        if (inWishlist)
            menu.findItem(R.id.action_favourite).setIcon(R.drawable.icon_favourite_green);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_add_cart) {
            repository.addToUserProducts(productID, inWishlist, !inCart, category);

            if (inCart) {
                inCart = false;
                item.setIcon(R.drawable.icon_add_cart);
                mobiles.get(currProductIndex).setInCart(inCart);
            } else {
                inCart = true;
                item.setIcon(R.drawable.icon_add_cart_green);
                mobiles.get(currProductIndex).setInCart(inCart);
            }
            return true;
        } else if (itemId == R.id.action_favourite) {
            repository.addToUserProducts(productID, !inWishlist, inCart, category);

            if (inWishlist) {
                inWishlist = false;
                item.setIcon(R.drawable.icon_favourite);
                mobiles.get(currProductIndex).setInWishlist(inWishlist);
            } else {
                inWishlist = true;
                item.setIcon(R.drawable.icon_favourite_green);
                mobiles.get(currProductIndex).setInWishlist(inWishlist);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        viewModel.sendMobiles(mobiles);
        super.onDestroy();
    }
}