package com.example.shopx.MobilesFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shopx.MainActivity.SharedViewModel;
import com.example.shopx.Model.Mobile;
import com.example.shopx.ProductDetailsFragment.ProductDetails;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.Utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MobilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MobilesFragment extends Fragment implements MobilesAdapter.onItemClickListener {

    private SearchView searchView;
    private TextView resultsNumber;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MobilesAdapter adapter;
    private Repository repository;
    private List<Mobile> mobilesCopy;
    private SharedViewModel viewModel;

    public MobilesFragment() {
        // Required empty public constructor
    }


    public static MobilesFragment newInstance() {
        MobilesFragment fragment = new MobilesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        adapter = new MobilesAdapter(getContext(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        repository = new Repository(getViewLifecycleOwner());
        return inflater.inflate(R.layout.fragment_mobiles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initializeViews(view);

        if(viewModel.mobiles!=null)  // load mobiles from viewModel
        {
            viewModel.mobiles.observe(this, mobiles -> {
                adapter.setMobiles(mobiles);
            });
        }
        else {
            loadMobiles();         // load mobiles from Firestore
        }

        setupRecycler();

    }

    private void initializeViews(View view) {
        Toolbar toolbar = view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mobiles");

        searchView = view.findViewById(R.id.search_view);
        resultsNumber = view.findViewById(R.id.results_number);
        recyclerView = view.findViewById(R.id.rv_mobiles);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void setupRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(getContext(), 12), true));
        recyclerView.setAdapter(adapter);
    }

    private void loadMobiles() {

            progressBar.setVisibility(View.VISIBLE);
            repository.getMobiles().observe(this, mobiles -> {
                progressBar.setVisibility(View.GONE);
                adapter.setMobiles(mobiles);
                viewModel.sendMobiles(mobiles);
                resultsNumber.setText("Filter " + mobiles.size() + " results");
            });
    }


    @Override
    public void onItemClick(String itemId, boolean inWishlist, boolean inCart) {
        ProductDetails fragment = ProductDetails.newInstance(itemId, inWishlist, inCart);
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
