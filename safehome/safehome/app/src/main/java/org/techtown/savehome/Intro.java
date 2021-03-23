package org.techtown.savehome;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.techtown.savehome.MainActivity;
import org.techtown.savehome.R;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handlerr = new Handler();
        handlerr.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intenttt = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intenttt); //다음화면으로 넘어감
                finish();
            }
        },2000); //3초 뒤에 Runner객체 실행하도록 함

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }


}
