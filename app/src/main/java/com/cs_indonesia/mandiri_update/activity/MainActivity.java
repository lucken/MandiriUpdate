package com.cs_indonesia.mandiri_update.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.model.Halte;
import com.cs_indonesia.mandiri_update.model.Koridor;
import com.raafstudio.raaf.rSp;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!rSp.isLogin(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            switch (Integer.parseInt(rSp.getString(this, getString(R.string.loginresult_role_id), "0"))) {
                case 4:
                    startActivity(new Intent(this, KoridorActivity.class));
                    finish();
                    break;
                case 11:
                    startActivity(new Intent(this, DevicesActivity.class));
                    //startActivity(new Intent(this, MapsActivity.class));
                    finish();
                    break;
                default:
                    setContentView(R.layout.activity_main);
                    break;
            }

        }
    }
}
