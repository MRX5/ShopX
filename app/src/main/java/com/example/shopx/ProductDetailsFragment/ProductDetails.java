package com.example.shopx.ProductDetailsFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.Product;
import com.example.shopx.Model.ProductInfo;
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

    private final static String CATEGORY = "category";
    private final static String PRODUCT_ID = "product_id";
    private String productCategory;
    private String productId;
    private FragmentProductDetailsBinding binding;
    private SearchFragment.BottomNavigationListener listener;
    private Repository repository;
    private SharedViewModel viewModel;
    private List<ProductInfo> products;
    private Product product;
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

    public static ProductDetails newInstance(String productId, String category) {
        ProductDetails fragment = new ProductDetails();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productId = getArguments().getString(PRODUCT_ID);
        productCategory = getArguments().getString(CATEGORY);

        listener.showBottomNavigation(false);

        repository = new Repository();

        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getProduct();

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeToolbar();
        binding.contentScrollView.setVisibility(View.INVISIBLE);
    }

    private void initializeToolbar() {
        Toolbar toolbar = binding.myToolbar.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getCurrProductIndex() {
        viewModel.mobiles.observe(this, products -> {
            this.products = products;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId().equals(product.getId())) {
                    currProductIndex = i;
                    break;
                }
            }
        });
    }

    private void getProduct() {
        repository.getProduct(productCategory,productId).observe(this,product -> {
            repository.getInWish_and_InCart(productId).observe(this,response->{
                product.setInCart(response.isInCart());
                product.setInWishlist(response.isInWish());
                this.product=product;
                updateUi();
            });
        });
    }

    private void updateUi() {
        Log.d("aaa", "updateUi: ");

        setHasOptionsMenu(true);
        binding.productName.setText(product.getName());
        binding.productPrice.setText(product.getPrice());
        binding.productDescription.setText(product.getDescription());

        binding.progressBar.setVisibility(View.GONE);
        binding.contentScrollView.setVisibility(View.VISIBLE);

        if (viewModel.mobiles != null) {
            getCurrProductIndex();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.product_details_menu, menu);
        if (product.isInCart())
            menu.findItem(R.id.action_add_cart).setIcon(R.drawable.icon_add_cart_green);

        if (product.isInWishlist())
            menu.findItem(R.id.action_favourite).setIcon(R.drawable.icon_favourite_green);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_add_cart) {
            repository.addToUserProducts(product.getId(), product.isInWishlist(), !product.isInCart(), product.getCategory());

            if (product.isInCart()) {
                product.setInCart(false);
                item.setIcon(R.drawable.icon_add_cart);
                product.setInCart(product.isInCart());
            } else {
                product.setInCart(true);
                item.setIcon(R.drawable.icon_add_cart_green);
                product.setInCart(product.isInCart());
            }
            return true;
        } else if (itemId == R.id.action_favourite) {
            repository.addToUserProducts(product.getId(), !product.isInWishlist(), product.isInCart(), product.getCategory());

            if (product.isInWishlist()) {
                product.setInWishlist(false);
                item.setIcon(R.drawable.icon_favourite);
                product.setInWishlist(product.isInWishlist());
            } else {
                product.setInWishlist(true);
                item.setIcon(R.drawable.icon_favourite_green);
                product.setInWishlist(product.isInWishlist());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (products != null) {
            products.get(currProductIndex).setInWish(product.isInWishlist());
            products.get(currProductIndex).setInCart(product.isInCart());
            viewModel.sendMobiles(products);
        }
        super.onDestroy();
    }
}