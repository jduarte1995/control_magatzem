package com.example.jess.practica1_openhelper;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ARTICLE_ID;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_DIA;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_QUANTITAT;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_TIPUS;

/**
 * Created by Jesús on 17/02/2017.
 */

public class Moviment extends ListActivity {
    private articleDataSource bd;
    private cursorAdapterMoviments cAdapter;
    private Cursor c;
    private String filtre;
    private String opcio;
    private String[] arraySpinner;
    private Spinner sp;


    private static String[] from = new String[]{COLUMN_ARTICLE_ID,COLUMN_DIA,COLUMN_QUANTITAT,COLUMN_TIPUS};
    private static int[] to = new int[]{R.id.lblIdArticle, R.id.lblDia, R.id.lblQuantitat, R.id.lblTipus};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviments);
        setTitle("Buscar Moviments");
        Button buscar = (Button) findViewById(R.id.btnBuscar);
        final EditText txtfiltre = (EditText) findViewById(R.id.txrFiltre);

        sp = (Spinner) findViewById(R.id.spMain);
        this.arraySpinner = new String[]{
                "Seleccionar una opció de busqueda","Buscar per data", "Buscar per article"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        sp.setAdapter(adapter);

        bd = new articleDataSource(this);
        carregarArticles();


        buscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                filtre = txtfiltre.getText().toString();
                c = bd.filtraMoviment(filtre);
                opcio = sp.getSelectedItem().toString();

                if (opcio.equals("Seleccionar una opció de busqueda")){
                    sp.performClick();
                }else {
                    filtra(filtre, opcio);
                }
            }
        });

        bd = new articleDataSource(this);

    }



    public void filtra(String filtre, String opcio){

        Cursor cursorArticles = bd.retornaFiltre(filtre, opcio);

        cAdapter.changeCursor(cursorArticles);
        cAdapter.notifyDataSetChanged();


        getListView().setSelection(0);
//        startActivity(getIntent());

    }

    public void carregarArticles(){

        Cursor cursorArticles = bd.retornaArticlesHistorial();


        cAdapter = new cursorAdapterMoviments(this, R.layout.fila_sin, cursorArticles, from, to, 1);
        setListAdapter(cAdapter);

    }

}
