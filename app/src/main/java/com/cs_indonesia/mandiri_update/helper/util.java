package com.cs_indonesia.mandiri_update.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;

import com.raafstudio.raaf.rMap;
import com.raafstudio.raaf.rSp;

public class util {
    public static Bitmap resizeMapIcons(Bitmap bmp, int width, int height) {

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, width, height, false);
        return resizedBitmap;
    }

    public static Bitmap getBipmapFromDrawable(Context ctx, String iconName){
        return  BitmapFactory.decodeResource(ctx.getResources(), ctx.getResources().getIdentifier(iconName, "drawable", ctx.getPackageName()));
    }

    public static rMap rmap=null;
    static Location location;
    public static void initMap(Activity act, Handler handler) {
        if (rmap != null) {
            rmap.destroy();
            rmap= null;
        }
        rmap= new rMap(act, handler);
        rmap.RequestLocation();
        location = rmap.getCurrentLocation();
        if (location == null)
            setLocation(act, rmap.getLastKnownLocation());
    }
    public static Location getLocation(Context ctx) {
        if (location == null) {
            location = new Location(rSp.getString(ctx, "provider", ""));// so.getSp(ctx).getString("provider", ""));
            String slong = rSp.getString(ctx, "long", "106.0");
            String slat = rSp.getString(ctx, "lat", "-6.0");
            location.setLatitude(Double.parseDouble(slat));
            location.setLongitude(Double.parseDouble(slong));
        }
        return location;
    }

    public static void setLocation(Context ctx, Location loc1) {
        if (loc1 != null) {
            rSp.setParamString(ctx, "provider", loc1.getProvider());
            rSp.setParamString(ctx, "long", loc1.getLongitude() + "");
            rSp.setParamString(ctx, "lat", loc1.getLatitude() + "");
            location = loc1;
        }
    }
}
