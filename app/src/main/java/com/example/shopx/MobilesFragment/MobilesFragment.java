package com.example.shopx.MobilesFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopx.ProductDetailsFragment.ProductDetails;
import com.example.shopx.R;
import com.example.shopx.Repository;
import com.example.shopx.Utils.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MobilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MobilesFragment extends Fragment implements MobilesAdapter.onItemClickListener{

    private SearchView searchView;
    private TextView resultsNumber;
    private RecyclerView recyclerView;
    private MobilesAdapter adapter;
    private Repository repository;
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

        repository=new Repository(getViewLifecycleOwner());
        adapter=new MobilesAdapter(getContext(),this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mobiles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        setupRecycler();

        loadMobiles();

    }

    private void loadMobiles() {
        repository.getMobiles().observe(this, mobiles -> {
            adapter.setMobiles(mobiles);
            resultsNumber.setText("Filter "+mobiles.size()+" results");
        });
    }

    private void initializeViews(View view) {
        Toolbar toolbar=view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mobiles");

        searchView=view.findViewById(R.id.search_view);
        resultsNumber=view.findViewById(R.id.results_number);
        recyclerView=view.findViewById(R.id.rv_mobiles);
    }

    private void setupRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(getContext(),12),true));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String itemId) {
        ProductDetails fragment=ProductDetails.newInstance(itemId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container,fragment)
                .addToBackStack(null)
                .commit();
    }
}