package com.example.wustls14.dy_beacon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wustls14.dy_beacon.adapter.Find_MyBeacon_Adapter;
import com.example.wustls14.dy_beacon.model.SavedBeacon_Model;
import com.example.wustls14.dy_beacon.reco.RecoActivity;
import com.example.wustls14.dy_beacon.util.DBHelper;
import com.example.wustls14.dy_beacon.util.U;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Find_MyBeacon_Activity extends RecoActivity implements RECORangingListener{

    // DB
    String DATABASE_NAME = "DY_Beacon_DB";
    String TABLE_NAME = "registered_Info_Table";
    public DBHelper dbHelper;
    private SQLiteDatabase db;

    TextView testText;

    // 기본 셋팅
    RecyclerView find_recyclerView;
    Find_MyBeacon_Adapter adapter;
    List<SavedBeacon_Model> savedList;  // DB에 저장된 정보를 담고 있는 리스트
    SavedBeacon_Model item;

    // DB와 reco의 공통된 부분만을 저장할 곳
    List<SavedBeacon_Model> sameResultList;

    // reco
    ArrayList<RECOBeacon> temp_reco;

    // 알람기능을 할 음악 재생 셋팅
    final SoundPool sp = new SoundPool(1,AudioManager.STREAM_MUSIC,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_beacon);

        // 초기화
        find_recyclerView = (RecyclerView) findViewById(R.id.find_recyclerView);
        savedList = new ArrayList<SavedBeacon_Model>();
        testText = (TextView)findViewById(R.id.test_txt);

        sameResultList = new ArrayList<SavedBeacon_Model>();


        initData();
        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);

    }

    // DB 구축 및 View 셋팅 =====================================================================================

    // 1. DB 열기
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


    // reco가 작동되기 전에 DB에 있는 리스트를 저장
    public void loadData(){
        String SQL = "select _id, beaconName, srlNo, distance_position, distance " + " from " + TABLE_NAME;
        // 아답터에 기존 정보를 전달하기 위해 리스트 만듬


        Cursor c1 = db.rawQuery(SQL, null);
        int recordCount = c1.getCount();

        for (int i = 0; i < recordCount; i++) {
            item  = new SavedBeacon_Model();
            c1.moveToNext();
            item.set_id(c1.getString(0));
            item.setBeaconName(c1.getString(1));
            item.setSrlNo(c1.getInt(2));
            item.setDistance(c1.getString(4));
            item.setDistance_number(c1.getShort(3));
            savedList.add(item);
        }
        c1.close();
    }

    // =========================================================================================================

    // DB가 열려있다면 저장된 정보 가져온 후 아답터에 셋팅 -> reco 용으로 만들기
    private void executeRawQueryParam(ArrayList<RECOBeacon> temp_reco) {

        for (int j= 0; j < temp_reco.size(); j++) {
            for (int i = 0; i<savedList.size(); i++) {

                // 시리얼 번호 생성
                int major = temp_reco.get(j).getMajor();
                String numStr1 = String.valueOf(major);
                int minor = temp_reco.get(j).getMinor();
                String numStr2 = String.valueOf(minor);
                String srlNo = major + "0" + minor;
                //int real_srlNo = Integer.parseInt(srlNo);

                int items_value = savedList.get(i).getSrlNo();

                String item_str = String.valueOf(items_value);
                String item_name =savedList.get(i).getBeaconName();



                /**
                 * 반복 셋팅을 막기 위해서 검사
                 * reco는 주기적으로 계속 탐색되므로
                 * 한번 탐색되어 저장된 것이 현재 탐색된 비콘과 같으면 저장하지 못하게 함

                if(!(sameResultList.isEmpty())){
                    for(int k=0; k<sameResultList.size(); k++){

                        String confrim_srlNo = String.valueOf(sameResultList.get(k).getSrlNo());

                        if(!(sameResultList.get(k).getBeaconName().equals(item_name))
                                && !(item_str.equals(sameResultList.get(k).getSrlNo()))){

                            SavedBeacon_Model item2 = new SavedBeacon_Model();
                            item2.setBeaconName(savedList.get(i).getBeaconName());
                            item2.setSrlNo(savedList.get(i).getSrlNo());
                            item2.setDistance(savedList.get(i).getDistance());
                            item2.setDistance_number(savedList.get(i).getDistance_number());
                            sameResultList.add(item2);
                        }
                    }
                }

                 sameResultList.isEmpty() &&
                 */

                if ( item_str.equals(srlNo)) {

                    SavedBeacon_Model item2 = new SavedBeacon_Model();
                    item2.setBeaconName(savedList.get(i).getBeaconName());
                    item2.setSrlNo(savedList.get(i).getSrlNo());
                    item2.setAccuracy(temp_reco.get(j).getAccuracy());
                    item2.setDistance_number(savedList.get(i).getDistance_number());
                    sameResultList.add(item2);
                }
            }
        }
        find_recyclerView.setAdapter(adapter = new Find_MyBeacon_Adapter(sameResultList));
        find_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        find_recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    // reco ========================================================================================


    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
        temp_reco = adapter.updateAllBeacons(recoBeacons);
        adapter.notifyDataSetChanged();
        // did 메소드가 매번 실행될 때마다 아래 메소드가 실행되서 문제임
        executeRawQueryParam(temp_reco);
        if(!(sameResultList.isEmpty())){

        }
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

    // 알람 ========================================================================================

    // 버튼을 누르면 알람이 재생됨 : 테스트용 =============================================================== 삭제
    public void playBtn(View view){
        playAalarm();
    }

    public void playAalarm(){
        int soundID = sp.load(this,R.raw.alarm,1);

        sp.play(soundID,1,1,0,0,0.5f);
    }


}
