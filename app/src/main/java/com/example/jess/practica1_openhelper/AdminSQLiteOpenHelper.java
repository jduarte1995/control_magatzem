package com.example.jess.practica1_openhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jesús on 19/01/2017.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_ARTICLES = "articulos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ARTICLE_ID = "idArticle";
    public static final String COLUMN_DESCRIPCIO = "descripcio";
    public static final String COLUMN_PVP = "pvp";
    public static final String COLUMN_ESTOC = "estoc";
    public static final String TABLE_HISTORIAL  = "historial";
    public static final String COLUMN_DIA = "dia";
    public static final String COLUMN_QUANTITAT = "quantitat";
    public static final String COLUMN_TIPUS = "tipus";


    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ARTICLES
            +"( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ARTICLE_ID + " text not null, "
            + COLUMN_DESCRIPCIO + " text not null, "
            + COLUMN_PVP  + " real not null, "
            + COLUMN_ESTOC + " real default 0 "
            +");";

    private static final String DATABASE_CREATE_HISTORIC = "create table "
            + TABLE_HISTORIAL
            +"( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ARTICLE_ID + " text not null, "
            + COLUMN_DIA + " text not null, "
            + COLUMN_QUANTITAT  + " real not null, "
            + COLUMN_TIPUS + " text not null )";
            //+" FOREIGN KEY(idArticle) REFERENCES articulos(idArticle))";



    public AdminSQLiteOpenHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE_HISTORIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(DATABASE_VERSION == 1){

            db.execSQL(DATABASE_CREATE_HISTORIC);

        }


    }

}
