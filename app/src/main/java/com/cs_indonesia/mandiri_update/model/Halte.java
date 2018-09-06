package com.cs_indonesia.mandiri_update.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Halte {
    String nm_halte;
    int jumlah_gate=0;
    double lat, lng,status=0;


    public String toJson() {
        return new Gson().toJson(this);
    }

    public static String toJsons(ArrayList<Halte> jsons) {
        return new Gson().toJson(jsons);
    }

    public static ArrayList<Halte> fromJsonArrayList(String json) {
        Type tt = new TypeToken<ArrayList<Halte>>() {
        }.getType();
        return new Gson().fromJson(json, tt);
    }

    public static Halte fromJson(String json) {
        return new Gson().fromJson(json, Halte.class);
    }

    public String getNm_halte() {
        return nm_halte;
    }

    public void setNm_halte(String nm_halte) {
        this.nm_halte = nm_halte;
    }

    public int getJumlah_gate() {
        return jumlah_gate;
    }

    public void setJumlah_gate(int jumlah_gate) {
        this.jumlah_gate = jumlah_gate;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
