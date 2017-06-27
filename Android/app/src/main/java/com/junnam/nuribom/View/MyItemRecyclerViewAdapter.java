package com.junnam.nuribom.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.junnam.nuribom.DTO.ListItemData;
import com.junnam.nuribom.R;
import com.junnam.nuribom.View.ItemFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ListItemData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<ListItemData> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<ListItemData> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mNameView.setContentDescription(mValues.get(position).getName() +", 평점 " + String.valueOf(mValues.get(position).getRate()) + "점");
        switch (mValues.get(position).getType()) { // 1 = hospital 2 = center 3 = emergency 4 = dentist
            case 1:
                holder.mTypeView.setText("병원");
                holder.mTypeView.setContentDescription("타입 병원");
                break;
            case 2:
                holder.mTypeView.setText("재활센터");
                holder.mTypeView.setContentDescription("타입 재활센터");
                break;
            case 3:
                holder.mTypeView.setText("응급의료병원");
                holder.mTypeView.setContentDescription("타입 응급의료병원");
                break;
            case 4:
                holder.mTypeView.setText("약국");
                holder.mTypeView.setContentDescription("타입 약국");
                break;
        }
        double distance = Double.parseDouble(String.format("%.2f",mValues.get(position).getDistance()));
        holder.mDistanceView.setText(String.valueOf(distance) + "Km");
        holder.mDistanceView.setContentDescription(String.valueOf(distance) +"킬로미터 떨어져있습니다.");
        holder.mAddrView.setText(mValues.get(position).getAddr());
        holder.ratingBar.setRating(mValues.get(position).getRate());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mDistanceView;
        public final TextView mAddrView;
        public final RatingBar ratingBar;
        public ListItemData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mTypeView = (TextView) view.findViewById(R.id.type);
            mDistanceView = (TextView) view.findViewById(R.id.distance);
            mAddrView = (TextView) view.findViewById(R.id.addr);
            ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
