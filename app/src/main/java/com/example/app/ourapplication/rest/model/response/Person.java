package com.example.app.ourapplication.rest.model.response;

import com.example.app.ourapplication.rest.model.Model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ROYSH on 6/23/2016.
 */
public class Person extends Model implements Serializable {

    private String mType;
    private String mPostId;
    private String mChatId;
    private String mUserId;
    private String mUserToken;
    private String mSenderName;
    private String mMessage;
    private String mPhotoId;
    private String mPhotoMsg;
    private String mTimeMsg;
    private String mFlag;

    @JsonCreator
    public Person(@JsonProperty("type") String type,@JsonProperty("postid") String postid,@JsonProperty("chatid") String chatid,@JsonProperty("userid") String userid,@JsonProperty("usertoken") String usertoken,@JsonProperty("name") String username,@JsonProperty("message") String msg,@JsonProperty("profileimage") String photoId,@JsonProperty("image") String photoMsg,@JsonProperty("time") String time,@JsonProperty("flag") String flag) {
        this.mType = type;
        this.mPostId = postid;
        this.mChatId = chatid;
        this.mUserId = userid;
        this.mUserToken = usertoken;
        this.mSenderName = username;
        this.mMessage = msg;
        this.mPhotoId = photoId;
        this.mPhotoMsg = photoMsg;
        this.mTimeMsg = time;
        this.mFlag = flag;
    }



    public String getType() {
        return mType;
    }

    public void setType(String type) {this.mType = type;}


    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        this.mPostId = postId;
    }

    public String getChatId() {
        return mChatId;
    }

    public void setChatId(String chatid) {
        this.mChatId = chatid;
    }


    public String getUserToken() {
        return mUserToken;
    }

    public void setUserToken(String usertoken) {
        this.mUserToken = usertoken;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }
    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String name) {
        this.mSenderName = name;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoId(String photoId) {
        this.mPhotoId = photoId;
    }

    public String getPhotoMsg() {
        return mPhotoMsg;
    }

    public void setPhotoMsg(String photoMsg) {
        this.mPhotoMsg = photoMsg;
    }

    public String getTimeMsg() {
        return mTimeMsg;
    }

    public void setTimeMsg(String timeMsg) {
        this.mTimeMsg = timeMsg;
    }

    public String getFlag() {
        return mFlag;
    }

    public void setFlag(String flag) {
        this.mFlag = flag;
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Person)) {
            return false;
        }

        Person that = (Person) other;

        // Custom equality check here.
        return this.mType.equals(that.mType)
                && this.mPostId.equals(that.mPostId)
                && this.mUserId.equals(that.mUserId)
                && this.mSenderName.equals(that.mSenderName)
                && this.mMessage.equals(that.mMessage)
                && this.mPhotoId.equals(that.mPhotoId)
                && this.mPhotoMsg.equals(that.mPhotoMsg)
                && this.mTimeMsg.equals(that.mTimeMsg)
                && this.mFlag.equals(that.mFlag)

                ;
    }



}