package com.cs_indonesia.mandiri_update.activity;

import android.content.Intent;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.helper.API;
import com.raafstudio.raaf.rDialog;
import com.raafstudio.raaf.rSp;
import com.raafstudio.raaf.rUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    TextInputLayout TiUserName, TiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TiUserName = (TextInputLayout) findViewById(R.id.TiUsername);
        TiPassword = (TextInputLayout) findViewById(R.id.TiPassword);
        ((TextView)findViewById(R.id.TvAppName)).setText(getString(R.string.app_name) + " v:" + rUtil.getAppVersions(this));
    }

    public void doLogin(View v) {
        if (TiUserName.getEditText().getText().toString().length() > 0 && TiPassword.getEditText().getText().toString().length() > 0) {
            if (API.doLogin(this, TiUserName.getEditText().getText().toString(), TiPassword.getEditText().getText().toString(), handler)) {
                rDialog.ShowProgressDialog(this, "Login to server", "please wait ...", false);
            }
        } else
            rDialog.ShowDialog(this, "Login failed", "Incorrect username or password", "OK");


    }

    @Override
    protected void handlerResponse(Message msg) {
        super.handlerResponse(msg);
        if (response.isValid()) {
            if (response.getModul() == API.mLogin) {
                rSp.SetLogin(this, true);
                rSp.SetToken(this, response.getToken());
                try {
                    JSONObject j = new JSONObject(response.getResponse());
                    rSp.setParamString(this, getString(R.string.loginresult_id), j.getString(getString(R.string.loginresult_id)));
                    rSp.setParamString(this, getString(R.string.loginresult_role_id), j.getString(getString(R.string.loginresult_role_id)));
                    rSp.setParamString(this, getString(R.string.loginresult_role_name), j.getString(getString(R.string.loginresult_role_name)));
                    rSp.setParamString(this, getString(R.string.loginresult_owner_id), j.getString(getString(R.string.loginresult_owner_id)));
                    rSp.setParamString(this, getString(R.string.loginresult_username), j.getString(getString(R.string.loginresult_username)));
                    rSp.setParamString(this, getString(R.string.loginresult_idoperator), j.getString(getString(R.string.loginresult_idoperator)));
                    rSp.setParamString(this, getString(R.string.loginresult_nama_lengkap), j.getString(getString(R.string.loginresult_nama_lengkap)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
        rDialog.CloseProgressDialog();
    }
}
