package com.example.shopx.ProductDetailsFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetails extends Fragment {


    private static final String PRODUCT_ID = "product_id";
    private static final String IN_WISHLIST = "in_wishlist";
    private static final String IN_CART = "in_cart";

    private SearchFragment.OnSearchViewClickListener listener;
    private String productID;
    private boolean inCart;
    private boolean inWishlist;
    private Repository repository;
    private SharedViewModel viewModel;
    private List<Mobile>mobiles;
    private int currProductIndex;
    public ProductDetails() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.OnSearchViewClickListener) {
            listener = (SearchFragment.OnSearchViewClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement searchProductListener");
        }
    }

    public static ProductDetails newInstance(String param1, boolean inWishlist, boolean inCart) {
        ProductDetails fragment = new ProductDetails();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, param1);
        args.putBoolean(IN_WISHLIST, inWishlist);
        args.putBoolean(IN_CART, inCart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listener.showBottomNavigation(false);
        repository = new Repository();
        if (getArguments() != null) {
            productID = getArguments().getString(PRODUCT_ID);
            inCart = getArguments().getBoolean(IN_CART);
            inWishlist = getArguments().getBoolean(IN_WISHLIST);

            viewModel= ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
            viewModel.mobiles.observe(this, mobiles -> {
                this.mobiles=mobiles;
                for(int i=0;i<mobiles.size();i++)
                {
                    if(mobiles.get(i).getId().equals(productID))
                    {
                        currProductIndex=i;
                        break;
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
    }

    private void initializeViews(View view) {

        Toolbar toolbar = view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
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
            repository.addToUserList(productID, inWishlist, !inCart);

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
            repository.addToUserList(productID, !inWishlist, inCart);

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