package com.kovas1ki.android.p3db;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Usuario on 05/09/2017.
 */

public class P3dbCursorAdapter extends CursorAdapter {

    // Atento que para el constructor he cogido este de las flags
    public P3dbCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
