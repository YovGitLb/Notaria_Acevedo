package com.example.notary_acevedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class Registration_Abogado extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = Registration_Abogado.this;

    private NestedScrollView nestedScrollView;


    public TextInputLayout lay_abogado_nombre;
    public TextInputLayout lay_abogado_colegiatura;
    public TextInputLayout lay_abogado_celular;


    private EditText et_Name_Abogado;
    private EditText et_Colegiatura;
    private EditText et_Celular;


    private AppCompatButton appCompatButtonRegister_abogado;
    private AppCompatTextView appCompatTextViewAbogadoLink;

    public InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private LoginActivity login;
    private Abogado abogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__abogado);

        //getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();

    }



    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView_abogado);
        lay_abogado_nombre = findViewById(R.id.lay_NombreAbogados);
        lay_abogado_colegiatura = findViewById(R.id.lay_Colegiaturas);
        lay_abogado_celular = findViewById(R.id.lay_Celulares);
        et_Name_Abogado=findViewById(R.id.et_Name_Abogado);
        et_Colegiatura=findViewById(R.id.et_Colegiatura);
        et_Celular=findViewById(R.id.et_Celular);

        appCompatButtonRegister_abogado = findViewById(R.id.appCompatButtonRegister_abogado);
        appCompatTextViewAbogadoLink = findViewById(R.id.appCompatTextViewAbogadoLink);
    }

    private void initListeners() {

        appCompatButtonRegister_abogado.setOnClickListener(this);
        appCompatTextViewAbogadoLink.setOnClickListener(this);
    }

    private void initObjects() {


        databaseHelper = new DatabaseHelper(activity);
        abogado = new Abogado();
    }

    @Override
    public void onClick(View v) {

        String nombre=et_Name_Abogado.getText().toString();
        String colegiatura=et_Colegiatura.getText().toString();
        String telefono=et_Celular.getText().toString();

        switch (v.getId()) {
            case R.id.appCompatButtonRegister_abogado:
            if(nombre.equals("") && colegiatura.equals("") && telefono.equals(""))
                {
                Toast.makeText(this,"Porfavor introducir datos",Toast.LENGTH_SHORT).show();
                }
            else {
                 postDataToSQLite();
                  }

            break;

            case R.id.appCompatTextViewAbogadoLink:
                finish();
                break;


        }


    }

    private void postDataToSQLite() {


       // if (!databaseHelper.checkAbogado(et_Name_Abogado.getText().toString().trim())) {

            abogado.setNombre(et_Name_Abogado.getText().toString().trim());
            abogado.setColegiatura(et_Colegiatura.getText().toString().trim());
            abogado.setTelefono(et_Celular.getText().toString().trim());


            databaseHelper.addAbogado(abogado);

            // Snack Bar to show success message that record saved successfully
            //Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            emptyInputEditText();
            et_Name_Abogado.requestFocus();

      //  }
        //   else {
            // Snack Bar to show error message that record already exists
        //     Snackbar.make(nestedScrollView, getString(R.string.error_nombre_exists), Snackbar.LENGTH_LONG).show();
        //  }

    }


    private void emptyInputEditText() {
        et_Name_Abogado.setText(null);
        et_Colegiatura.setText(null);
        et_Celular.setText(null);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu,menu);
            return  true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id=item.getItemId();

        if(id==R.id.action_settings){
            return true;
        }

        if(id==R.id.action_listar){
            Intent intentRegister = new Intent(getApplicationContext(),Lista_abogados.class);
            startActivity(intentRegister);

        }

         return super.onOptionsItemSelected(item);
    }




}