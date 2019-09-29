package com.example.app.ourapplication.rest.model.response;

import com.example.app.ourapplication.rest.model.Model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by ROYSH on 2/21/2017.
 */
public class GetDataRespModel extends Model  {

    private boolean mIsSuccess;
    private ArrayList<Person> mData;

    @JsonCreator
    public GetDataRespModel(@JsonProperty("success") Boolean success, @JsonProperty("data") ArrayList<Person> data) {
        this.mIsSuccess = success;
        this.mData = data;

    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public ArrayList<Person> getData() {
        return mData;
    }


}
