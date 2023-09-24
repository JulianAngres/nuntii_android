package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class ArrivalAirport {
    @SerializedName("icao")
    private String arrivalIcao;
    @SerializedName("iata")
    private String arrivalIata;
    @SerializedName("name")
    private String arrivalName;
    @SerializedName("shortName")
    private String arrivalShortName;
    @SerializedName("municipalityName")
    private String arrivalMunicipalityName;
    @SerializedName("location")
    private ArrivalAirportLocation arrivalAirportLocation;
    @SerializedName("countryCode")
    private String arrivalCountryCode;

    public ArrivalAirport(String arrivalIcao, String arrivalIata, String arrivalName, String arrivalShortName, String arrivalMunicipalityName, ArrivalAirportLocation arrivalAirportLocation, String arrivalCountryCode) {
        this.arrivalIcao = arrivalIcao;
        this.arrivalIata = arrivalIata;
        this.arrivalName = arrivalName;
        this.arrivalShortName = arrivalShortName;
        this.arrivalMunicipalityName = arrivalMunicipalityName;
        this.arrivalAirportLocation = arrivalAirportLocation;
        this.arrivalCountryCode = arrivalCountryCode;
    }

    public String getArrivalIcao() {
        return arrivalIcao;
    }

    public String getArrivalIata() {
        return arrivalIata;
    }

    public String getArrivalName() {
        return arrivalName;
    }

    public String getArrivalShortName() {
        return arrivalShortName;
    }

    public String getArrivalMunicipalityName() {
        return arrivalMunicipalityName;
    }

    public ArrivalAirportLocation getArrivalAirportLocation() {
        return arrivalAirportLocation;
    }

    public String getArrivalCountryCode() {
        return arrivalCountryCode;
    }
}
