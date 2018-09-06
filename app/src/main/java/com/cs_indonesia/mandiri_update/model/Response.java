package com.cs_indonesia.mandiri_update.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luqman Hakim on 07-Sep-15.
 */
public class Response {
    boolean valid;
    int errorCode = -1, modul;
    String errorMessage = "";
    String response="";
    JSONObject json;
    Object result;
    JSONArray jar;
    String token;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValid() {
        return errorCode == 0;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getModul() {
        return modul;
    }

    public void setModul(int modul) {
        this.modul = modul;
    }

    public JSONArray getJar() {
        return jar;
    }

    public void setJar(JSONArray jar) {
        this.jar = jar;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

//    public JSONObject getJson() {
//        return json;
//    }
//
//    public void setJson(JSONObject json) {
//        this.json = json;
//    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public JSONObject getJson() {
        try {
            return new JSONObject(getResponse());
        } catch (JSONException e) {
            return  null;
        }
    }
}
