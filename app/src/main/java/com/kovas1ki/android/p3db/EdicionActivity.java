package com.kovas1ki.android.p3db;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EdicionActivity extends AppCompatActivity {

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
                // Esto termina con el return true
                return true;
            case R.id.botonEliminar:
                Toast.makeText(this, "has pulsado BOTON ELIMINAR", Toast.LENGTH_SHORT).show();
                return true;
        }




        // Esta parte del return se deja tal cual para que
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);
    }

}
