package com.example.cecs491project.ui.map;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

public class pharmacySearch {
    double lat, lng;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public PlacesSearchResponse run(){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD7w9xXLFcDgB10Ba65oSWCCAGuFBZNrgU")
                .build();
        LatLng location = new LatLng(lat, lng);

        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .rankby(RankBy.PROMINENCE)
                    .keyword("pharmacy")
                    .language("en")
                    .type(PlaceType.PHARMACY)
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return request;

    }


}

