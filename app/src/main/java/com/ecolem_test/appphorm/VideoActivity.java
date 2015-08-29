package com.ecolem_test.appphorm;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class VideoActivity extends AppActivity {

    private String video_title;
    private String video_url;
    private MediaController mediaControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoActivity.this);
        }

        video_title = preferences.getString("videoTitle", "");
        video_url = preferences.getString("videoUrl", "");

        Log.d("TAG VIDEO URL : ", video_url);
        //Toast.makeText(getApplicationContext(), "VIDEO URL : " + video_url, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "VIDEO TITLE : " + video_title, Toast.LENGTH_SHORT).show();

        setTitle(video_title);

        TextView videoInfo = (TextView) findViewById(R.id.act_video_info);
        videoInfo.setText("Touchez la zone sombre pour lancer la vidéo.");

        VideoView videoView = (VideoView) findViewById(R.id.act_video_reader);
        //videoView.setVideoPath(video_url);
        try {
            //set the media controller in the VideoView
            videoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            videoView.setVideoURI(Uri.parse(video_url));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TAG VideoActivity : ", " IN onTouch Listener");
                SharedPreferences.Editor edit = preferences.edit();
                String viewedVideos = "";
                String activeUser = preferences.getString("ACTIVE_USER", "");
                if (!preferences.getString(activeUser + "_viewedVideos", "").equals("")){
                    viewedVideos = preferences.getString(activeUser + "_viewedVideos", "");
                }
                if (!viewedVideos.contains(video_title)){
                    viewedVideos += ", " + video_title;
                    edit.putString(activeUser + "_viewedVideos", viewedVideos);
                    edit.commit();
                }
                return false;
            }
        });
    }

}
