package com.example.app.ourapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.app.ourapplication.ComposeFragment;
import com.example.app.ourapplication.HomeFeedFragment;
import com.example.app.ourapplication.ProfileFragment;
import com.example.app.ourapplication.R;
import com.roughike.bottombar.BottomBar;

/**
 * Created by sarumugam on 17/01/17.
 */
public class HomeActivity extends AppCompatActivity {

    private final String TAG = HomeActivity.class.getSimpleName();

    BottomNavigationView navigation;

    private ActionBar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = getSupportActionBar();
//getting bottom navigation view and attaching the listener
        navigation = (BottomNavigationView) findViewById(R.id.bottomBar);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeFeedFragment());


        //navigation.getMenu().findItem(R.id.add_home).setChecked(true);


    }







private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
            Fragment fragment;
            switch (item.getItemId()) {

                case R.id.add_home:
                    //toolbar.setTitle("Home");
                    navigation.setVisibility(View.VISIBLE);
                    fragment = new HomeFeedFragment();
                    return loadFragment(fragment);

                case R.id.add_message:
                    //toolbar.setTitle("Compose");
                    navigation.setVisibility(View.GONE);
                    fragment = new ComposeFragment();
                    return loadFragment(fragment);

                case R.id.add_profile:
                    //toolbar.setTitle("Profile");
                    navigation.setVisibility(View.VISIBLE);
                    fragment = new ProfileFragment();
                    return loadFragment(fragment);
            }
            return false;
        }

};

    private boolean  loadFragment(Fragment fragment) {
        // load fragment
        //switching fragment
        if (fragment != null) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentContainer, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
            return true;
        }
        return false;
    }





 @Override
    public void onBackPressed() {


        int seletedItemId = navigation.getSelectedItemId();
        if (R.id.add_home != seletedItemId) {
            navigation.setSelectedItemId(R.id.add_home);
        } else {
            moveTaskToBack(true);
        }
    }



};

