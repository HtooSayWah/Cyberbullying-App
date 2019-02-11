package com.htoosaywah.cyberbullyingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private JSONArray postList;

    public PostAdapter(JSONArray postList) {
        Log.d("this is in adapter: ",""+postList.length() );
        this.postList = postList;

    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_view, viewGroup, false);
        return new PostViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder postViewHolder, int i) {
        Log.d("this onBindViewHolder: ","check postion");
        try{
            JSONObject jsonobject = postList.getJSONObject(i);
            Log.d("adapter username: ",""+jsonobject.getString("username") );
            Log.d("adapter postedTime: ",""+jsonobject.getString("postedTime") );
            Log.d("adapter post: ",""+jsonobject.getString("post") );
            postViewHolder.userName.setText(jsonobject.getString("username"));
            postViewHolder.postedTime.setText(jsonobject.getString("postedTime"));
            postViewHolder.post.setText(jsonobject.getString("post"));
            Log.d("bully status: ",""+ jsonobject.getBoolean("isBully"));
            Boolean flag = jsonobject.getBoolean("isBully");
            if(flag){
                Log.d("bully status: ",""+ jsonobject.getBoolean("isBully") + jsonobject.getString("post"));
                postViewHolder.linearLayout.setVisibility(View.VISIBLE);
                postViewHolder.isbully.setText("*This post contains Bully contents " );

                JSONArray arrJson = jsonobject.getJSONArray("bullyWords");
                if (!(arrJson.length() == 0)){
                    String bullyWords = "";
                    String[] arr = new String[arrJson.length()];
                    if(arrJson.length() == 1){
                        bullyWords = "(1) "+arrJson.getString(0);
                    }
                    else{
                        Log.d("BullwordsList:","In the bully status!"+ arrJson.length());
                        Log.d("BullwordsList:","In the bully status!"+ arrJson.getString(0));
                        Log.d("BullwordsList:","In the bully status!"+ arrJson.getString(1));
                        String temp = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j = 0; j <arrJson.length(); j++) {
                            int no = j+1;
                            Log.d("InsideLoop:","("+no+") "+arrJson.getString(j)+"\n");
                            stringBuilder.append("("+no+") "+arrJson.getString(j)+"\n");
                            //temp += "("+no+") "+arrJson.getString(j)+"\n";
                        }
/*
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int k = 0; k<=arr.length; k++){
                            int no = k+1;
                            stringBuilder.append("("+no+") "+arr[k]+"\n");
                        }*/

                        //bullyWords = stringBuilder.toString();
                        Log.d("BullwordsListMore:",stringBuilder.toString());
                        Log.d("Temp:",temp);
                        bullyWords = stringBuilder.toString();
                    }

                    postViewHolder.bullyWords.setText(bullyWords);
                }

            }
            else{
                //nothing do
                postViewHolder.linearLayout.setVisibility(View.GONE);
                postViewHolder.bullyWords.setText("");
            }


            try{
                URL url = new URL(jsonobject.getString("userpic"));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                postViewHolder.userPic.setImageBitmap(bmp);
            }catch (Exception e){
                Log.d("Error pic load",""+e);
            }
        }catch (Exception e){
            //
        }


    }

    @Override
    public int getItemCount() {
        return postList.length();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView userPic;
        public TextView post;
        public TextView isbully;
        public TextView bullyWords;
        public TextView postedTime;
        public LinearLayout linearLayout;


        public PostViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.bullyStatusCtn);
            userPic = view.findViewById(R.id.userimg);
            userName = view.findViewById(R.id.username);
            post = view.findViewById(R.id.userpost);
            isbully = view.findViewById(R.id.bullyStatus);
            bullyWords = view.findViewById(R.id.bullywords);
            postedTime = view.findViewById(R.id.posttime);
        }
    }
}
