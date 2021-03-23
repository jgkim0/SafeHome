package org.techtown.savehome;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.savehome.Alram1;
import org.techtown.savehome.Alram2;
import org.techtown.savehome.Alram3;
import org.techtown.savehome.R;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BlueActivity extends AppCompatActivity {

    //문자 전송
    Button buttonSend;
    private Handler mHandler;
    private Runnable mRunnable;
    private Runnable mRunnable_Gas1;
    private Runnable mRunnable_Gas2;
    //Alram1.mHandler.sendEmptyMessage(0);
    String data1;
    String data2="on";
    Intent mintent;


    private BluetoothSPP bt;

    int interval, interval2;
    int anum,bnum,cnum,fnum;   // anum : 화재알람에 사용 bnum: 가스 주의 알람에 사용


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //이거 문자 추가한거
        mRunnable = new Runnable() {
            @Override
            public void run() {

                SharedPreferences sf1 = getSharedPreferences("TelFile", MODE_PRIVATE);

                //입력한 값을 가져와 변수에 담는다

                String phoneNo = sf1.getString("tel", "");
                String addr = sf1.getString("address","");
                Log.v("phnoeNo",phoneNo);
                String sms = "[화재 발생] ( " + addr + ") 에서 화재가 감지되었습니다. ";

                try {
                    //전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "화재 발생 상황 전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        mRunnable_Gas1 = new Runnable() {
            @Override
            public void run() {
                SharedPreferences sf1 = getSharedPreferences("TelFile", MODE_PRIVATE);

                //입력한 값을 가져와 변수에 담는다
                String phoneNo = sf1.getString("tel", "");
                String addr = sf1.getString("address","");

                String sms = "[가스주의경보] (" + addr + ") 에서 일산화탄소 누출이 감지되었습니다. ";

                try {
                    //전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "가스 주의 상황 전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        mRunnable_Gas2 = new Runnable() {
            @Override
            public void run() {
                SharedPreferences sf1 = getSharedPreferences("TelFile", MODE_PRIVATE);

                //입력한 값을 가져와 변수에 담는다
                String phoneNo = sf1.getString("tel", "");
                String addr = sf1.getString("address","");

                String sms = "[가스위험경보] (" + addr + ") 에서 일산화탄소 누출이 감지되었습니다. ";

                try {
                    //전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "가스 위험 상황 전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        //여기까지 추가한거 밑에부터 원래 진경쓰꺼


        setContentView(R.layout.activity_blue);
        bt = new BluetoothSPP(this);

        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        //데이터 수신하는 코드
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {



                TextView textView = findViewById(R.id.text_income);
                textView.setText(message.substring(0,2));   // message 자르기(온도센서)

                TextView textView1 = findViewById(R.id.text_income2);
                textView1.setText(message.substring(2));    // message 자르기 (가스센서)


                Log.d("asd", message);
                interval = Integer.parseInt(message.substring(0,2));
                interval2 = Integer.parseInt(message.substring(2));
                int checked = 0;
                //Log.d("Bluetooth1", message);


                if(interval >= 35)
                    anum = anum + 1;

                if (interval >= 35 & anum ==1) {

                    //Toast.makeText(BlueActivity.this, message, Toast.LENGTH_SHORT).show();
                    //bt.send("2", true);
                    Intent intent = new Intent(getApplicationContext(), Alram1.class);
                    startActivity(intent);

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);// (Context.VIBRATE_SERVICE)
                    long millisecond = 3000;  // 1초
                    vibrator.vibrate(millisecond);

                    MediaPlayer player = MediaPlayer.create(BlueActivity.this,R.raw.hwahwa);
                    player.start();

                    Intent intent6 = getIntent();
                    String data1 = intent6.getStringExtra("value");

                    if(data1=="OffOff"){
                        data2="on";
                    }
                    if(data1=="Onon"){
                        data2="off";
                    }

                    if(data2=="on"){
                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 5000);
                    }
                    if(data2=="off" && data1=="Onon"){
                        mHandler.removeCallbacksAndMessages(null); //핸들러 딜레이 취소
                    }


                    if (interval > 35) {
                        Toast.makeText(BlueActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else if (interval < 35) {
                        finish();
                    }
                }



                if(interval2 > 300)
                    bnum = bnum + 1;

                if(interval2 > 700)
                    cnum = cnum + 1;


                if (interval2 > 300 & interval2 < 700 && bnum == 1) {

                    Intent intent = new Intent(getApplicationContext(), Alram2.class);
                    startActivity(intent);


                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);// (Context.VIBRATE_SERVICE)
                    long millisecond = 3000;  // 1초
                    vibrator.vibrate(millisecond);

                    MediaPlayer player = MediaPlayer.create(BlueActivity.this,R.raw.pipi);
                    player.start();



                    Intent intent6 = getIntent();
                    String data1 = intent6.getStringExtra("value");

                    if(data1=="OffOff"){
                        data2="on";
                    }
                    if(data1=="Onon") {
                        data2 = "off";
                    }
                    if(data2=="on"){
                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable_Gas1, 5000);
                    }
                    if(data2=="off" && data1=="Onon"){
                        mHandler.removeCallbacksAndMessages(null); //핸들러 딜레이 취소
                    }


                }


                if (interval2 >= 700 && cnum==1) {
                    Intent intent = new Intent(getApplicationContext(), Alram3.class);
                    startActivity(intent);


                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);// (Context.VIBRATE_SERVICE)
                    long millisecond = 3000;  // 1초
                    vibrator.vibrate(millisecond);

                    MediaPlayer player = MediaPlayer.create(BlueActivity.this,R.raw.morning);
                    player.start();


                    Intent intent6 = getIntent();
                    String data1 = intent6.getStringExtra("value");


                    if(data1=="OffOff"){
                        data2="on";
                    }
                    if(data1=="Onon") {
                        data2 = "off";
                    }
                    if(data2=="on"){
                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable_Gas2, 5000);
                    }
                    if(data2=="off" && data1=="Onon"){
                        mHandler.removeCallbacksAndMessages(null); //핸들러 딜레이 취소
                    }
                }

                if(interval2 >= 700 && interval >= 35){
                    fnum = fnum +1;
                }


                if (interval2 >= 700 && interval >= 35 && fnum==1) {
                    Intent intent = new Intent(getApplicationContext(), Alram1.class);
                    startActivity(intent);

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);// (Context.VIBRATE_SERVICE)
                    long millisecond = 3000;  // 1초
                    vibrator.vibrate(millisecond);

                    MediaPlayer player = MediaPlayer.create(BlueActivity.this,R.raw.morning);
                    player.start();


                    Intent intent6 = getIntent();
                    String data1 = intent6.getStringExtra("value");


                    if(data1=="OffOff"){
                        data2="on";
                    }
                    if(data1=="Onon") {
                        data2 = "off";
                    }
                    if(data2=="on"){
                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 5000);
                    }
                    if(data2=="off" && data1=="Onon"){
                        mHandler.removeCallbacksAndMessages(null); //핸들러 딜레이 취소
                    }
                }




            }


        });



        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                anum=0; bnum=0; cnum=0; data1="OffOff";
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
                anum=0; bnum=0; cnum=0; data1="OffOff";
            }
        });

        Button btnConnect = (Button) findViewById(R.id.button_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        if(bt.getServiceState()== BluetoothState.STATE_NONE ){
            BluetoothAdapter btAdapter= BluetoothAdapter.getDefaultAdapter();
            String Address ="98:D3:91:FD:61:08";
            BluetoothDevice device = btAdapter.getRemoteDevice(Address);
            bt.connect(String.valueOf(device));
        }




    }

    public void onButton5Clicked(View v) {
        anum =0;
        bnum =0;
        cnum =0;
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

//    public void setup() {
//        Button btnSend = (Button) findViewById(R.id.btnSend);
//        btnSend.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                bt.send("Text", true);
//            }
//        });
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);

            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

