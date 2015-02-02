package com.eseoteam.android.tagfinder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Display data from an SQLite database query
 * Created on 13/01/2014.
 * @author Charline LEROUGE
 * @version 0.1.
 */

public class CustomCursorAdapter extends CursorAdapter {

    /**
     * Definition of the SQL query
     * @param context the current context
     * @param c cursor
     */

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    /**
     * Creation of the view, adapters have to know on which item to focus
     * @return View of a single tag
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return inflater.inflate(R.layout.single_tag, parent, false);
    }

    /**
     * We populate the views for each row of the listView
     * Setting of our data: take data from the cursor and put it in views
     */

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTagName = (TextView) view.findViewById(R.id.tv_tag_name);
        textViewTagName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));

    }
}