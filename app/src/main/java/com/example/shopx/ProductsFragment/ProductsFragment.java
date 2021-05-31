package com.example.shopx.ProductsFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.Product;
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.ProductDetailsFragment.ProductDetails;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.SearchFragment.SearchFragment;
import com.example.shopx.Utils.GridSpacingItemDecoration;
import com.example.shopx.databinding.FragmentProductsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment implements ProductsAdapter.onItemClickListener {
    private final static String CATEGORY="category";

    private FragmentProductsBinding binding;
    private ProductsAdapter adapter;
    private Repository repository;
    private SharedViewModel viewModel;
    private String category;
    public ProductsFragment() {
        // Required empty public constructor
    }


    public static ProductsFragment newInstance(String category) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args=new Bundle();
        args.putString(CATEGORY,category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        category=bundle.getString(CATEGORY);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        adapter = new ProductsAdapter(getContext(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        repository = new Repository(getViewLifecycleOwner());
        binding=FragmentProductsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initializeViews(view);

        if(viewModel.mobiles!=null)  // load mobiles from viewModel
        {
            viewModel.mobiles.observe(this, mobiles -> {
                adapter.setProducts(mobiles);
                binding.resultsNumber.setText("Filter " + mobiles.size() + " results");
            });
        }
        else {
            loadMobiles();         // load mobiles from Firestore
        }

        setupRecycler();

    }

    private void initializeViews(View view) {
        Toolbar toolbar = binding.myToolbar.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(category);

        binding.homeSearchView.setOnClickListener(v -> {
            SearchFragment fragment=SearchFragment.newInstance();
            loadFragment(fragment);
        });

        binding.filterBtn.setOnClickListener(v->{
            openDialog();
        });
    }

    private void openDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        builder.create().show();
        Button btn1=dialogView.findViewById(R.id.btn1);
        Button btn2=dialogView.findViewById(R.id.btn2);
        btn1.setOnClickListener(v->
                Toast.makeText(getActivity(),category,Toast.LENGTH_SHORT).show());
        btn2.setOnClickListener(v->
                Toast.makeText(getActivity(),"btn2",Toast.LENGTH_SHORT).show());

    }

    private void setupRecycler() {

        binding.rvMobiles.setHasFixedSize(true);
        binding.rvMobiles.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvMobiles.addItemDecoration(new GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(getContext(), 2), true));
        binding.rvMobiles.setAdapter(adapter);
    }

    private void loadMobiles() {
            binding.progressBar.setVisibility(View.VISIBLE);
            repository.getProducts(category).observe(this, products -> {
                binding.progressBar.setVisibility(View.GONE);
                adapter.setProducts(products);
                viewModel.sendMobiles(products);
                binding.resultsNumber.setText("Filter " + products.size() + " results");
            });
    }


    @Override
    public void onItemClick(ProductInfo product) {
        ProductDetails fragment = ProductDetails.newInstance(product.getId(),product.getCategory());
        loadFragment(fragment);
    }

    public void loadFragment(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onDestroy() {
        viewModel.cleanMemory();
        super.onDestroy();
    }

}
