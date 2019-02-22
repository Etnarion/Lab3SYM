package com.example.etnarion.lab3sym;

/*******************************************************************
 * NfcActivity.java
 * Activity that presents a login that have to be validated by an
 * NFC key.
 * Authors: Alexandra Korukova and Samuel Mayor
 ******************************************************************/

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class NfcActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button nfcButton;
    private TextView textResult;
    private NfcAdapter nfcAdapter;
    private final int AUTHENTICATE_MAX = 10;
    private final int AUTHENTICATE_MEDIUM = 6;
    private final int AUTHENTICATE_LOW = 3;
    private final int NO_ATHENTICATE = 0;
    private int authLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        nfcButton = (Button) findViewById(R.id.nfc_login);
        textResult = (TextView) findViewById(R.id.textResult);
        nfcButton.setVisibility(View.GONE);
        textResult.setVisibility(View.GONE);

        authLevel = NO_ATHENTICATE;

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
                textResult.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, nfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Handles given intent for "each" type of NFC
     * @param intent Intent to handle
     */
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (type.equals("text/plain")) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * Checks if username and password are correct then enable NFC activation
     * @param view
     */
    public void login(View view) {
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Authentication successful. Validate with NFC",Toast.LENGTH_SHORT).show();
            nfcButton.setVisibility(View.VISIBLE);
            username.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
        }else{
            Toast.makeText(getApplicationContext(), "Wrong credentials",Toast.LENGTH_SHORT).show();
            password.setText("");
        }
    }

    /**
     * Asynchronously read NFC data
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        /**
         * Read text received by the NFC key and returns it
         * @param record The received payload
         * @return Decoded text
         * @throws UnsupportedEncodingException
         */
        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("test")) {
                authLevel = AUTHENTICATE_MAX;
                String authMessage = "Max";
                textResult.setText(getString(R.string.athenticationsuccess) + getString(R.string.auth_max) + authMessage);
                Timer authDecreaser =  new Timer();
                authDecreaser.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        authLevel--;
                        if (authLevel == AUTHENTICATE_MEDIUM) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textResult.setText(getString(R.string.auth_medium));
                                }
                            });
                        } else if (authLevel == AUTHENTICATE_LOW) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textResult.setText(getString(R.string.auth_low));
                                }
                            });
                        } else if (authLevel == NO_ATHENTICATE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Disconnected. Please log in again.",Toast.LENGTH_SHORT).show();
                                    username.setVisibility(View.VISIBLE);
                                    password.setVisibility(View.VISIBLE);
                                    loginButton.setVisibility(View.VISIBLE);
                                    nfcButton.setVisibility(View.GONE);
                                    textResult.setVisibility(View.GONE);
                                    authDecreaser.cancel();
                                    authDecreaser.purge();
                                }
                            });
                        }
                    }
                }, 1200, 1200);
            }
        }
    }
}
