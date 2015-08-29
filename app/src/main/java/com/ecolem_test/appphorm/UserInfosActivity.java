package com.ecolem_test.appphorm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


public class UserInfosActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infos);
        setTitle(R.string.act_user_info_title);

        // Auto-complete suggestions user account emails
        // ArrayList<String> accountsEmails = getUserAccountEmail();
        // setAutoCompleteText(R.id.act_subscribe_email, accountsEmails);

        EditText passwordField = (EditText) findViewById(R.id.act_user_info_password);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());

        setUserInfo();

    }

    public void onClickChange(View view) {

        String oldEmail = preferences.getString("ACTIVE_USER", "");
        String oldPassword = preferences.getString(oldEmail, "");

        String name = getEditTextValue(R.id.act_user_info_name);
        String pname = getEditTextValue(R.id.act_user_info_pname);
        String birthday = getEditTextValue(R.id.act_user_info_birthday);
        String email = getEditTextValue(R.id.act_user_info_email);
        String password = getEditTextValue(R.id.act_user_info_password);

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        SharedPreferences.Editor editor = preferences.edit();

        if (!name.isEmpty()) {
            if (!email.isEmpty()){
                editor.putString(email + "_name", name);
            }
            else {
                editor.putString(oldEmail + "_name", name);
            }
        }
        if (!pname.isEmpty()) {
            if (!email.isEmpty()){
                editor.putString(email + "_pname", pname);
            }
            else {
                editor.putString(oldEmail + "_pname", pname);
            }
        }
        if (!birthday.isEmpty()) {
            if (!email.isEmpty()){
                editor.putString(email + "_birthday", birthday);
            }
            else {
                editor.putString(oldEmail + "_birthday", birthday);
            }
        }
        if (!email.isEmpty() && !password.isEmpty()) {
            editor.putString("ACTIVE_USER", email);
            editor.putString(email, password);
        }
        else {
            if (!email.isEmpty()) {
                editor.putString("ACTIVE_USER", email);
                editor.putString(email, oldPassword);
            }
            if (!password.isEmpty()) {
                editor.remove(oldEmail);
                editor.putString(oldEmail, password);
            }
        }
        editor.commit();

        Intent mainIntent = new Intent(getApplicationContext(), AccountActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();

    }

    public void toggleVisibility(View view) {

        EditText editText = (EditText) findViewById(R.id.act_subscribe_password);

        CheckBox checkBox = (CheckBox) findViewById(R.id.togglePasswordVisibility);

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        if (checkBox.isChecked()) {
            editText.setTransformationMethod(null);
        }
        else {
            editText.setTransformationMethod(new PasswordTransformationMethod());
        }

        editText.setSelection(start, end);
    }
}
