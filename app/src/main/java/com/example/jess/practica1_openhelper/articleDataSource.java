package com.example.jess.practica1_openhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ARTICLE_ID;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_DESCRIPCIO;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_DIA;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ESTOC;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_ID;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_PVP;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_QUANTITAT;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.COLUMN_TIPUS;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.TABLE_ARTICLES;
import static com.example.jess.practica1_openhelper.AdminSQLiteOpenHelper.TABLE_HISTORIAL;

public class articleDataSource {


    private static SQLiteDatabase db;
    private AdminSQLiteOpenHelper admin;
    private MainActivity ma = new MainActivity();

   public articleDataSource(Context context){
        admin = new AdminSQLiteOpenHelper(context);

       open();
   }

    private void open() {
        db = admin.getWritableDatabase();
    }

    private void finlize(){
        db.close();
    }

    public Cursor retornaFiltre(String filtrar, String opcio){

        String[] filtro = new String[]{"%"+filtrar+"%"};

         if(opcio.equals("Buscar per descripcio")) {
            return db.rawQuery("select * from articulos where " + COLUMN_DESCRIPCIO + " LIKE ?", filtro);
         }
         if(opcio.equals("Buscar per data")) {
             return db.rawQuery("select * from historial where " + COLUMN_DIA + " LIKE ?", filtro);
         }else
//         if(opcio.equals("Buscar per article")) {
             return db.rawQuery("select * from historial where " + COLUMN_ARTICLE_ID + " LIKE ?",filtro);
//         }
    }

    public Cursor retornaArticles() {
        // Retorem tots els articles
        return db.rawQuery("SELECT * FROM articulos",null);
    }
    public Cursor retornaArticlesHistorial() {
        // Retorem tots els articles
        return db.rawQuery("SELECT * FROM historial",null);
    }

    public Cursor article(long id) {
        // Retorna un cursor només amb el id indicat
        return db.rawQuery("select * from articulos where _id="+id,null);
    }

    public Cursor filtraMoviment(String filtre){

        //String[] st = new String[] {filtre};

        return db.rawQuery("Select * from historial",null);
    }

    public void afegirHistoric(long id , String dia, int quantitat, String tipus){

        Cursor stoc;
        String s;
        int sInt;
        int total;

        ContentValues values = new ContentValues();
        values.put(COLUMN_ARTICLE_ID,id);
        values.put(COLUMN_DIA, dia);
        values.put(COLUMN_QUANTITAT, quantitat);
        values.put(COLUMN_TIPUS, tipus);
        db.insert(TABLE_HISTORIAL,null,values);

        String b= Integer.toString(quantitat);
        String idS= Long.toString(id);

        String[] ids = new String[]{idS};

        stoc = db.rawQuery("Select estoc from articulos where idArticle = ?", ids);
        stoc.moveToFirst();
        int estoc = stoc.getInt(stoc.getColumnIndex(COLUMN_ESTOC));


        if(tipus.equals("S")){
            estoc = estoc-quantitat;
            db.execSQL("update articulos set estoc = "+estoc+" where idArticle =?",ids);
        }else
            if(tipus.equals("E")){
                estoc = estoc+quantitat;
                db.execSQL("update articulos set estoc = "+estoc+" where idArticle = ?",ids);


            }

    }
    public static boolean existeixID(String id){

        String[] ids = new String[]{id};


        int comprueba = db.rawQuery("select * from articulos where idArticle = ?",ids).getCount();

        if(comprueba ==1){
            return true;
        }else {
            return false;
        }
    }

    // ******************
    // Funciones de manipualación de datos
    // ******************

    public long afegirArticle(String idArticle, String descripcio, double pvp, int estoc) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(COLUMN_ARTICLE_ID,idArticle);
        values.put(COLUMN_DESCRIPCIO, descripcio);
        values.put(COLUMN_PVP, pvp);
        values.put(COLUMN_ESTOC, estoc);

        return db.insert(TABLE_ARTICLES,null,values);
    }

    public void modificarArticle(long id, String idArticle, String descripcio, double pvp, int estoc) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(COLUMN_ARTICLE_ID,idArticle);
        values.put(COLUMN_DESCRIPCIO, descripcio);
        values.put(COLUMN_PVP, pvp);
        values.put(COLUMN_ESTOC, estoc);

        db.update(TABLE_ARTICLES,values, "_id= ?", new String[] { String.valueOf(id) });
    }

    public void eliminarArticle(long id) {
        // Eliminem la task amb clau primària "id"
        db.delete(TABLE_ARTICLES,COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

}
