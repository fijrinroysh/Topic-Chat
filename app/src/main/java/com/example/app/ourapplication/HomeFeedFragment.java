package com.example.app.ourapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app.ourapplication.database.DBHelper;
import com.example.app.ourapplication.pref.PreferenceEditor;
import com.example.app.ourapplication.rest.model.request.HomeFeedReqModel;
import com.example.app.ourapplication.rest.model.request.LocationModel;
import com.example.app.ourapplication.rest.model.response.ComposeRespModel;
import com.example.app.ourapplication.rest.model.response.GetDataRespModel;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.rest.model.response.SuccessRespModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFeedFragment extends Fragment {

    private final String TAG = HomeFeedFragment.class.getSimpleName();

   // private WebSocketClient mWebSocketClient;
   //private FCM mWebSocketClient;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /*Views*/
    private FcmTokenService mFcmTokenService = new FcmTokenService(getContext());
    private List<Person> mFeeds = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedRVAdapter mFeedListAdapter;
    private DBHelper mDBHelper;

    public LocationModel location;
    public CoordinatorLayout mCoordinatorLayout;

    public HomeFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = PreferenceEditor.getInstance(getContext()).getLocation();
        //mWebSocketClient = ((OurApplication)getActivity().getApplicationContext()).getClient();
        //mWebSocketClient.addWebSocketListener(this);
        mFcmTokenService.CreateGCMToken();
        mDBHelper = new DBHelper(getContext());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_feed, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // mFeeds = mDBHelper.getFeedDataAll();

        mFeedListAdapter = new FeedRVAdapter(getContext(),mFeeds);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_homefeed);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mFeedListAdapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /**
             * This method is called when swipe refresh is pulled down
             */
            @Override
            public void onRefresh() {
                // Refresh items
                getUpdatedFeeds();
            }

        });

//if (mFeeds.size()==0){
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                //mDBHelper.deleteFeedData();
                //HTTP requst to fetch data for Homefeed
                mSwipeRefreshLayout.setRefreshing(true);
                getUpdatedFeeds();

            }
        });
//}

        Log.d(TAG, "Length of Feed Array" + ": " + mFeeds.size());
//        getSubscribers();



    }


    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);

        //mFeedListAdapter.onViewDetachedFromWindow(ViewHolder);
        //recyclerView.setAdapter(mFeedListAdapter);
        super.onPause();
    }

    @Override
    public void onResume() {

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("feedevent"));


        recyclerView.setAdapter(mFeedListAdapter);
        mFeeds.clear();
        mFeeds.addAll(mDBHelper.getFeedDataAll());
        mFeedListAdapter.notifyDataSetChanged();
        super.onResume();
        //mFeedListAdapter.onViewDetachedFromWindow(ViewHolder);

    }


    @Override
    public void onDestroy() {

        super.onDestroy();
       // mWebSocketClient.removeWebSocketListener(this);

    }

   /* @Override
    public void onTextMessage(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                JSONObject msgObject = null;
                try {
                    msgObject = new JSONObject(message);
                    Log.d(TAG, "TYPE:" + msgObject.optString(Keys.KEY_TYPE) + ":");

                    if (msgObject.optString(Keys.KEY_TYPE).equals("F")) {
                        Log.d(TAG, "I am message type F:" + ":" + msgObject.optString(Keys.KEY_NAME));
                        try {
                            Person person = new ObjectMapper().readValue(message, Person.class);
                            //mFeeds.add(0, person);
                            mFeedListAdapter.notifyDataSetChanged();
                          //  mDBHelper.insertFeedData(person, "WS");
                          //  mDBHelper.insertCommentData(person);
                            HomeFeedFragment.this.notify(person);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/






    private void getUpdatedFeeds(){
        HomeFeedReqModel reqModel = new HomeFeedReqModel("F","5",
                location.getLongitude(),
                location.getLatitude(),
                mDBHelper.getFeedDataLatestTime(),
                PreferenceEditor.getInstance(getContext()).
                getLoggedInUserName());

        //Log.d(TAG, "Latest date :" + mDBHelper.getFeedDataLatestTime());

        Call<GetDataRespModel> queryHomeFeeds = ((OurApplication)getActivity().getApplicationContext())
                .getRestApi().queryHomeFeed(reqModel);
        queryHomeFeeds.enqueue(new Callback<GetDataRespModel>() {
            @Override
            public void onResponse(Call<GetDataRespModel> call, Response<GetDataRespModel> response) {
                if (response.body() != null) {
                    //do something

                    ArrayList<Person> data = response.body().getData();
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                           // mDBHelper.insertFeedData(data.get(i));
                           // Log.d(TAG, "insertFeedData :" + data.get(i));
                            //mFeeds.add(0, data.get(i));
                            //mFeedListAdapter.notifyItemInserted(0);
                        }
                        Toast.makeText(getActivity(), "New feeds", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "No more feeds to load", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Null Response body", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetDataRespModel> call, Throwable t) {
                Log.d(TAG, "Query failed: " + t);
                Toast.makeText(getActivity(), "Loading feeds failed", Toast.LENGTH_LONG).show();
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
    }




    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            // String message = intent.getStringExtra("chatdata");
            //Log.d(TAG, "Got message: " + message);

                /*Got message: {postid=201907282340009486946972, userid=, subscriptionflag=, name=, time=2000-12-31 12:00:00, type=C, image=, message=Ffw, profileimage=}*/

            Person person = (Person) intent.getSerializableExtra("person");




                int position = find_person(mFeeds,person);

                if (position == -1 ) {

                    Log.d(TAG, person.getPostId());
                    mFeeds.add(0, person);
                    mFeedListAdapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);

                }


        }

    };


    public int find_person(List<Person> list, Person person ){
        int p;

        Person prsn = new Person(person.getType(),
                person.getPostId(),
                "",
                person.getUserId(),
                "",
                person.getSenderName(),
                person.getMessage(),
                person.getPhotoId(),
                person.getPhotoMsg(),
                person.getTimeMsg(),
                person.getFlag());


        Log.d(TAG,"Check if this person is in the list: " + prsn.toString());
        if(list.contains(prsn)){
            p = list.indexOf(prsn);
            Log.d(TAG, "Position of the person found in list. Return position to replace");
        }else {
            p=-1;
            Log.d(TAG, "Position of the person not found in list. Send default value");
        }

        return p;

    }

/*    private void getSubscribers(){

        Call<SubscriberDataRespModel> querySubscriberData = ((OurApplication)getActivity().getApplicationContext())
                .getRestApi().querySubscriberData();
        querySubscriberData.enqueue(new Callback<SubscriberDataRespModel>() {
            @Override
            public void onResponse(Call<SubscriberDataRespModel> call, Response<SubscriberDataRespModel> response) {

                ArrayList<Subscriber> data = response.body().getData();
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        //mDBHelper.insertSubscriberData(data.get(i));
                        Log.d(TAG, "insertSubscriberData :" + data.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscriberDataRespModel> call, Throwable t) {
                Log.d(TAG, "Query failed: " + t);
                Toast.makeText(getActivity(), "Getting Subscriber Data Failed", Toast.LENGTH_LONG).show();
            }
        });
    }*/
}