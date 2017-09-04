package com.kovas1ki.android.p3db.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Usuario on 03/09/2017.
 */


// Creado el archivo java y extendido a ContentProvider y
    // creados automáticos todos los métodos
public class P3dbProvider extends ContentProvider {

    // Declaramos el código URIMATCHER para el contenido del uri en ambos casos
    private static final int TODA_LA_TABLA = 100 ;
    private static final int SINGLE_ITEM_ID = 101 ;

    // Creamos el URIMATCHER
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // Y dejamos hechas las dos direcciones de acceso
    static {
        uriMatcher.addURI(P3dbContract.CONTENT_AUTHORITY, P3dbContract.PATH_SEGMENT, TODA_LA_TABLA);
        uriMatcher.addURI(P3dbContract.CONTENT_AUTHORITY, P3dbContract.PATH_SEGMENT, SINGLE_ITEM_ID);
    }

    // Hay que declarar el objeto dbHelper
    private P3dbHelper dbHelper;

    @Override
    public boolean onCreate() {

        // En el onCreate del Provider está el acceso a la base
        // de datos. Así que vamos a nombrarlo. Afuera declaramos el objeto
        // y aquí lo inicializamos y retornamos TRUE yeeeeEaaA!!!!!
        dbHelper = new P3dbHelper(getContext()) ;

        // Que se retorna TRUE ahora que ya se va a poner a funcionar
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Para este cursor que yo he bautizado como EL CURSOR PREGUNTANTE,
        // va a ser muy fácil.
        // Accedemos a la base en modo lectura.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // También declaramos el cursor.
        Cursor cursor;

        // Llegaremos a este método con la una URI. Esta URI sabemos que,
        // depende de como termine haremos una cosa y otra. Simplemente
        // contemplamos los dos casos usando URIMATCHER.
        int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                cursor = db.query(P3dbContract.P3dbEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SINGLE_ITEM_ID:
                // para el single item tenemos que modificar las dos variables afectadas
                selection = P3dbContract.P3dbEntry.CN_ID + "=?" ;
                // tenemos que construir el selectionArgs con el _id que encontramos en la uri
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Ahora el cursor, claro.
                cursor = db.query(P3dbContract.P3dbEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("No se puede construir la query:" +
                        "por estar tratando con la URI desconocida " + uri) ;
        }

        // Y si esto se ha hecho, pues también hay que notificar que ha
        // habido cambios.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    // Devuelve el tipo MIME del dato contenido en la uri
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                // devolvemos un String que es una dirección
                return P3dbContract.P3dbEntry.CONTENT_LIST_TYPE ;
            case SINGLE_ITEM_ID:
                return P3dbContract.P3dbEntry.CONTENT_ITEM_TYPE ;
            default:
                throw new IllegalArgumentException("Desconocida URI "
                + uri + " con match " + match) ;
        } // Y el return me lo cargo porque ya estoy retornando valores tod el rato
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        
        // Medimos la URI y si es 101 abrimos otro método para la inserción
        final int match = uriMatcher.match(uri) ;
        switch (match){
            // La uri de inserción de llega hace referencia a que quiere insertar un registro
            // dentro de la tabla, DE TODA LA TABLA. Es por eso que este , case , deber ser
            // para TODA_LA_TABLA
            case TODA_LA_TABLA:
                return insertNewItem(uri, values) ;
            default:
                throw new IllegalArgumentException("La inserción no es soportada para " + uri) ;
        }  // El return del método nos lo cargamos porque ya lo estamos dando antes
    }

    private Uri insertNewItem(Uri uri, ContentValues values) {

        // TODO continuar aquí

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
