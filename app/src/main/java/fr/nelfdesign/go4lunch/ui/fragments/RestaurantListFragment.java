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
import com.facebook.share.Share;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.activity.MainActivity;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;
import fr.nelfdesign.go4lunch.ui.viewModels.MapViewModel;
import fr.nelfdesign.go4lunch.utils.Utils;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements RestaurantListAdapter.onClickRestaurantItemListener{

    //FIELD
    private ArrayList<Restaurant> mRestaurants;
    private MapViewModel mMapViewModel;
    private LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<Workers> mWorkersArrayList;
    private RestaurantListAdapter adapter;

    private static final String PREF_RADIUS = "radius_key";
    private static final String PREF_TYPE = "type_key";
    final CollectionReference workersRef = WorkersHelper.getWorkersCollection();
    private ListenerRegistration mListenerRegistration = null;

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
        return view;
    }

   @Override
    public void onStart() {
        super.onStart();
        initRestaurantList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null){
            mListenerRegistration.remove();
        }
    }

    private void initRestaurantList() {
        workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkersArrayList = new ArrayList<>();
            if(queryDocumentSnapshots != null){
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {

                    if (data.get("restaurantName") != null) {
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

    private void initListAdapter(ArrayList<Restaurant> restaurants) {
        mRestaurants = Utils.getChoicedRestaurants(restaurants, mWorkersArrayList);
        getDistanceFromMyPosition(mRestaurants);
        adapter = new RestaurantListAdapter(mRestaurants, Glide.with(this), this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickRestaurantItem(int position) {
        Intent intent = new Intent(getContext(), RestaurantDetail.class);
        intent.putExtra("placeId", mRestaurants.get(position).getPlaceId());
        intent.putExtra("restaurantName", mRestaurants.get(position).getName());
        startActivity(intent);
    }

    private void getDistanceFromMyPosition(ArrayList<Restaurant> restaurants){
        for (Restaurant restaurant : restaurants){
            float[] results = new float[1];
            Location.distanceBetween(currentPosition.latitude, currentPosition.longitude,
                    restaurant.getLocation().getLat(), restaurant.getLocation().getLng(), results);
            int distance = (int) results[0];
            restaurant.setDistance(distance);
        }
    }
}
