package com.example.wustls14.dy_beacon.reco;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.wustls14.dy_beacon.R;
import com.perples.recosdk.RECOBeacon;
import java.util.ArrayList;
import java.util.Collection;

public class RecoRangingListAdapter extends BaseAdapter {
    private ArrayList<RECOBeacon> mRangedBeacons;
    private LayoutInflater mLayoutInflater;

    public RecoRangingListAdapter(Context context) {
        super();
        mRangedBeacons = new ArrayList<RECOBeacon>();
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void updateBeacon(RECOBeacon beacon) {
        synchronized (mRangedBeacons) {
            if(mRangedBeacons.contains(beacon)) {
                mRangedBeacons.remove(beacon);
            }
            mRangedBeacons.add(beacon);
        }
    }

    public void updateAllBeacons(Collection<RECOBeacon> beacons) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
        }
    }

    public void clear() {
        mRangedBeacons.clear();
    }

    @Override
    public int getCount() {
        return mRangedBeacons.size();
    }

    @Override
    public Object getItem(int position) {
        return mRangedBeacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //==============================================================================
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_ranging_beacon, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.recoProximityUuid = (TextView)convertView.findViewById(R.id.recoProximityUuid);
            viewHolder.recoMajor = (TextView)convertView.findViewById(R.id.recoMajor);
            viewHolder.recoMinor = (TextView)convertView.findViewById(R.id.recoMinor);
            viewHolder.recoTxPower = (TextView)convertView.findViewById(R.id.recoTxPower);
            viewHolder.recoRssi = (TextView)convertView.findViewById(R.id.recoRssi);
            viewHolder.recoBattery = (TextView)convertView.findViewById(R.id.recoBattery);
            viewHolder.recoProximity = (TextView)convertView.findViewById(R.id.recoProximity);
            viewHolder.recoAccuracy = (TextView)convertView.findViewById(R.id.recoAccuracy);
            viewHolder.locationState = (TextView)convertView.findViewById(R.id.locationState);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RECOBeacon recoBeacon = mRangedBeacons.get(position);

        String proximityUuid = recoBeacon.getProximityUuid();


        viewHolder.recoProximityUuid.setText(String.format("비콘의 UUID : " + "%s-%s-%s-%s-%s", proximityUuid.substring(0, 8), proximityUuid.substring(8, 12), proximityUuid.substring(12, 16), proximityUuid.substring(16, 20), proximityUuid.substring(20) ));
        viewHolder.recoMajor.setText(recoBeacon.getMajor() + "");
        viewHolder.recoMinor.setText(recoBeacon.getMinor() + "");
        viewHolder.recoTxPower.setText(recoBeacon.getTxPower() + "");
        viewHolder.recoRssi.setText(recoBeacon.getRssi() + "");
        viewHolder.recoBattery.setText(recoBeacon.getBattery() + "");
        viewHolder.recoProximity.setText(recoBeacon.getProximity() + "");
        viewHolder.recoAccuracy.setText("정확도 : " + String.format("%.2f", recoBeacon.getAccuracy()));
        if(recoBeacon.getAccuracy()>0 && recoBeacon.getAccuracy()<=1){viewHolder.locationState.setText("1미터 이내에 있음");}
        else if(recoBeacon.getAccuracy()<=2){viewHolder.locationState.setText("2미터 이내에 있음");}
        else if(recoBeacon.getAccuracy()<=3){viewHolder.locationState.setText("3미터 이내에 있음");}
        else viewHolder.locationState.setText("3미터 이상 떨어져 있음");
        return convertView;
    }

    static class ViewHolder {
        TextView recoProximityUuid;
        TextView recoMajor;
        TextView recoMinor;
        TextView recoTxPower;
        TextView recoRssi;
        TextView recoBattery;
        TextView recoProximity;
        TextView recoAccuracy;
        TextView locationState;
    }

    public ArrayList<RECOBeacon> needBeacon(){
        return mRangedBeacons;
    }

}
