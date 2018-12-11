package com.example.etnarion.lab3sym;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnNfc;
    Button btnBarcode;
    Button btnIBeacon;
    Button btnCaptors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNfc = (Button)findViewById(R.id.nfc);
        btnBarcode = (Button)findViewById(R.id.barCode);
        btnIBeacon = (Button)findViewById(R.id.IBeacon);
        btnCaptors = (Button)findViewById(R.id.captors);

        btnNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, NfcActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, BarcodeActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnIBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, IBeaconActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnCaptors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, CaptorsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
