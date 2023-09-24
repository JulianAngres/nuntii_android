package com.nuntiias.nuntiitheone;

public class ChatItemLeft {

    private String rv_userInfoLeft, rv_textLeft, rv_timestampLeft, rv_role, rv_ownRole;

    public ChatItemLeft(String rv_userInfoLeft, String rv_textLeft, String rv_timestampLeft, String rv_role, String rv_ownRole) {
        this.rv_userInfoLeft = rv_userInfoLeft;
        this.rv_textLeft = rv_textLeft;
        this.rv_timestampLeft = rv_timestampLeft;
        this.rv_role = rv_role;
        this.rv_ownRole = rv_ownRole;
    }

    public String getRv_userInfoLeft() {
        return rv_userInfoLeft;
    }

    public String getRv_textLeft() {
        return rv_textLeft;
    }

    public String getRv_timestampLeft() {
        return rv_timestampLeft;
    }

    public String getRv_role() {return rv_role;}

    public String getRv_ownRole() {return rv_ownRole;}
}
