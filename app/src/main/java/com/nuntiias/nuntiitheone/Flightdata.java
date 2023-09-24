package com.nuntiias.nuntiitheone;

public class Flightdata {

    private GreatCircleDistance greatCircleDistance;
    private Departure departure;
    private Arrival arrival;
    private Location location;
    private String lastUpdatedUtc;
    private String number;
    private String callSign;
    private String status;
    private String codeshareStatus;
    private Boolean isCargo;
    private Aircraft aircraft;
    private Airline airline;

    public Flightdata(GreatCircleDistance greatCircleDistance, Departure departure, Arrival arrival, Location location, String lastUpdatedUtc, String number, String callSign, String status, String codeshareStatus, Boolean isCargo, Aircraft aircraft, Airline airline) {
        this.greatCircleDistance = greatCircleDistance;
        this.departure = departure;
        this.arrival = arrival;
        this.location = location;
        this.lastUpdatedUtc = lastUpdatedUtc;
        this.number = number;
        this.callSign = callSign;
        this.status = status;
        this.codeshareStatus = codeshareStatus;
        this.isCargo = isCargo;
        this.aircraft = aircraft;
        this.airline = airline;
    }

    public GreatCircleDistance getGreatCircleDistance() {
        return greatCircleDistance;
    }

    public Departure getDeparture() {
        return departure;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public Location getLocation() {
        return location;
    }

    public String getLastUpdatedUtc() {
        return lastUpdatedUtc;
    }

    public String getNumber() {
        return number;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getStatus() {
        return status;
    }

    public String getCodeshareStatus() {
        return codeshareStatus;
    }

    public Boolean getCargo() {
        return isCargo;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public Airline getAirline() {
        return airline;
    }
}
