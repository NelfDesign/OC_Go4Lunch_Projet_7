package fr.nelfdesign.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.RestaurantFavoris;
import fr.nelfdesign.go4lunch.utils.Utils;

import static fr.nelfdesign.go4lunch.utils.Utils.starsAccordingToRating;

/**
 * Created by Nelfdesign at 12/01/2020
 * fr.nelfdesign.go4lunch.ui.adapter
 */
public class RestaurantFavoritesAdapter extends FirestoreRecyclerAdapter<RestaurantFavoris,
        RestaurantFavoritesAdapter.FavorisItemViewholder> {

    //interface to set the click
    public interface favoritesClickListener {
        void onClickItemResto(int position);
    }

    //FIELD
    private favoritesClickListener mFavoritesClickListener;

    //ViewHolder
    public class FavorisItemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.restaurant_name)
        TextView mRestaurantName;
        @BindView(R.id.restaurant_category_and_adress)
        TextView mAddress;
        @BindView(R.id.restaurant_image)
        ImageView mRestaurantImage;
        @BindView(R.id.star_1)
        ImageView mStars1;
        @BindView(R.id.star_2)
        ImageView mStars2;
        @BindView(R.id.star_3)
        ImageView mStars3;

        favoritesClickListener mFavoritesClickListener;

        FavorisItemViewholder(@NonNull View itemView, favoritesClickListener listener) {
            super(itemView);
            this.mFavoritesClickListener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFavoritesClickListener.onClickItemResto(getAdapterPosition());
        }
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options configuration
     */
    public RestaurantFavoritesAdapter(@NonNull FirestoreRecyclerOptions<RestaurantFavoris> options, favoritesClickListener listener) {
        super(options);
        this.mFavoritesClickListener = listener;
    }

    @NonNull
    @Override
    public FavorisItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant_favoris, parent, false);
        return new FavorisItemViewholder(view, mFavoritesClickListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull FavorisItemViewholder holder, int i,
                                    @NonNull RestaurantFavoris restaurantFavoris) {

        holder.mRestaurantName.setText(restaurantFavoris.getName());
        holder.mAddress.setText(restaurantFavoris.getAddress());

        // Restaurants images
        if (restaurantFavoris.getPhotoReference().isEmpty()) {
            holder.mRestaurantImage.setImageResource(R.drawable.ic_bol);
        } else {
            String path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + restaurantFavoris.getPhotoReference() +
                    "&key=" + BuildConfig.google_maps_key;

            Glide.with(holder.mRestaurantImage.getContext())
                    .load(path)
                    .error(R.drawable.pic_logo_restaurant_400x400)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.mRestaurantImage);
        }

        //Stars according to rating level
        int rating;
        if (restaurantFavoris.getRating() > 0) {
            rating = starsAccordingToRating(restaurantFavoris.getRating());
        } else {
            rating = 0;
        }

        Utils.starsView(rating, holder.mStars1, holder.mStars2, holder.mStars3);
    }

}
