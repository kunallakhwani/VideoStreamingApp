package com.cse548.videostream;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import android.app.Activity;
import android.util.Log;

import com.facebook.FacebookSdk;

public class MainActivity extends Activity {
    private XWalkView mXWalkView;/*
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = null;
    FacebookLoginActivity mainFragment = null;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);/*

        fragmentTransaction = fragmentManager.beginTransaction();
        mainFragment = new FacebookLoginActivity();
        fragmentTransaction.add(R.id.fragmentParentViewGroup, mainFragment);*/
        /*String name = null;
        Bundle extras= getIntent().getExtras();
        if(extras != null) {
            name = extras.getString("name");
        }*/
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String friends = intent.getStringExtra("Friends");
        Log.d("Friends", friends);
        setContentView(R.layout.activity_main);
        mXWalkView = (XWalkView) findViewById(R.id.activity_main);
        // turn on debugging
        //XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        if(friends != null)
            mXWalkView.load("file:///android_asset/index.html?name=" + name + "&friends=" + friends,null);
        else if(friends == null)
            mXWalkView.load("file:///android_asset/index.html?name=" + name,null);
    }
}