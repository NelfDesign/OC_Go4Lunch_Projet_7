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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;
import fr.nelfdesign.go4lunch.ui.viewModels.MapViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements RestaurantListAdapter.onClickRestaurantItemListener{

    //FIELD
    private ArrayList<Restaurant> mRestaurants;
    private MapViewModel mMapViewModel;
    private LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    public RestaurantListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Location Services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        mMapViewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(MapViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__list_, container, false);

        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        initRestaurantList();
        return view;
    }

    private void initRestaurantList() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // get the location phone
                        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        //observe restaurants data
                        mMapViewModel.getAllRestaurants(currentPosition)
                                .observe(Objects.requireNonNull(this), this::getRestaurantList);
                    }
                });
    }

    private void getRestaurantList(ArrayList<Restaurant> restaurants) {
        initListAdapter(restaurants);
    }

    private void initListAdapter(ArrayList<Restaurant> restaurants) {
        mRestaurants = restaurants;
        RestaurantListAdapter adapter = new RestaurantListAdapter(mRestaurants, Glide.with(this), this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickRestaurantItem(int position) {
        Intent intent = new Intent(this.getContext(), RestaurantDetail.class);
        intent.putExtra("placeId", mRestaurants.get(position).getPlaceId());
        intent.putExtra("restaurantName", mRestaurants.get(position).getName());
        startActivity(intent);
    }
}
