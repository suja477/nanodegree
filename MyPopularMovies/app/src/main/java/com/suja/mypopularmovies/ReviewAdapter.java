package com.suja.mypopularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suja.popularmovies.R;

/**
 * Created by Suja Manu on 3/7/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private Review[] reviews;
    //private TextView trailerName;
    private final int VIEW_ITEM_HEADER = 1;  // type: name/value
    private final int VIEW_ITEM_FOOTER = 0;



    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,(RecyclerView)parent.findViewById(R.id.review_list), shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }


    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private   final TextView reviewName;
        private final TextView reviewContent;

        private ReviewAdapterViewHolder(View view) {
            super(view);
            reviewName = (TextView) view.findViewById(R.id.review_name);
            reviewContent=(TextView)view.findViewById(R.id.review_content);

            // COMPLETED (7) Call setOnClickListener on the view passed into the constructor (use 'this' as the OnClickListener)
            view.setOnClickListener(this);
        }

        // COMPLETED (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method
        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review review = reviews[adapterPosition];

        }
    }


    @Override
    public void onBindViewHolder(final ReviewAdapterViewHolder holder, int position) {
        final Review review = reviews[position];

        holder.reviewName.setText(review.getReviewerName());
        holder.reviewContent.setText(review.getReviewContent());

       // holder.reviewContent.setVisibility(View.INVISIBLE);
        holder.reviewContent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(holder.reviewContent.getLineCount()==2)
                holder.reviewContent.setMaxLines( review.getReviewContent().length());
                else holder.reviewContent.setMaxLines(2);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == reviews) return 0;
        return reviews.length;
    }
    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
            // declare your header views
        }
    }


}
