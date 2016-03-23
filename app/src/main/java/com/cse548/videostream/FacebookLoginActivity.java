package com.cse548.videostream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse548.videostream.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.*;


public class FacebookLoginActivity extends Activity {


    CallbackManager callbackManager;

    TextView tv2;

    public FacebookLoginActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        PackageInfo info;
        try {
           info  = getPackageManager().getPackageInfo(
                    this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }  catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.fragment_main);
        tv2 =(TextView)findViewById(R.id.textView);

        Typeface face1= Typeface.createFromAsset(getAssets(), "Anders.ttf");
        tv2.setTypeface(face1, Typeface.BOLD);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                final Profile profile = Profile.getCurrentProfile();
                if(profile != null)
                    Log.d("Profile", profile.getName());
                final StringBuilder stringBuilder = new StringBuilder();
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject jsonObject= response.getJSONObject();
                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for(int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject friend = jsonArray.getJSONObject(i);
                                        Log.d("Friend", friend.getString("name"));
                                        stringBuilder.append(friend.getString("name")).append("$");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startCrosswalkActivity(profile, stringBuilder.toString());
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startCrosswalkActivity(Profile profile, String friends) {
        System.out.println("Start activity");
        Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
        String name = profile.getName();
        intent.putExtra("Name", name);
        intent.putExtra("Friends", friends);
        FacebookLoginActivity.this.startActivity(intent);
    }
}
