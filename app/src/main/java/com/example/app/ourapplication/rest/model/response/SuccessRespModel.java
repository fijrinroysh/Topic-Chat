package com.example.app.ourapplication.rest.model.response;

import com.example.app.ourapplication.rest.model.Model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sarumugam on 15/01/17.
 */

public class SuccessRespModel extends Model implements Serializable {


    private boolean mIsSuccess;

    @JsonCreator
    public SuccessRespModel(@JsonProperty("success") Boolean success) {
        this.mIsSuccess = success;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.mIsSuccess = isSuccess;
    }
}