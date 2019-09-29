package com.example.app.ourapplication.rest.model.request;

import com.example.app.ourapplication.Keys;
import com.example.app.ourapplication.rest.model.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenReqModel extends Model {

    @JsonProperty(Keys.KEY_USERID)
    private String mUserId;
    @JsonProperty(Keys.KEY_TOKEN)
    private String mToken;



    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }


}