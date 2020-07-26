package com.example.notary_acevedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;



public class Kardex extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = Kardex.this;
    private NestedScrollView nestedScrollView;
    private DatabaseHelper databaseHelper;
   // private InputValidation inputValidation;
    private Expediente expediente;
    public TextInputLayout lay_kardex;
    public TextInputLayout lay_dnia;
    public TextInputLayout lay_participante;
    private AppCompatButton btn_registrar;
    private FloatingActionButton btn_fab;
    private EditText et_kardex;
    private EditText et_dnia;
    private EditText et_participante;
    private EditText edi_dni2, edi_participante2;
    String[] servicios = {"SELECCIONE SERVICIOS", "Compra venta", "Donacion", "Poderes", "Mutuos Hipotecarios", "Separacion de bienes", "Sucesion Intestadas", "Transferencia Vehicular", "Anticipo de Legitima", "Constituciones"};
    String[] estados = {"SELECCIONE EL ESTADO", "Falta firma", "Observado", "Inscrito", "Falta pago", "Liquidado", "En proceso", "Falta documentacion", "Archivado"};
    String[] abogados = {"SELECCIONE ABOGADO", "Gomez De la Torre", "Lazo Gomez Ana", "Paredes Vargas Manuel", "Barboza Quispe Abelino", "Zapata Carpio Vanessa", "Jimenez Lopez Antonio", "Sandoval Zapata Miguel"};
    private Spinner servicios_notariales;
    private Spinner estados_expedientes;
    private Spinner lista_abogados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);




        initViews();
        initListeners();
        initObjects();
        // obtenerLista();


    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView_expedientes);
        edi_dni2 = findViewById(R.id.et_dni2);
        edi_participante2 = findViewById(R.id.et_particpante2);
        et_kardex = findViewById(R.id.et_Kardex);
        et_dnia = findViewById(R.id.et_dni);
        et_participante = findViewById(R.id.et_Participante);
        lay_kardex = findViewById(R.id.lay_Kardex);
        lay_dnia = findViewById(R.id.lay_dni);
        lay_participante = findViewById(R.id.lay_Participante);
        servicios_notariales = findViewById(R.id.sp_servicios);
        estados_expedientes = findViewById(R.id.sp_estado);
        lista_abogados = findViewById(R.id.sp_abogados);
        btn_fab=findViewById(R.id.fab);
        btn_registrar = findViewById(R.id.btnregistrarkardex);
        CargarAbogados();
        CargarEstados();
        CargarServicio();

    }



    private void initListeners() {


        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.fab:
                        if (edi_dni2.getVisibility() == View.GONE && edi_participante2.getVisibility() == View.GONE) {
                            edi_dni2.setVisibility(View.VISIBLE);
                            edi_participante2.setVisibility(View.VISIBLE);

                        } else {
                            edi_dni2.setVisibility(View.GONE);
                            edi_participante2.setVisibility(View.GONE);
                        }
                        break;
                }


            }
        });


        btn_registrar.setOnClickListener(this);


    }



    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        expediente = new Expediente();
    }



    @Override
    public void onClick(View v) {


                String kardex = et_kardex.getText().toString().trim();
                String participante1 = et_participante.getText().toString().trim();
                String dni1 = et_dnia.getText().toString().trim();
                String expedientes_lista=estados_expedientes.getSelectedItem().toString();
                String abogados_lista=estados_expedientes.getSelectedItem().toString();
                String estado_lista=estados_expedientes.getSelectedItem().toString();
                String participante2 = edi_participante2.getText().toString().trim();
                String dni2 = edi_dni2.getText().toString().trim();





                switch (v.getId()) {
                    case R.id.btnregistrarkardex:
                        if (kardex.equals("") && participante1.equals("") && dni1.equals("") && expedientes_lista.equals("") &&
                            abogados_lista.equals("") && estado_lista.equals("") && participante2.equals("")&& dni2.equals("")) {
                            Toast.makeText(this, "Porfavor introducir datos", Toast.LENGTH_SHORT).show();
                        } else {
                            postDataToSQLite();
                            CargarServicio();
                            CargarEstados();
                            CargarAbogados();
                        }

                        break;




                }

            }




    private void postDataToSQLite() {


        expediente.setKardex(et_kardex.getText().toString().trim());
        expediente.setName_abogado(lista_abogados.getSelectedItem().toString());
        expediente.setServicios(servicios_notariales.getSelectedItem().toString());
        expediente.setDni(et_dnia.getText().toString().trim());
        expediente.setNombre(et_participante.getText().toString().trim());
        expediente.setDni2(edi_dni2.getText().toString().trim());
        expediente.setNombre2(edi_participante2.getText().toString().trim());
        expediente.setEstado(estados_expedientes.getSelectedItem().toString());

        databaseHelper.addExpedientes(expediente);


        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        emptyInputEditText();
        et_kardex.requestFocus();



    }




    public void CargarServicio() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, servicios);
        servicios_notariales.setAdapter(adapter);
    }

    public void CargarEstados() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        estados_expedientes.setAdapter(adapter);
    }

    public void CargarAbogados() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, abogados);
        lista_abogados.setAdapter(adapter);
    }

  /*  private void obtenerLista(){
        listaabogados=new ArrayList<String>();
        listaabogados.add("Seleccione abogado");

        for(int i=0;i<abogadoslista.size();i++){
            listaabogados.add(abogadoslista.get(i).getId()+" - "+abogadoslista.get(i).getNombre());

        }


    }*/



    private void emptyInputEditText() {
        et_kardex.setText(null);
        et_participante.setText(null);
        et_dnia.setText(null);
        edi_participante2.setText(null);
        edi_dni2.setText(null);
       CargarEstados();
        CargarServicio();
        CargarAbogados();


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
            Intent intentRegister = new Intent(getApplicationContext(),Kardex.class);
            startActivity(intentRegister);

        }

        return super.onOptionsItemSelected(item);
    }


}
