package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class Airline {
    @SerializedName("name")
    private String airlineName;

    public Airline(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineName() {
        return airlineName;
    }
}
