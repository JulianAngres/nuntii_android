package com.nuntiias.nuntiitheone;

public class GreatCircleDistance {

    public float meter;
    public float km;
    public float mile;
    public float nm;
    public float feet;

    public GreatCircleDistance(int meter, int km, int mile, int nm, int feet) {
        this.meter = meter;
        this.km = km;
        this.mile = mile;
        this.nm = nm;
        this.feet = feet;
    }

    public float getMeter() {
        return meter;
    }

    public float getKm() {
        return km;
    }

    public float getMile() {
        return mile;
    }

    public float getNm() {
        return nm;
    }

    public float getFeet() {
        return feet;
    }
}
