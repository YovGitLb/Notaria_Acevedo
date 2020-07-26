package com.example.notary_acevedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;//


public class Adm_Activity extends AppCompatActivity implements View.OnClickListener {

     NestedScrollView nestedScrollView;


     FloatingActionButton btnusuario, btnabogado, btnexpediente;

     TextView txtuser, txtabogado, txtexpediente;

     User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_);


       initViews();
       initListeners();




    }



    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView_listauser);

        txtuser = findViewById(R.id.textuser);
        txtabogado = findViewById(R.id.txtabogado);
        txtexpediente = findViewById(R.id.textexpediente);

        btnusuario = findViewById(R.id.fab1);
        btnabogado = findViewById(R.id.fab2);
        btnexpediente = findViewById(R.id.fab3);
    }

    private void initListeners() {


        btnusuario.setOnClickListener(this);
        btnabogado.setOnClickListener(this);
        btnexpediente.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab1:
                Intent intentRegister = new Intent(getApplicationContext(), Lista_User.class);
                startActivity(intentRegister);
            break;
            case R.id.fab2:
                Intent intentRegister1 = new Intent(getApplicationContext(), Registration_Abogado.class);
                startActivity(intentRegister1);
                break;
            case R.id.fab3:
                Intent intentRegister2 = new Intent(getApplicationContext(), Kardex.class);
                startActivity(intentRegister2);
                break;

        }
    }
}