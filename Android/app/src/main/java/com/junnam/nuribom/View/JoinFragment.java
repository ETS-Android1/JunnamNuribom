package com.junnam.nuribom.View;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.junnam.nuribom.DTO.MemberData;
import com.junnam.nuribom.R;
import com.junnam.nuribom.Util.JsonParse;
import com.junnam.nuribom.Util.URLDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JoinFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JoinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinFragment extends DialogFragment implements OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private ProgressBar progressBar;
    private ImageButton button_close;
    private String member_id;
    private String member_pass;
    private String member_name;
    private String oldName;

    private MemberData memberData;

    public JoinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinFragment newInstance(String param1, String param2) {
        JoinFragment fragment = new JoinFragment();
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

        rootView = inflater.inflate(R.layout.fragment_join, container, false);

        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        ((Button)rootView.findViewById(R.id.button_join)).setOnClickListener(this);
        button_close = (ImageButton)rootView.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
        member_id = ((EditText)rootView.findViewById(R.id.editText_id)).getText().toString();
        if(!isValidEmail(member_id)) {
            Toast.makeText(getActivity(), "아이디 형식은 이메일입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        member_pass = ((EditText)rootView.findViewById(R.id.editText_pass)).getText().toString();
        member_name = ((EditText)rootView.findViewById(R.id.editText_name)).getText().toString();
        oldName = member_name;

        new JoinAsyncTask().execute();
    }

    public boolean isValidEmail(String inputStr) {

        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputStr);

        if( !m.matches() ) {
            return false;
        }
        return true;
    }

    public class JoinAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonJoin();
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {
                Toast.makeText(getActivity(), "회원가입이 성공하였습니다.", Toast.LENGTH_SHORT).show();
                if (mListener != null) {
                    mListener.onFragmentInteraction(memberData);
                }
                dismiss();

            } else {
                Toast.makeText(getActivity(), "회원가입이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


    private String JsonJoin() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;
            member_name = URLEncoder.encode(member_name, "UTF-8");

            json = jsonParse.getJSONFromUrl(URLDefine.INSERT_MEMBER_URL +"member_id=" +member_id +"&member_pass=" +member_pass +"&member_name=" +member_name);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    if(c.getString("isJoin").equals("false"))
                        return "false";
                    else {
                        memberData = new MemberData();
                        memberData.setMember_id(member_id);
                        memberData.setMember_pass(member_pass);
                        memberData.setMember_name(oldName);
                    }

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
        void onFragmentInteraction(MemberData memberData);
    }
}
