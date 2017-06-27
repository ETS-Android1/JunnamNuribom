package com.junnam.nuribom.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.junnam.nuribom.R;

public class DialogGuideFragment extends DialogFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private TextView textView_guide;
    private ImageView imageView_guide;
    private int guideCount;
    private ImageButton button_close;
    private Button button_pre, button_next;

//    private OnFragmentInteractionListener mListener;

    public DialogGuideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogGuideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogGuideFragment newInstance(String param1, String param2) {
        DialogGuideFragment fragment = new DialogGuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dialog_guide, container, false);
        guideCount = 1;
        textView_guide = (TextView)rootView.findViewById(R.id.textView_guide);
        textView_guide.setText("전남 장애인 누리봄은\n" +
                "자신의 위치와 가까운\n병원, 재활센터, 응급의료 병원, 약국의 정보를\n" +
                "\"지도와 리스트\" 로 확인할 수 있습니다.");
        imageView_guide = (ImageView)rootView.findViewById(R.id.imageView_guide);
        imageView_guide.setImageResource(R.drawable.image_guide1);
        button_close = (ImageButton)rootView.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        button_pre = (Button)rootView.findViewById(R.id.button_pre);
        button_next = (Button)rootView.findViewById(R.id.button_next);
        button_pre.setOnClickListener(this);
        button_next.setOnClickListener(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_pre:
                if(guideCount == 1) {
                    Toast.makeText(getActivity(), "처음 페이지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    guideCount--;
                    initGuide();
                }
                break;
            case R.id.button_next:
                if(guideCount == 5) {
                    Toast.makeText(getActivity(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    guideCount++;
                    initGuide();
                }
                break;
        }
    }

    private void initGuide() {
        switch (guideCount) {
            case 1:
                textView_guide.setText("전남 장애인 누리봄은\n" +
                        "자신의 위치와 가까운\n병원, 재활센터, 응급의료 병원, 약국의 정보를\n" +
                        "\"지도와 리스트\" 로 확인할 수 있습니다.");
                imageView_guide.setImageResource(R.drawable.image_guide1);
                break;
            case 2:
                textView_guide.setText("\"검색 필터\" 를 통해\n원하는 의료기관의 정보만을\n확인 할 수 있습니다.");
                imageView_guide.setImageResource(R.drawable.image_guide2);
                break;
            case 3:
                textView_guide.setText("의료기관을 이용 하고 난 뒤 후기를 남겨\n의료기관에 대한 \"평가\" 를 할 수 있습니다.");
                imageView_guide.setImageResource(R.drawable.image_guide3);
                break;
            case 4:
                textView_guide.setText("자주 이용하는 의료기관에 대한 \n\"단골 장소\" 를 지정할 수 있습니다.");
                imageView_guide.setImageResource(R.drawable.image_guide4);
                break;
            case 5:
                textView_guide.setText("시각 장애인을 위한\n\"톡백\" 서비스를 제공합니다.\n" +
                        "시각장애인은 리스트로 정보를 \n확인하는 것을 권장합니다.");
                imageView_guide.setImageResource(R.drawable.image_guide5);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
