package com.junnam.nuribom.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junnam.nuribom.R;

/**
 * Created by hong on 2016. 10. 11..
 */
public class GuideFragment extends Fragment {

    int position;

    public GuideFragment() {

    }

    public static GuideFragment newInstance(int position) {

        GuideFragment guideFragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        guideFragment.setArguments(args);
        return guideFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);

        position = getArguments().getInt("position");
        ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView_guide);
        TextView textView = (TextView)rootView.findViewById(R.id.textView_guide);

        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.image_guide1);
                textView.setText("전남 장애인 누리봄은\n" +
                        "자신의 위치와 가까운\n병원, 재활센터, 응급의료 병원, 약국의 정보를\n" +
                        "\"지도와 리스트\" 로 확인할 수 있습니다.");
                break;
            case 1:
                imageView.setImageResource(R.drawable.image_guide2);
                textView.setText("\"검색 필터\" 를 통해\n원하는 의료기관의 정보만을\n확인 할 수 있습니다.");
                break;
            case 2:
                imageView.setImageResource(R.drawable.image_guide3);
                textView.setText("의료기관을 이용 하고 난 뒤\n후기를 남겨 의료기관에 대한\n\"평가\" 를 할 수 있습니다.");
                break;
            case 3:
                imageView.setImageResource(R.drawable.image_guide4);
                textView.setText("자주 이용하는 의료기관에 대한\n\"단골 장소\" 를\n지정할 수 있습니다.");
                break;
            case 4:
                imageView.setImageResource(R.drawable.image_guide5);
                textView.setText("시각 장애인을 위한\n\"톡백\" 서비스를 제공합니다.\n" +
                        "시각장애인은 리스트로 정보를 \n확인하는 것을 권장합니다.");
                break;

        }

        return rootView;
    }


}