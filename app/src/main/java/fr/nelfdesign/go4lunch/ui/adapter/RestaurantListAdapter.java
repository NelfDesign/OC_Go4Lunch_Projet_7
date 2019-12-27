package fr.nelfdesign.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

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
        @BindView(R.id.stars) LinearLayout mStars;
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

            String path2 = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + restaurantDetail.getPhotoReference() +
                    "&key=" + BuildConfig.google_maps_key;

            // Images
            if (restaurantDetail.getPhotoReference() != null){
                glide.load(path2)
                        .error(R.drawable.pic_logo_restaurant_400x400)
                        .into(mRestaurantImage);
            }
            else {
                this.mRestaurantImage.setImageResource(R.drawable.ic_bol);
            }
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

}
