package com.nuntiias.nuntiitheone;

public class Aircraft {

    private String reg;
    private String modeS;
    private String model;
    private Image image;

    public Aircraft(String reg, String modeS, String model, Image image) {
        this.reg = reg;
        this.modeS = modeS;
        this.model = model;
        this.image = image;
    }

    public String getReg() {
        return reg;
    }

    public String getModeS() {
        return modeS;
    }

    public String getModel() {
        return model;
    }

    public Image getImage() {
        return image;
    }
}
