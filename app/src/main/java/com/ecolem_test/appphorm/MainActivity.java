package com.ecolem_test.appphorm;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String current_user = preferences.getString("ACTIVE_USER", "");

        if (!current_user.equals("") ) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }
}
