package fr.nelfdesign.go4lunch.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;
import fr.nelfdesign.go4lunch.viewModels.MapViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements RestaurantListAdapter.onClickRestaurantitemListener{

    //FIELD
    private RecyclerView mRecyclerView;


    public RestaurantListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapViewModel mapViewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(MapViewModel.class);
        mapViewModel.getAllRestaurants().observe(this.getActivity(), this::getRestaurantList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__list_, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    private void getRestaurantList(ArrayList<Restaurant> restaurants) {
        initListAdapter(restaurants);
    }

    private void initListAdapter(ArrayList<Restaurant> restaurants) {
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurants, Glide.with(this), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickRestaurantItem(int position) {
        Intent intent = new Intent(this.getContext(), RestaurantDetail.class);
        startActivity(intent);
    }
}
