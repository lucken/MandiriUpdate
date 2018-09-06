package com.cs_indonesia.mandiri_update.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.helper.API;
import com.cs_indonesia.mandiri_update.helper.util;
import com.cs_indonesia.mandiri_update.model.Device;
import com.cs_indonesia.mandiri_update.model.Halte;
import com.cs_indonesia.mandiri_update.model.Koridor;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raafstudio.raaf.Log;
import com.raafstudio.raaf.rImaging;
import com.raafstudio.raaf.rUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends BaseActivity {

    Spinner spinner;
    ArrayList<String> spinnerArray;
    ArrayAdapter<String> spinnerArrayAdapter;

    private Timer timer;
    GoogleMap gmap;
    MapView mapView;
    List<Device> devices;
    List<Halte> haltes;
    Button BtBuka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        BtBuka = findViewById(R.id.BtBukaHalte);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerArray = new ArrayList<String>();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                API.getHalteList(MapsActivity.this, "1", position + "", handler);
                // dbpos = position;
                // rDialog.ShowDialog(getActivity(),"",DbList.get(dbpos).getId() + dbpos,"OK");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        devices = new ArrayList<Device>();
        mapView = findViewById(R.id.Map);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        timer = new Timer();
        timer.scheduleAtFixedRate(new mainTask(), 0, 1000);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        BtBuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Halte h = (Halte) v.getTag();
                Intent i = new Intent(MapsActivity.this, HalteGateActivity.class);
                i.putExtra("halte", h.toJson());
                i.putExtra("koridor", spinner.getSelectedItem().toString());
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            gmap = googleMap;

                            //  gmap.setTrafficEnabled(true);
                            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.

                            }
                            //gmap.setTrafficEnabled(true);
                            gmap.setMyLocationEnabled(true);
                            gmap.getUiSettings().setMyLocationButtonEnabled(true);
                            gmap.getUiSettings().setCompassEnabled(true);
                            // gmap.setIndoorEnabled(true);
                            gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    for (Halte h : haltes) {
                                        if (h.getNm_halte().equals(marker.getTitle())) {
                                            BtBuka.setText("Buka Halte " + marker.getTitle());
                                            BtBuka.setTag(h);
                                            BtBuka.setVisibility(View.VISIBLE);
                                        }
                                    }


                                    return false;
                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                }
        }
    }

    private class mainTask extends TimerTask {
        public void run() {
            handlerTimer.sendEmptyMessage(0);
        }
    }

    private final Handler handlerTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            for (Device d : devices) {
                if (!d.isOnload())
                    API.getDevice(MapsActivity.this, d.getDevicemac(), handler);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        API.getKoridorList(this, "1", handler);
    }

    Marker m;

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
                    if (haltes == null)
                        haltes = new ArrayList<Halte>();
                    haltes.clear();
                    if (gmap != null)
                        gmap.clear();
                    LatLngBounds.Builder b = new LatLngBounds.Builder();
                    for (Halte h : Halte.fromJsonArrayList(response.getResponse())) {
                        haltes.add(h);
                        LatLng l = new LatLng(h.getLat(), h.getLng());
                        Bitmap marker = util.getBipmapFromDrawable(this, "marker_biru");
                        if (h.getStatus() < 2 && h.getStatus() > 0) {
                            marker = util.getBipmapFromDrawable(this, "marker_start");
                        } else if (h.getStatus() == 2) {
                            marker = util.getBipmapFromDrawable(this, "marker_sukses");
                        } else if (h.getStatus() > 2) {
                            marker = util.getBipmapFromDrawable(this, "marker_trouble");
                        }

                        String[] ss = h.getNm_halte().split(" ");
                        int i = 25;
                        for (String s : ss) {
                            marker = rImaging.drawTextToBitmap(this, marker, s, 25, 0, i, Color.parseColor("#000000"));
                            i += 25;
                        }

                        marker = util.resizeMapIcons(marker, 64, 64);

                        MarkerOptions mo = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(marker));
                        mo.position(l);
                        mo.anchor(0.5f, 1.0f);
                        mo.title(h.getNm_halte());
                        mo.snippet(h.getJumlah_gate() + " gate");
                        if (gmap != null) {
                            gmap.addMarker(mo);
                            b.include(l);
                            LatLngBounds bounds = b.build();
                            int width = mapView.getMeasuredWidth();//  getResources().getDisplayMetrics().widthPixels;
                            int height = mapView.getMeasuredHeight();// getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.10);
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                            gmap.animateCamera(cu);
                        }

                    }
                    // adapter.notifyDataSetChanged();
                    break;
                case API.mDevices:
                    devices.clear();
                    for (Device d : Device.fromJsonArrayList(response.getResponse())) {
                        devices.add(d);
                    }
                    break;
                case API.mDevice:
                    try {
                        Device d = Device.fromJson(response.getResponse());
                        String devicemac = msg.getData().getString("devicemac");
                        for (int i = 0; i < devices.size(); i++) {
                            if (devices.get(i).getDevicemac().equals(devicemac)) {
                                devices.get(i).setOnload(false);
                                if (!d.getTrxtime().equals(devices.get(i).getTrxtime())) {
                                    if (devices.get(i).getmAkhir() != null)
                                        devices.get(i).getmAkhir().remove();
                                    devices.get(i).setLat_terakhir(d.getLat_terakhir());
                                    devices.get(i).setLng_terakhir(d.getLng_terakhir());
                                    devices.get(i).setSpeed(d.getSpeed());
                                    devices.get(i).setNo_body(d.getNo_body());
                                    devices.get(i).setTrxtime(d.getTrxtime());
                                    LatLng l = new LatLng(Double.parseDouble(d.getLat_terakhir()), Double.parseDouble(d.getLng_terakhir()));

                                    MarkerOptions mo = new MarkerOptions()
                                            .position(l)
                                            .title(d.getNo_body() + " - " + d.getPlat_nomor())
                                            .snippet(d.getTrxtime().split(" ")[1] + " [ " + d.getSpeed() + " ]");
                                    //.icon(icon_current);
                                    devices.get(i).setmAkhir(gmap.addMarker(mo));
                                    gmap.addMarker(mo).showInfoWindow();
                                }
                            }
                        }
                    } catch (Exception e) {

                    }

                    break;
            }
        }
    }


}
