package com.example.jess.practica1_openhelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ESTOC;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ID;

/**
 * Created by Jesús on 31/01/2017.
 */

public class cursorAdapter extends android.widget.SimpleCursorAdapter {
    private static final String vermell = "#d78290";
    private static final String blanc = "#ffffff";
    MainActivity miContexto;

    public cursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor linia = (Cursor) getItem(position);
        int stock = linia.getInt(linia.getColumnIndexOrThrow(COLUMN_ESTOC));

        // Pintem el fons de la view segons està completada o no
        if (stock>0) {
            view.setBackgroundColor(Color.parseColor(blanc));
        }
        else {
            view.setBackgroundColor(Color.parseColor(vermell));
        }

        ImageView iv = (ImageView) view.findViewById(R.id.ivList);
        iv.setTag(position);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Cursor item = (Cursor) getItem(position);
                long id = item.getLong(item.getColumnIndexOrThrow(COLUMN_ID));
                Bundle bundle = new Bundle();
                bundle.putLong("id",id);

                Intent i = new Intent(miContexto, historic.class );
                i.putExtras(bundle);
                miContexto.startActivity(i);

            }
        });
        return view;
    }
}
