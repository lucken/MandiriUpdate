package com.cs_indonesia.mandiri_update.model;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Device {
    String devicemac, plat_nomor, nama_koridor, berangkat, tujuan, speed, trxtime, no_body, nama_pemilik, hp_pemilik, nama_sopir, hp_sopir, hp_device, lat_terakhir, lng_terakhir;
    int sehat, jumlah_penumpang;
    boolean onload = false;
    Marker mAkhir;

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static String toJsons(ArrayList<Device> jsons) {
        return new Gson().toJson(jsons);
    }

    public static ArrayList<Device> fromJsonArrayList(String json) {
        Type tt = new TypeToken<ArrayList<Device>>() {
        }.getType();
        return new Gson().fromJson(json, tt);
    }

    public static Device fromJson(String json) {
        return new Gson().fromJson(json, Device.class);
    }

    public String getDevicemac() {
        return devicemac;
    }

    public void setDevicemac(String devicemac) {
        this.devicemac = devicemac;
    }

    public String getPlat_nomor() {
        return plat_nomor;
    }

    public void setPlat_nomor(String plat_nomor) {
        this.plat_nomor = plat_nomor;
    }

    public String getNama_koridor() {
        return nama_koridor;
    }

    public void setNama_koridor(String nama_koridor) {
        this.nama_koridor = nama_koridor;
    }

    public String getBerangkat() {
        return berangkat;
    }

    public void setBerangkat(String berangkat) {
        this.berangkat = berangkat;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTrxtime() {
        return trxtime;
    }

    public void setTrxtime(String trxtime) {
        this.trxtime = trxtime;
    }

    public String getNo_body() {
        return no_body;
    }

    public void setNo_body(String no_body) {
        this.no_body = no_body;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getHp_pemilik() {
        return hp_pemilik;
    }

    public void setHp_pemilik(String hp_pemilik) {
        this.hp_pemilik = hp_pemilik;
    }

    public String getNama_sopir() {
        return nama_sopir;
    }

    public void setNama_sopir(String nama_sopir) {
        this.nama_sopir = nama_sopir;
    }

    public String getHp_sopir() {
        return hp_sopir;
    }

    public void setHp_sopir(String hp_sopir) {
        this.hp_sopir = hp_sopir;
    }

    public String getHp_device() {
        return hp_device;
    }

    public void setHp_device(String hp_device) {
        this.hp_device = hp_device;
    }

    public String getLat_terakhir() {
        return lat_terakhir;
    }

    public void setLat_terakhir(String lat_terakhir) {
        this.lat_terakhir = lat_terakhir;
    }

    public String getLng_terakhir() {
        return lng_terakhir;
    }

    public void setLng_terakhir(String lng_terakhir) {
        this.lng_terakhir = lng_terakhir;
    }

    public int getSehat() {
        return sehat;
    }

    public void setSehat(int sehat) {
        this.sehat = sehat;
    }

    public int getJumlah_penumpang() {
        return jumlah_penumpang;
    }

    public void setJumlah_penumpang(int jumlah_penumpang) {
        this.jumlah_penumpang = jumlah_penumpang;
    }

    public boolean isOnload() {
        return onload;
    }

    public void setOnload(boolean onload) {
        this.onload = onload;
    }

    public Marker getmAkhir() {
        return mAkhir;
    }

    public void setmAkhir(Marker mAkhir) {
        this.mAkhir = mAkhir;
    }
}
