package com.cs_indonesia.mandiri_update.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Koridor {
    String koridor;

    public String getKoridor() {
        return koridor;
    }


    public String toJson() {
        return new Gson().toJson(this);
    }

    public static String toJsons(ArrayList<Koridor> jsons) {
        return new Gson().toJson(jsons);
    }

    public static ArrayList<Koridor> fromJsonArrayList(String json) {
        Type tt = new TypeToken<ArrayList<Koridor>>() {
        }.getType();
        return new Gson().fromJson(json, tt);
    }

    public static Koridor fromJson(String json) {
        return new Gson().fromJson(json, Koridor.class);
    }

    public void setKoridor(String koridor) {
        this.koridor = koridor;
    }
}
