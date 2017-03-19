package com.example.jess.practica1_openhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ARTICLE_ID;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_DESCRIPCIO;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ESTOC;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_PVP;

/**
 * Created by Jesús on 31/01/2017.
 */

public class Articles extends Activity {

    private long idArticle;

    private articleDataSource bd;

    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_article);

        bd = new articleDataSource(this);

        // Botones de aceptar y cancelar
        // Boton ok
        Button btnAceptar = (Button) findViewById(R.id.aceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptarCambios();
            }
        });

        // Boton eliminar
        Button btnCancelar = (Button) findViewById(R.id.cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelarCambios();
            }
        });

        // Boton cancelar
        Button btnEliminar = (Button) findViewById(R.id.eliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                eliminarArticle();

            }
        });


        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idArticle = this.getIntent().getExtras().getLong("id");

        if (idArticle != -1) {
            // Si estem modificant carreguem les dades en pantalla
            carregarDades();

        }else {
            // Si estem creant amaguem el botó d'eliminar
            btnEliminar.setVisibility(View.GONE);
        }
    }

    private void carregarDades() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = bd.article(idArticle);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;
        //EditText txt;


        tv = (TextView) findViewById(R.id.txtCodi);
        tv.setText(datos.getString(datos.getColumnIndex(COLUMN_ARTICLE_ID)));
        tv.setEnabled(false);

        tv = (TextView) findViewById(R.id.txtDescripcio);
        tv.setText(datos.getString(datos.getColumnIndex(COLUMN_DESCRIPCIO)));

        tv = (TextView) findViewById(R.id.txtPVP);
        tv.setText(datos.getString(datos.getColumnIndex(COLUMN_PVP)));

        tv = (TextView) findViewById(R.id.txtEstoc);
        tv.setText(datos.getString(datos.getColumnIndex(COLUMN_ESTOC)));


    }

    private void aceptarCambios() {
        // Validem les dades
        TextView tv;

        // Codi ha d'estar informat
        tv = (TextView) findViewById(R.id.txtCodi);
        String codi = tv.getText().toString();
        if (codi.trim().equals("")) {
            myDialogs.showToast(this, "Ha d'informar el codi");
            return;
        }

        // Títol ha d'estar informat
        tv = (TextView) findViewById(R.id.txtDescripcio);
        String descripcio = tv.getText().toString();
        if (descripcio.trim().equals("")) {
            myDialogs.showToast(this, "Ha d'informar la descripció");
            return;
        }

        // PVP numeric
        tv = (TextView) findViewById(R.id.txtPVP);
        String pvp = tv.getText().toString();
        Double preu = 0.0;
        try {
            preu = Double.parseDouble(pvp);
        } catch (NumberFormatException e) {
            myDialogs.showToast(this, "El pvp ha de ser decimal");
        }

        //estoc
        tv = (TextView) findViewById(R.id.txtEstoc);
        String stoc = tv.getText().toString();
        int estoc = 0;
        try {
            estoc = Integer.parseInt(stoc);
        } catch (NumberFormatException e) {
            myDialogs.showToast(this, "L'estoc ha de ser un numero");
        }

        // Mirem si estem creant o estem guardant
        if (idArticle == -1) {
            if(articleDataSource.existeixID(codi)){
                myDialogs.showToast(this, "ID Existent");
                return;
            }
            if(estoc<0){
                myDialogs.showToast(this, "L'estoc ha de ser com a mínim 0");
            }else {
                idArticle = bd.afegirArticle(codi, descripcio, preu, estoc);
            }
        } else {
            bd.modificarArticle(idArticle, codi, descripcio, preu, estoc);
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idArticle);
        setResult(RESULT_OK, mIntent);

        finish();

    }


    private void cancelarCambios() {

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idArticle);
        setResult(RESULT_CANCELED, mIntent);

        finish();
    }

    private void eliminarArticle() {

        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desitja eliminar l'article??");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.eliminarArticle(idArticle);

                Intent mIntent = new Intent();
                mIntent.putExtra("id", -1);  // Devolvemos -1 indicant que s'ha eliminat
                setResult(RESULT_OK, mIntent);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
}


