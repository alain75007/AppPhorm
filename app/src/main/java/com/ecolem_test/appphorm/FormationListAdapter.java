package com.ecolem_test.appphorm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 15/08/2015.
 */
public class FormationListAdapter extends ArrayAdapter<Formation> {

    public FormationListAdapter(Context context, ArrayList<Formation> formations) {
        super(context, 0, formations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Formation formation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.formation_list_row, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.formation_title);
        TextView tvHome = (TextView) convertView.findViewById(R.id.formation_finished);

        tvName.setText(formation.getTitle());
        if (formation.isDone() == true) {
            tvHome.setText("(Terminée)");
        }
        else {
            tvHome.setText("");
        }

        return convertView;
    }
}
