package com.cs_indonesia.mandiri_update.helper;

import android.content.Context;
import android.os.Handler;

import com.raafstudio.raaf.rMap;

/**
 * Created by luqman on 5/27/2016.
 */
public class so {
    public  static   boolean cekJob = false;
    public static final String PREF_GCM_REG_ID = "PREF_GCM_REG_ID";
    public static final String PREF_APP_VERSION = "PREF_APP_VERSION";
    public static final String PUSH_NOTIFICATION = "pushnotification";
    public static final String GCM_SENDER_ID = "435417105852";
    public static final String URL_API = "http://trans.cs-indonesia.com/";
    public static final String APP_KEY = "63522a7e8b648f9da174ae21a8d8f8a7";
    public static final int PERMISSIONS_CAM = 123, PERMISSIONS_WRITE = 234, PERMISSIONS_LOCATION = 345;
    public static final String[] perms_location = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    public static final String[] perms_write = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    public static final String[] perms_camera = {"android.permission.CAMERA"};
    public static final int GPLUS_RESULT = 9001;
   // public  static Location roadLocation;
    public static rMap map;
    public  static  Context current_context;

    public static rMap getMap(Context c, Handler h) {
        if (map == null)
            map = new rMap(c, h);
        return map;
    }

}
