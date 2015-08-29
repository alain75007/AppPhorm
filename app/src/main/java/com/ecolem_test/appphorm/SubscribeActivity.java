package com.ecolem_test.appphorm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class SubscribeActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        setTitle(R.string.act_subscribe_title);

        // Auto-complete suggestions user account emails
        // ArrayList<String> accountsEmails = getUserAccountEmail();
        // setAutoCompleteText(R.id.act_subscribe_email, accountsEmails);

        EditText passwordField = (EditText) findViewById(R.id.act_subscribe_password);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());

    }

    public void onClickSubscribe(View view) {

        final String name = getEditTextValue(R.id.act_subscribe_name);
        final String pname = getEditTextValue(R.id.act_subscribe_pname);
        final String birthday = getEditTextValue(R.id.act_subscribe_birthday);
        final String email = getEditTextValue(R.id.act_subscribe_email);
        final String password = getEditTextValue(R.id.act_subscribe_password);

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !birthday.isEmpty() && !pname.isEmpty()) {

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ACTIVE_USER", email);
            editor.putString(email, password);
            editor.putString(email + "_name", name);
            editor.putString(email + "_pname", pname);
            editor.putString(email + "_birthday", birthday);
            editor.commit();

            Intent mainIntent = new Intent(getApplicationContext(), AccountActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();

        } else {
            Toast toast = Toast.makeText(this, R.string.form_empty, Toast.LENGTH_SHORT);
            toast.show();
        }

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
