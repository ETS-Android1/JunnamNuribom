package com.junnam.nuribom.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.junnam.nuribom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "hospitalVisible";
    private static final String ARG_PARAM2 = "centerVisible";
    private static final String ARG_PARAM3 = "emergencyVisible";
    private static final String ARG_PARAM4 = "dentistVisible";

    // TODO: Rename and change types of parameters
    private boolean mParam1;
    private boolean mParam2;
    private boolean mParam3;
    private boolean mParam4;

    private CheckBox checkBox_hospital, checkBox_center, checkBox_emergency, checkBox_dentist;
    private Button button_ok;
    private ImageButton button_close;

    private OnFragmentInteractionListener mListener;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hospitalVisible Parameter 1.
     * @param centerVisible Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(boolean hospitalVisible, boolean centerVisible, boolean emergencyVisible, boolean dentistVisible) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, hospitalVisible);
        args.putBoolean(ARG_PARAM2, centerVisible);
        args.putBoolean(ARG_PARAM3, emergencyVisible);
        args.putBoolean(ARG_PARAM4, dentistVisible);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
            mParam3 = getArguments().getBoolean(ARG_PARAM3);
            mParam4 = getArguments().getBoolean(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_filter, container, false);


        checkBox_hospital = (CheckBox)rootView.findViewById(R.id.checkbox_hospital);
        checkBox_center = (CheckBox)rootView.findViewById(R.id.checkbox_center);
        checkBox_emergency = (CheckBox)rootView.findViewById(R.id.checkbox_emergency);
        checkBox_dentist = (CheckBox)rootView.findViewById(R.id.checkbox_dentist);

        checkBox_hospital.setChecked(mParam1);
        checkBox_center.setChecked(mParam2);
        checkBox_emergency.setChecked(mParam3);
        checkBox_dentist.setChecked(mParam4);

        button_ok = (Button)rootView.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(checkBox_hospital.isChecked(), checkBox_center.isChecked(), checkBox_emergency.isChecked(), checkBox_dentist.isChecked());
                dismiss();
            }
        });
        button_close = (ImageButton)rootView.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(boolean hospitalVisible, boolean centerVisible, boolean emergencyVisible, boolean dentistVisible) {
        if (mListener != null) {
            mListener.onFragmentInteraction(hospitalVisible, centerVisible, emergencyVisible, dentistVisible);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(boolean hospitalVisible, boolean centerVisible, boolean emergencyVisible, boolean dentistVisible);
    }
}
