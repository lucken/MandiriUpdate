package com.cs_indonesia.mandiri_update.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cs_indonesia.mandiri_update.helper.API;
import com.cs_indonesia.mandiri_update.model.Response;
import com.raafstudio.raaf.rDialog;
import com.raafstudio.raaf.rSp;

/**
 * Created by lucke on 21-Jan-18.
 */

public class BaseActivity extends AppCompatActivity {
    protected Response response;
    protected final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            handlerResponse(msg);
        }
    };

    protected void handlerResponse(Message msg) {
        response = API.parse(msg);
        if (!response.isValid())
            if(!this.isFinishing())
            {
                rDialog.SetToast(this, response.getErrorMessage());    //show dialog
            }

    }

    public void doLogout(View v){
        rSp.SetLogin(this,false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
