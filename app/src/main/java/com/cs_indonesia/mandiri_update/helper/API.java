package com.cs_indonesia.mandiri_update.helper;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.model.Response;
import com.raafstudio.raaf.http.rLoader;
import com.raafstudio.raaf.io.rFile;
import com.raafstudio.raaf.rDialog;
import com.raafstudio.raaf.rNet;
import com.raafstudio.raaf.rSp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by luqman on 5/27/2016.
 */
public class API {
    public final static int mLogin = 1;
    public final static int mLogout = 2;

    public final static int mRegister = 3;
    public final static int mForgotPassword = 4;
    public final static int mChangeEmail = 5;
    public final static int mChangePassword = 6;

    public final static int mListTrayek = 11;
    public final static int mListDevice = 12;
    public final static int mDeviceInfo = 13;
    public final static int mLiveTrack = 15;
    public final static int mLivePos = 16;
    public final static int mDevices = 17;
    public final static int mDevice = 18;

    public final static int mListKoridor = 21;
    public final static int mListHalte = 22;
    public final static int mListGate = 23;
    public final static int mGateStart = 24;
    public final static int mGateStop = 25;


    static ContentValues params;


    public static boolean isOnline(Context ctx) {
        if (!rNet.isNetworkConnected(ctx)) {
            rDialog.SetToast(ctx, ctx.getString(R.string.internet_connection_required));
            return false;
        } else
            return true;
    }

    public static boolean doLogin(Context ctx, String username, String password, Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API + "apilogin";
        params = new ContentValues();
        Bundle bundle = new Bundle();

        params.put("appId", so.APP_KEY);
        params.put("username", username);
        params.put("password", password);
        bundle.putInt("modul", mLogin);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }

    public static boolean getKoridorList(Context ctx, String idoperator, Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API + "api/" + rSp.GetToken(ctx) + "/general/koridorlist";
        params = new ContentValues();
        Bundle bundle = new Bundle();
        params.put("idoperator", idoperator);
        bundle.putInt("modul", mListKoridor);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }

    public static boolean getHalteList(Context ctx, String idoperator, String idkoridor, Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API + "api/" + rSp.GetToken(ctx) + "/general/haltelist";
        params = new ContentValues();
        Bundle bundle = new Bundle();
        params.put("idoperator", idoperator);
        params.put("idkoridor", idkoridor);
        bundle.putInt("modul", mListHalte);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }

    public static boolean getGateList(Context ctx, String idoperator, String nmhalte, Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API + "api/" + rSp.GetToken(ctx) + "/general/gatelist";
        params = new ContentValues();
        Bundle bundle = new Bundle();
        params.put("idoperator", idoperator);
        params.put("nmhalte", nmhalte);
        bundle.putInt("modul", mListGate);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }

    public static boolean doStartGate(Context ctx, String gateid,int processtatus,String troublenote,Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API + "api/" + rSp.GetToken(ctx) + "/general/gateupdate";
        params = new ContentValues();
        Bundle bundle = new Bundle();
        params.put("gateid", gateid);
        params.put("trxlatpos", util.getLocation(ctx).getLatitude()+"");
        params.put("trxlongpos", util.getLocation(ctx).getLongitude()+"");
        params.put("processtatus", processtatus+"");
        params.put("troublenote", troublenote);
        bundle.putInt("modul", mGateStart);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }
    public static boolean doStopGate(Context ctx, String gateid, int processtatus, String troublenote, String samuid, List<rFile> files ,Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API + "api/" + rSp.GetToken(ctx) + "/general/gateupdate";
        params = new ContentValues();
        Bundle bundle = new Bundle();
        params.put("gateid", gateid);
        params.put("trxlatpos", util.getLocation(ctx).getLatitude()+"");
        params.put("trxlongpos", util.getLocation(ctx).getLongitude()+"");
        params.put("processtatus", processtatus+"");
        params.put("troublenote", troublenote);
        params.put("samuid", samuid);
        bundle.putInt("modul", mGateStop);
        //rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.multipart, params, files);
        loader.start();
        return true;
    }

    public static void getTrayekList(Context ctx, String idoperator, Handler callback) {
        String url = so.URL_API;
        params = new ContentValues();
        Bundle bundle = new Bundle();
        url += "api/" + rSp.GetToken(ctx) + "/trayek/list";
        params.put("idoperator", idoperator);
        bundle.putInt("modul", mListTrayek);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
    }

    public static void getDeviceList(Context ctx, String idtrayek, Handler callback)

    {
        String url = so.URL_API;
        params = new ContentValues();
        Bundle bundle = new Bundle();
        url += "api/" + rSp.GetToken(ctx) + "/device/list";
        params.put("idtrayek", idtrayek);
        bundle.putInt("modul", mListDevice);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
    }

    public static void getDevices(Context ctx, Handler callback)

    {
        String url = so.URL_API;

        Bundle bundle = new Bundle();
        url += "monitoring/" + rSp.GetToken(ctx) + "/devices";
        bundle.putInt("modul", mDevices);
        rLoader loader = new rLoader(url, callback, bundle);
        loader.start();
    }

    public static boolean getDevice(Context ctx, String devicemac, Handler callback)

    {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API;
        params = new ContentValues();
        Bundle bundle = new Bundle();
        url += "monitoring/" + rSp.GetToken(ctx) + "/device";
        params.put("devicemac", devicemac);
        bundle.putInt("modul", mDevice);
        bundle.putString("devicemac", devicemac);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }


    public static void getDeviceInfo(Context ctx, String devicemac, Handler callback)

    {
        String url = so.URL_API;
        params = new ContentValues();
        Bundle bundle = new Bundle();
        url += "api/" + rSp.GetToken(ctx) + "/device/getconfig";
        params.put("devicemac", devicemac);
        bundle.putInt("modul", mDeviceInfo);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
    }

    public static boolean getLiveTrack(Context ctx, String devicemac, String tgltrx, Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API;
        params = new ContentValues();
        Bundle bundle = new Bundle();
        url += "api/" + rSp.GetToken(ctx) + "/device/getlivetrack";
        params.put("devicemac", devicemac);
        params.put("tgltrx", tgltrx);
        bundle.putInt("modul", mLiveTrack);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }

    public static boolean getLivePos(Context ctx, String devicemac, Handler callback) {
        if (!isOnline(ctx))
            return false;
        String url = so.URL_API;
        params = new ContentValues();
        Bundle bundle = new Bundle();
        url += "api/" + rSp.GetToken(ctx) + "/device/getlastpos";
        params.put("devicemac", devicemac);
        bundle.putInt("modul", mLivePos);
        rLoader loader = new rLoader(url, callback, bundle, rLoader.Http_type.post, params, "");
        loader.start();
        return true;
    }


    public static Response parse(Message msg) {
        Response retval = new Response();
        String result = msg.getData().getString("message");
        try {

            if (result.contains("/div>")) {
                String[] data = result.split("/div>");
                result = data[data.length - 1];
            }
            int modul = msg.getData().getInt("modul");

            retval.setModul(modul);
            retval.setResult(result);
            JSONObject j = new JSONObject(result);
            retval.setResult(j);
            retval.setErrorCode(j.getInt("errorcode"));
            retval.setErrorMessage(j.getString("errormessage"));
            if (!j.isNull("data"))
            retval.setResponse(j.getString("data"));
            if (!j.isNull("token"))
                retval.setToken(j.getString("token"));
//            if (retval.isValid()) {
//                if (!j.isNull("response"))
//                    retval.setJson(j.getJSONObject("response"));
//
//            }
        } catch (JSONException e) {
            e.printStackTrace();
            retval.setErrorCode(-1);
            //retval.setMessage(e.getMessage());
            retval.setErrorMessage(result);
        }
        return retval;
    }
}