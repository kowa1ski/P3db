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
import android.util.Log;

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
        uriMatcher.addURI(P3dbContract.CONTENT_AUTHORITY, P3dbContract.PATH_SEGMENT + "/#", SINGLE_ITEM_ID);
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

        // Tenemos que chequear que el nombre no sea null, porque
        // la base de datos no lo admitiría, le dijimos NOT NULL
        String nombreQueEstamosComprobando = values.getAsString(P3dbContract.P3dbEntry.CN_NOMBRE);
        if (nombreQueEstamosComprobando == null){
            // si es null hay que controlar la excepción
            throw new IllegalArgumentException("ERROS, el nombre es null");
        }
        // El teléfono no lo chequeamos porque puede ser null si hiciera falta

        // Es el momento. Accedemos a la base
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Ahora vamos a introducir una nueva , row ,.
        // Ten en cuenta que la inserción devuelve un long.
        long id = db.insert(P3dbContract.P3dbEntry.TABLE_NAME, null, values);
        // ahora comprobamos que la inserción ha sido correcta.
        if (id == -1) {
            // Si es -1 está claro que algo a fallado.
            Log.e("TAG_PROVIDER", "ERROR al insertar row con la uri " +uri );
        }
        // Si estamos aquí está claro que tod ha ido bien.

        // Antes de retornar y dar por finalizado este método tenemos que
        // notificar a todos los listener que ha habido cambios en la tabla
        // o lo que es lo mismo, en la dirección a donde apunta la , uri ,.
        getContext().getContentResolver().notifyChange(uri, null);

        // El id nos da el número de fila así que se lo pegamos a la
        // uri y lo devolvemos :-)
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Estamos en el último paso que es completar el delete de este lado,
        // del lado del Provider.
        // en primer lugar, como no, accedemos a la base en modo escritura.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Y creamos una variable que recoja el número de rows borradas.
        int rowsDeleted;

        // Tenemos que tener en cuenta que vamos a confeccionar esto
        // de una manera completa. Es cierto que, como tenemos pensada esta
        // app, no se contempla la opción de borrar toda la tabla. Sin
        // embargo esto es una práctica así que lo vamos a dejar preparado para
        // tener esa opción si quisiéramos.
        // Para lo expuesto ya sabes lo que toca. Pues sí, echar mano
        // del urimatcher. Venga, es fácil.
        final int match = uriMatcher.match(uri);
        // y ahora las dos posibilidades en un switch case
        switch (match){
            case TODA_LA_TABLA:
                // borramos tod
                rowsDeleted = db.delete(P3dbContract.P3dbEntry.TABLE_NAME, null, null);
                // ya estaría esto. Después de todos los , case , ya retornaríamos
                // la variable.
                break;
            case SINGLE_ITEM_ID:
                // Este es el caso que nos interesa. Tenemos la uri, donde al final de
                // la misma nos dice el _id que tenemos que borrar. Así que lo primero
                // que se nos viene a la mente es que la selection será, como no,
                // por el _id. Venga.
                selection = P3dbContract.P3dbEntry.CN_ID + "=?";
                // Y, joder, los argumentos que se le dan para encontrar ese _id
                // son el _id que encontramos al final del currentItemUri que ha pasado
                // a este método con el nombre de , uri ,.
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Chupado.

                // Y ahora a BORRAR COMO LOCOSSSSSSSS
                rowsDeleted = db.delete(P3dbContract.P3dbEntry.TABLE_NAME,
                        selection,       // String whereClause
                        selectionArgs    // String[] whereArgs //
                ); // Y ya
                break;
            // Y el típico default que no puede faltar. Lo aprovechamos para
            // atrapar excepciones en caso de que salga algo muy muy mal.
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        // Ahora ya toca retornar el valor de las rows, pero antes, si tod
        // ha salido bien, MUY IMPORTANTE, hay que notificar a todos los
        // listeners que la base ha cambiado.
        // La instrucción te pide un , observer , que le paso en null. No
        // tengo idea de qué es pero se le pasa en null, está bien hecho así.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Por fin retornamos el valor de las rows y damos por terminado esto.
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                // No encuentro muy práctico lo de updatear toda la
                // tabla con algo en esta app. Sí que tendría sentido en una
                // app como la de las mascotas para resetear todos los
                // campos de sexo y dejarlos a 0, por ejemplo.
                return updateItem(uri, values, selection, selectionArgs);

            // Y ahora el que nos interesa de verdad
            case SINGLE_ITEM_ID:
                // Para este caso debemos extraer el _id de la URI para identificar
                // el item en concreto que queremos updatear. Luego, en la selección
                // haremos , "_id=?" , y también los selection arguments serán
                // un String Array conteniendo el actual ID.
                // En el contenedor de valores ContentValues ya nos vendrán
                // los datos a cambiar. Eso último se lo pasamos tal cual, claro.

                // Venga, le decimos que queremos updatear los registros en base
                // a su _id
                selection = P3dbContract.P3dbEntry.CN_ID + "=?";
                // En la selección le marcamos el id de esa columna que queremos updatear
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                // Y con tod esto modificado, pues le pasamos los parámetros allí
                return updateItem(uri, values, selection, selectionArgs);
            // Y como, no, le metemos un default para controlar la excepción
            default:
                throw new IllegalArgumentException("UPDATE no es soportada para la uri " + uri);

        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Este método retornaba 0 hasta ahora porque ese era el número
        // de filas updateadas.

        // Al lío. Accedemos a la base en modo escritura.
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Damos la orden del updateado y recogemos el número que
        // devuelve que es el número de filas updateadas.
        int rowsUpdated = database.update(
                P3dbContract.P3dbEntry.TABLE_NAME,
                values, selection, selectionArgs);

        // IMPORTANTE
        // Si una o más filas se han visto afectadas hay que
        // avisar a todos los listeners que los datos han cambiado.
        if (rowsUpdated != 0 ) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Por último, como es lógico, retornamos el número de filas
        return rowsUpdated;
    }
}