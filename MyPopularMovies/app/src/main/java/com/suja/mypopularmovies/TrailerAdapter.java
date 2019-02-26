package com.suja.mypopularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suja.popularmovies.R;

/**
 * Created by Suja Manu on 3/7/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private Trailer[] trailers;

    //private TextView trailerName;

    private final TrailerAdapterOnClickListener mClickHandler;

    public TrailerAdapter( TrailerAdapterOnClickListener mClickHandler) {

        this.mClickHandler = mClickHandler;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickListener {
        void onClickOpenYoutube(String youtubeVideo, String trailerName);
       void shareTrailer(String trailerId);
    }
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,(RecyclerView)parent.findViewById(R.id.trailer_list), shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }


    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

public  final TextView trailerName;
public final ImageView trailerImage;
public final ImageView shareTrailer;
        public TrailerAdapterViewHolder(View view) {
            super(view);
            trailerName = view.findViewById(R.id.trailer_name);
            trailerImage=view.findViewById(R.id.trailer_image);
            shareTrailer=view.findViewById(R.id.share_trailer);

        }


    }


    @Override
    public void onBindViewHolder(final TrailerAdapterViewHolder holder, final int position) {
        Trailer trailer = trailers[position];
        holder.trailerName.setText(trailer.getTrailerName());
        holder.trailerImage.setTag(trailer.getTrailerId());
        holder.shareTrailer.setTag(trailer.getTrailerId());

        holder.trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mClickHandler.onClickOpenYoutube(trailers[position].getTrailerId(),trailers[position].getTrailerName());
            }
        });
        holder.trailerName.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                mClickHandler.onClickOpenYoutube(trailers[position].getTrailerId(), trailers[position].getTrailerName());
            }
        });
        holder.shareTrailer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                mClickHandler.shareTrailer(trailers[position].getTrailerId());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.length;
    }
    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }


}
