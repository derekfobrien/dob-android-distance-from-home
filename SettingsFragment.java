package com.example.distancefromhome;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Typeface;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // declare the objects associated with the controls
    private TextView txtCurrentCoords;
    private TextView lblGPSBefore;
    private Button cmdSetHome;
    private Switch swShowFullNumbers;

    SettingsListener activityCallback;

    // this interface is for communicating with the Main Activity when
    // the button is pressed
    // this method is called in the Main Activity
    public interface SettingsListener{
        void onButtonClicked();
    }


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // assign the variables to the controls
        txtCurrentCoords = (TextView)view.findViewById(R.id.textGPSCurrent);
        lblGPSBefore = (TextView)view.findViewById(R.id.textGPSBefore);
        cmdSetHome = (Button)view.findViewById(R.id.button);
        cmdSetHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                buttonClicked(v);

            }
        });
        swShowFullNumbers = (Switch)view.findViewById(R.id.swDisplayFullValues);

        // Assign font for the text boxes etc.
        Typeface futura = Typeface.createFromAsset(getContext().getAssets(), "fonts/futura medium condensed bt.ttf");
        txtCurrentCoords.setTypeface(futura);
        lblGPSBefore.setTypeface(futura);

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            activityCallback = (SettingsListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement SettingsListener");
        }
    }

    // calls the onButtonClicked method in the MainActivity class when the button is clicked
    private void buttonClicked(View view){
        activityCallback.onButtonClicked();
    }

    // display the current location coordinates
    void displayCurrentCoords(GpsStamp stamp){
        txtCurrentCoords.setText(stamp.getFullCoords(swShowFullNumbers.isChecked()));
    }
}
