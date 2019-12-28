package fr.nelfdesign.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantItemViewHolder> {

    public interface onClickRestaurantitemListener{
        void onClickRestaurantItem(int position);
    }

    private ArrayList<Restaurant> mRestaurantList;
    private RequestManager glide;
    private onClickRestaurantitemListener mOnClickRestaurantitemListener;

    public RestaurantListAdapter(ArrayList<Restaurant> restaurantList, RequestManager glide, onClickRestaurantitemListener onClickRestaurantitemListener) {
        mRestaurantList = restaurantList;
        this.glide = glide;
        this.mOnClickRestaurantitemListener = onClickRestaurantitemListener;
    }

    class RestaurantItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.restaurant_name) TextView mRestaurantName;
        @BindView(R.id.restaurant_category_and_adresse) TextView mCategory;
        @BindView(R.id.restaurant_hour) TextView mHourRestaurant;
        @BindView(R.id.restaurant_distance) TextView mDistance;
        @BindView(R.id.restaurant_image) ImageView mRestaurantImage;
        @BindView(R.id.star_1) ImageView mStars1;
        @BindView(R.id.star_2) ImageView mStars2;
        @BindView(R.id.star_3) ImageView mStars3;
        @BindView(R.id.workers_number) TextView mWorkersNumbers;

        onClickRestaurantitemListener mListener;

        RestaurantItemViewHolder(@NonNull View itemView, onClickRestaurantitemListener listener) {
            super(itemView);
            this.mListener = listener;
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        void updateWithDetailRestaurant(Restaurant restaurantDetail, RequestManager glide){

            this.mRestaurantName.setText(restaurantDetail.getName());
            this.mCategory.setText(restaurantDetail.getAddress());

            String open;

            if (restaurantDetail.getHour()){
                open = "Restaurant open";
            }else{
                open = "Not open yet";
            }

            this.mHourRestaurant.setText(open);

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

            //Stars according to rating level
            int rating;
            if (restaurantDetail.getRating() > 0){
                rating = starsAccordingToRating(restaurantDetail.getRating());
            }else{
                rating = 0;
            }

            starsView(rating, mStars1, mStars2, mStars3);

        }

        @Override
        public void onClick(View v) {
            mListener.onClickRestaurantItem(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RestaurantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantItemViewHolder(view, mOnClickRestaurantitemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemViewHolder holder, int position) {

            holder.updateWithDetailRestaurant(this.mRestaurantList.get(position),this.glide);

    }

    @Override
    public int getItemCount() {
        return (mRestaurantList != null) ? mRestaurantList.size() : 0;
    }

    private int starsAccordingToRating(double rating){
        if ( rating <= 2){
            return 1;
        }else if (rating < 3.7){
            return 2;
        }else {
            return 3;
        }
    }

    private void starsView(int rating, ImageView s1,ImageView s2,ImageView s3){
        switch (rating){
            case 0 :
                s1.setVisibility(View.GONE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                break;
            case 1 :
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                break;
            case 2:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.GONE);
                break;
            case 3:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.VISIBLE);
                break;

        }
    }
}
