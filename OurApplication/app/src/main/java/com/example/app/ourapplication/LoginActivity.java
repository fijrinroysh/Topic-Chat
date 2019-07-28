package com.example.app.ourapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.rest.model.request.SignInReqModel;
import com.example.app.ourapplication.rest.model.request.SignUpReqModel;
import com.example.app.ourapplication.rest.model.response.SignInRespModel;
import com.example.app.ourapplication.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sarumugam on 17/04/16.
 */
public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    //private EditText mUserNameBox;
    private EditText mUserIdBox;
    private EditText mPasswordBox;
    private Button mLoginButton;
    private EditText mSignUpUserIdBox;
    private EditText mSignUpNameBox;
    private EditText mSignUpPasswordBox;
    private Button mSignUpButton;
    private TextView mNewUserText;
    private ViewFlipper mScreenFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.login));
        initializeViews();
        setUpListeners();
    }

    @Override
    public void onBackPressed() {
        if(mScreenFlipper.getDisplayedChild() == 1){
            mScreenFlipper.setInAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.right_out_short));
            mScreenFlipper.setOutAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.left_in_short));
            mScreenFlipper.setDisplayedChild(0);
            setTitle(getString(R.string.login));
            return;
        }
        super.onBackPressed();
    }

    private void initializeViews() {
        mUserIdBox = (EditText) findViewById(R.id.userid_field);
        //mUserNameBox = (EditText) findViewById(R.id.username_field);
        mPasswordBox = (EditText) findViewById(R.id.password_field);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mSignUpUserIdBox = (EditText) findViewById(R.id.sign_up_userid_field);
        mSignUpNameBox = (EditText) findViewById(R.id.sign_up_username_field);
        mSignUpPasswordBox = (EditText) findViewById(R.id.sign_up_password_field);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mNewUserText = (TextView) findViewById(R.id.new_user_text);
        mScreenFlipper = (ViewFlipper) findViewById(R.id.flipper);
    }

    private void setUpListeners() {
        mNewUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScreenFlipper.setInAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.right_in_short));
                mScreenFlipper.setOutAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.left_out_long));
                mScreenFlipper.setDisplayedChild(1);
                setTitle(getString(R.string.sign_up));
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(mUserIdBox.getText().toString(), mPasswordBox.getText().toString());


            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(mSignUpUserIdBox.getText().toString(), mSignUpNameBox.getText().toString(),
                        mSignUpPasswordBox.getText().toString());
            }
        });
    }

    private void login(String userId, String password){
        SignInReqModel reqModel = new SignInReqModel();
        reqModel.setUserId(userId);
        reqModel.setPassword(password);
        Call<SignInRespModel> login = ((OurApplication)getApplicationContext()).getRestApi().signIn(reqModel);

        login.enqueue(new Callback<SignInRespModel>() {
            @Override
            public void onResponse(Call<SignInRespModel> call, Response<SignInRespModel> response) {
                if(response.body().isSuccess()){
                    PreferenceEditor.getInstance(LoginActivity.this).setLoggedInUserName(mUserIdBox.getText().toString(),
                            mPasswordBox.getText().toString());
                    ((OurApplication)getApplicationContext()).setUserToken(response.body().getToken());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignInRespModel> call,Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp(final String userId, String name, final String password){
        SignUpReqModel reqModel = new SignUpReqModel();
        reqModel.setUserId(userId);
        reqModel.setName(name);
        reqModel.setPassword(password);
        final Call<Void> login = ((OurApplication)getApplicationContext()).getRestApi().signUp(reqModel);

        login.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,Response<Void> response) {
                login(userId,password);
            }

            @Override
            public void onFailure(Call<Void> call,Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, getString(R.string.sign_up_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeToken(){
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
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREFERENCE), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.FCM_TOKEN), token);
                        editor.commit();
                        // Get new Instance ID token


                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}