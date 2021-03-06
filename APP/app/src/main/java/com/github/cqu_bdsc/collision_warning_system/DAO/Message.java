package com.github.cqu_bdsc.collision_warning_system.DAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Message implements Serializable {
    public static final String TYPE_MESSAGE = "TYPE_MESSAGE";
    public static final String TYPE_TIME_SYNC_MESSAGE = "TYPE_TIME_SYNC_MESSAGE";
    public static final String TYPE_WIFI_TIME_SYNC_RESULT = "TYPE_WIFI_TIME_SYNC_RESULT";
    public static final String TYPE_WIFI_MESSAGE = "TYPE_WIFI_MESSAGE";
    public static int ERROR_VALUE = -666;
    private int id;
    private long timeStamp;
    private float speed;
    private float direction;
    private double lat;
    private double lon;
    private double ace;
    private String mac;
    private String type;

    //新增
    private long RSUreceivetime;

   public   Message(){
        type = "-666";
        id = ERROR_VALUE;
        timeStamp = ERROR_VALUE;
        speed = ERROR_VALUE;
        direction = ERROR_VALUE;
        lat = ERROR_VALUE;
        lon = ERROR_VALUE;
        ace = ERROR_VALUE;
        mac = "-666";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getDirection() {
        return direction;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setAce(double ace) {
        this.ace = ace;
    }

    public double getAce() {
        return ace;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public JSONObject toJSON(){

        try {
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", "1");
            jsonObject.put("id", String.valueOf(getId()));

            jsonObject.put("type", getType());
            jsonObject.put("timeStamp",String.valueOf(getTimeStamp()));
//            jsonObject.put("speed","0");
//            jsonObject.put("direction","0");
//            jsonObject.put("lat","0");
//            jsonObject.put("lon","0");
//            jsonObject.put("acc","0");
//            jsonObject.put("adddata","");
            jsonObject.put("speed",String.valueOf(getSpeed()));
            jsonObject.put("direction",String.valueOf(getDirection()));
            jsonObject.put("lat",String.valueOf(getLat()));
            jsonObject.put("lon",String.valueOf(getLon()));
            jsonObject.put("acc",String.valueOf(getAce()));
//            jsonObject.put("adddata","adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl\n" +
//                    "adfadsfsdfasdfasdfsdafsadfsdfsdfsdfasdfkljkjdjkljskcjkljsfkljsjkflajl");
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
