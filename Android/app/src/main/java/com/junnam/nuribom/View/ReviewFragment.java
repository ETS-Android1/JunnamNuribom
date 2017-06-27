package com.junnam.nuribom.View;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.junnam.nuribom.R;
import com.junnam.nuribom.Util.JsonParse;
import com.junnam.nuribom.Util.URLDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends DialogFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "member_id";
    private static final String ARG_PARAM2 = "type";
    private static final String ARG_PARAM3 = "type_idx";
    private static final String ARG_PARAM4 = "type_name";
    private static final String ARG_PARAM5 = "name";

    // TODO: Rename and change types of parameters
    private String member_id;
    private int type;
    private int type_idx;
    private String type_name;
    private String name;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private Button button_insert;
    private ImageButton button_close;
    private ProgressBar progressBar;
    private float rate;
    private String content;
    private String oldContent;

    private float newRate;

    public ReviewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String member_id, int type, int type_idx, String type_name, String name) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, member_id);
        args.putInt(ARG_PARAM2, type);
        args.putInt(ARG_PARAM3, type_idx);
        args.putString(ARG_PARAM4, type_name);
        args.putString(ARG_PARAM5, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            member_id = getArguments().getString(ARG_PARAM1);
            type = getArguments().getInt(ARG_PARAM2);
            type_idx = getArguments().getInt(ARG_PARAM3);
            type_name = getArguments().getString(ARG_PARAM4);
            name = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_review, container, false);

        button_insert = (Button)rootView.findViewById(R.id.button_insert);
        button_insert.setOnClickListener(this);
        button_close = (ImageButton)rootView.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        return rootView;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_insert:
                content = ((EditText)rootView.findViewById(R.id.editText_content)).getText().toString();
                rate = ((RatingBar)rootView.findViewById(R.id.ratingBar)).getRating();
                if(content.equals("")) {
                    Toast.makeText(getActivity(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(rate == 0.0) {
                    Toast.makeText(getActivity(), "평점을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new InsertReviewAsyncTask().execute();

            break;
        }
    }

    public class InsertReviewAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonInsertReview();
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {
                Toast.makeText(getActivity(), "후기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                if (mListener != null) {
                    mListener.onFragmentInteraction(newRate, member_id, oldContent, rate);
                }
                dismiss();
            } else {
                Toast.makeText(getActivity(), "후기 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


    private String JsonInsertReview() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;
            oldContent = content;

            content = URLEncoder.encode(content, "UTF-8");
            type_name = URLEncoder.encode(type_name, "UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
            Log.d("MyTag", "content = " +content);

            json = jsonParse.getJSONFromUrl(URLDefine.INSERT_REVIEW_URL +"type=" +type +"&type_idx=" +type_idx +"&member_id=" +member_id +"&content=" +content +"&rate=" +rate +"&type_name=" +type_name +"&name=" +name);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    Log.d("MyTag", "isReview" + c.getString("isReview"));

                    if(c.getString("isReview").equals("false"))
                        return "false";

                    newRate = Float.parseFloat(c.getString("rate"));
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
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
        void onFragmentInteraction(Float newRate, String member_id, String content, float rate);
    }
}
