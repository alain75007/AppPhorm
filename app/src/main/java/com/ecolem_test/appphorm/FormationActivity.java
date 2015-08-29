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
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class FormationActivity extends AppActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);
        setTitle(R.string.act_formation_title);

        hideLeftBar();
        setUserInfo();
        loadFormations();

        mListView = (ListView) findViewById(R.id.act_formation_catalog);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Formation sFormation = (Formation) mListView.getItemAtPosition(position);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sFormationId", sFormation.getId());
                editor.putString("sFormationTitle", sFormation.getTitle());
                editor.putString("sFormationInfo", sFormation.getDescription());

                editor.commit();

                //Toast.makeText(getApplicationContext(), "View info " + sGroups.title, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainFormationActivity.class);
                startActivity(intent);
            }
        });
    }

    // Load formation list
    protected void loadFormations(){

        final String url = "http://eas.elephorm.com/api/v1/subcategories/" + preferences.getString("sSubCategoryId", "") + "/trainings";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", response.toString());
                        //Toast.makeText(getApplicationContext(), "TEST BEFORE TRY", Toast.LENGTH_SHORT).show();

                        try {
                           //Toast.makeText(getApplicationContext(), "TEST AFTER TRY", Toast.LENGTH_SHORT).show();

                            ArrayList<Formation> formations = new ArrayList<>();

                            for (int i = 0; i < response.length(); ++i) {

                                JSONObject groupObj = (JSONObject) response.get(i);

                                if (!groupObj.getString("active").equals("true")){
                                    continue;
                                }

                                String id = groupObj.getString("ean13");
                                String title = groupObj.getString("title");
                                String description = groupObj.getString("description");

                                Formation formation = new Formation(id, title, description);

                                String activeUser = preferences.getString("ACTIVE_USER", "");
                                String finished_label = activeUser + "_finished_" + title;
                                String finished = preferences.getString(finished_label, "false");

                                if (finished.equals("true")){
                                    formation.setDone(true);
                                }

                                formations.add(formation);
                            }

                            Collections.reverse(formations);

                            setFormationListAdapter(formations);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", error.getMessage());
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

    private void setFormationListAdapter(ArrayList<Formation> catalog) {

        FormationListAdapter adapter = new FormationListAdapter(this, catalog);

        ListView listView = (ListView) findViewById(R.id.act_formation_catalog);
        listView.setAdapter(adapter);
    }
}
