package com.example.etnarion.lab3sym;

/*******************************************************************
 * IBeaconActivity.java
 * Activity that tracks IBeacons nearby and display them in a list
 * Authors: Alexandra Korukova and Samuel Mayor
 ******************************************************************/

import android.Manifest;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class IBeaconActivity extends AppCompatActivity implements BeaconConsumer {
    private BeaconManager beaconManager;
    private ListView beaconsListview;
    ArrayAdapter<String> adapter;
    private ArrayList<String> beaconList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibeacon);

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toast.makeText(getApplicationContext(), "Fine location permission accepted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Fine location permission denied", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            }
        }).check();

        Dexter.withActivity(this).withPermission(Manifest.permission.BLUETOOTH).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toast.makeText(getApplicationContext(), "Bluetooth permission accepted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Bluetooth permission denied", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            }
        }).check();

        beaconsListview = (ListView) findViewById(R.id.beaconsList);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, beaconList);
        beaconsListview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size() > 0){
                    beaconList.clear();
                    for (Beacon b : beacons) {
                        System.out.print(b.getId1()+""+b.getId2());
                        beaconList.add("RSSI: " + b.getRssi()
                                + "\nMAJOR: " + b.getId2()
                                + "\nMINOR: " + b.getId3());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
