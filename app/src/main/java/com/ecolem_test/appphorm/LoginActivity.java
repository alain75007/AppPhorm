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

import java.util.ArrayList;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.act_login_title);

        // Auto-complete suggestions user account emails
        ArrayList<String> accountsEmails = getUserAccountEmail();
        setAutoCompleteText(R.id.act_login_email, accountsEmails);

        EditText passwordField = (EditText) findViewById(R.id.act_login_password);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());

    }

    public void onClickLogin(View view) {

        final String email = getEditTextValue(R.id.act_login_email);
        final String password = getEditTextValue(R.id.act_login_password);

        if (!email.isEmpty() && !password.isEmpty()) {

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Chargement...");
            pDialog.show();

            if (preferences.getString(email, "").equals(password)){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ACTIVE_USER", email);
                editor.commit();

                Intent mainIntent = new Intent(getApplicationContext(), AccountActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.act_login_wrong_email_or_password, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast toast = Toast.makeText(this, R.string.form_empty, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void toggleVisibility(View view) {

        EditText editText = (EditText) findViewById(R.id.act_login_password);
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



