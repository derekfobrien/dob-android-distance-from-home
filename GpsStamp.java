package com.example.distancefromhome;

import android.location.Location;

// this class encapsulates the values associated with the locations, which we will
// be using, including the various methods, so that only one line will be needed in the other
// classes whenever we want to perform some certain functions
public class GpsStamp {
    private double homeLatitude;
    private double homeLongitude;
    private double currentLatitude;
    private double currentLongitude;
    private double distance;

    // getters
    double getHomeLatitude(){
        return homeLatitude;
    }

    double getHomeLongitude(){
        return homeLongitude;
    }

    public double getCurrentLatitude(){
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    void setDistance(){
        float[] result = new float[1];
        Location.distanceBetween(homeLatitude, homeLongitude, currentLatitude, currentLongitude, result);
        distance = result[0];
    }

    // setters
    void setHomeCoords(){
        homeLatitude = currentLatitude;
        homeLongitude = currentLongitude;
        setDistance();
    }

    void setCurrentCoords(double lat, double lng){
        currentLatitude = lat;
        currentLongitude = lng;
        setDistance();
    }

    private String getLatitudeString(double lat, boolean fullnumber) {
        // return the latitude as a string, formatted "0.000000 N/S"
        String ns = "";
        String str;
        if (lat < 0) {
            ns = " S";
        } else if (lat > 0) {
            ns = " N";
        }

        double abslat = Math.abs(lat);
        // if full number is to be displayed, display to six places of decimals
        // otherwise display to one place of decimals
        if (fullnumber){
            str = String.format("%1.6f", (float)abslat) + ns;
        }
        else{
            str = String.format("%1.1f", (float)abslat) + "****" + ns;
        }
        return str;
    }

    private String getLongitudeString(double lng, boolean fullnumber) {
        // return the longitude as a string, formatted "0.000000 W/E"
        String ew = "";
        String str;
        if (lng < 0) {
            ew = " W";
        } else if (lng > 0) {
            ew = " E";
        }

        double abslong = Math.abs(lng);
        // if full number is to be displayed, display to six places of decimals
        // otherwise display to one place of decimals
        if (fullnumber){
            str = String.format("%1.6f", (float)abslong) + ew;
        } else{
            str = String.format("%1.1f", (float)abslong) + "****" + ew;
        }
        return str;
    }

    String getFullCoords(boolean fullnum) {
        // return both the longitude and longitude on one string
        String str;
        str = getLatitudeString(currentLatitude, fullnum) + ", " + getLongitudeString(currentLongitude, fullnum);

        return str;
    }

    double getDistance(){
        return distance;
    }

    String getDistanceToString(){
        String str;
        str = String.format("%1.3f", (float)(distance / 1000));
        return str;
    }

    void setHomeCoordsFromStrings(String strLat, String strLng){
        homeLatitude = Double.parseDouble(strLat);
        homeLongitude = Double.parseDouble(strLng);
    }
}
