package com.junnam.nuribom.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.junnam.nuribom.DTO.MyReviewData;
import com.junnam.nuribom.R;

import java.util.ArrayList;

/**
 * Created by hong on 2016. 10. 10..
 */
public class MyReviewRecycleViewAdapter extends RecyclerView.Adapter<MyReviewRecycleViewAdapter.ViewHolder> {

    private final ArrayList<MyReviewData> mValues;
    private final MyReviewFragment.OnReviewFragmentInteractionListener mListener;

    public MyReviewRecycleViewAdapter(ArrayList<MyReviewData> items, MyReviewFragment.OnReviewFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mNameView.setContentDescription("이름 ," +mValues.get(position).getName() +", 평점 " +String.valueOf(mValues.get(position).getRate()) +"점");
        holder.mTypeView.setText(mValues.get(position).getType_name());
        holder.mRatingBar.setRating(mValues.get(position).getRate());
        holder.mContentView.setText(mValues.get(position).getContent());
        holder.mContentView.setContentDescription("후기 내용 ," +mValues.get(position).getContent());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnReviewFragmentInteractionListener(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mTypeView;
        public final RatingBar mRatingBar;
        public final TextView mContentView;
        public MyReviewData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mTypeView = (TextView) view.findViewById(R.id.type);
            mRatingBar = (RatingBar)view.findViewById(R.id.ratingBar);
            mContentView = (TextView)view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
