package fr.nelfdesign.go4lunch.ui.fragments;


import android.content.Intent;
import android.content.IntentSender;
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
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.LocationData;
import fr.nelfdesign.go4lunch.models.LocationLiveData;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.pojos.Location;
import fr.nelfdesign.go4lunch.ui.activity.MainActivity;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;
import fr.nelfdesign.go4lunch.viewModels.MapViewModel;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements RestaurantListAdapter.onClickRestaurantitemListener{

    //FIELD
    private ArrayList<Restaurant> mRestaurants;
    private MapViewModel mMapViewModel;
    private LatLng currentPosition;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    public RestaurantListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationLiveData locationLiveData = new LocationLiveData(Objects.requireNonNull(this.getContext()));
        locationLiveData.observe(this, this::handleLocationData);

        mMapViewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(MapViewModel.class);
        //mMapViewModel.getAllRestaurants().observe(this.getActivity(), this::getRestaurantList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__list_, container, false);

        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    private void handleLocationData(LocationData locationData) {

        currentPosition = new LatLng(locationData.getLocation().getLatitude(),
                locationData.getLocation().getLongitude());

        mMapViewModel.getAllRestaurants(currentPosition.latitude, currentPosition.longitude)
                        .observe(Objects.requireNonNull(this), this::getRestaurantList);
    }

    private void getRestaurantList(ArrayList<Restaurant> restaurants) {
        initListAdapter(restaurants);
    }

    private void initListAdapter(ArrayList<Restaurant> restaurants) {
        mRestaurants = restaurants;
        RestaurantListAdapter adapter = new RestaurantListAdapter(mRestaurants, Glide.with(this), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickRestaurantItem(int position) {
        String placeId = mRestaurants.get(position).getPlaceId();
        Intent intent = new Intent(this.getContext(), RestaurantDetail.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }
}
