package com.example.wustls14.dy_beacon.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.RemoteException;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.wustls14.dy_beacon.R;
import com.example.wustls14.dy_beacon.adapter.Find_MyBeacon_Adapter;
import com.example.wustls14.dy_beacon.model.AlarmModel;
import com.example.wustls14.dy_beacon.model.SavedBeacon_Model;
import com.example.wustls14.dy_beacon.reco.RecoActivity;
import com.example.wustls14.dy_beacon.util.DBHelper;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Find_MyBeacon_Activity extends RecoActivity implements RECORangingListener{

    RecyclerView find_recyclerView;
    Find_MyBeacon_Adapter adapter;
    ArrayList<RECOBeacon> temp_reco;

    // DB
    String DATABASE_NAME = "DY_Beacon_DB";
    String TABLE_NAME = "registered_Info_Table";
    public DBHelper dbHelper;
    private SQLiteDatabase db;

    // 알람기능을 할 음악 재생 셋팅
    final SoundPool sp = new SoundPool(1,AudioManager.STREAM_MUSIC,0);

    // 임시
    List<SavedBeacon_Model> result1;

    // 알람을 위해 정확도를 넣는 리스트
    List<AlarmModel> accuracy_list = new ArrayList<AlarmModel>();
    // 알람을 위한 임시 리스트
    List<AlarmModel> temp_accuracy;
    boolean alarm;

    // 수정할 것=================================================================================================
    SavedBeacon_Model item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_beacon);

        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);
        initLayout();
    }

    private void initLayout(){find_recyclerView = (RecyclerView) findViewById(R.id.find_recyclerView);}

    private List<AlarmModel> initData(ArrayList<RECOBeacon> temp_reco){

        // 1. 데이터 셋팅
        initData();
        // 2. 공통된 데이터를 저장해줄 곳 선언
        List<SavedBeacon_Model> beaconDataList = new ArrayList<SavedBeacon_Model>();

        // 알람을 위한 선언
        AlarmModel alarmModel;

        for (int i=0; i<result1.size(); i++){
            for (int j=0; j<temp_reco.size(); j++){

                int major = temp_reco.get(j).getMajor();
                String numStr1 = String.valueOf(major);
                int minor = temp_reco.get(j).getMinor();
                String numStr2 = String.valueOf(minor);
                String srlNo = major + "0" + minor;

                String temp_realSrlNO = String.valueOf(result1.get(i).getSrlNo());

                double temp_accuracy = Double.parseDouble(String.format("%.3f", temp_reco.get(j).getAccuracy()));

                if(temp_realSrlNO.equals(srlNo)){

                    SavedBeacon_Model beaconData = new SavedBeacon_Model();
                    beaconData.setBeaconName(result1.get(i).getBeaconName());
                    beaconData.setSrlNo(result1.get(i).getSrlNo());
                    beaconData.setDistance(result1.get(i).getDistance());
                    beaconData.setAccuracy(temp_accuracy);
                    beaconDataList.add(beaconData);

                    if(temp_reco.get(j).getAccuracy() - result1.get(i).getDistance_double() > 0){
                        alarmModel = new AlarmModel(true);
                    }
                    else {
                        alarmModel = new AlarmModel(false);
                    }

                    //알람 기능을 위한 데이터 저장
                    if(accuracy_list.size()<7){
                        accuracy_list.add(alarmModel);
                    }
                    else{
                        accuracy_list.remove(0);
                        accuracy_list.add(alarmModel);
                    }

                }
            }
        }

        find_recyclerView.setAdapter(new Find_MyBeacon_Adapter(beaconDataList, R.layout.item_find_beacon_layout));
        find_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        find_recyclerView.setItemAnimator(new DefaultItemAnimator());

        return accuracy_list;
    }

    // DB 구축 및 View 셋팅 ============================================================================

    // 1.DB 열기
    private boolean openDatabase() {
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. DB가 열려있는지 확인
    public void initData() {
        adapter = new Find_MyBeacon_Adapter(this);
        boolean isOpen = openDatabase();
        // 3. 열려있다면 실행
        if (isOpen) {
            loadData();
        }
    }

    // 4. reco가 작동되기 전에 DB에 있는 리스트를 저장
    public void loadData(){

        result1 = new ArrayList<SavedBeacon_Model>();  // DB에 저장된 정보를 담고 있는 리스트

        // beaconName, srlNo, distance_position, distance_int, distance)

        String SQL = "select _id, beaconName, srlNo, distance_position, distance_double, distance " + " from " + TABLE_NAME;
        // 아답터에 기존 정보를 전달하기 위해 리스트 만듬


        Cursor c1 = db.rawQuery(SQL, null);
        int recordCount = c1.getCount();

        for (int i = 0; i < recordCount; i++) {
            item  = new SavedBeacon_Model();
            c1.moveToNext();
            item.set_id(c1.getString(0));
            item.setBeaconName(c1.getString(1));
            item.setSrlNo(c1.getInt(2));
            item.setDistance_number(c1.getShort(3));
            item.setDistance_double(c1.getDouble(4));
            item.setDistance(c1.getString(5));

            result1.add(item);
        }
        c1.close();
    }

    // reco ========================================================================================
    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
        temp_reco = adapter.updateAllBeacons(recoBeacons);
        adapter.notifyDataSetChanged();
        temp_accuracy = initData(temp_reco);

       // 알람 리스트에 최소 데이터가 5개 이상 있을 경우 알람 작동하도록 함
        if(temp_accuracy.size()>5){
            check(temp_accuracy);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new Find_MyBeacon_Adapter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stop(mRegions);
        this.unbind();
    }

    private void unbind() {
        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.i("RECORangingActivity", "Remote Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnect() {
        Log.i("RECORangingActivity", "onServiceConnect()");
        mRecoManager.setDiscontinuousScan(MainActivity.DISCONTINUOUS_SCAN);
        this.start(mRegions);
    }

    @Override
    protected void start(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onServiceFail(RECOErrorCode errorCode) {
        return;
    }
    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {return;}


    // 알람 =======================================================================================

    public void playAlarm(){
        int soundID = sp.load(this,R.raw.alarm,1);
        sp.play(soundID,1,1,0,0,0.5f);
    }


    public void check(List<AlarmModel> temp_accuracy){

        for (int i=1; i<temp_accuracy.size(); i++) {

            if (temp_accuracy.get(i - 1).isAlarmtest() != temp_accuracy.get(i).isAlarmtest()) {
                playAlarm();
                i++;
            }
        }
    }







}
