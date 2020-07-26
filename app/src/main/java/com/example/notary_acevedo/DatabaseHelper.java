package com.example.notary_acevedo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "Notaria.db";


    // Tabla Usuario
    private static final String TABLE_USER = "user";
    // Columnas de la tabla usuario
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";



    // Creando la tabla Usuario sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";


    // Tabla Abogado
    private static final String TABLE_ABOGADO = "abogado";
    // Columnas de la tabla Abogado
    private static final String COLUMN_ABOGADO_ID = "abogado_id";
    private static final String COLUMN_ABOGADO_NOMBRE = "abogado_name";
    private static final String COLUMN_ABOGADO_COLEGIATURA = "abogado_colegiatura";
    private static final String COLUMN_ABOGADO_TELEFONO = "abogado_telefono";


    // Creando la tabla Abogado sql query
    private  String CREATE_ABOGADO_TABLE = "CREATE TABLE " + TABLE_ABOGADO + "("
            + COLUMN_ABOGADO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ABOGADO_NOMBRE + " TEXT,"
            + COLUMN_ABOGADO_COLEGIATURA + " TEXT," + COLUMN_ABOGADO_TELEFONO + " TEXT" + ")";


    // Tabla Expedientes
    private static final String TABLE_EXPEDIENTES = "expedientes";
    // Columnas de la tabla expedientes
    private static final String COLUMN_EXPEDIENTES_ID = "expediente_id";
    private static final String COLUMN_EXPEDIENTES_KARDEX = "expediente_kardex";
    private static final String COLUMN_EXPEDIENTES_SERVICIOS = "expediente_servicios";
    private static final String COLUMN_EXPEDIENTES_ID_ABOGADO = "expediente_abogado";
    private static final String COLUMN_EXPEDIENTES_DNI = "expediente_dni";
    private static final String COLUMN_EXPEDIENTES_PARTICIPANTE = "expediente_participante";
    private static final String COLUMN_EXPEDIENTES_DNI2 = "expediente_dni2";
    private static final String COLUMN_EXPEDIENTES_PARTICIPANTE2 = "expediente_participante2";
    private static final String COLUMN_EXPEDIENTES_ESTADO = "expediente_estado";


    // Creando la tabla Expediente sql query
    private  String CREATE_EXPEDIENTES_TABLE = "CREATE TABLE " + TABLE_EXPEDIENTES + "("
            + COLUMN_EXPEDIENTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EXPEDIENTES_KARDEX + " TEXT,"
            + COLUMN_EXPEDIENTES_SERVICIOS + " TEXT," + COLUMN_EXPEDIENTES_ID_ABOGADO + " TEXT,"
            + COLUMN_EXPEDIENTES_DNI + " TEXT," + COLUMN_EXPEDIENTES_PARTICIPANTE + " TEXT,"
            + COLUMN_EXPEDIENTES_DNI2 + " TEXT," + COLUMN_EXPEDIENTES_PARTICIPANTE2 + " TEXT,"
            + COLUMN_EXPEDIENTES_ESTADO + " TEXT" + ")";



    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_ABOGADO_TABLE = "DROP TABLE IF EXISTS " + TABLE_ABOGADO;
    private String DROP_EXPEDIENTES_TABLE = "DROP TABLE IF EXISTS " + TABLE_EXPEDIENTES;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }







    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ABOGADO_TABLE);
        db.execSQL(CREATE_EXPEDIENTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        //Si existe la tabla user elimina
        db.execSQL(DROP_USER_TABLE);

        //Si existe la tabla abogado elimina
        db.execSQL(DROP_ABOGADO_TABLE);

        //Si existe la tabla expediente elimina
        db.execSQL(DROP_EXPEDIENTES_TABLE);


        // Create tables again
        onCreate(db);

    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //Agregando a la tabla abogados

    public void addAbogado(Abogado abogado) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ABOGADO_NOMBRE, abogado.getNombre());
        values.put(COLUMN_ABOGADO_COLEGIATURA, abogado.getColegiatura());
        values.put(COLUMN_ABOGADO_TELEFONO, abogado.getTelefono());

        // Inserting Row
        db.insert(TABLE_ABOGADO, null, values);
        db.close();
    }


    //Agregando a la tabla abogados

    public void addExpedientes(Expediente expediente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPEDIENTES_KARDEX, expediente.getKardex());
        values.put(COLUMN_EXPEDIENTES_SERVICIOS, expediente.getServicios());
        values.put(COLUMN_EXPEDIENTES_ID_ABOGADO, expediente.getName_abogado());
        values.put(COLUMN_EXPEDIENTES_DNI, expediente.getDni());
        values.put(COLUMN_EXPEDIENTES_PARTICIPANTE, expediente.getNombre());
        values.put(COLUMN_EXPEDIENTES_DNI2, expediente.getDni2());
        values.put(COLUMN_EXPEDIENTES_PARTICIPANTE2, expediente.getNombre2());
        values.put(COLUMN_EXPEDIENTES_ESTADO, expediente.getEstado());



        // Inserting Row
        db.insert(TABLE_EXPEDIENTES, null, values);
        db.close();
    }




    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }



    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        String[] selectionArgs = {email, password};


        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }










}
