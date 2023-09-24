package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Arrival {

    @SerializedName("airport")
    private ArrivalAirport arrivalAirport;
    @SerializedName("scheduledTimeLocal")
    private String arrivalScheduledTimeLocal;
    @SerializedName("actualTimeLocal")
    private String arrivalActualTimeLocal;
    @SerializedName("runwayTimeLocal")
    private String arrivalRunwayTimeLocal;
    @SerializedName("terminal")
    private String arrivalTerminal;
    @SerializedName("checkInDesk")
    private String arrivalCheckInDesk;
    @SerializedName("gate")
    private String arrivalGate;
    @SerializedName("baggageBelt")
    private String arrivalBaggageBelt;
    @SerializedName("runway")
    private String arrivalRunway;
    @SerializedName("quality")
    private List<String> arrivalQuality;

    public Arrival(ArrivalAirport arrivalAirport, String arrivalScheduledTimeLocal, String arrivalActualTimeLocal, String arrivalRunwayTimeLocal, String arrivalTerminal, String arrivalCheckInDesk, String arrivalGate, String arrivalBaggageBelt, String arrivalRunway, List<String> arrivalQuality) {
        this.arrivalAirport = arrivalAirport;
        this.arrivalScheduledTimeLocal = arrivalScheduledTimeLocal;
        this.arrivalActualTimeLocal = arrivalActualTimeLocal;
        this.arrivalRunwayTimeLocal = arrivalRunwayTimeLocal;
        this.arrivalTerminal = arrivalTerminal;
        this.arrivalCheckInDesk = arrivalCheckInDesk;
        this.arrivalGate = arrivalGate;
        this.arrivalBaggageBelt = arrivalBaggageBelt;
        this.arrivalRunway = arrivalRunway;
        this.arrivalQuality = arrivalQuality;
    }

    public ArrivalAirport getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalScheduledTimeLocal() {
        return arrivalScheduledTimeLocal;
    }

    public String getArrivalActualTimeLocal() {
        return arrivalActualTimeLocal;
    }

    public String getArrivalRunwayTimeLocal() {
        return arrivalRunwayTimeLocal;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public String getArrivalCheckInDesk() {
        return arrivalCheckInDesk;
    }

    public String getArrivalGate() {
        return arrivalGate;
    }

    public String getArrivalBaggageBelt() {
        return arrivalBaggageBelt;
    }

    public String getArrivalRunway() {
        return arrivalRunway;
    }

    public List<String> getArrivalQuality() {
        return arrivalQuality;
    }
}
