package com.cs_indonesia.mandiri_update.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.helper.API;
import com.cs_indonesia.mandiri_update.model.Device;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raafstudio.raaf.rDialog;

import java.util.Timer;
import java.util.TimerTask;

public class    DeviceActivity extends BaseActivity {
    private Timer timer;
    GoogleMap gmap;
    MapView mapView;
    Device d1, d2;
    Marker mAwal, mAkhir;
    boolean isLoadMap = false;

    int itouch = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        d1 = Device.fromJson(getIntent().getStringExtra("device"));
        d1.setLng_terakhir("0");
        d1.setLat_terakhir("0");
        mapView = (MapView) findViewById(R.id.Map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;

                //  gmap.setTrafficEnabled(true);
                if (ActivityCompat.checkSelfPermission(DeviceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DeviceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                gmap.setTrafficEnabled(true);
//                gmap.setMyLocationEnabled(true);
 //
                //               gmap.getUiSettings().setMyLocationButtonEnabled(true);
                gmap.getUiSettings().setCompassEnabled(true);
                // gmap.setIndoorEnabled(true);
                gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                gmap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {
                        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE)
                            itouch = 0;
                    }
                });
                View locationButton = mapView.findViewById(Integer.parseInt("2"));// ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                locationButton.setLayoutParams(rlp);

            }
        });


        if (API.getDevice(DeviceActivity.this, d1.getDevicemac(), handler))
            isLoadMap = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new mainTask(), 0, 1000);
    }

    private class mainTask extends TimerTask {
        public void run() {
            handlerTimer.sendEmptyMessage(0);
        }
    }

    private final Handler handlerTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            itouch++;

            if (isLoadMap)
                return;
            //   String tgl = rDate.getCurrentDate("yyyy-MM-dd");
            if (API.getDevice(DeviceActivity.this, d1.getDevicemac(), handler))
                //if (API.getLiveTrack(TrackingActivity.this, deviceMac, tgl, handler))
                isLoadMap = true;

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void handlerResponse(Message msg) {
        super.handlerResponse(msg);
        if (this.isFinishing())
            return;
        if (response.isValid()) {
            d2 = Device.fromJson(response.getResponse());
            if (!d1.getLat_terakhir().equals(d2.getLat_terakhir()) || !d1.getLng_terakhir().equals(d2.getLng_terakhir())) {
                d1.setLat_terakhir(d2.getLat_terakhir());
                d1.setLng_terakhir(d2.getLng_terakhir());
                LatLng l = new LatLng(Double.parseDouble(d2.getLat_terakhir()), Double.parseDouble(d2.getLng_terakhir()));

                MarkerOptions mo = new MarkerOptions()
                        .position(l)
                        .title(d2.getTrxtime())
                        .snippet("[" + d2.getSpeed() + " km/j]");
                //.icon(icon_current);
                mAkhir = gmap.addMarker(mo);
                gmap.addMarker(mo).showInfoWindow();
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(l, 15);
                if (itouch > 5)
                    gmap.animateCamera(cu);

            }

        }
        isLoadMap = false;
    }
}
