package fr.nelfdesign.go4lunch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import butterknife.BindView;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.RepositoryFirebase;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.models.RestaurantFavoris;
import fr.nelfdesign.go4lunch.ui.adapter.RestaurantFavoritesAdapter;
import timber.log.Timber;

public class FavoritesRestaurant extends BaseActivity implements RestaurantFavoritesAdapter.favoritesClickListener{

    private RecyclerView mRecyclerView;
    private RestaurantFavoritesAdapter adapter;
    private ArrayList<RestaurantFavoris> mRestaurantFavorisList = new ArrayList<>();

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_favorites_restaurant;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = findViewById(R.id.recyclerView_favoris);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));

        this.configureToolBar(getString(R.string.favorites_restaurant));

        initListAdapter();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                Timber.i( "Search");
                //mSearchView.setVisibility(View.VISIBLE);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initListAdapter() {
        Query query = RepositoryFirebase.getQueryFavoritesRestaurant(mRestaurantFavorisList);
        FirestoreRecyclerOptions<RestaurantFavoris> options = new FirestoreRecyclerOptions.Builder<RestaurantFavoris>()
                .setQuery(query, RestaurantFavoris.class)
                .build();

        adapter = new RestaurantFavoritesAdapter(options, this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickItemResto(int position) {
        Intent intent = new Intent(this, RestaurantDetail.class);
        intent.putExtra("placeId", mRestaurantFavorisList.get(position).getPlaceId());
        intent.putExtra("restaurantName", mRestaurantFavorisList.get(position).getName());
        startActivity(intent);
    }
}
