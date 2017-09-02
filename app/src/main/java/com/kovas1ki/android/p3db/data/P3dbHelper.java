package com.kovas1ki.android.p3db.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.kovas1ki.android.p3db.data.P3dbContract.*;
import static com.kovas1ki.android.p3db.data.P3dbContract.P3dbEntry.*;

/**
 * Created by Usuario on 02/09/2017.
 */

public class P3dbHelper extends SQLiteOpenHelper {
    // Una vez creada esta clase, nombrados los dos métodos y el constructos, vamos
    // a declarar las variables y ajustasr el constructor.

    // Nombre de la base de datos
    private final static String DATA_BASE_NAME = "P3db";
    // Número de la versión de la base de datos
    private final static int DATA_BASE_VERSION = 1 ;

    // Le quitamos lo sobrante porque se lo estamos dando directo con las variables
    // que acabamos de declarar.
    public P3dbHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creamos el String que contiene las sentencias SQL
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CN_NOMBRE + " TEXT NOT NULL, "
                + CN_NUMERO + " INTEGER);" ;

        // Y ejecutamos el código invocando al , db , de este método
        db.execSQL(SQL_CREATE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }
}
