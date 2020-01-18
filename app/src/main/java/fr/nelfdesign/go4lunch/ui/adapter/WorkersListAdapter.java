package fr.nelfdesign.go4lunch.ui.adapter;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;
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
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.R.color;
import fr.nelfdesign.go4lunch.models.Workers;
import io.opencensus.resource.Resource;

import static fr.nelfdesign.go4lunch.R.color.background;

/**
 * Created by Nelfdesign at 17/12/2019
 * fr.nelfdesign.go4lunch.ui.adapter
 */
public class WorkersListAdapter extends FirestoreRecyclerAdapter<Workers, WorkersListAdapter.WorkersItemViewholder> {

    public interface workerClickListener{
        void onClickItemWorker(int position);
    }

    private workerClickListener mWorkerClickListener;

    public class WorkersItemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.worker_avatar) ImageView mImageView;
        @BindView(R.id.worker_name) TextView mTextView;

        workerClickListener mWorkerClickListener;

        WorkersItemViewholder(@NonNull View itemView, workerClickListener listener) {
            super(itemView);
            this.mWorkerClickListener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mWorkerClickListener.onClickItemWorker(getAdapterPosition());
        }
    }

    public WorkersListAdapter(@NonNull FirestoreRecyclerOptions<Workers> options, workerClickListener mWorkerListener) {
        super(options);
        this.mWorkerClickListener = mWorkerListener;
    }

    @NonNull
    @Override
    public WorkersItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_worker,parent, false);
        return new WorkersItemViewholder(view, mWorkerClickListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkersItemViewholder holder, int i, @NonNull Workers workers) {
        Resources resource = holder.itemView.getContext().getResources();
        String text;
        if (!workers.getRestaurantName().equals("")){
            text = workers.getName() + " " + resource.getString(R.string.is_eating_at) + workers.getRestaurantName();
        }else{
            text = workers.getName() + " " + resource.getString(R.string.hasn_t_decided);
            holder.mTextView.setTextColor(resource.getColor(color.color_workers));
        }
        holder.mTextView.setText(text);


        Glide.with(holder.mImageView.getContext())
                .load(workers.getAvatarUrl())
                .error(R.drawable.pic_logo_restaurant_400x400)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageView);
    }

}
