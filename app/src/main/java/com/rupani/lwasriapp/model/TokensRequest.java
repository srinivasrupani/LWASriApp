package com.rupani.lwasriapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Srinivas Rupani on 19/12/18.
 * Copyright (c) 2018 Smartron, All rights reserved.
 */
public class TokensRequest {

    @SerializedName("grant_type")
    @Expose
    private String grantType = "authorization_code";

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("redirect_uri")
    @Expose
    private String redirectUri;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("code_verifier")
    @Expose
    private String codeVerifier;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }


}
