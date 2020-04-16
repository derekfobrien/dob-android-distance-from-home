package com.example.distancefromhome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.os.Bundle;

public class MainActivity extends FragmentActivity implements SettingsFragment.SettingsListener {

    private LocationManager locationManager;
    private LocationListener listener;

    private GpsStamp myStamp;

    private SettingsFragment sf;
    private DisplayFragment df;

    private String fnText="MyHomeGPS.txt";
    private String filepath="MyFileStorage";
    File myExternalFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declare the classwide objects
        sf = (SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentSettings);
        df = (DisplayFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentDisplay);

        myExternalFile = new File(getExternalFilesDir(filepath), fnText);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        myStamp = new GpsStamp();

        // if there is an file with the home coordinates, read them from the file
        if (myExternalFile.exists()){
            readHomeCoords();
        }

        // the procedure when a new location is read
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    myStamp.setCurrentCoords(location.getLatitude(), location.getLongitude());
                    myStamp.setDistance();
                    sf.displayCurrentCoords(myStamp);
                    df.displayDistance(myStamp);

                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        configureButton();

    }

    // executed following clicking of the button - called by a line in the buttonClicked() method
    // in the SettingsFragment class
    public void onButtonClicked(){
        myStamp.setHomeCoords();

        // now we use a confirm button, to confirm that these coordinates are to be
        // written into the file
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set the title
        alertDialogBuilder.setTitle("Confirm save"); // set the title
        alertDialogBuilder.setMessage("Are you sure you want to save the Home Coordinates into the file?"); // setting the message
        alertDialogBuilder.setCancelable(false);

        // Confirm Yes - save the home coordinates into the file
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                writeHomeCoords(myStamp);
            }
        });

        // Confirm No - don't save
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "The home coordinates are not saved into the file.", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // write the home coordinates into a file
    private void writeHomeCoords(GpsStamp stamp){
        // if the file doesn't already exist, create it
        if (!myExternalFile.exists()){
            try{
                myExternalFile.createNewFile();
            } catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        // it is here, we open the file, write in the values of the home coordinates
        // overwriting what is already there
        try{
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(myExternalFile, false));
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(Double.toString(stamp.getHomeLatitude()) + "\n");
            bw.write(Double.toString(stamp.getHomeLongitude()) + "\n");
            bw.close();

        } catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // read in the home coordinates from the file
    private void readHomeCoords(){
        BufferedReader br;
        String strLat, strLng;

        // open the file
        try{
            br = new BufferedReader(new FileReader(myExternalFile));
            strLat = br.readLine();
            strLng = br.readLine();
            myStamp.setHomeCoordsFromStrings(strLat, strLng);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    // configure the GPS-related settings
    void configureButton() {
        // check for permissions to access GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }

        // here we set the minimum interval between location updates
        // the second argument in the requestLocationUpdates() method is the minimum interval, in milliseconds
        try {
            locationManager.requestLocationUpdates("gps", 5000, 0, listener);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Problem updating GPS", Toast.LENGTH_LONG).show();
        }
    }
}
