package org.techtown.savehome;


import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.techtown.savehome.R;

import java.util.Timer;
import java.util.TimerTask;

public class Alram1 extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private Runnable mRunnable_Gas1;
    private Runnable mRunnable_Gas2;
    int mnum;

    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram1);

    }

  /*  private void onClick() {
        Intent aintent=new Intent(this,BlueActivity.class);
        aintent.putExtra("value",1);
        startActivity(aintent);
    }*/

    //다이얼 연결 메소드
    public void onButton3Click(View v) {//'119' 다이얼 연결 onclick 메소드
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:119"));
        startActivity(myIntent);

        mHandler.removeCallbacksAndMessages(null); //핸들러 딜레이 취소

    }

}