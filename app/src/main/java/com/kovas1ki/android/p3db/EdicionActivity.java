package com.kovas1ki.android.p3db;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.kovas1ki.android.p3db.data.P3dbContract;

import static java.lang.Integer.parseInt;

public class EdicionActivity extends AppCompatActivity {

    // -------------------------------------------------------
    //     ZONA DE DECLARACIÓN DE VARIABLES DE CLASE
    //--------------------------------------------------------

    // Como mandan las buenas prácticas, vamos a declarar un LOG_TAG
    String LOG_TAG_EDIT_ACTIVITY = "EDIT_ACTIVITY" ;


    // Vamos a declarar los dos textViews

    EditText editTextCampoNombre;
    EditText editTextCampoTelefono;
    // Y aquí vamos a declarar las variables que van a recoger la
    // información que metamos en los editText
    private String nombre;
    private int telefono;

    // Declaramos el currentItemUri. Con el mismo nombre que en la otra
    // actividad porque LO VAMOS A RESCATAR. yeeeeeeeaaaaa!!
    private Uri currentItemUri;




    // ARRIBA SOLO HAY DECLARACIONES DE VARIABLES ----------------------------



    // Vamos a casar el nuevo menú
    // Estoy adaptando del del MAin.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    // Vamos a darle funcionalidad a los botones del menú
    // escribiendo onOptionIte... etc
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // IMPORTANTE. Evidentemente hay que diferenciar
        // qué botón es el que se pulsa. Para ello
        // vamos a llamar a la función, getItemId,
        // que tiene toda la pinta de ser un getter.
        // Nos fijamos en que la variable que nos pasan a este
        // Método es item así que la usaremos para obtener
        // su id y saber qué botón es. Y de eso se trata esta función
        // puesto que se llama OPCIONES DE ITEM SELECIONADO.
        int itemIdOseaBotonPulsado = item.getItemId();

        // Exactamente, no hay que decir que aquí va un switch
        switch (itemIdOseaBotonPulsado){
            case R.id.botonAgregar:
                Toast.makeText(this, "has pulsado BOTON AGREGAR", Toast.LENGTH_SHORT).show();

                // Simple aquí. Le enviamos a un método que haga el trabajo y
                // finalizamos la actividad.
                guardarNuevoRegistro();
                finish();

                // Esto termina con el return true
                return true;
            case R.id.botonEliminar:
                Toast.makeText(this, "has pulsado BOTON ELIMINAR", Toast.LENGTH_SHORT).show();
                return true;
        }
        // Esta parte del return se deja tal cual para que
        return super.onOptionsItemSelected(item);
    }

    private void guardarNuevoRegistro() {

        // Nos hacemos con la información contenida en los editText.
        nombre = editTextCampoNombre.getText().toString();
        telefono =  parseInt(editTextCampoTelefono.getText().toString());
        // Cargamos esto en un ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(P3dbContract.P3dbEntry.CN_NOMBRE, nombre);
        contentValues.put(P3dbContract.P3dbEntry.CN_NUMERO, telefono);
        // Ya está tod, ahora lo mandamos al provider y recordemos que
        // nos devolverá una uri.
        // El contentResolver nos resuelve todas estas gestiones con
        // el provider.
        Uri uriQueDevuelveElProvider = getContentResolver().insert(P3dbContract.P3dbEntry.CONTENT_URI, contentValues);

        // Comprobamos que la uri ha sido devuelta de manera correcta y ya.
        if (uriQueDevuelveElProvider == null){
            Toast.makeText(this, "ERROR, uri " + uriQueDevuelveElProvider + " que ha devuelto " +
                    "el provider es incorrecta", Toast.LENGTH_SHORT).show();
        } else {
            // Si estamos aquí, es que hemos pasado tod los filtros y tod ha salido bien
            Toast.makeText(this, "REGISTRO AÑADIDO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
        }



    }

    // Accedemos ya al método onBackPressed que siempre es bueno tener control
    @Override
    public void onBackPressed() {
        // Comprobamos en el log que se recogen bien lo introducido en los editText
        Log.i(LOG_TAG_EDIT_ACTIVITY, "Acabas de introducir: " + editTextCampoNombre.getText().toString() + " y " + editTextCampoTelefono.getText().toString()) ;
        // Comprobado el , Log.i , funciona perfectamente.
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);

        // Vamos a llenar las variables editText
        editTextCampoNombre = (EditText) findViewById(R.id.editTextNombre) ;
        editTextCampoTelefono = (EditText) findViewById(R.id.editTextTelefono) ;
        // y pasamos la información en algún botón para verla. Lo haremos en el
        // botón atrás.

        // Si venimos a esta pantalla en modo edición tenemos que saberlo.
        // Para ello leemos en intent que nos a traído hasta aquí.
        Intent intent = getIntent();
        currentItemUri = intent.getData(); // LO TENEMOS!!!!

        // Vamos a probarlo
        if (currentItemUri == null){
            // Evidentemente en null, es que NO viene en modo edición
            // sino que ha venido con el botón de agregar.
            setTitle("NUEVO REGISTRO");
            Toast.makeText(this, "El currentItemUri es el "
                    + currentItemUri, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "El currentItemUri es el "
                    + currentItemUri, Toast.LENGTH_SHORT).show();
        }




    }

}
