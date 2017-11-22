package com.example.wustls14.dy_beacon.model;

import java.io.Serializable;

public class SavedBeacon_Model implements Serializable{

    String beaconName;
    int srlNo;
    String distance;
    int distance_number;

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
}
