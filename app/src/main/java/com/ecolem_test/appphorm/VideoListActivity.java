package com.ecolem_test.appphorm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class VideoListActivity extends AppActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);

        setUserInfo();

        setTitle("Vidéos : " + preferences.getString("mainFormationTitle", ""));

        loadVideos();

        mListView = (ListView) findViewById(R.id.act_list_video_listview);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video sVideo = (Video) mListView.getItemAtPosition(position);

                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("videoTitle", sVideo.getTitle());
                edit.putString("videoUrl", sVideo.getUrl());
                edit.commit();

                //Toast.makeText(getApplicationContext(), "View info " + sGroups.title, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(intent);
            }
        });
    }


    // Load formation list
    protected void loadVideos(){

        final String url = "http://eas.elephorm.com/api/v1/trainings/" + preferences.getString("sFormationId", "");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("TAG", response.toString());
                        //Toast.makeText(getApplicationContext(), "TEST BEFORE TRY", Toast.LENGTH_SHORT).show();

                        try {
                            //Toast.makeText(getApplicationContext(), "TEST AFTER TRY", Toast.LENGTH_SHORT).show();
                            JSONObject groupObj = response;
                            Log.d("After Set 0", "TEST");

                            JSONArray items = groupObj.getJSONArray("items");

                            SharedPreferences.Editor edit = preferences.edit();

                            ArrayList<Video> videos = new ArrayList<>();

                            for (int i = 0; i < items.length(); ++i){
                                JSONObject item = (JSONObject) items.get(i);

                                if (item.getString("type").equals("video")){
                                    String vid_id = item.getString("_id");
                                    String vid_title = item.getString("title");

                                    JSONArray field_video = item.getJSONArray("field_video");
                                    String vid_url = ((JSONObject) field_video.get(0)).getString("filepath");

                                    String free = item.getString("free");

                                    Video vid = new Video(vid_id, vid_title, vid_url);

                                    if (free.equals("true")){
                                        vid.setFree(true);
                                    }
                                    String activeUser = preferences.getString("ACTIVE_USER", "");
                                    String viewedVideos = preferences.getString(activeUser + "_viewedVideos", "");
                                    if (viewedVideos.contains(vid_title)){
                                        vid.setViewed(true);
                                    }
                                    else {
                                        vid.setViewed(false);
                                    }

                                    videos.add(vid);
                                }
                            }
                            edit.commit();

                            Collections.reverse(videos);
                            setVideoListAdapter(videos);

                            Log.d("After Set", "TEST");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR TAG", error.getMessage());
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        VolleyApplication.getInstance().getRequestQueue().add(jsObjRequest);

    }

    private void setVideoListAdapter(ArrayList<Video> videos) {

        VideoListAdapter adapter = new VideoListAdapter(this, videos);

        ListView listView = (ListView) findViewById(R.id.act_list_video_listview);
        listView.setAdapter(adapter);
    }
}
