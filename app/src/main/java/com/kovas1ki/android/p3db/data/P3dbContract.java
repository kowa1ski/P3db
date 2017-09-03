package com.kovas1ki.android.p3db.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Usuario on 02/09/2017.
 */

public class P3dbContract {

    public P3dbContract() {
    }   // Le nombramos el constructor vacío para evitar malentendidos.

    // Declaramos el content authority con el nombre del paquete
    public static final String CONTENT_AUTHORITY = "com.kovas1ki.android.p3db" ;

    // Declaramos la base para la URI del content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY) ;

    // Ahora el segment, que es un posible path
    public static final String PATH_SEGMENT = "p3tabla" ;

    // Ahora la clase interna ENTRY e implementamos BaseColums
    public static final class P3dbEntry implements BaseColumns {

        // Contenido de la URI para acceder a la tabla del provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SEGMENT) ;

        // Nombre de la tabla
        public static final String TABLE_NAME = "p3tabla" ;

        // Ahora el nombre de las columnas
        public static final String CN_ID = BaseColumns._ID ;
        public static final String CN_NOMBRE = "nombre" ;
        public static final String CN_NUMERO = "numero" ;

        // Ahora las direcciones que me hacen falta para el content
        // provider. La de la lista y la de un sólo item.

        // Lista completa
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_SEGMENT ;

        // Un solo item
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_SEGMENT ;
    }
}










