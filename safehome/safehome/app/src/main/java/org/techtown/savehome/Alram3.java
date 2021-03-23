package org.techtown.savehome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Alram3 extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private Runnable mRunnable_Gas1;
    private Runnable mRunnable_Gas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram3);

    }

    //다이얼 연결 메소드
    public void onButton3Click(View v) {            //'119' 다이얼 연결 onclick 메소드
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:119"));
        startActivity(myIntent);
        mHandler.removeCallbacksAndMessages(null); //핸들러 딜레이 취소
    }

}
