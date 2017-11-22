package com.example.wustls14.dy_beacon.model;

public class SavedBeacon_Model {

    String beaconName;
    int srlNo;
    String distance;
    boolean isChecked;

    public SavedBeacon_Model() {}

    public SavedBeacon_Model(String beaconName, int srlNo, String distance) {
        this.beaconName = beaconName;
        this.srlNo = srlNo;
        this.distance = distance;
    }

    public SavedBeacon_Model(String beaconName, int srlNo, boolean isChecked) {
        this.beaconName = beaconName;
        this.srlNo = srlNo;
        this.isChecked = isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
