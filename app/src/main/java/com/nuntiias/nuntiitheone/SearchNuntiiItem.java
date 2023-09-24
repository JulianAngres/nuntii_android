package com.nuntiias.nuntiitheone;

public class SearchNuntiiItem {

    private String rv_iataOrigin, rv_iataDestination, rv_dateDestination, rv_timeDestination, rv_airportDestination, rv_dateOrigin, rv_timeOrigin, rv_airportOrigin, rv_nuntius, rv_price, rv_id;

    public SearchNuntiiItem(String rv_iataOrigin, String rv_iataDestination, String rv_dateDestination, String rv_timeDestination, String rv_airportDestination, String rv_dateOrigin, String rv_timeOrigin, String rv_airportOrigin, String rv_nuntius, String rv_price, String rv_id) {
        this.rv_iataOrigin = rv_iataOrigin;
        this.rv_iataDestination = rv_iataDestination;
        this.rv_dateDestination = rv_dateDestination;
        this.rv_timeDestination = rv_timeDestination;
        this.rv_airportDestination = rv_airportDestination;
        this.rv_dateOrigin = rv_dateOrigin;
        this.rv_timeOrigin = rv_timeOrigin;
        this.rv_airportOrigin = rv_airportOrigin;
        this.rv_nuntius = rv_nuntius;
        this.rv_price = rv_price;
        this.rv_id = rv_id;
    }

    public String getRv_iataOrigin() {
        return rv_iataOrigin;
    }

    public String getRv_iataDestination() {
        return rv_iataDestination;
    }

    public String getRv_dateDestination() {
        return rv_dateDestination;
    }

    public String getRv_timeDestination() {
        return rv_timeDestination;
    }

    public String getRv_airportDestination() {
        return rv_airportDestination;
    }

    public String getRv_dateOrigin() {
        return rv_dateOrigin;
    }

    public String getRv_timeOrigin() {
        return rv_timeOrigin;
    }

    public String getRv_airportOrigin() {
        return rv_airportOrigin;
    }

    public String getRv_nuntius() {
        return rv_nuntius;
    }

    public String getRv_price() {
        return rv_price;
    }

    public String getRv_id() { return rv_id; }
}
