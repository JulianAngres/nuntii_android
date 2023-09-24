package com.nuntiias.nuntiitheone;

public class ProposedItineraryItem {
    private String rv_dateItinerary;
    private String rv_itineraryOrigin;
    private String rv_itineraryDestination;
    private String rv_id;

    public ProposedItineraryItem(String rv_dateItinerary, String rv_itineraryOrigin, String rv_itineraryDestination, String rv_id) {
        this.rv_dateItinerary = rv_dateItinerary;
        this.rv_itineraryOrigin = rv_itineraryOrigin;
        this.rv_itineraryDestination = rv_itineraryDestination;
        this.rv_id = rv_id;
    }

    public String getRv_dateItinerary() {
        return rv_dateItinerary;
    }

    public String getRv_itineraryOrigin() {
        return rv_itineraryOrigin;
    }

    public String getRv_itineraryDestination() {
        return rv_itineraryDestination;
    }

    public String getRv_id() {
        return rv_id;
    }
}
