package com.htoosaywah.cyberbullyingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    CallbackManager callbackManager;
    EditText editPost;
    PostAdapter mAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_post);
        sharedPref = getApplicationContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        editPost = findViewById(R.id.edit_post);
        editPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(PostActivity.this, UserPostActivity.class));
                return true;
            }
        });


        recyclerView = findViewById(R.id.my_recycler);
        getNewFeedsArray();
    }

    public void getNewFeedsArray() {
        final JSONArray jsonObject = null;
        String url = "https://peaceful-ridge-12816.herokuapp.com/feed";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success

                JSONObject string = response;
                try {
                    Log.d("string for jsonArray", "" + string.getJSONArray("posts"));
                    mAdapter = new PostAdapter(string.getJSONArray("posts"));
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PostActivity.this, "volley error: " + error, Toast.LENGTH_SHORT).show();
                //TODO: handle failure
            }
        });

        Volley.newRequestQueue(PostActivity.this).add(jsonRequest);
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
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // your code.
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent _intentOBJ = new Intent(Intent.ACTION_MAIN);
                        _intentOBJ.addCategory(Intent.CATEGORY_HOME);
                        _intentOBJ.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        _intentOBJ.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(_intentOBJ);
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNewFeedsArray();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewFeedsArray();
    }
}
