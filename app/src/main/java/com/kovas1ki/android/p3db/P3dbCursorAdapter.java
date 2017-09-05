package com.kovas1ki.android.p3db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kovas1ki.android.p3db.data.P3dbContract;

/**
 * Created by Usuario on 05/09/2017.
 */

public class P3dbCursorAdapter extends CursorAdapter {

    // Atento que para el constructor he cogido este de las flags

    // Le añadimos las flags en bruto, que las ponemos a 0. Por tanto
    // no van a cambiar cuando llame a la superclase así que no nos va a hacer
    // falta ese parámetro y lo borramos de las variables que tratamos.
    public P3dbCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags*/);
    }

    /**
     *
     * @param context   El contexto.
     * @param cursor    El cursor desde el cual se obtienen los datos. El cursor
     *                  es movido a la posición correcta.
     * @param parent    El parent al cual la nueva vista es adjuntada(incorporada)
     * @return          La nueva lista de items creada.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Aquí es guay. Usamos esto de LayoutInflater que hay que aprender
        // un poco más cómo funciona pero en esencia veo que los métodos
        // que usamos de esa clase son muy sencillos. Enumero:
        // llamamos a la clase, le decimos donde queremos que se infle y luego
        // inflamos el ViewGroup que queremos. En este caso he cogido este
        // predeterminado que viene con Android.
        return LayoutInflater.from(context).inflate(android.R.layout.two_line_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Declaramos los textViews
        TextView textViewNombre = (TextView) view.findViewById(android.R.id.text1);
        TextView textViewTelefono = (TextView) view.findViewById(android.R.id.text2);

        // Localizamos las columnas en las que estamos interesados.
        int textNombreColumIndex = cursor.getColumnIndex(P3dbContract.P3dbEntry.CN_NOMBRE);
        int textTelefonoColumIndex = cursor.getColumnIndex(P3dbContract.P3dbEntry.CN_NUMERO);

        // Leemos los datos en esas columnas
        String stringNombreColumIndex = cursor.getString(textNombreColumIndex);
        String stringTelefonoColumIndex = cursor.getString(textTelefonoColumIndex);

        // Y lo metemos en los textViews. FÁCIL.
        textViewNombre.setText(stringNombreColumIndex);
        textViewTelefono.setText(stringTelefonoColumIndex);

        // El contexto no se usa aquí así que YA ESTÁ !!!


    }
}












