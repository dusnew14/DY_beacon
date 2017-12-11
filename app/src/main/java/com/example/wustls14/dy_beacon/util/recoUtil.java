package com.example.wustls14.dy_beacon.util;

// 아답터 없이 reco의 일부 기능을 사용하기 위해서 Singleton으로 만듬

import com.perples.recosdk.RECOBeacon;

import java.util.ArrayList;
import java.util.Collection;

public class recoUtil {

    ArrayList<RECOBeacon> mRangedBeacons;

    private static final recoUtil ourInstance = new recoUtil();

    public static recoUtil getInstance() {
        return ourInstance;
    }

    private recoUtil() {}

    public void updateBeacon(RECOBeacon beacon) {
        mRangedBeacons = new ArrayList<RECOBeacon>();
        synchronized (mRangedBeacons) {
            if(mRangedBeacons.contains(beacon))
            { mRangedBeacons.remove(beacon); }
            mRangedBeacons.add(beacon);
        }
    }

    public ArrayList<RECOBeacon> updateAllBeacons(Collection<RECOBeacon> beacons) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
        }
        return mRangedBeacons;
    }

    public void clear() {mRangedBeacons.clear();}

    public int getCount(){return mRangedBeacons.size();}

    public Object getItem(int position){return mRangedBeacons.get(position);}

}
