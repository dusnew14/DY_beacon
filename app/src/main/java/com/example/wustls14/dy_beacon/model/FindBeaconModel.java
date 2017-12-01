package com.example.wustls14.dy_beacon.model;

// 탐색된 비콘의 정보를 저장하기 위한 그릇

public class FindBeaconModel {

    String BeaconName;
    int srlNo;
    String distance;
    double accuaracy;

    public FindBeaconModel(String beaconName, int srlNo, String distance, double accuaracy) {
        BeaconName = beaconName;
        this.srlNo = srlNo;
        this.distance = distance;
        this.accuaracy = accuaracy;
    }

    public String getBeaconName() {
        return BeaconName;
    }

    public void setBeaconName(String beaconName) {
        BeaconName = beaconName;
    }

    public int getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(int srlNo) {
        this.srlNo = srlNo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getAccuaracy() {
        return accuaracy;
    }

    public void setAccuaracy(double accuaracy) {
        this.accuaracy = accuaracy;
    }
}
