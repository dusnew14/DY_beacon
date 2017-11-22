package com.example.wustls14.dy_beacon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Modify_Data_Activity extends AppCompatActivity {

    private TextView modify_beaconName;
    private TextView modify_srlNo;
    private Spinner s;

    String former_beaconName;
    String former_srlNo;
    int former_distance;
    String after_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        // 그 전 Activity에서 전달되는 값 받아오기
        Intent intent = getIntent();

        // 수정 전 beaconName 값 셋팅
        former_beaconName = intent.getExtras().getString("beaconName");
        modify_beaconName = (AutoCompleteTextView) findViewById(R.id.modify_name_txt);
        modify_beaconName.setText(former_beaconName);

        // 수정 전 srlNo 셋팅
        modify_srlNo = (EditText) findViewById(R.id.modify_srlNo_txt);
        former_srlNo = intent.getExtras().getString("srlNo");
        modify_srlNo.setText(former_srlNo);

        s = (Spinner)findViewById(R.id.modify_distance_spinner);
        former_distance = intent.getExtras().getInt("distance");
        s.setSelection(former_distance);


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(s.getSelectedItemPosition()>=1){

                    after_distance = s.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void modify_clicked(View view){
        Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
