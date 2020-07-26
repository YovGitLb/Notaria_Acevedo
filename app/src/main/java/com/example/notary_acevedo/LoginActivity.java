package com.example.notary_acevedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout lay_Email;
    private TextInputLayout lay_Password;

    private EditText et_Email;
    private EditText et_Password;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();


    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);
        lay_Email = findViewById(R.id.lay_Email);
        lay_Password = findViewById(R.id.lay_Password);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
    }


    private void initObjects() {

        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);


    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String admin=et_Email.getText().toString();
        String pass=et_Password.getText().toString();


        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                if(admin.equals("admin_notaria@gmail.com")&& pass.equals("12345"))
                {
                    Intent intentRegister = new Intent(getApplicationContext(), Adm_Activity.class);
                    startActivity(intentRegister);
                }

                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), Registration_Activity.class);
                startActivity(intentRegister);
                emptyInputEditText();
                break;
        }

    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(et_Email, lay_Email, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(et_Email, lay_Email, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(et_Password, lay_Password, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(et_Email.getText().toString().trim()
                , et_Password.getText().toString().trim())) {


            Intent accountsIntent = new Intent(activity, Opciones_Menu.class);
            accountsIntent.putExtra("EMAIL", et_Email.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);


        } else {

            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }


    }

    private void emptyInputEditText() {

        et_Email.setText(null);
        et_Password.setText(null);
    }
}
