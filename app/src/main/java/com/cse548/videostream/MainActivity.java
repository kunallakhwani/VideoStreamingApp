package com.cse548.videostream;

import android.os.Bundle;

import org.xwalk.core.XWalkView;

import android.app.Activity;

public class MainActivity extends Activity {
    private XWalkView mXWalkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mXWalkView = (XWalkView) findViewById(R.id.activity_main);
        // mXWalkView.load("http://crosswalk-project.org/", null);
       mXWalkView.load("file:///android_asset/index.html",null);
    }
}