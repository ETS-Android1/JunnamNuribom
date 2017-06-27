package com.junnam.nuribom.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junnam.nuribom.DTO.MyFavorData;
import com.junnam.nuribom.R;

import java.util.ArrayList;

/**
 * Created by hong on 2016. 10. 9..
 */
public class MyFavorRecycleViewAdapter extends RecyclerView.Adapter<MyFavorRecycleViewAdapter.ViewHolder> {

    private final ArrayList<MyFavorData> mValues;
    private final MyFavorFragment.OnFavorFragmentInteractionListener mListener;

    public MyFavorRecycleViewAdapter(ArrayList<MyFavorData> items, MyFavorFragment.OnFavorFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_favor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mTypeView.setText(mValues.get(position).getType_name());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnFavorFragmentInteractionListener(holder.mItem);
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
        public MyFavorData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mView.setClickable(true);
            mNameView = (TextView) view.findViewById(R.id.name);
            mTypeView = (TextView) view.findViewById(R.id.type);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
