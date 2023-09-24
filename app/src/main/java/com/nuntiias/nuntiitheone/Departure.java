package com.nuntiias.nuntiitheone;

import java.util.List;

public class Departure {

    private Airport airport;
    private String scheduledTimeLocal;
    private String actualTimeLocal;
    private String runwayTimeLocal;
    private String scheduledTimeUtc;
    private String actualTimeUtc;
    private String runwayTimeUtc;
    private String terminal;
    private String checkInDesk;
    private String gate;
    private String baggageBelt;
    private String runway;
    private List<String> quality;

    public Departure(Airport airport, String scheduledTimeLocal, String actualTimeLocal, String runwayTimeLocal, String scheduledTimeUtc, String actualTimeUtc, String runwayTimeUtc, String terminal, String checkInDesk, String gate, String baggageBelt, String runway, List<String> quality) {
        this.airport = airport;
        this.scheduledTimeLocal = scheduledTimeLocal;
        this.actualTimeLocal = actualTimeLocal;
        this.runwayTimeLocal = runwayTimeLocal;
        this.scheduledTimeUtc = scheduledTimeUtc;
        this.actualTimeUtc = actualTimeUtc;
        this.runwayTimeUtc = runwayTimeUtc;
        this.terminal = terminal;
        this.checkInDesk = checkInDesk;
        this.gate = gate;
        this.baggageBelt = baggageBelt;
        this.runway = runway;
        this.quality = quality;
    }

    public Airport getAirport() {
        return airport;
    }

    public String getScheduledTimeLocal() {
        return scheduledTimeLocal;
    }

    public String getActualTimeLocal() {
        return actualTimeLocal;
    }

    public String getRunwayTimeLocal() {
        return runwayTimeLocal;
    }

    public String getScheduledTimeUtc() {
        return scheduledTimeUtc;
    }

    public String getActualTimeUtc() {
        return actualTimeUtc;
    }

    public String getRunwayTimeUtc() {
        return runwayTimeUtc;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getCheckInDesk() {
        return checkInDesk;
    }

    public String getGate() {
        return gate;
    }

    public String getBaggageBelt() {
        return baggageBelt;
    }

    public String getRunway() {
        return runway;
    }

    public List<String> getQuality() {
        return quality;
    }
}
