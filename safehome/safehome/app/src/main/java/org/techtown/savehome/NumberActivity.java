package org.techtown.savehome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NumberActivity extends AppCompatActivity {

    private Intent mlntent;
    private GpsTracker gpsTracker;
    private String name;
    private String tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        final TextView textview_address = (TextView)findViewById(R.id.textview);
        TextView textview_tel = (TextView)findViewById(R.id.text_phone);

        SharedPreferences sf1 = getSharedPreferences("TelFile", MODE_PRIVATE);

        textview_tel.setText(name +" : "+tel);
        textview_address.setText(sf1.getString("address", ""));





        Button ShowLocationButton = (Button) findViewById(R.id.btn_addr);
        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                SharedPreferences sharedPreferences1 = getSharedPreferences("TelFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();

                gpsTracker = new GpsTracker(NumberActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                textview_address.setText(address);
                editor.putString("address",address); // key, value??? ???????????? ???????????? ??????
                editor.apply();

                Toast.makeText(NumberActivity.this, "??????????????? ?????????????????????", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getCurrentAddress( double latitude, double longitude) {

        //????????????... GPS??? ????????? ??????
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //???????????? ??????
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }

    public void phoneClicked(View v){
        Intent intent2 = new Intent(Intent.ACTION_PICK);
        intent2.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent2, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //SharedPreferences sharedPreferences = getSharedPreferences("NameFile", MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences("TelFile", MODE_PRIVATE);
        TextView textview_tel = (TextView)findViewById(R.id.text_phone);


        if (resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            name = cursor.getString(0);
            tel = cursor.getString(1);
            textview_tel.setText(name +" : "+tel);
            Log.v("name", name);
            Log.v("tel", tel);

            //????????? ???????????? editor??? ???????????? ?????? ??????????????????.

            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("name",name);
            editor.putString("tel",tel); // key, value??? ???????????? ???????????? ??????
            editor.apply();

            cursor.close();

            Toast.makeText(NumberActivity.this, "?????????????????? ?????????????????????", Toast.LENGTH_LONG).show();

        }
    }

    public void resetClicked(View v){
        final TextView textview_address = (TextView)findViewById(R.id.textview);
        TextView textview_tel = (TextView)findViewById(R.id.text_phone);

        textview_tel.setText("????????? ????????? ??????");
        textview_address.setText("????????? ?????? ??????");
    }

}