package com.example.wustls14.dy_beacon.model;

import java.io.Serializable;

public class SavedBeacon_Model implements Serializable{

    String _id;
    String beaconName;
    int srlNo;
    String distance;
    double distance_double;
    int distance_number;
    double accuracy;

    public SavedBeacon_Model(String _id, String beaconName, int srlNo, String distance, int distance_number, double accuracy) {
        this._id = _id;
        this.beaconName = beaconName;
        this.srlNo = srlNo;
        this.distance = distance;
        this.distance_number = distance_number;
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public SavedBeacon_Model() {}

    public SavedBeacon_Model(String beaconName, int srlNo, String distance) {
        this.beaconName = beaconName;
        this.srlNo = srlNo;
        this.distance = distance;
    }


    public SavedBeacon_Model(String beaconName, int srlNo, String distance, int distance_number) {
        this.beaconName = beaconName;
        this.srlNo = srlNo;
        this.distance = distance;
        this.distance_number = distance_number;
    }

    public SavedBeacon_Model(String beaconName, int srlNo, String distance, double distance_double, int distance_number, double accuracy) {
        this.beaconName = beaconName;
        this.srlNo = srlNo;
        this.distance = distance;
        this.distance_double = distance_double;
        this.distance_number = distance_number;
        this.accuracy = accuracy;
    }

    public SavedBeacon_Model(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public int getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(int srlNo) {
        this.srlNo = srlNo;
    }

    public String getDistance() {return distance;}

    public void setDistance(String distance) {this.distance = distance;}

    public int getDistance_number() {
        return distance_number;
    }

    public void setDistance_number(int distance_number) {
        this.distance_number = distance_number;
    }

    public double getDistance_double() {
        return distance_double;
    }

    public void setDistance_double(double distance_double) {
        this.distance_double = distance_double;
    }
}
