package com.example.distancefromhome;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // declare the three objects for the text boxes in this fragment
    private TextView txtActualDist;
    private TextView lblMsgBefore;
    private TextView lblMsgAfter;

    public DisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static DisplayFragment newInstance(String param1, String param2) {
        DisplayFragment fragment = new DisplayFragment();
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
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        Typeface futura = Typeface.createFromAsset(getContext().getAssets(), "fonts/futura medium condensed bt.ttf");

        // assign the text boxes to the declared objects
        txtActualDist = (TextView)view.findViewById(R.id.textActualDistance);
        lblMsgBefore = (TextView)view.findViewById(R.id.textMsgBefore);
        lblMsgAfter = (TextView)view.findViewById(R.id.textMsgAfter);

        txtActualDist.setTypeface(futura);
        lblMsgBefore.setTypeface(futura);
        lblMsgAfter.setTypeface(futura);

        return view;
    }

    void displayDistance(GpsStamp stamp){
        if (stamp.getDistance() > 2000){
            // the value returned by the class is in metres, not kilometres!
            // red if greater than 2km
            txtActualDist.setTextColor(Color.argb(255,255,0,0));
        }
        else{
            // green if inside 2km
            txtActualDist.setTextColor(Color.argb(255,0,255,0));
        }

        String myStr = stamp.getDistanceToString() + "km";
        txtActualDist.setText(myStr);

    }
}
