package com.cs_indonesia.mandiri_update.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.adapter.AdapterHalte;
import com.cs_indonesia.mandiri_update.adapter.AdapterHalteGate;
import com.cs_indonesia.mandiri_update.helper.API;
import com.cs_indonesia.mandiri_update.helper.so;
import com.cs_indonesia.mandiri_update.helper.util;
import com.cs_indonesia.mandiri_update.model.Halte;
import com.cs_indonesia.mandiri_update.model.HalteGate;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raafstudio.raaf.custom.ImageviewNormal;
import com.raafstudio.raaf.io.rFile;
import com.raafstudio.raaf.rDialog;
import com.raafstudio.raaf.rIO;
import com.raafstudio.raaf.rImaging;
import com.raafstudio.raaf.rUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class HalteGateActivity extends BaseActivity {

    TextView TvKoridor, TvHalteName;

    RecyclerView Rv;
    AdapterHalteGate adapter;
    List<HalteGate> halteGates;
    ImageviewNormal ImgFoto1, ImgFoto2;
    GoogleMap gmap;
    MapView mapView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halte_gate);
        TvKoridor = findViewById(R.id.TvKoridor);
        TvHalteName = findViewById(R.id.TvHalteName);
        Rv = findViewById(R.id.Rv);

        halteGates = new ArrayList<HalteGate>();
        adapter = new AdapterHalteGate(this, halteGates);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        Rv.setLayoutManager(layoutManager);
        Rv.setItemAnimator(new DefaultItemAnimator());
        Rv.setAdapter(adapter);
        adapter.setOnStartListener(new AdapterHalteGate.OnStartListener() {
            @Override
            public void onStart(View view, HalteGate item) {
                util.rmap.CheckGps();
                if (!util.rmap.isGpsEnable()) {
                    util.rmap.ShowBuilderGps();
                    return;
                }
                if (akurasi > 20 || akurasi == 0)
                    return;
                gateid = item.getId();
                rDialog.ConfirmDialog(HalteGateActivity.this, "Update Status", "Apakah Anda yakin akan Memulai " + item.getNm_gate() + " ?", "YA", "BATAL", hanMulai);
            }
        });

        adapter.setOnStopListener(new AdapterHalteGate.OnStopListener() {
            @Override
            public void onStop(View view, HalteGate item) {
                util.rmap.CheckGps();
                if (!util.rmap.isGpsEnable()) {
                    util.rmap.ShowBuilderGps();
                    return;
                }
                if (akurasi > 20 || akurasi == 0)
                    return;
                gateid = item.getId();
                rDialog.ConfirmDialog(HalteGateActivity.this, "Update Status", "Apakah Anda yakin akan Menyelesaikan  " + item.getNm_gate() + " ?", "YA", "BATAL", hanYa);
            }
        });
        mapView = findViewById(R.id.Map);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }


    int gateid;
    Halte h;
    protected final Handler hanMulai = new Handler() {
        public void handleMessage(Message msg) {
            API.doStartGate(HalteGateActivity.this, gateid + "", 1, "", handler);
        }
    };
    Dialog dialog;
    Button BtSimpan;
    @SuppressLint("HandlerLeak")
    protected final Handler hanYa = new Handler() {
        public void handleMessage(Message msg) {

            dialog = new Dialog(HalteGateActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_simpan);

            final TextInputLayout TiNomorSam = dialog.findViewById(R.id.TiNomorSam);
            final TextInputLayout TiCatatan = dialog.findViewById(R.id.TiCatatan);
            final RadioButton RbSukses = dialog.findViewById(R.id.RbSukses);
            final RadioButton RbGagal = dialog.findViewById(R.id.RbGagal);
            Button BtFoto = dialog.findViewById(R.id.BtFoto);
            BtSimpan = dialog.findViewById(R.id.BtSimpan);
            Button BtBatal = dialog.findViewById(R.id.BtBatal);
            ImgFoto1 = dialog.findViewById(R.id.ImgFoto1);
            ImgFoto2 = dialog.findViewById(R.id.ImgFoto2);
            BtFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HalteGateActivity.this, new String[]{Manifest.permission.CAMERA}, 1234);
                        return;
                    } else
                        cekTulis();
                }
            });
            BtSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<rFile> files = new ArrayList<rFile>();
                    pos = 1;
                    rFile file = new rFile();
                    file.setParam("foto1");
                    file.setFile(new File(getFileName()));
                    file.setMimeType("image/jpeg");
                    files.add(file);
                    pos = 0;
                    file = new rFile();
                    file.setParam("foto2");
                    file.setFile(new File(getFileName()));
                    file.setMimeType("image/jpeg");
                    files.add(file);
                    if (RbSukses.isChecked())
                        API.doStopGate(HalteGateActivity.this, gateid + "", 2, TiCatatan.getEditText().getText().toString(), TiNomorSam.getEditText().getText().toString(), files, handler);
                    else
                        API.doStopGate(HalteGateActivity.this, gateid + "", 3, TiCatatan.getEditText().getText().toString(), TiNomorSam.getEditText().getText().toString(), files, handler);
                }
            });
            BtBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File photoFile = new File(getFileName());
                    if (photoFile.exists())
                        photoFile.delete();
                    dialog.cancel();
                }
            });
            dialog.show();


            //rDialog.ConfirmDialog(HalteGateActivity.this, "Status", "Pilih SELESAI apabila SAM berhasil di update" + rUtil.newLine()  + "Pilih TROUBLE apabila SAM gagal di update ", "SELESAI", "TROUBLE", hanSelesai ,hanTrouble);
            //  API.doStopGate(HalteGateActivity.this, gateid + "", "0", "0", handler);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            gmap = googleMap;

                            //  gmap.setTrafficEnabled(true);
                            if (ActivityCompat.checkSelfPermission(HalteGateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HalteGateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            MarkerOptions mo = new MarkerOptions();
                            mo.position(new LatLng(h.getLat(), h.getLng()));
                            mo.anchor(0.5f, 0.5f);
                            mo.title(h.getNm_halte());
                            mo.snippet(h.getJumlah_gate() + " gate");
                            if (gmap != null)
                                gmap.addMarker(mo).showInfoWindow();
                            util.initMap(HalteGateActivity.this, handler_map);
                            util.rmap.CheckGps();
                            if (!util.rmap.isGpsEnable())
                                util.rmap.ShowBuilderGps();
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                }
            case 1234: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cekTulis();
                }
            }
            case 1235: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    dofoto();
                }
            }
            break;
        }
    }

    int akurasi = 100;
    private final Handler handler_map = new Handler() {
        public void handleMessage(Message msg) {
            util.setLocation(HalteGateActivity.this, util.rmap.getCurrentLocation());
            LatLng latLng = new LatLng(util.getLocation(HalteGateActivity.this).getLatitude(), util.getLocation(HalteGateActivity.this).getLongitude());
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            akurasi = (int) util.getLocation(HalteGateActivity.this).getAccuracy();
            ((TextView) findViewById(R.id.TvAkurasi)).setText(akurasi + "");
            gmap.animateCamera(cu);
            //rDialog.SetToast(ActivityMain.this,so.rMap.getCurrentLocation().getLatitude() + " " + so.rMap.getCurrentLocation().getLongitude());
            // BtNavigasi.setEnabled(true);
        }
    };


    private void cekTulis() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HalteGateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1235);
            return;
        } else
            dofoto();
    }

    int pos = 0;

    private void dofoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (pos == 0)
                pos = 1;
            else
                pos = 0;
            Uri photoURI = null;
            File photoFile = new File(getFileName());
            photoURI = FileProvider.getUriForFile(this,
                    getString(R.string.file_provider_authority),
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, 123);
        }
    }

    private String getFileName() {
        return rIO.getDir(this) + "temp" + pos + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    File photoFile = new File(getFileName());
                    Bitmap b = rImaging.getImageFromFile(getFileName());
                    if (b.getWidth() > b.getHeight())
                        b = rImaging.getPreview(b, 1280);
                    else
                        b = rImaging.getPreview(b, 720);
                    rImaging.SaveImageToFile(b, getFileName(), Bitmap.CompressFormat.JPEG, 80);
                    if (photoFile.exists()) {
                        if (pos == 0) {
                            Glide.with(this)
                                    .load(getFileName())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(ImgFoto2);
                            BtSimpan.setEnabled(true);
                        } else
                            Glide.with(this)
                                    .load(getFileName())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(ImgFoto1);
                    }
                } catch (Exception e) {

                }

                // String foto_path = util.getRealPathFromURI(getActivity(), data.getData());
                // rSp.setParamString(getActivity(), "foto_path", foto_path);
            }
        }
    }
//
//    @SuppressLint("HandlerLeak")
//    protected final Handler hanSelesai = new Handler() {
//        public void handleMessage(Message msg) {
//            API.doStopGate(HalteGateActivity.this, gateid + "", 2, "", handler);
//        }
//    };
//
//    @SuppressLint("HandlerLeak")
//    protected final Handler hanTrouble = new Handler() {
//        public void handleMessage(Message msg) {
//            API.doStopGate(HalteGateActivity.this, gateid + "", 3, "", handler);
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
//        i.putExtra("halte", item.toJson());
//        i.putExtra("koridor", spinner.getSelectedItem().toString());
        h = Halte.fromJson(getIntent().getStringExtra("halte"));
        String k = getIntent().getStringExtra("koridor");
        TvKoridor.setText(k);
        TvHalteName.setText(h.getNm_halte());
        API.getGateList(this, "1", h.getNm_halte(), handler);
        if (util.rmap != null)
            util.rmap.RequestLocation();

    }

    @Override
    protected void handlerResponse(Message msg) {
        super.handlerResponse(msg);
        if (response.isValid()) {
            switch (response.getModul()) {
                case API.mListGate:
                    halteGates.clear();
                    for (HalteGate h : HalteGate.fromJsonArrayList(response.getResponse())) {
                        halteGates.add(h);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case API.mGateStart:
                    rDialog.SetToast(this, "Gate berhasil di update");
                    API.getGateList(this, "1", h.getNm_halte(), handler);

                    break;
                case API.mGateStop:
                    rDialog.SetToast(this, "Gate berhasil di update");
                    API.getGateList(this, "1", h.getNm_halte(), handler);
                    File photoFile = new File(getFileName());
                    if (photoFile.exists())
                        photoFile.delete();
                    dialog.cancel();
                    break;
            }


        }
    }

    public void update(View v) {

    }
}
