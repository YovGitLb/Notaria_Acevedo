package com.example.notary_acevedo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class Reserva_cita extends AppCompatActivity {


    private TextInputLayout lay_nombre_cliente;
    private TextInputLayout lay_dni;

    private EditText et_nombre;
    private EditText et_dni;

    TextView fecha, hora;

    String[] servicios = {"SELECCIONE","Compra venta", "Donacion", "Poderes","Mutuos Hipotecarios","Separacion de bienes","Sucesion Intestadas","Transferencia Vehicular","Anticipo de Legitima","Constituciones"};
    String[] abogados = {"SELECCIONE","Gomez De la Torre", "Lazo Gomez Ana", "Paredes Vargas Manuel", "Barboza Quispe Abelino", "Zapata Carpio Vanessa", "Jimenez Lopez Antonio", "Sandoval Zapata Miguel"};

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Spinner servicio, abogado;
    Button btnreserva;


    CheckBox tomafirma, abrirexpediente, consulta;
    ArrayList<String> Nombre_abogados = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_cita);


        lay_nombre_cliente = findViewById(R.id.lay_nombre);
        lay_dni = (TextInputLayout) findViewById(R.id.lay_dni);
        et_nombre=(EditText)findViewById(R.id.et_nombre);
        et_dni=(EditText)findViewById(R.id.et_dni);

        tomafirma = (CheckBox) findViewById(R.id.toma_de_firma);
        abrirexpediente = (CheckBox) findViewById(R.id.abrir_expediente);
        consulta = (CheckBox) findViewById(R.id.consultas_expediente);


        servicio = (Spinner) findViewById(R.id.servicios);
        abogado = (Spinner) findViewById(R.id.abogado_lista);

        hora = (TextView) findViewById(R.id.hora_cita);
        fecha = (TextView) findViewById(R.id.fecha_cita);

        btnreserva = (Button) findViewById(R.id.btnregistrarkardex);


        abogado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

             /*   NombreMedicoespecialidad.clear();
                BuscarMedicoEspecialidad();
                CargarMedicoEspecialidad();*/

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Reserva_cita.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hora.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Selecione Hora");
                mTimePicker.show();

            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYer = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormatear = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(Reserva_cita.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        fecha.setText(dateFormatear.format(newDate.getTime()));
                    }
                }, mYer, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btnreserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = et_nombre.getText().toString().trim();
                String ape = et_dni.getText().toString().trim();
                String fec = fecha.getText().toString().trim();
                String hr = hora.getText().toString().trim();

                if (TextUtils.isEmpty(nom)) {
                    Toast.makeText(Reserva_cita.this, "Ingrese su nombre", Toast.LENGTH_SHORT).show();

                    et_nombre.requestFocus();


                } else if (TextUtils.isEmpty(ape)) {
                    Toast.makeText(Reserva_cita.this, "Ingrese Apellido Paciente", Toast.LENGTH_SHORT).show();
                    et_dni.requestFocus();
                } else if (TextUtils.isEmpty(fec)) {
                    Toast.makeText(Reserva_cita.this, "Ingrese Fecha", Toast.LENGTH_SHORT).show();
                    fecha.requestFocus();

                } else if (TextUtils.isEmpty(hr)) {
                    Toast.makeText(Reserva_cita.this, "Ingrese Hora ", Toast.LENGTH_SHORT).show();
                    hora.requestFocus();
                } else if (tomafirma.isChecked()) {
                    String seg_pa = tomafirma.getText().toString();
                    escribirFichero(seg_pa);
                    et_nombre.setText("");
                    et_dni.setText("");
                    abogado.setSelection(0);
                    servicio.setSelection(0);
                    hora.setText("");
                    fecha.setText("");

                } else if (abrirexpediente.isChecked()) {
                    String seg_pa = abrirexpediente.getText().toString();
                   escribirFichero(seg_pa);
                    et_nombre.setText("");
                    et_dni.setText("");
                    abogado.setSelection(0);
                    servicio.setSelection(0);
                    hora.setText("");
                    fecha.setText("");

                } else {
                    Toast.makeText(Reserva_cita.this, "Escoja el tipo de consulta", Toast.LENGTH_SHORT).show();

                }

            }


        });


        CargarListaAbogados();
        CargarServicio();
    }

    public void CargarListaAbogados() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, abogados);
        abogado.setAdapter(adapter);
    }

    /*public void CargarMedicoEspecialidad() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, NombreMedicoespecialidad);
        medicos.setAdapter(adapter);
    }*/


    public void CargarServicio() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, servicios);
        servicio.setAdapter(adapter);
    }


  /* private void BuscarMedicoEspecialidad() {
        InputStreamReader flujo = null;
        BufferedReader lector = null;
        try {
            flujo = new InputStreamReader(openFileInput("especilidades.txt"));
            lector = new BufferedReader(flujo);
            String texto = lector.readLine();
            String cadena[];
            while (texto != null) {
                cadena = texto.split(Pattern.quote("|"));

                //Toast.makeText(this,texto.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(this,cadena[0].toString(),Toast.LENGTH_LONG).show();

                String especial = especilidad.getSelectedItem().toString();

                if (cadena[0].toString().equals(especial)) {
                    NombreMedicoespecialidad.add(cadena[1].toString());


                } else {

                }

                texto = lector.readLine();

            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (flujo != null) {
                    flujo.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/

    private void escribirFichero(String seguro) {
        OutputStreamWriter escrictor = null;
        try {
            FileOutputStream f = openFileOutput("CITAS_NOTARIALES.txt", Context.MODE_APPEND);
            String nom_cita = et_nombre.getText().toString();
            String dni_cita = et_dni.getText().toString();
            String abogado_cita = abogado.getSelectedItem().toString();
            String servicio_cita = servicio.getSelectedItem().toString();
            String hora_cita = hora.getText().toString();
            String fecha_cita = fecha.getText().toString();



            String texto = nom_cita + "|" + dni_cita + "|" + seguro + "|" + abogado_cita + "|" + servicio_cita + "|" + hora_cita + "|" + fecha_cita + "\n";
            f.write(texto.getBytes());
            Toast.makeText(this, "Registro con exito", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (escrictor != null) {
                    escrictor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
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
            Intent intentRegister = new Intent(getApplicationContext(),Cita.class);
            startActivity(intentRegister);

        }

        return super.onOptionsItemSelected(item);
    }
}