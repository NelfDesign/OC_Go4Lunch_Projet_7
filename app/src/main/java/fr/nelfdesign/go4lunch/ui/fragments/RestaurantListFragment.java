package fr.nelfdesign.go4lunch.ui.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;
import fr.nelfdesign.go4lunch.ui.viewModels.MapViewModel;
import fr.nelfdesign.go4lunch.utils.Utils;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements RestaurantListAdapter.onClickRestaurantItemListener {

    //FIELD
    private ArrayList<Restaurant> mRestaurants;
    private ArrayList<Restaurant> mRestaurantsToDisplay = new ArrayList<>();
    private MapViewModel mMapViewModel;
    private LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<Workers> mWorkersArrayList;
    //constant
    private static final String PREF_RADIUS = "radius_key";
    private static final String PREF_TYPE = "type_key";
    private final CollectionReference workersRef = WorkersHelper.getWorkersCollection();
    private ListenerRegistration mListenerRegistration = null;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    //constructor
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
        return view;
    }

    @OnClick({R.id.no_star,
            R.id.star_1,
            R.id.star_3,
            R.id.star_2,
            R.id.no_filter})
    void onClickFilter(View view) {
        mRestaurantsToDisplay.clear();
        switch (view.getId()) {
            case R.id.no_star:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurants, 0));
                break;
            case R.id.star_1:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurants, 1));
                break;
            case R.id.star_2:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurants, 2));
                break;
            case R.id.star_3:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurants, 3));
                break;
            case R.id.no_filter:
                mRestaurantsToDisplay.addAll(mRestaurants);
                break;
        }
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        initRestaurantList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    /**
     * create a list of restaurant with settings and current position
     */
    private void initRestaurantList() {
        workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkersArrayList = new ArrayList<>();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {
                    if (data.get("placeId") != null) {
                        Workers workers = data.toObject(Workers.class);
                        mWorkersArrayList.add(workers);
                        Timber.i("snap workers List Restaurant : %s", mWorkersArrayList.size());
                    }
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // get the location phone
                        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        //observe restaurants data
                        mMapViewModel.getAllRestaurants(currentPosition,
                                sharedPreferences.getString(PREF_RADIUS, ""),
                                sharedPreferences.getString(PREF_TYPE, "restaurant"))
                                .observe(Objects.requireNonNull(this.getActivity()), this::initListAdapter);

                    }
                });
    }

    /**
     * init adapter for RV
     *
     * @param restaurants list
     */
    private void initListAdapter(ArrayList<Restaurant> restaurants) {
        mRestaurantsToDisplay.addAll(getRestaurantFromJson(restaurants));
        RestaurantListAdapter adapter = new RestaurantListAdapter(mRestaurantsToDisplay, Glide.with(this), this);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * format restaurant list with distance and workers information choice
     *
     * @param restaurants list
     * @return restaurant list
     */
    private ArrayList<Restaurant> getRestaurantFromJson(ArrayList<Restaurant> restaurants) {
        mRestaurants = Utils.getChoicedRestaurants(restaurants, mWorkersArrayList);
        getDistanceFromMyPosition(mRestaurants);
        return mRestaurants;
    }

    @Override
    public void onClickRestaurantItem(int position) {
        Intent intent = new Intent(getContext(), RestaurantDetail.class);
        intent.putExtra("placeId", mRestaurants.get(position).getPlaceId());
        intent.putExtra("restaurantName", mRestaurants.get(position).getName());
        startActivity(intent);
    }

    /**
     * calculate distance between restaurant and current position
     *
     * @param restaurants list
     */
    private void getDistanceFromMyPosition(ArrayList<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            float[] results = new float[1];
            Location.distanceBetween(currentPosition.latitude, currentPosition.longitude,
                    restaurant.getLocation().getLat(), restaurant.getLocation().getLng(), results);
            int distance = (int) results[0];
            restaurant.setDistance(distance);
        }
    }
}
