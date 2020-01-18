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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Workers;

/**
 * Created by Nelfdesign at 06/01/2020
 * fr.nelfdesign.go4lunch.ui.adapter
 */
public class DetailWorkerAdapter extends RecyclerView.Adapter<DetailWorkerAdapter.DetailViewHolder> {

    private List<Workers> mWorkers;

    public DetailWorkerAdapter(List<Workers> workers) {
        mWorkers = workers;
    }

    class DetailViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.worker_avatar) ImageView mImageView;
        @BindView(R.id.worker_name) TextView mTextView;

        DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_worker, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Workers workers = mWorkers.get(position);


        holder.mTextView.setText(workers.getName());

        Glide.with(holder.mImageView.getContext())
                .load(workers.getAvatarUrl())
                .error(R.drawable.pic_logo_restaurant_400x400)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return (mWorkers != null) ? mWorkers.size() : 0;
    }
}
