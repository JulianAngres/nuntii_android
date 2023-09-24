package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class ArrivalAirportLocation {
    @SerializedName("lat")
    private float arrivalLat;
    @SerializedName("lon")
    private float arrivalLon;

    public ArrivalAirportLocation(int arrivalLat, int arrivalLon) {
        this.arrivalLat = arrivalLat;
        this.arrivalLon = arrivalLon;
    }

    public float getArrivalLat() {
        return arrivalLat;
    }

    public float getArrivalLon() {
        return arrivalLon;
    }
}
