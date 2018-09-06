package com.cs_indonesia.mandiri_update.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.model.Halte;
import com.raafstudio.raaf.rSp;

public class KoridorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koridor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.TvName)).setText(rSp.getString(this,getString(R.string.loginresult_nama_lengkap),""));
    }


    public void doListHalte(View v) {
        startActivity(new Intent(this, HalteActivity.class));
    }


    public void doViewMap(View v) {
        startActivity(new Intent(this, MapsActivity.class));

    }

    @Override
    public void doLogout(View v) {
        super.doLogout(v);

    }
}
