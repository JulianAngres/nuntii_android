package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class Location {

    private int pressureAltFt;
    private int gsKt;
    private int trackDeg;
    private String reportedAtUtc;
    @SerializedName("lat")
    private int locationLat;
    @SerializedName("lon")
    private int locationLon;

    public Location(int pressureAltFt, int gsKt, int trackDeg, String reportedAtUtc, int locationLat, int locationLon) {
        this.pressureAltFt = pressureAltFt;
        this.gsKt = gsKt;
        this.trackDeg = trackDeg;
        this.reportedAtUtc = reportedAtUtc;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
    }

    public int getPressureAltFt() {
        return pressureAltFt;
    }

    public int getGsKt() {
        return gsKt;
    }

    public int getTrackDeg() {
        return trackDeg;
    }

    public String getReportedAtUtc() {
        return reportedAtUtc;
    }

    public int getLocationLat() {
        return locationLat;
    }

    public int getLocationLon() {
        return locationLon;
    }
}
