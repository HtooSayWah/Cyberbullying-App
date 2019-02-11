package com.htoosaywah.cyberbullyingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPostActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    CallbackManager callbackManager;

    Button btnCancle;
    Button btnSubmit;

    EditText userPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_user_post);
        sharedPref = getApplicationContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        btnCancle = findViewById(R.id.btn_cancle);
        btnSubmit = findViewById(R.id.btn_submit);

        userPost = findViewById(R.id.edit_user_post);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserPostActivity.this, PostActivity.class));
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Do add post to cloud
                String userid = sharedPref.getString("login_id", null);
                String username = sharedPref.getString("username", null) ;
                String userpost = userPost.getText().toString();
                Long timestamp = System.currentTimeMillis();

                savePost(userid,username,userpost,timestamp);
            }
        });

    }

    public void savePost(String id, String username, String post, Long timestamp){
        String url = "http://peaceful-ridge-12816.herokuapp.com/post";
        JSONObject registerId = new JSONObject();
        userPost.setFocusable(false);

        try {
            registerId.put("loginId", id);
            registerId.put("username", username);
            registerId.put("post", post);
            registerId.put("timestamp", timestamp);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, registerId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                Boolean success = false;
                int bully_count = 0;
                try {
                    success = response.getBoolean("success");
                    bully_count = response.getInt("total_bullying_post_count");
                } catch (JSONException e) {

                }

                if(success){
                    if (bully_count == 3){
                        new AlertDialog.Builder(UserPostActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Post Warning Alert")
                                .setMessage("Dear user, you submitted posts containing bullying contents at least three time.If you will submit a post containing bullying contents next time, the system will block you!")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(UserPostActivity.this, PostActivity.class));
                                    }
                                }).show();
                        finish();
                        startActivity(new Intent(UserPostActivity.this, PostActivity.class));
                    }
                    else{
                        finish();
                        startActivity(new Intent(UserPostActivity.this, PostActivity.class));
                    }

                }
                else{
                    new AlertDialog.Builder(UserPostActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Post Error")
                            .setMessage("Dear user you are currently unavailable to post any update status because you have suspected as posting something related about Cyberbullying contents")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(UserPostActivity.this, PostActivity.class));
                                }
                            }).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(UserPostActivity.this, "volley error: "+error, Toast.LENGTH_SHORT).show();
                //TODO: handle failure
            }
        });

        Volley.newRequestQueue(UserPostActivity.this).add(jsonRequest);


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
                startActivity(new Intent(UserPostActivity.this, MainActivity.class));
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
        startActivity(new Intent(UserPostActivity.this, PostActivity.class));
    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }
}
