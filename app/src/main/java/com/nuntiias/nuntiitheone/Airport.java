package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class Airport {

    private String icao;
    private String iata;
    private String name;
    private String shortName;
    private String municipalityName;
    @SerializedName("location")
    private AirportLocation airportLocation;
    private String countryCode;

    public Airport(String icao, String iata, String name, String shortName, String municipalityName, AirportLocation airportLocation, String countryCode) {
        this.icao = icao;
        this.iata = iata;
        this.name = name;
        this.shortName = shortName;
        this.municipalityName = municipalityName;
        this.airportLocation = airportLocation;
        this.countryCode = countryCode;
    }

    public String getIcao() {
        return icao;
    }

    public String getIata() {
        return iata;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public AirportLocation getAirportLocation() {
        return airportLocation;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
