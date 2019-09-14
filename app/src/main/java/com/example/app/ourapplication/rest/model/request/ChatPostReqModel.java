package com.example.app.ourapplication.rest.model.request;

import com.example.app.ourapplication.Keys;
import com.example.app.ourapplication.rest.model.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sarumugam on 15/01/17.
 */
public class ChatPostReqModel extends Model {

    @JsonProperty(Keys.KEY_TYPE)
    private String mType;
    @JsonProperty(Keys.KEY_ID)
    private String mPostId;
    @JsonProperty(Keys.KEY_TOKEN)
    private String mToken;
    @JsonProperty(Keys.KEY_MESSAGE)
    private String mMessage;
    @JsonProperty(Keys.KEY_TIME)
    private String mTime;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        this.mPostId = postId;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

}