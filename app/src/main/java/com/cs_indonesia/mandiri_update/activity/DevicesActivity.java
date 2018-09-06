package com.cs_indonesia.mandiri_update.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.adapter.AdapterDevice;
import com.cs_indonesia.mandiri_update.helper.API;
import com.cs_indonesia.mandiri_update.model.Device;
import com.raafstudio.raaf.rSp;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends BaseActivity {

    RecyclerView Rv;
    AdapterDevice adapter;
    List<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        Rv = (RecyclerView) findViewById(R.id.Rv);
        devices = new ArrayList<Device>();
        adapter = new AdapterDevice(this, devices);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        Rv.setLayoutManager(layoutManager);
        Rv.setItemAnimator(new DefaultItemAnimator());
        Rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterDevice.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Device item) {
                Intent i = new Intent(DevicesActivity.this, DeviceActivity.class);
                i.putExtra("device", item.toJson());
                startActivity(i);
            }
        });
      //  rSp.SetToken(this, "f98f415c23f3c6a4878fb8ef175a7f1d");
    }

    @Override
    protected void onResume() {
        super.onResume();
        API.getDevices(this, handler);
    }

    @Override
    protected void handlerResponse(Message msg) {
        super.handlerResponse(msg);
        if (response.isValid()) {
            if (response.getModul() == API.mDevices) {
                devices.clear();
                for (Device d : Device.fromJsonArrayList(response.getResponse())) {
                    devices.add(d);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void doLogout(View v) {
        super.doLogout(v);

    }
}
