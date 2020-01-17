package fr.nelfdesign.go4lunch.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.utils.Utils;

import static fr.nelfdesign.go4lunch.utils.Utils.starsAccordingToRating;

/**
 * Created by Nelfdesign at 29/12/2019
 * fr.nelfdesign.go4lunch.ui.adapter
 */
public class RestaurantItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.restaurant_name) TextView mRestaurantName;
    @BindView(R.id.restaurant_category_and_adress) TextView mCategory;
    @BindView(R.id.restaurant_hour) TextView mHourRestaurant;
    @BindView(R.id.restaurant_distance) TextView mDistance;
    @BindView(R.id.restaurant_image) ImageView mRestaurantImage;
    @BindView(R.id.worker_icon_item_restaurant) LinearLayout mLinearLayout;
    @BindView(R.id.workers_number) TextView getWorkersNumbers;
    @BindView(R.id.star_1) ImageView mStars1;
    @BindView(R.id.star_2) ImageView mStars2;
    @BindView(R.id.star_3) ImageView mStars3;

    private RestaurantListAdapter.onClickRestaurantItemListener mListener;

    RestaurantItemViewHolder(@NonNull View itemView, RestaurantListAdapter.onClickRestaurantItemListener listener) {
        super(itemView);
        this.mListener = listener;
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

    void updateWithDetailRestaurant(Restaurant restaurantDetail, RequestManager glide){

        this.mRestaurantName.setText(restaurantDetail.getName());
        this.mCategory.setText(restaurantDetail.getAddress());

        if ((restaurantDetail.getHour())) {
            this.mHourRestaurant.setText(R.string.restaurant_open);
        } else {
            this.mHourRestaurant.setText(R.string.not_open_yet);
        }

        // Restaurants images
        if (restaurantDetail.getPhotoReference().isEmpty()){
            mRestaurantImage.setImageResource(R.drawable.ic_bol);
        }else {
            String path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + restaurantDetail.getPhotoReference() +
                    "&key=" + BuildConfig.google_maps_key;

            glide.load(path)
                    .error(R.drawable.pic_logo_restaurant_400x400)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestaurantImage);
        }

        //distance
        if (restaurantDetail.getDistance() > 0){
            String dist = restaurantDetail.getDistance() + " m";
            mDistance.setText(dist);
        }

        //workers
        if (restaurantDetail.getWorkers() > 0){
            mLinearLayout.setVisibility(View.VISIBLE);
            String text = "(" + restaurantDetail.getWorkers() + ")";
            getWorkersNumbers.setText(text);
        }else{
            mLinearLayout.setVisibility(View.INVISIBLE);
        }

        //Stars according to rating level
        int rating;
        if (restaurantDetail.getRating() > 0){
            rating = starsAccordingToRating(restaurantDetail.getRating());
        }else{
            rating = 0;
        }

       Utils.starsView(rating, mStars1, mStars2, mStars3);

    }

    @Override
    public void onClick(View v) {
        mListener.onClickRestaurantItem(getAdapterPosition());
    }


}
