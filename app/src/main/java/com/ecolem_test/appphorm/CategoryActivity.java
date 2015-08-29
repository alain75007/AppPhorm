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


public class CategoryActivity extends AppActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle(R.string.act_category_title);

        hideLeftBar();
        setUserInfo();
        loadSubCategories();

        mListView = (ListView) findViewById(R.id.act_category_catalog);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category sCategory = (Category) mListView.getItemAtPosition(position);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sSubCategoryId", sCategory.getId());
                editor.putString("sSubCategoryTitle", sCategory.getTitle());
                editor.putString("sSubCategoryInfo", sCategory.getDescription());

                editor.commit();

                //Toast.makeText(getApplicationContext(), "View info " + sGroups.title, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), FormationActivity.class);
                startActivity(intent);
            }
        });
    }

    // Load formation list
    protected void loadSubCategories(){

        final String url = "http://eas.elephorm.com/api/v1/categories";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", response.toString());
                        //Toast.makeText(getApplicationContext(), "TEST BEFORE TRY", Toast.LENGTH_SHORT).show();

                        try {
                            //Toast.makeText(getApplicationContext(), "TEST AFTER TRY", Toast.LENGTH_SHORT).show();

                            ArrayList<Category> subCategories = new ArrayList<>();

                            for (int i = 0; i < response.length(); ++i) {

                                JSONObject groupObj = (JSONObject) response.get(i);

                                if (!groupObj.getString("active").equals("true")){
                                    continue;
                                }

                                String id = groupObj.getString("_id");

                                if (id.equals(preferences.getString("sCategoryId", ""))) {

                                    JSONArray subCats = groupObj.getJSONArray("subcategories");

                                    for (int j = 0; j < subCats.length(); ++j) {
                                        JSONObject sc = (JSONObject) subCats.get(j);
                                        String subCatId = sc.getString("_id");
                                        String subCatTitle = sc.getString("title");
                                        String subCatDescription = sc.getString("description");

                                        subCategories.add(new Category(subCatId, subCatTitle, subCatDescription));
                                    }

                                    break;
                                }

                            }

                            Collections.reverse(subCategories);

                            setCategoryListAdapter(subCategories);

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

    private void setCategoryListAdapter(ArrayList<Category> catalog) {

        CategoryListAdapter adapter = new CategoryListAdapter(this, catalog);

        ListView listView = (ListView) findViewById(R.id.act_category_catalog);
        listView.setAdapter(adapter);
    }
}
