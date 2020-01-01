package fr.nelfdesign.go4lunch.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.utils.Utils;
import fr.nelfdesign.go4lunch.viewModels.MapViewModel;

public class RestaurantDetail extends AppCompatActivity {

    @BindView(R.id.imageRestaurant) ImageView mImageView;
    @BindView(R.id.restaurant_text_name) TextView mRestaurantTextname;
    @BindView(R.id.restaurant_text_adress) TextView mRestaurantTextadress;
    @BindView(R.id.restaurant_detail_star1) ImageView mRestaurantStar1;
    @BindView(R.id.restaurant_detail_star2) ImageView mRestaurantStar2;
    @BindView(R.id.restaurant_detail_star3) ImageView mRestaurantStar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ButterKnife.bind(this);
        String placeId = getIntent().getStringExtra("placeId");

        MapViewModel mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getDetailRestaurant(placeId).observe(this, this::updateUi);

    }

    private void updateUi(DetailRestaurant detailRestaurant) {
        String path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + detailRestaurant.getPhotoReference() +
                "&key=" + BuildConfig.google_maps_key;

        Glide.with(this)
                .load(path)
                .into(mImageView);

        mRestaurantTextname.setText(detailRestaurant.getName());
        mRestaurantTextadress.setText(detailRestaurant.getFormatted_address());

        Utils.starsView(Utils.starsAccordingToRating(detailRestaurant.getRating()), mRestaurantStar1, mRestaurantStar2, mRestaurantStar3);
    }

}
