package com.junnam.nuribom.View;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.junnam.nuribom.DTO.ListItemData;
import com.junnam.nuribom.DTO.MarkerData;
import com.junnam.nuribom.DTO.NuribomData;
import com.junnam.nuribom.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment  {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private LatLng myLatLng;
    private NuribomData nuribomData;
    private ArrayList<ListItemData> listItemDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter  recyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    public void setData(LatLng myLatLng, NuribomData nuribomData) {
        this.myLatLng = myLatLng;
        this.nuribomData = nuribomData;

        listItemDatas.clear();

        Location myLocation = new Location("Mylocation");
        myLocation.setLatitude(myLatLng.latitude);
        myLocation.setLongitude(myLatLng.longitude);

        for(int i=0; i<nuribomData.getDataSize(); i++) {
            MarkerData markerData = nuribomData.getData(i);

            if(markerData.getLocaton().latitude == 0 || markerData.getLocaton().longitude == 0)
                continue;

            if(markerData.getType() ==1 && nuribomData.isHospitalVisible() == false)
                continue;

            if(markerData.getType() == 2 && nuribomData.isCenterVisible() == false)
                continue;

            if(markerData.getType() == 3 && nuribomData.isEmergencyVisible() == false)
                continue;

            if(markerData.getType() == 4 && nuribomData.isDentistVisible() ==false)
                continue;

            Location markerLocation = new Location("Markerlocation");
            markerLocation.setLatitude(markerData.getLocaton().latitude);
            markerLocation.setLongitude(markerData.getLocaton().longitude);

            double distance = myLocation.distanceTo(markerLocation);
            ListItemData item = new ListItemData(markerData.getIdx(), markerData.getType(), markerData.getName(), distance/1000, markerData.getAddr(), markerData.getRate());

            listItemDatas.add(item);
        }


        Collections.sort(listItemDatas, new Comparator<ListItemData>() {
            @Override
            public int compare(ListItemData lhs, ListItemData rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getDistance() < rhs.getDistance() ? -1 : (lhs.getDistance() > rhs.getDistance() ) ? 1 : 0;
            }
        });

        recyclerViewAdapter.notifyDataSetChanged();
    }



    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

        }

        recyclerViewAdapter = new MyItemRecyclerViewAdapter(listItemDatas, mListener);
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ListItemData item);
    }
}
