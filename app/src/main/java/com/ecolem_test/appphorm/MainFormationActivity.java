package com.ecolem_test.appphorm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainFormationActivity extends AppActivity {

    String activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_formation);
        setTitle(R.string.act_main_formation_title);
        hideLeftBar();

        activeUser = preferences.getString("ACTIVE_USER", "");

        if (preferences.getString("ACTIVE_USER", "").equals("")){
            RelativeLayout all = (RelativeLayout) findViewById(R.id.all);
            Button videoButton = (Button) findViewById(R.id.act_main_formation_video_btn);
            RelativeLayout mainRight = (RelativeLayout) findViewById(R.id.mainRight);
            if (mainRight != null) {
                mainRight.removeView(videoButton);
            }
            else {
                all.removeView(videoButton);
            }
        }
        setUserInfo();

        loadFormationData();
    }

    // Load formation list
    protected void loadFormationData(){

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

                            String title = groupObj.getString("title");
                            String subtitle = groupObj.getString("subtitle");
                            String duration = groupObj.getString("duration");
                            String price = groupObj.getString("price");
                            String imageUrl = groupObj.getJSONObject("images").getJSONObject("landscapes").getString("small");
                            String descriptif = groupObj.getString("description");

                            int countViewed = 0;
                            String viewedVideos = preferences.getString(activeUser + "_viewedVideos", "");

                            JSONArray items = groupObj.getJSONArray("items");
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
                                    if (viewedVideos.contains(vid_title)){
                                        vid.setViewed(true);
                                        ++countViewed;
                                    }
                                    else {
                                        vid.setViewed(false);
                                    }

                                    videos.add(vid);
                                }
                            }

                            SharedPreferences.Editor edit = preferences.edit();

                            if (videos.size() == countViewed) {
                                edit.putString(activeUser + "_finished_" + title, "true");
                                TextView finished = (TextView) findViewById(R.id.act_main_formation_finished);
                                finished.setText("(Formation terminée)");
                            }

                            float fPrice = Float.parseFloat(price);
                            fPrice = Math.round(fPrice * 1000) / 1000;

                            setTitle(title);

                            ((TextView) findViewById(R.id.act_main_formation_subtitle)).setText(subtitle);
                            ((TextView) findViewById(R.id.act_main_formation_duration)).setText(splitToComponentTimes(duration));
                            ((TextView) findViewById(R.id.act_main_formation_price)).setText(fPrice + " €");

                            setMainFormationAdapter(imageUrl);

                            edit.putString("mainFormationTitle", title);
                            edit.putString("descriptif", descriptif);
                            edit.commit();

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

    public String splitToComponentTimes(String biggy)
    {
        long longVal = Long.parseLong(biggy);
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        String ints = hours + "h " + mins + "min " + secs + "s";
        return ints;
    }

    private void setMainFormationAdapter(String imageUrl) {

        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.act_main_formation_image);

        ImageLoader mImageLoader = new ImageLoader(VolleyApplication.getInstance().getRequestQueue(), new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });

        thumbNail.setImageUrl(imageUrl, mImageLoader);
    }

    public void onClickDescriptif(View view){
        Intent descriptif = new Intent(getApplicationContext(), DescriptifActivity.class);
        startActivity(descriptif);
    }

    public void onClickVideo(View view){
        Intent descriptif = new Intent(getApplicationContext(), VideoListActivity.class);
        startActivity(descriptif);
    }
}
