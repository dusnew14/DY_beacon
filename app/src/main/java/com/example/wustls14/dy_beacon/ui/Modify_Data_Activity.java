package com.example.wustls14.dy_beacon.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wustls14.dy_beacon.R;
import com.example.wustls14.dy_beacon.ui.Saved_Beacons_Activity;
import com.example.wustls14.dy_beacon.util.DBHelper;

public class Modify_Data_Activity extends AppCompatActivity {

    // UI
    private TextView modify_beaconNameView;
    private TextView modify_srlNoView;
    private Spinner s;

    // 기존에 저장 된 값
    String former_beaconName;
    String former_srlNo;
    int former_distance;

    // 이후에 저장된 값
    String after_distance;
    double after_ditance_double;
    int after_distance_position;

    // DB 관련 정보
    private static String DATABASE_NAME = "DY_Beacon_DB";
    private static String TABLE_NAME = "registered_Info_Table";
    private static int DATABASE_VERSION = 1;
    DBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        // UI 초기화
        modify_beaconNameView = (AutoCompleteTextView) findViewById(R.id.modify_name_txt);
        modify_srlNoView = (EditText) findViewById(R.id.modify_srlNo_txt);


        // 그 전 Activity에서 전달되는 값 받아오기
        Intent intent = getIntent();

        // 수정 전 beaconName 값 셋팅
        former_beaconName = intent.getExtras().getString("beaconName");
        modify_beaconNameView.setText(former_beaconName);

        // 수정 전 srlNo 셋팅
        former_srlNo = intent.getExtras().getString("srlNo");
        modify_srlNoView.setText(former_srlNo);


        // 수정 전 스피너 값 셋팅
        former_distance = intent.getExtras().getInt("distance");

        // 스피너 선택시 값 적용해주기
        s = (Spinner)findViewById(R.id.modify_distance_spinner);
        s.setSelection(former_distance);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(s.getSelectedItemPosition()>=1){

                    after_distance = s.getSelectedItem().toString();
                    after_ditance_double = Double.parseDouble(after_distance);
                    after_distance_position = s.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    public void modify_clicked(View view){
        modifyDB();
    }

    // === DB 수정 =================================================================================

    // 로그인 작동함
    private void modifyDB(){
        boolean isOpen = openDatabase();
        if (isOpen) {
            String strSQL = "UPDATE "+ TABLE_NAME+ " SET beaconName = '"+ modify_beaconNameView.getText().toString()+"', srlNo = " + modify_srlNoView.getText().toString() +", distance_position = "+ after_distance_position + ", distance_double = " +after_ditance_double+", distance = '" + after_distance +"' WHERE srlNo = "+ former_srlNo;
            db.execSQL(strSQL);
            }
        Toast.makeText(this, "비콘 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, Saved_Beacons_Activity.class));
        }

    private boolean openDatabase() {
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        return true;
    }

    //=========================================================================================================


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
