package com.example.app.ourapplication;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import static com.example.app.ourapplication.rest.ApiUrls.WS_URL;



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
import com.example.app.ourapplication.rest.model.response.Person;
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

import static com.example.app.ourapplication.rest.ApiUrls.WS_URL;

/**
 * Created by ROYSH on 8/3/2016.
 */
public class DiscussionActivity extends AppCompatActivity  {





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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion);
        LayoutInflater inflater=getLayoutInflater();
        view=(View) inflater.inflate(R.layout.discussion,null);
        //token = ((OurApplication) getApplicationContext()).getUserToken();
        Button mSendButton = (Button) findViewById(R.id.send_button);
        final EditText mMessageBox = (EditText) findViewById(R.id.msg_box);
        senderMsg = (TextView) findViewById(R.id.sender_message);
        //mWebSocketClient = ((OurApplication)getApplicationContext()).getClient();
        //mWebSocketClient.addWebSocketListener(this);
        //token = ((OurApplication)getApplicationContext()).getUserToken();
        UI.showSoftKeyboard(this,mMessageBox);
        token=mFcmTokenService.getGCMToken();
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        // mWebSocketClient.sendMessage(Helper.formSubscribeMessage("S", keyid, token));
        person = (Person) getIntent().getSerializableExtra("person");
        keyid = person.getPostId();
        Log.d(TAG, "keyid:" + keyid);
        if (keyid != null) {

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
                if (!TextUtils.isEmpty(msg)) {

                    Log.d(TAG, "Messaage:" + msg);
                    Log.d(TAG, "Token:" + token);
                    //mWebSocketClient.connectToWSS(WS_URL + "/" + ((OurApplication) getApplicationContext()).getUserToken());

                    postchat(keyid, token, msg);
                    Log.d(TAG, "Formcommentmessage" + msg);
                    //mWebSocketClient.sendMessage(msg);
                    UI.closeKeyboard(getApplicationContext(), mMessageBox.getWindowToken());
                    mMessageBox.setText(null);

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

    /*@Override
    public void onOpen() {}

    @Override
    public void onClose() {}

    @Override
    public void onTextMessage(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Person person = objectMapper.readValue(message, Person.class);

                    Log.d(TAG, message);

                    if (person.getType().equals("C")) {
                        Log.d(TAG, "I am message type C:");
                        Log.d(TAG, person.getPostId());

                        if (person.getPostId().equals(keyid)) {
                            //Add to Comment array if it belongs to same post id and notify dataset changed
                            mComments.add(person);
                            mCommentListAdapter.notifyDataSetChanged();
                        }

                        //Notify using Inbox style
                        //Notify(mDBHelper.getProfileInfo(msgObject.optString(Keys.KEY_NAME), 1),
                        //      msgObject.optString(Keys.KEY_MESSAGE),
                        //    mDBHelper.getProfileInfo(msgObject.optString(Keys.KEY_NAME), 2));
                    }

                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

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



            if (person.getType().equals("C")) {
                Log.d(TAG, "I am message type C:");
                Log.d(TAG, person.getPostId());

                if (person.getPostId().equals(keyid)) {
                    //person.setSubscriptionFlag("");
                    //person.setSenderName("You");
                    //person.setPhotoId("");

                    Log.d(TAG, mComments.get(mComments.size() - 1).getMessage());
                    Log.d(TAG, person.getMessage());
                    if(mComments.get(mComments.size() - 1).getMessage().equals(person.getMessage())){
                        mComments.set(mComments.size() - 1,person);
                        mCommentListAdapter.notifyItemChanged(mComments.size() - 1);

                    }
                }
            }

            }


        };






        private void postchat(String id, String token, String message) {

            ChatPostReqModel reqModel = new ChatPostReqModel();
            reqModel.setTime(Helper.getCurrentTimeStamp());
            reqModel.setPostId(id);
            reqModel.setMessage(message);
            reqModel.setType("C");
            reqModel.setToken(token);

            final Person prsn = new Person(
                    reqModel.getType(),
                    reqModel.getPostId(),
                    PreferenceEditor.getInstance(DiscussionActivity.this).getLoggedInUserName(),
                    "You",
                    reqModel.getMessage(),

                    "",
                    "",
                    reqModel.getTime(),
                    "SENT"
            );
        /*Log.d(TAG, "Latest date :" + mDBHelper.getFeedDataLatestTime());*/

            Call<SuccessRespModel> composeChat = ((OurApplication) getApplicationContext())
                    .getRestApi().ComposeChat(reqModel);
            composeChat.enqueue(new Callback<SuccessRespModel>() {
                @Override
                public void onResponse(Call<SuccessRespModel> call, Response<SuccessRespModel> response) {
                    Log.d(TAG, "Response body for post chat :" + response.body());
                    if (response.body() != null) {
                        //do something
                        if (response.body().isSuccess()) {

                             Log.d(TAG, "Response body for post chat :" + response.body());
                            Toast.makeText(getApplicationContext(), "Chat Posted Successfully", Toast.LENGTH_LONG).show();

                            mComments.add(prsn);
                            recyclerView.scrollToPosition(mComments.size() - 1);
                            mCommentListAdapter.notifyItemInserted(mComments.size() - 1);
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