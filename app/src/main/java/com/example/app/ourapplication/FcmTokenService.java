package com.example.app.ourapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.app.ourapplication.pref.PreferenceEditor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmTokenService   {

    private Context mContext;
    public static String TAG = "FcmTokenService";

    public FcmTokenService(Context context) {
        this.mContext = context;
    }



    public void CreateGCMToken() {
        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        Log.d(TAG, "GCM TOKEN IS: " + token);
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);


                        storeGCMToken(token);
                        // Get new Instance ID token



                        //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public String getGCMToken(){
        String token = PreferenceEditor.getInstance(mContext).getGCMToken();
        return token;
    }

    public void storeGCMToken(String token){
        PreferenceEditor.getInstance(mContext).setGCMToken(token);
    }
}




