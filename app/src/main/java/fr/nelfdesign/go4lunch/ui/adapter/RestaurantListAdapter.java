package fr.nelfdesign.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantItemViewHolder> {

    List<Restaurant> mRestaurantList;

    public RestaurantListAdapter(List<Restaurant> restaurantList) {
        mRestaurantList = restaurantList;
    }

    public class RestaurantItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.restaurant_name)
        TextView mRestaurantName;
        @BindView(R.id.restaurant_category_and_adresse)
        TextView mCategory;
        @BindView(R.id.restaurant_hour)
        TextView mHourRestaurant;
        @BindView(R.id.restaurant_distance)
        TextView mDistance;
        @BindView(R.id.restaurant_image)
        ImageView mRestaurantImage;
        @BindView(R.id.stars)
        LinearLayout mStars;
        @BindView(R.id.workers_number)
        TextView mWorkersNumbers;

        public RestaurantItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @NonNull
    @Override
    public RestaurantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemViewHolder holder, int position) {
        Restaurant restaurant = mRestaurantList.get(position);
        holder.mRestaurantName.setText(restaurant.getName());
        holder.mCategory.setText(restaurant.getCategory());
        holder.mHourRestaurant.setText(restaurant.getHour());
        holder.mDistance.setText(restaurant.getDistance());
        //holder.mWorkersNumbers.setText(restaurant.getStars());
    }

    @Override
    public int getItemCount() {
        return this.mRestaurantList.size();
    }
}
