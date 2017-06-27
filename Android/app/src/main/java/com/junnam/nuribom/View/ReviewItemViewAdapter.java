package com.junnam.nuribom.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.junnam.nuribom.DTO.ReviewItemData;
import com.junnam.nuribom.R;

import java.util.ArrayList;

/**
 * Created by hong on 2016. 10. 9..
 */
public class ReviewItemViewAdapter extends ArrayAdapter<ReviewItemData> {

    private Context context;
    private ArrayList<ReviewItemData> items;

    private TextView textView_id, textView_content;
    private RatingBar ratingBar;



    public ReviewItemViewAdapter(Context context, int layoutViewResourceId, ArrayList<ReviewItemData> items) {
        super(context, layoutViewResourceId, items);

        this.context = context;
        this.items = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_review, null);
        }

        textView_id = (TextView)convertView.findViewById(R.id.review_id);

        textView_content = (TextView)convertView.findViewById(R.id.review_content);
        ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);

        ReviewItemData reviewItemData = items.get(position);

        textView_id.setText(reviewItemData.getMember_id());
        textView_id.setContentDescription("후기 작성자 아이디 ," +items.get(position).getMember_id() +", 평점 " +String.valueOf(items.get(position).getRate()) +"점");
        textView_content.setText(reviewItemData.getContent());
        textView_content.setContentDescription("후기 내용 , " +items.get(position).getContent());
        ratingBar.setRating(reviewItemData.getRate());


        return convertView;
    }

}
