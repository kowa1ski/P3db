package com.kovas1ki.android.p3db;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.kovas1ki.android.p3db.data.P3dbContract;


// NOOOOOOO . no implementamos nada. Desde el , this , de abajo lo forzamos y es más práctico. Eso sí
// donde pone , <Objetct> , en automático hay que cambiarlo por la palabra , Cursor ,.
// (y en los métodos que generamos a partir de esto también le cambiamos a , Cursor ,.
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Hemos creado la clase cursorAdapter para algo así que
    // Declaramos el cursorAdapter y ponemos su
    // parámetro de inicio a 0.
    P3dbCursorAdapter p3dbCursorAdapter;
    private static final int ITEMS_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Vamos aprobar que funciona reemplazando el mensaje de este button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "PROBANDO FUNCIONAMIENTO DEL BOTÓN", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // Añadiendo intent para ir a la pantalla de edición
                Intent intent = new Intent(MainActivity.this, EdicionActivity.class);
                startActivity(intent);
            }
        });

        // Buscamos la lista donde vamos a publicar los registros devueltos
        // por el cursor
        ListView itemListView = (ListView) findViewById(R.id.listViewDeRegistros);

        // Arriba hemos declarado el adaptador pero no lo hemos iniciado.
        // Vamos a coger esa variable y le vamos a decirle que cree un
        // nuevo cursorAdapter.
        // Cuidado!!!!, le pasamos el parámetro cursor en , null ,. Importante.
        p3dbCursorAdapter = new P3dbCursorAdapter(this, null);
        // Y ordenamos la adaptación de la lista
        itemListView.setAdapter(p3dbCursorAdapter);

        // Hacemos uso de la clase LoaderManager para iniciar la misma,
        // que para eso la implementamos en la declaración de esta.
        // Es aquí donde tenemos el , this , que dispara toda la secuencia
        // de la implementación automática en el código de tod lo necesario
        // con la implementeación de LoaderManager.
        getLoaderManager().initLoader(ITEMS_LOADER, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Estos se generan automáticos desde arriba. Pero, como arriba cambiamos
    // Object por Cursor, pues aquí también.

    // Vamos a completar estos métodos

    // Lo primero es el onCreate del Loader. Le tenemos que decir aquello que
    // queremos que haga cuando se le llame.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Definimos una projection que le diga las columnas
        // que queremos capturar en el cursor.

        String[] projection = {
                P3dbContract.P3dbEntry.CN_ID,
                P3dbContract.P3dbEntry.CN_NOMBRE,
                P3dbContract.P3dbEntry.CN_NUMERO
        };

        // Este loader ejecutará el método QUERY del ContentProvider en un
        // hilo en background.
        // Y eso mismo es lo que retornamos
        return new CursorLoader(this,               // Contexto
                P3dbContract.P3dbEntry.CONTENT_URI, // Uri del ContentProvider
                                                    // para consultar
                projection,                         // COLUMNAS A INCLUIR
                null,                               // clausula
                null,                               // no selection arguments
                null);                              // default short order
    }    // Ok, hecho

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Cuando finaliza lo que se ha estado haciendo en segundo plano
        // es hora de montar esos datos. Se supone que aquí llega el Cursor
        // ya cargadito. El cursor en cuestion vemos que lo llaman , data ,.

        // Vamos a ordenarlo pues.
        p3dbCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Igual que el anterior pero para resetearlo lo que voy a
        // hacer es no pasarle lo de antes(que tampoco podría por el
        // método que es jajaj). En el Finished le pasábamos , data , que
        // era el cursor cargadito.
        // Aquí lo reseteamos así que le decimos que carque también
        // pero en , null ,.
        p3dbCursorAdapter.swapCursor(null);

    }

}



