package com.htoosaywah.cyberbullyingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private TextView info;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    SharedPreferences sharedPref;
    final AccessToken accessToken = AccessToken.getCurrentAccessToken();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        AppEventsLogger.activateApp(this);


        sharedPref = getApplicationContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", accessToken != null && !accessToken.isExpired());
        editor.commit();

        if (sharedPref.getBoolean("isLoggedIn", false) == true) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));

        } else if (sharedPref.getString("login_id", null) != null) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        } else {
            LoginButton loginButton = findViewById(R.id.loginButton);
            loginButton.setReadPermissions(Arrays.asList(EMAIL));

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    // App code

                    SharedPreferences.Editor editor = sharedPref.edit();
                    Log.d("UserID_AccessToken", loginResult.getAccessToken().getUserId() + " " + loginResult.getAccessToken().getToken());
                    editor.putString("login_id", loginResult.getAccessToken().getUserId());
                    editor.putString("username", Profile.getCurrentProfile().getName());
                    editor.putBoolean("isLoggedIn", true);
                    editor.commit();
                    Log.d("registerCheck",loginResult.getAccessToken().getUserId()+" "+loginResult.getAccessToken().getToken());

                    if (sharedPref.getBoolean("register", false) == false) {
                        Boolean flag = registerOnHeruku(loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getToken());
                        if (flag) {
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                        }
                    }
                    else{
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                    }
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                Log.d("Error login",""+error);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref = getApplicationContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", accessToken != null && !accessToken.isExpired());
        editor.commit();

        if (sharedPref.getBoolean("isLoggedIn", false) == true) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));

        } else if (sharedPref.getString("login_id", null) != null) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        } else {
            LoginButton loginButton = findViewById(R.id.loginButton);
            loginButton.setReadPermissions(Arrays.asList(EMAIL));

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    // App code

                    SharedPreferences.Editor editor = sharedPref.edit();
                    Log.d("UserID_AccessToken", loginResult.getAccessToken().getUserId() + " " + loginResult.getAccessToken().getToken());
                    editor.putString("login_id", loginResult.getAccessToken().getUserId());
                    editor.putString("username", Profile.getCurrentProfile().getName());
                    editor.putBoolean("isLoggedIn", true);
                    editor.commit();
                    Log.d("registerCheck", loginResult.getAccessToken().getUserId() + " " + loginResult.getAccessToken().getToken());

                    if (sharedPref.getBoolean("register", false) == false) {
                        Boolean flag = registerOnHeruku(loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getToken());
                        if (flag) {
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                        }
                    } else {
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                    }
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(MainActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                    Log.d("Error login", "" + error);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("login_id", null);
                editor.putBoolean("register", false);
                editor.putBoolean("isLoggedIn", false);
                editor.commit();
                LoginManager.getInstance().logOut();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Boolean registerOnHeruku(String id, String token) {
        String url = "https://peaceful-ridge-12816.herokuapp.com/register";
        final Boolean[] flag = {false};
        JSONObject registerId = new JSONObject();
        try {
            registerId.put("facebook_id", id);
            registerId.put("access_token", token);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("user id and token: ", id+ token);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, registerId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    flag[0] = response.getBoolean("success");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("register",flag[0] );
                    editor.commit();
                    //startActivity(new Intent(MainActivity.this, PostActivity.class));
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "volley error: "+error, Toast.LENGTH_SHORT).show();
                //TODO: handle failure
            }
        });

        Volley.newRequestQueue(MainActivity.this).add(jsonRequest);
        return flag[0];

    }
}
