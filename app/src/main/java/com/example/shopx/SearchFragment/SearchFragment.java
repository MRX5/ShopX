package com.example.shopx.SearchFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopx.R;
import com.example.shopx.Repository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    OnSearchViewClickListener listener;
    private SearchView searchView;
    private AppCompatButton cancelBtn;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnSearchViewClickListener)
        {
            listener=(OnSearchViewClickListener)context;
        }
        else
        {
            throw new ClassCastException(context.toString()
                    + " must implement searchProductListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView=view.findViewById(R.id.search_view);
        cancelBtn=view.findViewById(R.id.cancel_btn);

        listener.showBottomNavigation(false);

        searchView.setOnQueryTextListener(this);
        cancelBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listener.showBottomNavigation(true);
        return false;
    }

    public interface OnSearchViewClickListener {
        void showBottomNavigation(boolean state);
    }
}