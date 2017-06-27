package com.junnam.nuribom.View;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.junnam.nuribom.DTO.MyReviewData;
import com.junnam.nuribom.R;
import com.junnam.nuribom.Util.JsonParse;
import com.junnam.nuribom.Util.URLDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyReviewFragment.OnReviewFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyReviewFragment extends DialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PARAM = "member_id";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String member_id;

    private OnReviewFragmentInteractionListener mListener;
    private ArrayList<MyReviewData> reviewDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyReviewRecycleViewAdapter  recyclerViewAdapter;

    private ProgressBar progressBar;
    private ImageButton button_close;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyReviewFragment() {
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyReviewFragment newInstance(int columnCount, String member_id) {
        MyReviewFragment fragment = new MyReviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_PARAM, member_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            member_id = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_review, container, false);
        View recycleView = view.findViewById(R.id.list);


        //favorDatas = new ArrayList<>();

        // Set the adapter
        if (recycleView instanceof RecyclerView) {
            Context context = recycleView.getContext();
            recyclerView = (RecyclerView) recycleView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

        }

        recyclerViewAdapter = new MyReviewRecycleViewAdapter(reviewDatas, mListener);
        recyclerView.setAdapter(recyclerViewAdapter);
        Log.d("MyTag", "adapter = " +recyclerViewAdapter);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        button_close = (ImageButton)view.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        new MyReviewAsyncTask().execute();

        return view;
    }


    public class MyReviewAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonMyReview();
        }

        @Override
        protected void onPostExecute(String result) {
//            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {
                progressBar.setVisibility(View.GONE);
                Log.d("MyTag", "adapter2 = " +recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


    private String JsonMyReview() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_MYREVIEW_URL +"member_id=" +member_id);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");


                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    MyReviewData myReviewData = new MyReviewData();
                    myReviewData.setIdx(Integer.parseInt(c.getString("idx")));
                    myReviewData.setType(Integer.parseInt(c.getString("type")));
                    myReviewData.setType_idx(Integer.parseInt(c.getString("type_idx")));
                    myReviewData.setMember_id(c.getString("member_id"));
                    myReviewData.setType_name(c.getString("type_name"));
                    myReviewData.setContent(c.getString("content"));
                    myReviewData.setRate(Float.parseFloat(c.getString("rate")));
                    myReviewData.setName(c.getString("name"));

                    reviewDatas.add(myReviewData);
                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("MyTag", "Error = " + e.toString());
            e.printStackTrace();
            result = "false";
        }
        return result;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewFragmentInteractionListener) {
            mListener = (OnReviewFragmentInteractionListener) context;
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
    public interface OnReviewFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnReviewFragmentInteractionListener(MyReviewData item);
    }
}
