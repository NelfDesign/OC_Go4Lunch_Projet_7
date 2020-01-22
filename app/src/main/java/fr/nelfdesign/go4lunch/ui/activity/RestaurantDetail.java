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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.RestaurantsFavorisHelper;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.RestaurantFavoris;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.adapter.DetailWorkerAdapter;
import fr.nelfdesign.go4lunch.ui.viewModels.MapViewModel;
import fr.nelfdesign.go4lunch.utils.Utils;
import timber.log.Timber;

public class RestaurantDetail extends BaseActivity {

    private static final int PERMISSION_CALL = 100;
    private String phonenumber;
    private String websiteUrl;
    private ArrayList<Workers> mWorkers;
    private ArrayList<RestaurantFavoris> mRestaurantFavorises;
    private ListenerRegistration mListenerRegistration = null;
    private String placeId;
    private  String nameResto;
    private RestaurantFavoris mRestaurantFavoris;

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

        //get placeId and name extra intent
        placeId = getIntent().getStringExtra("placeId");
        nameResto = getIntent().getStringExtra("restaurantName");

        getFavoriteRestaurant();

        MapViewModel mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getDetailRestaurant(placeId).observe(this, this::updateUi);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null){
            mListenerRegistration.remove();
        }
    }

    @OnClick({R.id.fab_restaurant_detail,
              R.id.like,
              R.id.favorite_restaurant})
    public void onClickFabButton(View view){

        switch (view.getId()){
            case R.id.fab_restaurant_detail:
                this.updateFab();
                break;
            case R.id.like:
                this.updateLikeButton();
                break;
            case R.id.favorite_restaurant :
                this.updateFavoriteRestaurant();
                break;
        }
    }

    private void updateFavoriteRestaurant() {
        getFavoriteRestaurant();
        for (RestaurantFavoris restaurantFavoris : mRestaurantFavorises){
            if (restaurantFavoris.getPlaceId().equalsIgnoreCase(placeId)){
                deleteRestaurantToFavorite(restaurantFavoris.getUid());
            }
        }
    }

    private void updateLikeButton() {
        saveRestaurantToFavorite(mRestaurantFavoris);
    }

    private void updateFab() {
        Query query = WorkersHelper.getAllWorkers().whereEqualTo("name",
                Objects.requireNonNull(getCurrentUser()).getDisplayName());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String id = document.getId();
                    if (placeId.equalsIgnoreCase(Objects.requireNonNull(document.get("placeId")).toString())){
                        updateRestaurantChoice(id, "", "",
                                getString(R.string.you_haven_t_choice_restaurant), false);
                    }else {
                        updateRestaurantChoice(id, nameResto, placeId, getString(R.string.chosen_restaurant), true);
                    }
                }
            }
        });
    }

    private void updateUi(DetailRestaurant detailRestaurant) {
        //listen change on worker list
        final CollectionReference workersRef = WorkersHelper.getWorkersCollection();
        mListenerRegistration = workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkers = new ArrayList<>();
            if (queryDocumentSnapshots != null){
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {

                    if (Objects.requireNonNull(data.get("restaurantName")).toString().equals(detailRestaurant.getName())) {

                        Workers workers = data.toObject(Workers.class);
                        mWorkers.add(workers);
                        Timber.i("snap workers : %s", mWorkers.size());
                    }
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
                Toast.makeText(this, R.string.no_telephone_number_for_this_restaurant, Toast.LENGTH_SHORT).show();
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
               Toast.makeText(this, R.string.no_website_for_restaurant, Toast.LENGTH_SHORT).show();
           }else {
               websiteUrl = detailRestaurant.getWebsite();
               callWebsiteUrl();
           }
        });

        mRestaurantFavoris = new RestaurantFavoris("", nameResto, placeId, detailRestaurant.getFormatted_address(),
                detailRestaurant.getPhotoReference(), detailRestaurant.getRating());
        //configure click on like star
        for (RestaurantFavoris restaurantFavoris : mRestaurantFavorises){
            if (restaurantFavoris.getPlaceId().equalsIgnoreCase(placeId)){
                favoriteButton.setVisibility(View.VISIBLE);
                mTextFavorite.setVisibility(View.VISIBLE);
                likeButton.setVisibility(View.GONE);
                mTextLike.setVisibility(View.GONE);
            }
        }
    }

    private void getFavoriteRestaurant(){
        final Query refResto = RestaurantsFavorisHelper.
                getAllRestaurantsFromWorkers(Objects.requireNonNull(getCurrentUser()).getDisplayName());

        refResto.get()
                .addOnCompleteListener(task -> {
                    mRestaurantFavorises = new ArrayList<>();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        RestaurantFavoris resto = document.toObject(RestaurantFavoris.class);
                        resto.setUid(document.getId());
                        Timber.d("resto uid = %s", resto.getUid());
                        mRestaurantFavorises.add(resto);
                    }
                    Timber.d("restofav list : %s", mRestaurantFavorises.size());
                });
    }

    private void updateRestaurantChoice(String id, String restoName, String placeId, String message, boolean isChosen) {
        WorkersHelper.updateRestaurantChoice(id, restoName, placeId);
        Utils.showSnackBar(this.mCoordinatorLayout, message);
        mFloatingActionButton.setImageResource(isChosen
                        ? R.drawable.ic_check_checked
                        : R.drawable.ic_check_circle_black_24dp);
    }

    private void deleteRestaurantToFavorite(String uid) {
        RestaurantsFavorisHelper.deleteRestaurant(Objects.requireNonNull(getCurrentUser()).getDisplayName(),uid);
        Utils.showSnackBar(this.mCoordinatorLayout, getResources().getString(R.string.restaurant_delete));
        favoriteButton.setVisibility(View.GONE);
        mTextFavorite.setVisibility(View.GONE);
        likeButton.setVisibility(View.VISIBLE);
        mTextLike.setVisibility(View.VISIBLE);
    }

    private void saveRestaurantToFavorite(RestaurantFavoris resto) {
        RestaurantsFavorisHelper.createFavoriteRestaurant(Objects.requireNonNull(getCurrentUser()).getDisplayName(),
                        resto.getUid(), resto.getName(), resto.getPlaceId(),
                        resto.getAddress(), resto.getPhotoReference(),
                        resto.getRating()).addOnFailureListener(this.onFailureListener());

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
        DetailWorkerAdapter adapter = new DetailWorkerAdapter(workers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(adapter);
    }

}
