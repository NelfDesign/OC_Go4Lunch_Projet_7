package fr.nelfdesign.go4lunch.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiGoogleMap.NearbyPlaces;
import fr.nelfdesign.go4lunch.base.App;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;
import fr.nelfdesign.go4lunch.utils.Utils;
import fr.nelfdesign.go4lunch.viewModels.MapViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements NearbyPlaces{

    private RecyclerView mRecyclerView;
    private RestaurantListAdapter adapter;
    List<Restaurant> mRestaurantList;
    private String myLocation = "47.21,-1.55";


    public RestaurantListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureRetrofitCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__list_, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        initListAdapter(mRestaurantList);
        return view;
    }

    private void initListAdapter(List<Restaurant> restaurants) {
        adapter = new RestaurantListAdapter(mRestaurantList, Glide.with(this), restaurants.size(), myLocation);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void configureRetrofitCall() {
        mRestaurantList = new ArrayList<>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("location", myLocation);
        parameters.put("radius", "1500");
        parameters.put("type", "restaurant");
        parameters.put("keyword", "");
        parameters.put("key", getString(R.string.google_api_key));

        Call<RestaurantsResult> mListCall;

        mListCall = App.retrofitCall().getNearByRestaurant(myLocation,getString(R.string.google_maps_key));

        mListCall.enqueue(new Callback<RestaurantsResult>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantsResult> call,
                                   @NotNull Response<RestaurantsResult> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    Timber.i("onResponse: erreur");
                    return;
                }

                RestaurantsResult resultsListRestaurants = response.body();

                if (resultsListRestaurants != null) {

                    Utils.mapRestaurantResultToRestaurant(resultsListRestaurants, mRestaurantList);
                    initListAdapter(mRestaurantList);
                    Timber.i("Restaurant = %s", mRestaurantList.get(0).getName());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantsResult> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Timber.i(t.toString());
            }
        });
    }

}
