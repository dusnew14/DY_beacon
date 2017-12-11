package com.example.wustls14.dy_beacon.util;

// 자주 쓰이는 메소드를 모아놓은 싱글톤

import android.content.Context;
import android.util.Log;

public class U {

    private static final U ourInstance = new U();
    public static U getInstance() {return ourInstance;}
    private U() {}

    // 로그 출력
    final String TAG = "T";
    public void log(String msg){
        if (msg !=null){
            Log.i(TAG, "=========================================================================");
            Log.i(TAG, msg);
            Log.i(TAG, "=========================================================================");
        }
    }

    // context 출력
    Context context;
    public Context getContext(){return context;}
    public void setContext(Context context){this.context = context;}
}
