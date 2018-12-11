package com.example.etnarion.lab3sym;

import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NfcActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button nfcButton;
    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        nfcButton = (Button) findViewById(R.id.nfc_login);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (nfcAdapter == null) {
                Toast.makeText(NfcActivity.this, getString(R.string.no_nfc), Toast.LENGTH_LONG).show();
            } else {

            }
            }
        });
    }

    public void login(View view) {
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "WrongCredentials",Toast.LENGTH_SHORT).show();
            password.setText("");
        }
    }
}
