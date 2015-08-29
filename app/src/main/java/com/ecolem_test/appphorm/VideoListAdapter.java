package com.ecolem_test.appphorm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 26/08/2015.
 */
public class VideoListAdapter extends ArrayAdapter<Video> {
    public VideoListAdapter(Context context, ArrayList<Video> videos) {
        super(context, 0, videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Video video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_list_row, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.video_title);
        TextView tvHome = (TextView) convertView.findViewById(R.id.video_viewed);

        tvName.setText(video.getTitle());
        if (video.isViewed()) {
            tvHome.setText("Vu");
        }
        else {
            tvHome.setText("");
        }

        return convertView;
    }
}
