package com.kovas1ki.android.p3db;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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


// Estamos preparando esta pantalla para que sea también de edición así que
// lo haremos con un cursorLoader y tenemos que implementar el loaderManager
// para lo de los campos. Creo que es así.

// CUIDADO EN LA IMPLEMENTACIÓN he tenido que añadir <Cursor>. Que no se olvide que
// si no los métodos que se generan no son los apropiados.
public class EdicionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

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

    //Ya que tenemos lo del Loader, tenemos que ponerle el iniciador
    // así que declaramos su estado de 0.
    private static final int EXISTING_ITEM_LOADER = 0 ;




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
                eliminarRegistro(); // Llamamos a una NUEVA función y la creamos con alt+enter
                return true;
        }
        // Esta parte del return se deja tal cual para que
        return super.onOptionsItemSelected(item);
    }

    private void eliminarRegistro() {

        // Una pequeña comprobación antes de comenzar.
        // Tod lo que vamos a hacer es sólo si el currentItemUri es
        // no null, o sea, que contiene algo que borrar. Esto quiere decir
        // que tod el bloque de código que vamos a generar es con la
        // condición de que haya algún registro que borrar.
        if (currentItemUri != null ) {

            // Una vez comprobado, ya sabemos que el currentItemUri contiene
            // algo así que sólo tenemos que borrarlo.
            // Los parámetros when y la claúsula se la pasamos al Provider en null
            // porque ya apañamos allí con el currentItemUri que contiene toda
            // la información necesaria.
            int rowsDeleted = getContentResolver().delete(currentItemUri, null, null);

            // El rowsDeleted vuelve del Provider con un valor y éste tiene que ser
            // diferente de 0 para demostrar que hemos borrado alguna fila.
            if (rowsDeleted == 0){
                // Si no hay filas borradas, no hemos borrado nada.
                Toast.makeText(this, "ERROR, no se ha borrado nada", Toast.LENGTH_SHORT).show();
            } else {
                // Si , else , entonces sí que hay filas borradas
                Toast.makeText(this, "REGISTRO BORRADO correctamente", Toast.LENGTH_SHORT).show();
            }
            // Y después de tod, no nos olvidamos de finalizar esta Actividad
            finish();
        }
    }

    private void guardarNuevoRegistro() {

        // Nos hacemos con la información contenida en los editText.
        nombre = editTextCampoNombre.getText().toString();

        // ERROR DETECTADO IMPORTANTE --------
        try {
            telefono =  parseInt(editTextCampoTelefono.getText().toString());
            // ATENTOS !!!!!!!!! -------------------
            // He detectado un error posible. El teléfono es un número muy largo y ser , int , lo
            // hace muy limitado. Necesito poner un medidor para que no se me pase de largo

        } catch (Exception e){
            Toast.makeText(this, "Integer NO ES VALIDO", Toast.LENGTH_SHORT).show();
            // Una vez lanzado el Toast no puedo dejar que continúe el flujo así
            // que retorno
            return;


        }



        // Cargamos esto en un ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(P3dbContract.P3dbEntry.CN_NOMBRE, nombre);
        contentValues.put(P3dbContract.P3dbEntry.CN_NUMERO, telefono);
        // Ya está tod, ahora lo mandamos al provider y recordemos que
        // nos devolverá una uri.
        // El contentResolver nos resuelve todas estas gestiones con
        // el provider.

        // Es hora de diferenciar si de verdad queremos guardar un nuevo registro
        // o queremos actualizar uno existente. Para ello debemos comenzar con un
        // if que nos mida el currentItemUri

        if (currentItemUri == null){
           // Y si es null, entonces sí que ejecutamos este bloque de código que
            // antes operaba único ya ahora hemos metido dentro de este if.

            Uri uriQueDevuelveElProvider = getContentResolver().insert(P3dbContract.P3dbEntry.CONTENT_URI, contentValues);

            // Comprobamos que la uri ha sido devuelta de manera correcta y ya.
            if (uriQueDevuelveElProvider == null){
                Toast.makeText(this, "ERROR, uri " + uriQueDevuelveElProvider + " que ha devuelto " +
                        "el provider es incorrecta", Toast.LENGTH_SHORT).show();
            } else {
                // Si estamos aquí, es que hemos pasado tod los filtros y tod ha salido bien
                Toast.makeText(this, "REGISTRO AÑADIDO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Y ahora sí, ahora estamos seguros de que en el currentItemUri tenemos
            // algo. Y si es así, lo que queremos es updatear así que vamos a ello.
            // Nos fijamos bien en que el mismo programa nos pide dentro de los paréntesis
            // NO una url como en el caso del insert. Aquí necesita una uri que evidentemente
            // es el currentItemUri y el resto de cosas que ya tenemos.
            // También nos pide la clausula where pero eso ya se lo damos en el provider
            int rowsAfected = getContentResolver().update(currentItemUri, contentValues, null, null );
            // Y ahora comprobamos que tod ha salido bien, o sea, que hay más de una
            // row que ha sido updateada.
            if (rowsAfected == 0){
                // Si no hay filas updateadas, algo ha salido mal.
                Toast.makeText(this, "EDICIÓN DE ITEM FALLIDA", Toast.LENGTH_SHORT).show();
            } else {
                // Si sí que ha habido filas, entonces sí que tod ha salido bien.
                Toast.makeText(this, "EDICIÓN REALIZADA CON ÉXITO", Toast.LENGTH_SHORT).show();
            }
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

            // Vamos a dar caña a este , else , y le vamos a iniciar el
            // loader.
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
            //Claro está que ahora tenemos que rellenar los métodos para que
            // cargue la información que queremos en el cursor, la cual
            // será cargada en segundo plano.

            // y de paso le cambiamos el título
            setTitle("EDICION DE REGISTRO");

        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // En este onCreateLoader, pues lo de siempre. La projection
        // y luego retornamos el cursor resultante. Perdón, el
        // NUEVO CURSOR LOADER resultante.
        String[] projetion = {
                P3dbContract.P3dbEntry.CN_ID,
                P3dbContract.P3dbEntry.CN_NOMBRE,
                P3dbContract.P3dbEntry.CN_NUMERO };

        return new CursorLoader(this,   // este contexto
                currentItemUri,         // La uri con la dirección completa
                                            // del item
                projetion,              // la projection, las columnas
                null,                   // no selection CLAUSE
                null,                   // no selection ARGUMENTS
                null);                  // default SHORT ORDER
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Una vez finalizado el onCreateLoader, tenemos que medir el
        // cursorloader resultante. Si tiene menos de una fila es que
        // es un cursor vacío y por lo tanto no contiene información,
        // lo que explicaría que NO había ninguna dirección en el
        // currentItem, entre otras.
        if (data == null || data.getCount() < 1 ) {
            // En este caso de que está vacío y tal pues se retorna y ya.
            return;
        }

        // En esta línea es que tod ha ido bien con la edición de los
        // items. Vamos al lío.

        // Empezamos con el movimiento al comienzo del cursor y a partir
        // de ahí tod.
        // Notamos que el cursor es llamado , data , en este método.
        if (data.moveToFirst()){

            // buscamos las colmnas con los atributos del item.
            int nombreColumIndex = data.getColumnIndex(P3dbContract.P3dbEntry.CN_NOMBRE);
            int telefonoColumIndex = data.getColumnIndex(P3dbContract.P3dbEntry.CN_NUMERO);

            // Después de saber las columnas, extraemos los valores.
            String nombreParaEditar = data.getString(nombreColumIndex);
            int telefonoParaEditar = data.getInt(telefonoColumIndex);

            // Y actualizamos los campo. ES FACIL
            editTextCampoNombre.setText(nombreParaEditar);
            editTextCampoTelefono.setText(Integer.toString(telefonoParaEditar));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Y el reset lo que hace es borrar los campor y ya.
        editTextCampoNombre.setText("");
        editTextCampoTelefono.setText("");

    }
}











