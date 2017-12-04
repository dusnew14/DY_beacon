package com.example.wustls14.dy_beacon.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wustls14.dy_beacon.R;
import com.example.wustls14.dy_beacon.model.SavedBeacon_Model;
import com.perples.recosdk.RECOBeacon;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

public class Find_MyBeacon_Adapter extends RecyclerView.Adapter<Find_MyBeacon_Adapter.findViewHolder>{


    public List<SavedBeacon_Model> savedBeacon_modelList;
    public int itemLayout;

    private ArrayList<RECOBeacon> mRangedBeacons;
    private LayoutInflater mLayoutInflater;

    //===================================================================================================

    public Find_MyBeacon_Adapter(Context context) {
        super();
        mRangedBeacons = new ArrayList<RECOBeacon>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public Find_MyBeacon_Adapter(List<SavedBeacon_Model> savedBeacon_modelList, int itemLayout) {
        this.savedBeacon_modelList = savedBeacon_modelList;
        this.itemLayout = itemLayout;
    }


    @Override
    public Find_MyBeacon_Adapter.findViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_beacon_layout, parent,false);
        return new Find_MyBeacon_Adapter.findViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Find_MyBeacon_Adapter.findViewHolder holder, int position) {
        SavedBeacon_Model item = savedBeacon_modelList.get(position);

        holder.find_name.setText("비콘 이름 : " + item.getBeaconName());
        holder.find_srlNo.setText("시리얼 번호 : " + item.getSrlNo());
        holder.find_distance.setText("알람 설정 거리 : " + item.getDistance());
        holder.find_howFar.setText("측정 거리 : " + item.getAccuracy());

        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() { return savedBeacon_modelList.size(); }

    //  ViewHolder ==========================================================================================
    public static class findViewHolder extends RecyclerView.ViewHolder{

        public ImageView find_img;
        public TextView find_name;
        public TextView find_srlNo;
        public TextView find_distance;
        public TextView find_howFar;

        public findViewHolder(View itemView) {
            super(itemView);
            find_img = (ImageView) itemView.findViewById(R.id.find_Img);
            find_name = (TextView) itemView.findViewById(R.id.find_name_txt);
            find_srlNo = (TextView) itemView.findViewById(R.id.find_srlNo_txt);
            find_distance = (TextView) itemView.findViewById(R.id.find_distance_txt);
            find_howFar = (TextView) itemView.findViewById(R.id.find_how_far_txt);

        }
    }

    // RECO =================================================================================================

    public void updateBeacon(RECOBeacon beacon) {
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

    public void clear() {
        mRangedBeacons.clear();
    }

    public int getCount(){return mRangedBeacons.size();}

    public Object getItem(int position){return mRangedBeacons.get(position);}

    @Override
    public long getItemId(int position) { return position; }

}