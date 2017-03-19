package com.example.jess.practica1_openhelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_TIPUS;

/**
 * Created by Jesús on 31/01/2017.
 */

public class cursorAdapterMoviments extends android.widget.SimpleCursorAdapter {
    private static final String vermell = "#d78290";
    private static final String verd = "#15ec7f";
    Moviment MContexto;

    public cursorAdapterMoviments(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor linia = (Cursor) getItem(position);
        String tipus = linia.getString(linia.getColumnIndexOrThrow(COLUMN_TIPUS));

        // Pintem el fons de la view segons està completada o no
        if (tipus.equals("E")) {
            view.setBackgroundColor(Color.parseColor(verd));
        }
        else {
            view.setBackgroundColor(Color.parseColor(vermell));
        }
        return view;
    }
}
