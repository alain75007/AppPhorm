package com.ecolem_test.appphorm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 17/08/2015.
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {

    public CategoryListAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category categorie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_row, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.category_title);
        //TextView tvHome = (TextView) convertView.findViewById(R.id.category_desc);

        tvName.setText(categorie.getTitle());
        //tvHome.setText(categorie.getDescription());

        return convertView;
    }
}
