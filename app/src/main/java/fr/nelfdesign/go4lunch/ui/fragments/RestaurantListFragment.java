package fr.nelfdesign.go4lunch.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    List<Restaurant> mRestaurantList;

    public RestaurantListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_restaurant__list_, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        initListAdapter(initList());

        return view;
    }

    private void initListAdapter(List<Restaurant> restaurants) {
        mRecyclerView.setAdapter(new RestaurantListAdapter(restaurants));
    }

    private List<Restaurant> initList(){
        mRestaurantList = Arrays.asList(
                new Restaurant("Le Zinc", "French - 21 rue Lucas", "open",
                        "https://api.adorable.io/AVATARS/512/1.png", "200m", 2, 1),
                new Restaurant("Le temple d'or", "Japanese - 2 rue du temple", "ouvre à 10h",
                        "https://api.adorable.io/AVATARS/512/1.png", "800m", 1,0)
        );

        return mRestaurantList;
    }
}
