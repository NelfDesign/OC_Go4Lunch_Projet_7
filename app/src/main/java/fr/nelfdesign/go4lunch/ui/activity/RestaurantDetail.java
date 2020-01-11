package fr.nelfdesign.go4lunch.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.RepositoryFirebase;
import fr.nelfdesign.go4lunch.apiFirebase.RestaurantsFavorisHelper;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.apiGoogleMap.RestaurantService;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.RestaurantFavoris;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.adapter.DetailWorkerAdapter;
import fr.nelfdesign.go4lunch.ui.viewModels.MapViewModel;
import fr.nelfdesign.go4lunch.utils.Utils;
import io.reactivex.Observable;
import timber.log.Timber;

public class RestaurantDetail extends BaseActivity {

    private static final int PERMISSION_CALL = 100;
    private String phonenumber;
    private String websiteUrl;
    private DetailWorkerAdapter adapter;
    private ArrayList<Workers> mWorkers;
    private ArrayList<RestaurantFavoris> mRestaurantFavorises;
    String nameResto;
    String placeId;

    @BindView(R.id.coordinator_detail) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbarDetails) Toolbar mToolbar;
    @BindView(R.id.imageRestaurant) ImageView mImageView;
    @BindView(R.id.restaurant_text_name) TextView mRestaurantTextname;
    @BindView(R.id.restaurant_text_adress) TextView mRestaurantTextadress;
    @BindView(R.id.text_Like) TextView mTextLike;
    @BindView(R.id.text_favorite) TextView mTextFavorite;
    @BindView(R.id.restaurant_detail_star1) ImageView mRestaurantStar1;
    @BindView(R.id.restaurant_detail_star2) ImageView mRestaurantStar2;
    @BindView(R.id.restaurant_detail_star3) ImageView mRestaurantStar3;
    @BindView(R.id.call_image) ImageButton callPhone;
    @BindView(R.id.website) ImageButton websiteButton;
    @BindView(R.id.like) ImageButton likeButton;
    @BindView(R.id.fab_restaurant_detail) FloatingActionButton mFloatingActionButton;
    @BindView(R.id.favorite_restaurant) ImageButton favoriteButton;
    @BindView(R.id.recyclerView_workers_restaurant_detail) RecyclerView mRecyclerView;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_restaurant_detail;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Add toolbar and return arrow
        this.configureToolBar("");

        if (mToolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //get placeid and name extra intent
        placeId = getIntent().getStringExtra("placeId");
        nameResto = getIntent().getStringExtra("restaurantName");

        MapViewModel mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getDetailRestaurant(placeId).observe(this, this::updateUi);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
        }
    }

    private void updateUi(DetailRestaurant detailRestaurant) {
        //listen change on worker list
        final CollectionReference workersRef = WorkersHelper.getWorkersCollection();
        workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkers = new ArrayList<>();

            for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {

                if(Objects.requireNonNull(data.get("restaurantName")).toString().equals(detailRestaurant.getName())){
                    Workers workers = new Workers(data.get("name").toString(),
                            data.get("avatarUrl").toString(),
                            data.get("restaurantName").toString(),
                            data.get("placeId").toString());
                    mWorkers.add(workers);
                    Timber.i("snap workers : %s", mWorkers.size());
                }
            }
            initAdapter(mWorkers);
        });
        //path for photo url
        String path;
        if (detailRestaurant.getPhotoReference() == null) {
            path = "https://www.chilhoweerv.com/storage/app/public/blog/noimage930.png";
        } else {
            path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + detailRestaurant.getPhotoReference() +
                    "&key=" + BuildConfig.google_maps_key;
        }

        Glide.with(this)
                .load(path)
                .apply(RequestOptions.centerCropTransform())
                .into(mImageView);
        //set name and address
        String name;
        if (detailRestaurant.getName().length() > 23) {
            name = detailRestaurant.getName().substring(0, 23) + " ...";
        } else {
            name = detailRestaurant.getName();
        }
        mRestaurantTextname.setText(name);
        mRestaurantTextadress.setText(detailRestaurant.getFormatted_address());
        //stars method according to rating
        Utils.starsView(Utils.starsAccordingToRating(detailRestaurant.getRating()), mRestaurantStar1, mRestaurantStar2, mRestaurantStar3);
        // Call restaurant if possible
        callPhone.setOnClickListener(v -> {
            if (detailRestaurant.getFormatted_phone_number() == null){
                Toast.makeText(this, "No telephone number for this restaurant", Toast.LENGTH_SHORT).show();
            }else {
                phonenumber = detailRestaurant.getFormatted_phone_number();
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE} ,
                            PERMISSION_CALL);
            }else {
                callPhone();
            }
        });

        // go to website if there is one
        websiteButton.setOnClickListener(view ->{
           if (detailRestaurant.getWebsite() == null){
               Toast.makeText(this, "No website for this restaurant", Toast.LENGTH_SHORT).show();
           }else {
               websiteUrl = detailRestaurant.getWebsite();
               callWebsiteUrl();
           }
        });

        //configure click on like star
        final CollectionReference refResto = RestaurantsFavorisHelper.getRestaurantsCollection();
        refResto.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mRestaurantFavorises = new ArrayList<>();

            for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {
                RestaurantFavoris resto = new RestaurantFavoris(data.get("name").toString(),
                        data.get("placeId").toString(),
                        data.get("address").toString(),
                        data.get("photoReference").toString(),
                        data.getDouble("rating"));

                mRestaurantFavorises.add(resto);

                for (RestaurantFavoris restoFav : mRestaurantFavorises){
                    if (restoFav.getPlaceId().equals(resto.getPlaceId())){
                        favoriteButton.setVisibility(View.VISIBLE);
                        mTextFavorite.setVisibility(View.VISIBLE);
                        likeButton.setVisibility(View.GONE);
                        mTextLike.setVisibility(View.GONE);
                    }else{
                        likeButton.setOnClickListener(v -> {
                            saveRestaurantToFavorite(resto);
                        });
                    }
                }
            }
            Timber.d("restofav list : %s", mRestaurantFavorises.size());
        });

        //configure click on FAB Button
        mFloatingActionButton.setOnClickListener(v ->{
            Query query = WorkersHelper.getAllWorkers().whereEqualTo("name",
                    Objects.requireNonNull(getCurrentUser()).getDisplayName());
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String id = document.getId();
                        WorkersHelper.updateRestaurantChoice(id,
                                detailRestaurant.getName(),
                                placeId);
                    }
                    Utils.showSnackBar(this.mCoordinatorLayout, getString(R.string.choosen_restaurant));
                    mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_checked));
                }
            });
        });
    }

    private void saveRestaurantToFavorite(RestaurantFavoris r) {
        RestaurantsFavorisHelper.createFavoriteRestaurant(r.getName(),
                        r.getPlaceId(),
                        r.getAddress(),
                        r.getPhotoReference(),
                        r.getRating()).addOnFailureListener(this.onFailureListener());

        Utils.showSnackBar(this.mCoordinatorLayout, getString(R.string.favorite_restaurant_message));
        favoriteButton.setVisibility(View.VISIBLE);
        mTextFavorite.setVisibility(View.VISIBLE);
        likeButton.setVisibility(View.GONE);
        mTextLike.setVisibility(View.GONE);
    }

    private void callWebsiteUrl() {
        Intent intentWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        startActivity(intentWebsite);
    }

    @SuppressLint("MissingPermission")
    private void callPhone() {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenumber));
        startActivity(intentCall);
    }

    private void initAdapter(ArrayList<Workers> workers){
        adapter = new DetailWorkerAdapter(workers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(adapter);
    }

    private ArrayList<RestaurantFavoris> initFavoriteListRestaurant() {
        mRestaurantFavorises = RepositoryFirebase.getFavoritesRestaurant();
        return mRestaurantFavorises;
    }

}
