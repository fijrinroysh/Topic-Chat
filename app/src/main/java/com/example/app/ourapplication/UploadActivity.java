package com.example.app.ourapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;

import com.example.app.ourapplication.rest.model.request.ProfileUpdateModel;
import com.example.app.ourapplication.util.Helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadActivity {




/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case UPDATE_PIC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Bundle extras = data.getExtras();
                    Uri filePath = data.getData();
                    Log.d(TAG, "Data : " + filePath);
                    try {
                        // mBitmap = data.getParcelableExtra("data");
                        Bitmap bitmap  = MediaStore.Images.Media.getBitmap((getActivity().getApplicationContext()).getContentResolver(), filePath);
                        if (bitmap != null) {
                            Bitmap bitmapRef = Helper.scaleBitmap(bitmap);
                            Log.d(TAG,"L : "+bitmapRef.getWidth()+ "  : "+bitmapRef.getScaledHeight(getResources().getDisplayMetrics()));
                            profileImgView.setImageBitmap(bitmapRef);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmapRef.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                            if(!TextUtils.isEmpty(mUserId)) {
                                String imageProfileString = Helper.getStringImage(bitmapRef);
                                // Log.d(TAG, "Image message value length : " + imageProfileString.length());
                                // Log.d(TAG, "Image message value is : " + imageProfileString);
                                ProfileUpdateModel model = new ProfileUpdateModel(mUserId, Keys.KEY_PROFIMG, imageProfileString);
                                updateProfile(model);
                                //   mDBHelper.updateProfile(model.toString());
                            }
                        }else{
                            Snackbar.make(profileImgView, "Bitmap is null", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Log.d(TAG, "Bitmap is null");}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
*/

}