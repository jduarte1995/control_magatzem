package com.example.jess.practica1_openhelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ARTICLE_ID;

/**
 * Created by Jesús on 17/02/2017.
 */

public class historic extends Activity {
    private static long idArticle;
    private articleDataSource bd;
    TextView tv;
    Spinner sp;
    private MainActivity ma = new MainActivity();
    private String[] arraySpinner;



    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_historic);
        bd = new articleDataSource(this);

        Button btnAceptar = (Button) findViewById(R.id.hAceptar);
        Button btnSortir = (Button) findViewById(R.id.hSortir);

        sp = (Spinner) findViewById(R.id.spTipus);
        this.arraySpinner = new String[]{
                "E", "S"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        sp.setAdapter(adapter);

        tv = (TextView) findViewById(R.id.hDia);
        tv = (TextView) findViewById(R.id.hQuantitat);
        tv = (TextView) findViewById(R.id.hTipus);

        idArticle = this.getIntent().getExtras().getLong("id");


        btnSortir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                setResult(RESULT_CANCELED, mIntent);

                finish();
            }
        });

        carregarDades();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceptarCambios(idArticle);
            }
        });
    }

    private void carregarDades() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = bd.article(idArticle);
        datos.moveToFirst();


        tv = (TextView) findViewById(R.id.txtCodi);
        tv.setText(datos.getString(datos.getColumnIndex(COLUMN_ARTICLE_ID)));
        tv.setEnabled(false);

    }

    private void aceptarCambios(long idArticle){
        long id = idArticle;

        tv = (TextView) findViewById(R.id.txtDia);
        String dia = tv.getText().toString();

        tv = (TextView) findViewById(R.id.txtQuantitat);
        int quantitat = Integer.parseInt( tv.getText().toString());

        String tipus = sp.getSelectedItem().toString();


        bd.afegirHistoric(id,dia,quantitat,tipus);
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

    }
}
