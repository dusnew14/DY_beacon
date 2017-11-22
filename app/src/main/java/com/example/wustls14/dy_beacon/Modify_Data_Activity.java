package com.example.wustls14.dy_beacon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Modify_Data_Activity extends AppCompatActivity {

    public TextView modify_beaconName;
    public TextView modify_srlNo;

    String former_beaconName;
    String former_srlNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        // 그 전 Activity에서 전달되는 값 받아오기
        Intent intent = getIntent();

        // 수정 전 beaconName 값 셋팅
        former_beaconName = intent.getExtras().getString("beaconName");
        modify_beaconName = (TextView)findViewById(R.id.modify_name_txt);
        modify_beaconName.setText(former_beaconName);

        // 수정 전 srlNo 셋팅
        former_srlNo = intent.getExtras().getString("srlNo");
        modify_srlNo = (TextView)findViewById(R.id.modify_srlNo_txt);
        modify_srlNo.setText(former_srlNo);

    }
}
