package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ArrayList<String> viewitems = new ArrayList<String>();
    private ArrayList<String> phonenum = new ArrayList<String>();
    private String t1;
    private GpsTracker gpsTracker;
    private double latitude, longitude;
    private MediaPlayer sound;
    private Switch switch_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MyApplication myapp = (MyApplication) getApplication();
        viewitems = myapp.getPhone();

        GetNumber();
        GetGps();
        SendMessage();
        SirenPlayer();
//        sound = MediaPlayer.create(this, R.raw.siren);
//        switch_sound = (Switch) findViewById(R.id.switch1);
//        switch_sound.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(switch_sound.isChecked()==true){
//                    sound.start();
//                    sound.setLooping(true);
//                }
//                else{
//                    sound.stop();
//                }
//            }
//        });
    }

    private void GetNumber(){
        int index = 1;
        while(index < viewitems.size()){
            String str = viewitems.get(index);
            phonenum.add(str);
            index += 2;
        }
        System.out.println(phonenum);
    }

    private void GetGps(){
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();
            gpsTracker = new GpsTracker(MainActivity2.this);

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }
    }

    public void SendMessage(){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            int index=0;
            String where="http://maps.google.com/maps?f=q&q=("+latitude+","+longitude +")";
            String TextMessage = load();
            String pnum;
            while(index < phonenum.size()) {
                pnum = phonenum.get(index);
                System.out.println(pnum);
                System.out.println(TextMessage);
                System.out.println(where);
                smsManager.sendTextMessage(pnum,null, TextMessage, null, null);
                smsManager.sendTextMessage(pnum,null, where, null, null);
                index++;
            }
            Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "문자 전송이 실패하였습니다. 잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    protected String load(){
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("memo",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("key1","");
        if(text.equals("")){
            text = "도움이 필요해요!!!!!! SOS";
        }else{

        }
        return text;
    }
    public void SirenPlayer(){
        sound = MediaPlayer.create(this, R.raw.siren);
        switch_sound = (Switch) findViewById(R.id.switch1);
        switch_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch_sound.isChecked()==true){
                    sound.start();
                    sound.setLooping(true);
                }
                else{
                    sound.pause();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.main_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.msg:
                Intent intent1 = new Intent(MainActivity2.this, Message.class);
                startActivity(intent1);
                break;
            case R.id.call:
                Intent intent2 = new Intent(MainActivity2.this, Number.class);
                startActivity(intent2);
                break;
            case R.id.gps:
                Intent intent3 = new Intent(MainActivity2.this, Gps.class);
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}