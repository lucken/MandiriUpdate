package com.cs_indonesia.mandiri_update.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.adapter.AdapterDevice;
import com.cs_indonesia.mandiri_update.adapter.AdapterHalte;
import com.cs_indonesia.mandiri_update.helper.API;
import com.cs_indonesia.mandiri_update.helper.util;
import com.cs_indonesia.mandiri_update.model.Device;
import com.cs_indonesia.mandiri_update.model.Halte;
import com.cs_indonesia.mandiri_update.model.Koridor;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class HalteActivity extends BaseActivity {
    Spinner spinner;
    ArrayList<String> spinnerArray;
    ArrayAdapter<String> spinnerArrayAdapter;

    RecyclerView Rv;
    AdapterHalte adapter;
    List<Halte> haltes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halte);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerArray = new ArrayList<String>();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                API.getHalteList(HalteActivity.this, "1", position + "", handler);
                dbpos = position;
                // rDialog.ShowDialog(getActivity(),"",DbList.get(dbpos).getId() + dbpos,"OK");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Rv = (RecyclerView) findViewById(R.id.Rv);
        haltes = new ArrayList<Halte>();
        adapter = new AdapterHalte(this, haltes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        Rv.setLayoutManager(layoutManager);
        Rv.setItemAnimator(new DefaultItemAnimator());
        Rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterHalte.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Halte item) {
                Intent i = new Intent(HalteActivity.this, HalteGateActivity.class);
                i.putExtra("halte", item.toJson());
                i.putExtra("pos", dbpos + "");
                i.putExtra("koridor", spinner.getSelectedItem().toString());
                startActivity(i);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    util.initMap(this, handler_map);
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                }
        }
    }

    private final Handler handler_map = new Handler() {
        public void handleMessage(Message msg) {
            util.setLocation(HalteActivity.this, util.rmap.getCurrentLocation());
            //rDialog.SetToast(ActivityMain.this,so.rMap.getCurrentLocation().getLatitude() + " " + so.rMap.getCurrentLocation().getLongitude());
            // BtNavigasi.setEnabled(true);
        }
    };

    int dbpos;

    @Override
    protected void onResume() {
        super.onResume();
        API.getKoridorList(this, "1", handler);
    }

    @Override
    protected void handlerResponse(Message msg) {
        super.handlerResponse(msg);
        if (response.isValid()) {
            switch (response.getModul()) {
                case API.mListKoridor:
                    spinnerArray.clear();
                    for (Koridor k : Koridor.fromJsonArrayList(response.getResponse())) {
                        spinnerArray.add("Koridor : " + k.getKoridor());
                    }
                    spinnerArrayAdapter.notifyDataSetChanged();
                    break;
                case API.mListHalte:
                    haltes.clear();
                    for (Halte h : Halte.fromJsonArrayList(response.getResponse())) {
                        haltes.add(h);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}
