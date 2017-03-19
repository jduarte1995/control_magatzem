package com.example.jess.practica1_openhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ARTICLE_ID;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_DESCRIPCIO;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ESTOC;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_PVP;

public class MainActivity extends ListActivity {

    private articleDataSource bd;
    private cursorAdapter cAdapter;
    private long idActual;
    private String filtre;

    private static String[] from = new String[]{COLUMN_ARTICLE_ID,COLUMN_DESCRIPCIO,COLUMN_PVP,COLUMN_ESTOC};
    private static int[] to = new int[]{R.id.lblIdArticle, R.id.lblDescripcio, R.id.lblPvp, R.id.lblStock};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnMoviments = (Button) findViewById(R.id.btnBuscarHistorial);
        Button btn = (Button) findViewById(R.id.btnCrear);
        Button buscar = (Button) findViewById(R.id.btnBuscar);
        final EditText txtfiltre = (EditText) findViewById(R.id.txrFiltre);

        btnMoviments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Moviment.class);

                startActivity(i);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                afegirArticle();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                filtre = txtfiltre.getText().toString();
                filtra(filtre);


            }
        });

        bd = new articleDataSource(this);
        carregarArticles();

    }

    public void filtra(String filtre){

        Cursor cursorArticles = bd.retornaFiltre(filtre, "Buscar per descripcio");

        cAdapter.changeCursor(cursorArticles);
        cAdapter.notifyDataSetChanged();

        getListView().setSelection(0);


    }

    public void carregarArticles(){

        Cursor cursorArticles = bd.retornaArticles();


        cAdapter = new cursorAdapter(this, R.layout.fila, cursorArticles, from, to, 1);
        cAdapter.miContexto= this;
        setListAdapter(cAdapter);

    }

    public void refrescarArticles(){

        Cursor cursorArticles = bd.retornaArticles();

        cAdapter.changeCursor(cursorArticles);
        cAdapter.notifyDataSetChanged();

    }

    private void afegirArticle() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, Articles.class );
        i.putExtras(bundle);
        startActivityForResult(i,1);
    }

    private void ModificarArticle(long id) {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;


        Intent i = new Intent(this, Articles.class );
        i.putExtras(bundle);
        startActivityForResult(i,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {//afegir
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                refrescarArticles();
            }
        }

        if (requestCode == 2) {//modificar
            if (resultCode == RESULT_OK) {
                refrescarArticles();
            }
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // modifiquem el id
        ModificarArticle(id);
    }






}
