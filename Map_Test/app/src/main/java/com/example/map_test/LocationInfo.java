package com.example.map_test;

import com.google.android.gms.maps.model.LatLng;

public class LocationInfo {

    private LatLng latLng;
    String markerTitle;
    String markerSnippet;

    public LocationInfo() {}

    public LocationInfo(LatLng latLng, String markerTitle, String markerSnippet){
        this.latLng=latLng;
        this.markerTitle=markerTitle;
        this.markerSnippet=markerSnippet;
    }

    public LatLng getLatLng(){return latLng;}
    public String getMarkerTitle(){
        return markerTitle;
    }
    public String getMarkerSnippet() {return markerSnippet;}

    public void setLatLng(LatLng latLng){this.latLng=latLng;}
    public void setMarkerTitle(String markerTitle){this.markerTitle=markerTitle;}
    public void setMarkerSnippet(String markerSnippet){this.markerSnippet=markerSnippet;}


}
