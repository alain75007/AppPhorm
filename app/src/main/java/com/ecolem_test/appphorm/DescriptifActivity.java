package com.ecolem_test.appphorm;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


public class DescriptifActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptif);

        String title = preferences.getString("sFormationTitle", "");
        String descriptif = preferences.getString("descriptif", "");

        TextView textDescriptif = (TextView) findViewById(R.id.act_descriptif_text);

        setTitle(title);
        textDescriptif.setText(Html.fromHtml(descriptif));
        textDescriptif.setMovementMethod(new ScrollingMovementMethod());
    }

}
