package com.suja.bakingapp.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suja.bakingapp.R;
import com.suja.bakingapp.data.Steps;

/**
 * Created by Suja Manu on 4/5/2018.
 */

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepRecyclerViewHolder> {
    private Steps[] mSteps;
    private Context mContext;
    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnStepClickListener {
        void onStepSelected(int nextPos, Steps mStep);
    }


    public StepRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        this.mCallback=(OnStepClickListener) mContext;
    }

    @Override
    public StepRecyclerViewAdapter.StepRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.step_item_layout,parent, false);
        return new StepRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StepRecyclerViewAdapter.StepRecyclerViewHolder holder, int position) {
        holder.mStepShortDesc.setText(mSteps[position].getShortDescription());
        String imagePath=mSteps[position].getThumbnailUrl();
        if(imagePath!=null&&!imagePath.equalsIgnoreCase(""))
        {
            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                    holder.mImage.setImageResource(R.drawable.icons_chef_hat);
                }
            });
            builder.build().load(imagePath).into(holder.mImage);
           /* Picasso.with(mContext)
                    .load(imagePath)
                    .resize(20, 20)                        // optional
                    .into(holder.mImage);*/}
        Log.i("step name",mSteps[position].getShortDescription());
    }
    public void setSteps(Steps[] steps)
    {
        mSteps=steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        Log.i("steps count", String.valueOf(mSteps.length));
        return mSteps.length;
    }

    public class StepRecyclerViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {

        TextView mStepShortDesc;
        private ImageView mImage;

        public StepRecyclerViewHolder(View itemView) {
            super(itemView);
            mStepShortDesc=itemView.findViewById(R.id.step_short_desc);
            mImage=itemView.findViewById(R.id.step_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            mCallback.onStepSelected(getAdapterPosition(),mSteps[getAdapterPosition()]);


        }

    }
}

