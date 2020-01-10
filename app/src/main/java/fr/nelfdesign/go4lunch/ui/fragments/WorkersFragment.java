package fr.nelfdesign.go4lunch.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.RepositoryFirebase;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.adapter.WorkersListAdapter;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkersFragment extends Fragment implements WorkersListAdapter.workerClickListener {

    private RecyclerView mRecyclerView;
    private WorkersListAdapter adapter;
    private List<Workers> mWorkers = new ArrayList<>();

    public WorkersFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        firestore.setFirestoreSettings(settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workers, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView_workers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        initListAdapter();
        return  view;
    }

    private void initListAdapter() {
        Query query = RepositoryFirebase.getQueryWorkers(mWorkers);
        FirestoreRecyclerOptions<Workers> options = new FirestoreRecyclerOptions.Builder<Workers>()
                .setQuery(query, Workers.class)
                .build();

        adapter = new WorkersListAdapter(options, this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClickItemWorker(int position) {
        Intent intent = new Intent(getContext(), RestaurantDetail.class);
        intent.putExtra("placeId", mWorkers.get(position).getPlaceId());
        intent.putExtra("restaurantName", mWorkers.get(position).getRestaurantName());
        startActivity(intent);
    }
}
