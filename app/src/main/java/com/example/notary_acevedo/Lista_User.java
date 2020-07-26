package com.example.notary_acevedo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lista_User extends AppCompatActivity {
    private final AppCompatActivity activity = Lista_User.this;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__user);


        Cargar();
    }

    public void Cargar() {

        databaseHelper = new DatabaseHelper(activity);
        SQLiteDatabase bd = databaseHelper.getReadableDatabase();
        if (bd != null) {
            Cursor cursor = bd.rawQuery("SELECT * FROM user", null);
            int cantidad = cursor.getCount();
            int i = 0;
            String[] arreglo = new String[cantidad];
            if (cursor.moveToFirst()) {
                do {
                    String linea = cursor.getInt(0) + "    " + cursor.getString(1) + "   " + cursor.getString(2) + "        " + cursor.getString(3);
                    arreglo[i] = linea;
                    i++;

                } while (cursor.moveToNext());

            }
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
            ListView lista = findViewById(R.id.listado_user);
            lista.setAdapter(adaptador);

        }


    }

}