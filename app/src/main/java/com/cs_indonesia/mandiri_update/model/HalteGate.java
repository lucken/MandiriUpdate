package com.cs_indonesia.mandiri_update.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HalteGate {
    int id, st_update;
    float lat, lng;
    String nm_gate, merek, gate_type, gate_info, alamat, update_time;


    public String toJson() {
        return new Gson().toJson(this);
    }

    public static String toJsons(ArrayList<HalteGate> jsons) {
        return new Gson().toJson(jsons);
    }

    public static ArrayList<HalteGate> fromJsonArrayList(String json) {
        Type tt = new TypeToken<ArrayList<HalteGate>>() {
        }.getType();
        return new Gson().fromJson(json, tt);
    }

    public static HalteGate fromJson(String json) {
        return new Gson().fromJson(json, HalteGate.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSt_update() {
        return st_update;
    }

    public void setSt_update(int st_update) {
        this.st_update = st_update;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getNm_gate() {
        return nm_gate;
    }

    public void setNm_gate(String nm_gate) {
        this.nm_gate = nm_gate;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getGate_type() {
        return gate_type;
    }

    public void setGate_type(String gate_type) {
        this.gate_type = gate_type;
    }

    public String getGate_info() {
        return gate_info;
    }

    public void setGate_info(String gate_info) {
        this.gate_info = gate_info;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
