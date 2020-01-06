package fr.nelfdesign.go4lunch.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.sql.Array;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.utils.Utils;
import fr.nelfdesign.go4lunch.viewModels.MapViewModel;

public class RestaurantDetail extends AppCompatActivity {

    private static final int PERMISSION_CALL = 100;
    private String phonenumber;
    private String websiteUrl;

    @BindView(R.id.toolbarDetails) Toolbar mToolbar;
    @BindView(R.id.imageRestaurant) ImageView mImageView;
    @BindView(R.id.restaurant_text_name) TextView mRestaurantTextname;
    @BindView(R.id.restaurant_text_adress) TextView mRestaurantTextadress;
    @BindView(R.id.restaurant_detail_star1) ImageView mRestaurantStar1;
    @BindView(R.id.restaurant_detail_star2) ImageView mRestaurantStar2;
    @BindView(R.id.restaurant_detail_star3) ImageView mRestaurantStar3;
    @BindView(R.id.call_image) ImageView callPhone;
    @BindView(R.id.website) ImageView websiteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ButterKnife.bind(this);

        //Add toolbar and return arrow
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (mToolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //get placeid extra intent
        String placeId = getIntent().getStringExtra("placeId");

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


        String name;
        if (detailRestaurant.getName().length() > 23) {
            name = detailRestaurant.getName().substring(0, 23) + " ...";
        } else {
            name = detailRestaurant.getName();
        }
        mRestaurantTextname.setText(name);
        mRestaurantTextadress.setText(detailRestaurant.getFormatted_address());

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

        //configure click on like image
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

}
