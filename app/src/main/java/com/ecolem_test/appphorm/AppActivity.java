package com.ecolem_test.appphorm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 13/08/2015.
 */
public class AppActivity extends AppCompatActivity {

    protected SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void hideLeftBar(){
        if (preferences.getString("ACTIVE_USER", "").equals("")){
            RelativeLayout all = (findViewById(R.id.all) != null) ? (RelativeLayout) findViewById(R.id.all) : null;
            if (all != null){
                LinearLayout child = (LinearLayout) all.findViewById(R.id.mainLeft);
                all.removeView(child);
            }
        }
        else {
            Log.d("AppActivity : ", "ACTIVE_USER is defined !!!");
        }
    }

    public void changeUserInfo(){
        Intent editUserInfo = new Intent(getApplicationContext(), UserInfosActivity.class);
        startActivity(editUserInfo);
        finish();
    }

    public void setUserInfo(){

        String email = preferences.getString("ACTIVE_USER", "");
        String name = preferences.getString(email + "_name", "");
        String pname = preferences.getString(email + "_pname", "");
        String birthday = preferences.getString(email + "_birthday", "");

        if (!email.isEmpty()) {
            ((TextView) findViewById(R.id.act_account_name)).setText(name);
            ((TextView) findViewById(R.id.act_account_pname)).setText(pname);
            ((TextView) findViewById(R.id.act_account_birthday)).setText(birthday);
            ((TextView) findViewById(R.id.act_account_email)).setText(email);
        }
    }

    public void backToAccountActivity(){
        Intent accountIntent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(accountIntent);
        finish();
    }

    public void confirmExit() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.dialog_exit_msg)
                .setTitle(R.string.dialog_exit_title);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("ACTIVE_USER");
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void logout() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.dialog_logout_msg)
                .setTitle(R.string.dialog_logout_title);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String user_email = preferences.getString("ACTIVE_USER", "");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("ACTIVE_USER");
                        editor.remove(user_email + "_token");
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!preferences.getString("ACTIVE_USER", "").equals("")) {
            getMenuInflater().inflate(R.menu.menu_account, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_login) {
            onClickLogin(getCurrentFocus());
        }

        if (id == R.id.action_catalog) {
            onClickCatalog(getCurrentFocus());
        }

        if (id == R.id.action_subscribe) {
            onClickSubscribe(getCurrentFocus());
        }

        if (id == R.id.action_account) {
            onClickHome(getCurrentFocus());
        }

        if (id == R.id.action_logout) {
            logout();
        }

        if (id == R.id.action_quit) {
            confirmExit();
        }

        if (id == R.id.action_change_user_info) {
            changeUserInfo();
        }

        if (id == R.id.action_account) {
            backToAccountActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get user accounts email
     * See setAutoCompleteText for set this on text field
     * @return List of emails or empty array
     */
    public ArrayList<String> getUserAccountEmail() {

        ArrayList<String> stringArrayList = new ArrayList<>();

        try{
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            Account[] accounts = manager.getAccountsByType("com.google");
            //Pattern emailPattern = Patterns.EMAIL_ADDRESS;

            for (Account account : accounts) {
                stringArrayList.add(account.name);
            }
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:" + e) ;
        }

        return stringArrayList;
    }

    /**
     * Add auto-complete suggestions list on text field
     * Use AutoCompleteTextView text field
     * See getUserAccountEmail for example list
     * @param AutoCompleteTextViewId Id of AutoCompleteTextView
     * @param stringArrayList Suggestions list
     */
    public void setAutoCompleteText(int AutoCompleteTextViewId, ArrayList<String> stringArrayList) {

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(AutoCompleteTextViewId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringArrayList);
        textView.setAdapter(adapter);
    }

    /**
     * Get value of EditText
     * @param EditTextId EditText Id
     * @return Value of field
     */
    public String getEditTextValue(int EditTextId) {

        EditText editTextEmail = (EditText) findViewById(EditTextId);
        return editTextEmail.getText().toString();
    }

    /**
     * Check if device is connected on internet
     * @return boolean
     */
    public boolean deviceIsConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickCatalog(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void onClickSubscribe(View view) {
        Intent intent = new Intent(this, SubscribeActivity.class);
        startActivity(intent);
    }

    public void onClickHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
