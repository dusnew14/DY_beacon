package com.example.wustls14.dy_beacon;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wustls14.dy_beacon.model.SavedBeacon_Model;
import com.example.wustls14.dy_beacon.util.DBHelper;
import com.example.wustls14.dy_beacon.util.U;

import java.util.ArrayList;
import java.util.List;

public class Saved_Beacons_Activity extends AppCompatActivity {


    // DB 연동에 필요한 것들
    String DATABASE_NAME = "DY_Beacon_DB";
    String TABLE_NAME = "registered_Info_Table";
    public DBHelper dbHelper;
    private SQLiteDatabase db;

    // RecyclerView 이용에 필요한 것들
    RecyclerView saved_recyclerView;
    List<SavedBeacon_Model> savedList;
    saveAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_beacons);

        // 초기화
        saved_recyclerView = (RecyclerView) findViewById(R.id.saved_recyclerView);
        savedList = new ArrayList<SavedBeacon_Model>();

        initData();
    }

    // DB 구축 ====================================================================================

    // 2. DB가 열려있는지 확인
    public void initData() {
        boolean isOpen = openDatabase();
        if (isOpen) {
            // 3. 열려있다면 실행
            executeRawQueryParam();
        }
    }

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

    // 3. DB가 열려있다면 저장된 정보 가져온 후 아답터에 셋팅
    private void executeRawQueryParam() {

        String SQL = "select name, age, phone " + " from " + TABLE_NAME;
        // 아답터에 기존 정보를 전달하기 위해 리스트 만듬


        Cursor c1 = db.rawQuery(SQL, null);
        int recordCount = c1.getCount();

        for (int i = 0; i < recordCount; i++) {
            SavedBeacon_Model item = new SavedBeacon_Model();
            c1.moveToNext();
            item.setBeaconName(c1.getString(0));
            item.setSrlNo(c1.getInt(1));
            item.setDistance(c1.getString(2));
            savedList.add(item);
            saved_recyclerView.setAdapter(adapter = new saveAdapter(this,savedList));
            saved_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            saved_recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        c1.close();
    }
    // 삭제버튼 클릭시 해당되는 데이터 DB에서 삭제

    public boolean deleteMethod(String id) {
        try {
            String[] whereArgs = {id};
            db.delete(TABLE_NAME, "name = ?", whereArgs);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //==========================================================================================================
    class saveAdapter extends RecyclerView.Adapter<saveAdapter.saveViewHolder>{

        Context mContext;       // intent를 실행하기 위해서 필요
        public List<SavedBeacon_Model> test_list;

        public saveAdapter(Context mContext, List<SavedBeacon_Model> test_list) {
            this.mContext = mContext;
            this.test_list = test_list;
        }

        @Override
        public saveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_beacon_layout, parent,false);
            return new saveViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final saveViewHolder holder, final int position) {
            final SavedBeacon_Model item = test_list.get(position);
            holder.saved_name.setText("비콘 이름 : " + item.getBeaconName());
            holder.saved_srlNo.setText("시리얼 번호 : " + item.getSrlNo ());
            holder.saved_distance.setText("알람 설정 거리 : " + item.getDistance());
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.showPopupMenu(holder.imageButton,position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return test_list == null ? 0: test_list.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

    // =============================================================================================

    public class saveViewHolder extends RecyclerView.ViewHolder{
        public ImageView saved_img;
        public TextView saved_name;
        public TextView saved_srlNo;
        public TextView saved_distance;
        public ImageButton imageButton;

        public saveViewHolder(View itemView) {
            super(itemView);
            saved_img = (ImageView) itemView.findViewById(R.id.saved_Img);
            saved_name = (TextView) itemView.findViewById(R.id.saved_name_txt);
            saved_srlNo = (TextView) itemView.findViewById(R.id.saved_srlNo_txt);
            saved_distance = (TextView) itemView.findViewById(R.id.saved_distance_txt);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
        }

        public void showPopupMenu(View view, int position){
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
            popup.show();
        }
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.modify_menu:
                    // 수정 버튼 클릭시 수정페이지로 이동
                    Intent intent = new Intent(mContext, Modify_Data_Activity.class);
                    // 저장된 값 전달하기
                    intent.putExtra("beaconName", savedList.get(position).getBeaconName());
                    intent.putExtra("srlNo", savedList.get(position).getSrlNo());
                    intent.putExtra("distance", savedList.get(position).getDistance());
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.delete_menu:
                    if(deleteMethod(savedList.get(position).getBeaconName()))   //  DB에서 삭제가 성공적으로 이루어지고나면 if문 실행
                    {
                        test_list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, test_list.size());
                        Toast.makeText(getBaseContext()," 성공적으로 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),"삭제에 실패하였습니다. " + savedList.get(position).getBeaconName(),Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
            }
            return false;
        }
    }
}


}

