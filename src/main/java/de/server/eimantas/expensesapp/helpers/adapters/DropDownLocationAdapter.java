package de.server.eimantas.expensesapp.helpers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.server.eimantas.expensesapp.entities.Location;

public class DropDownLocationAdapter extends ArrayAdapter<Location> {
    public DropDownLocationAdapter(Context context, List<Location> objects) {
        super(context ,android.R.layout.simple_spinner_item, objects);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.RED);
        label.setText(getItem(position).getName());

        return label;
    }
}
