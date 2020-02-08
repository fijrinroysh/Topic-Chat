package com.example.app.ourapplication;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.ourapplication.FeedRVAdapter;
import com.example.app.ourapplication.OurApplication;
import com.example.app.ourapplication.R;
import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.rest.model.request.ChatPostReqModel;
import com.example.app.ourapplication.rest.model.request.CommentFeedReqModel;
import com.example.app.ourapplication.rest.model.request.HomeFeedReqModel;
import com.example.app.ourapplication.rest.model.response.ComposeRespModel;
import com.example.app.ourapplication.rest.model.response.GetDataRespModel;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.rest.model.response.SuccessRespModel;
import com.example.app.ourapplication.ui.HomeActivity;
import com.example.app.ourapplication.util.Helper;
import com.example.app.ourapplication.util.UI;
import com.example.app.ourapplication.wss.WebSocketListener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.app.ourapplication.rest.ApiUrls.HTTP_URL;




import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.LayoutInflater;
import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.rest.model.request.CommentFeedReqModel;
import com.example.app.ourapplication.rest.model.request.SubscribeReqModel;
import com.example.app.ourapplication.rest.model.response.Kid;
import com.example.app.ourapplication.rest.model.response.SuccessRespModel;
import com.example.app.ourapplication.util.Helper;
import com.example.app.ourapplication.util.UI;
//import com.example.app.ourapplication.wss.WebSocketClient;
import com.example.app.ourapplication.wss.WebSocketListener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by ROYSH on 8/3/2016.
 */
public class DiscussionActivity extends AppCompatActivity {


    private List<Person> mComments = new ArrayList<>();
    private FeedRVAdapter mCommentListAdapter;
   // private WebSocketClient mWebSocketClient;
    private final String TAG = DiscussionActivity.class.getSimpleName();
    private String keyid;
    // private String token;
    private DBHelper mDBHelper = new DBHelper(this);
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private static View view;
    private  String token;
    private Person person;
    TextView senderMsg;

    FcmTokenService mFcmTokenService = new FcmTokenService(this);
    private final String userId = PreferenceEditor.getInstance(this).getLoggedInUserName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion);
        LayoutInflater inflater=getLayoutInflater();
        view=(View) inflater.inflate(R.layout.discussion,null);




        Button mSendButton = (Button) findViewById(R.id.send_button);
        final EditText mMessageBox = (EditText) findViewById(R.id.msg_box);
        senderMsg = (TextView) findViewById(R.id.sender_message);

        token=mFcmTokenService.getGCMToken();
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        // mWebSocketClient.sendMessage(Helper.formSubscribeMessage("S", keyid, token));
        person = (Person) getIntent().getSerializableExtra("person");
        keyid = person.getPostId();
        Log.d(TAG, "keyid:" + keyid);
        if (keyid != null) {

            mDBHelper.updateCommentRead(keyid);
            mComments.clear();
            //mComments.add(0, person);
            mComments.add(0, mDBHelper.getFeedData(keyid));
            mComments.addAll(mDBHelper.getCommentData(keyid));
            recyclerView.scrollToPosition(mComments.size() - 1);
            mCommentListAdapter = new FeedRVAdapter(DiscussionActivity.this,mComments);
            recyclerView.setAdapter(mCommentListAdapter);
            Log.d(TAG, "Get comment messages from database:" + mDBHelper.getCommentData(keyid));
            //PostSubscription(view,keyid, mComments.get(0).getUserId(),"Y");
            // Helper.PostSubscription(view,token, keyid, "Y");




            if (Build.VERSION.SDK_INT >= 11) {
                recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v,
                                               int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom < oldBottom) {
                            recyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.smoothScrollToPosition(
                                            recyclerView.getAdapter().getItemCount() - 1);
                                }
                            }, 100);
                        }
                    }
                });
            }


        }


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mMessageBox.getText().toString();


                if(isNetworkConnected()){

                    if (!TextUtils.isEmpty(msg)) {


                        final Person prsn = new Person(
                                "C",
                                keyid,
                                "",
                                userId,
                                token,
                                "",
                                msg,
                                "",
                                "",
                                Helper.getCurrentTimeStamp(),
                                "REQ"
                        );
                        Log.d(TAG, "Messaage:" + msg);
                        Log.d(TAG, "Token:" + token);
                        //mWebSocketClient.connectToWSS(WS_URL + "/" + ((OurApplication) getApplicationContext()).getUserToken());
                        Log.d(TAG, "Formcommentmessage: " + msg);

                        postchat(prsn);
                        //mWebSocketClient.sendMessage(msg);
                        UI.closeKeyboard(getApplicationContext(), mMessageBox.getWindowToken());
                        mMessageBox.setText(null);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection. Please connect and try again", Toast.LENGTH_LONG).show();
                }
            }
        });




        /*
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getUpdatedComments();
            }
        });
*/


            // Register to receive messages.
            // We are registering an observer (mMessageReceiver) to receive Intents
            // with actions named "custom-event-name".
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("chatevent"));



    }



    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        //mWebSocketClient.sendMessage(Helper.formSubscribeMessage("U", keyid, token));
        //mWebSocketClient.removeWebSocketListener(this);
        //PostSubscription(view,keyid, mComments.get(0).getUserId(),"N");
        // Helper.PostSubscription(view, token,keyid, "N");
    }
    @Override
    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



    private void getUpdatedComments(){
        CommentFeedReqModel reqModel = new CommentFeedReqModel();
        //reqModel.setLatestDate("2000-12-31 12:00:00");
        reqModel.setLatestDate(mDBHelper.getCommentDataLatestTime(keyid));
        reqModel.setPostId(keyid);
        reqModel.setType("C");
        Call<GetDataRespModel> queryComments = ((OurApplication)getApplicationContext())
                .getRestApi().queryCommentFeed(reqModel);
        queryComments.enqueue(new Callback<GetDataRespModel>() {
            @Override
            public void onResponse(Call<GetDataRespModel> call, Response<GetDataRespModel> response) {
                if (response.body() != null) {
                    //do something
                ArrayList<Person> data = response.body().getData();
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        mDBHelper.insertCommentData(data.get(i));
                        mComments.add(data.get(i));
                        //mComments.addAll(response.body().getData());
                        mCommentListAdapter.notifyDataSetChanged();

                    }

                    Toast.makeText(getApplicationContext(), "New chats", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No more chats to Load", Toast.LENGTH_LONG).show();
                }
                } else {
                    Toast.makeText(getApplicationContext(), "No chats", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDataRespModel> call, Throwable t) {
                Log.d(TAG, "Query failed");
                Toast.makeText(getApplicationContext(), "Loading Comments Failed", Toast.LENGTH_LONG).show();
            }
        });
       // mSwipeRefreshLayout.setRefreshing(false);
    }




    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "chatdata" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            // String message = intent.getStringExtra("chatdata");
            //Log.d(TAG, "Got message: " + message);

                /*Got message: {postid=201907282340009486946972, userid=, subscriptionflag=, name=, time=2000-12-31 12:00:00, type=C, image=, message=Ffw, profileimage=}*/

            Person person = (Person) intent.getSerializableExtra("person");

                Log.d(TAG, person.getPostId());

                if (person.getPostId().equals(keyid)) {
                    Log.d(TAG, "Message found in the Commentlist:");
                    if (person.getUserId().equals(userId)) {
                        Log.d(TAG, "Message found in the Commentlist:");
                        int position = find_person(mComments, person);
                        if( position != -1) {
                            Log.d(TAG, "Message found in the Commentlist: " + mComments.get(find_person(mComments, person)).getMessage());
                            //Log.d(TAG, person.getMessage());
                            // if (mComments.get(mComments.size() - 1).getMessage().equals(person.getMessage())) {
                            mComments.set(position, person);
                            mCommentListAdapter.notifyItemChanged(position);
                        }
                    //}
                    }
                    else{

                        mComments.add(person);
                        recyclerView.scrollToPosition(mComments.size() - 1);
                        mCommentListAdapter.notifyItemInserted(mComments.size() - 1);
                    }
                }


            }


        };


    public int find_person(List<Person> list, Person person ){
        int p;

        Person prsn = new Person(person.getType(),person.getPostId(),"",person.getUserId(),token,"",person.getMessage(),"",person.getPhotoMsg(),person.getTimeMsg(),"REQ");

        Log.d(TAG,"Message to find in recyclerview after receiving: " + prsn.toString());
        if(list.contains(prsn)){
            p = list.indexOf(prsn);
            Log.d(TAG, "Position of the object found in mComments. Return position to replace");
        }else {
            p=-1;
            Log.d(TAG, "Position of the object not found in mComments. Send default value");
        }

        return p;

    }





        private void postchat(Person prsn) {

            /*ChatPostReqModel reqModel = new ChatPostReqModel();
            reqModel.setTime(Helper.getCurrentTimeStamp());
            reqModel.setPostId(id);
            reqModel.setMessage(message);
            reqModel.setType("C");
            reqModel.setToken(token);*/



            Log.d(TAG, "Message added to recyclerview before sending :" + prsn.toString());

            mComments.add(prsn);
            recyclerView.scrollToPosition(mComments.size() - 1);
            mCommentListAdapter.notifyItemInserted(mComments.size() - 1);
        /*Log.d(TAG, "Latest date :" + mDBHelper.getFeedDataLatestTime());*/

            Call<SuccessRespModel> composeChat = ((OurApplication) getApplicationContext())
                    .getRestApi().ComposeChat(prsn);
            composeChat.enqueue(new Callback<SuccessRespModel>() {
                @Override
                public void onResponse(Call<SuccessRespModel> call, Response<SuccessRespModel> response) {
                    Log.d(TAG, "Response body for post chat :" + response.body());
                    if (response.body() != null) {
                        //do something
                        if (response.body().isSuccess()) {

                            Log.d(TAG, "Response body for post chat :" + response.body());
                            Toast.makeText(getApplicationContext(), "Chat Posted Successfully", Toast.LENGTH_LONG).show();


                            //msg = Helper.formCommentMessage("C", keyid, token, message);

                        } else {
                            Toast.makeText(getApplicationContext(), "Post chat response code is not true", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Post chat response body is null", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<SuccessRespModel> call, Throwable t) {
                    Log.d(TAG, "Posting Chat Failed: " + t);
                    Toast.makeText(getApplicationContext(), "Posting Chat Failed" + t, Toast.LENGTH_LONG).show();
                }
            });


        }


   /* public void onBackPressed() {

        final Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        getApplicationContext().startActivity(homeIntent);
    }*/




}