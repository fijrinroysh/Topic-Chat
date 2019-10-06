package com.example.app.ourapplication;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.app.ourapplication.rest.RetrofitClient;
import com.example.app.ourapplication.rest.api.RestApi;
import com.example.app.ourapplication.util.NetworkReceiver;

public class OurApplication extends Application {
    //implements NetworkReceiver.NetworkIntf
    private  static String mToken;
    //private WebSocketClient mClient;
    private RestApi mRestApi;
    //private NetworkReceiver mNetworkReceiver;

    @Override
    public void onCreate() {



        super.onCreate();
    }

    public RestApi getRestApi() {
        if(mRestApi == null){
            mRestApi = RetrofitClient.getRetroClient().create(RestApi.class);
        }
        return mRestApi;
    }

    public static String getUserToken(){
        return mToken;
    }

    public void setUserToken(String token){
        mToken = token;
    }






}