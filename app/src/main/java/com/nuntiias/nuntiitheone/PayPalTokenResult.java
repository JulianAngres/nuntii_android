package com.nuntiias.nuntiitheone;

public class PayPalTokenResult {

    private String scope, access_token, token_type, app_id, expires_in, nonce;

    public PayPalTokenResult(String scope, String access_token, String token_type, String app_id, String expires_in, String nonce) {
        this.scope = scope;
        this.access_token = access_token;
        this.token_type = token_type;
        this.app_id = app_id;
        this.expires_in = expires_in;
        this.nonce = nonce;
    }

    public String getScope() {
        return scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public String getNonce() {
        return nonce;
    }
}
