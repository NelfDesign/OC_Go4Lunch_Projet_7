package fr.nelfdesign.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantItemViewHolder> {

    public interface onClickRestaurantItemListener{
        void onClickRestaurantItem(int position);
    }

    private ArrayList<Restaurant> mRestaurantList;
    private RequestManager glide;
    private onClickRestaurantItemListener mOnClickRestaurantitemListener;

    public RestaurantListAdapter(ArrayList<Restaurant> restaurantList,
                                 RequestManager glide,
                                 onClickRestaurantItemListener onClickRestaurantitemListener) {

        mRestaurantList = restaurantList;
        this.glide = glide;
        this.mOnClickRestaurantitemListener = onClickRestaurantitemListener;
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
