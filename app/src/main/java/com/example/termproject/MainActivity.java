package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GestureDetector gd;
    GestureDetector.OnDoubleTapListener listener;
    Button btn1;
    int doubleClickFlag = 0;
    final long  CLICK_DELAY = 250;

    private static final int REQUEST_USED_PERMISSION = 200;
    private static final String[] needPermissons = { Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SEND_SMS ,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionToFileAccepted = true;

        switch(requestCode)

        {
            case REQUEST_USED_PERMISSION:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionToFileAccepted = false;
                        break;
                    }
                }
                break;
        }
        if(permissionToFileAccepted == false) {
            finish();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.button);

        for(String permission : needPermissons){
            if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,needPermissons, REQUEST_USED_PERMISSION);
                break;
            }
        }

        setDoubleClick();
    }

    public void setDoubleClick() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleClickFlag++;
                Handler handler = new Handler();
                Runnable clickRunnable = new Runnable() {
                    @Override
                    public void run() {
                        doubleClickFlag = 0;
                        Intent intent_main = new Intent(MainActivity.this, MainActivity.class);
                        Toast.makeText(MainActivity.this, "한 번 더 버튼을 누르면 SOS 메시지가 전송됩니다!", Toast.LENGTH_LONG).show();
                    }
                };
                if( doubleClickFlag == 1 ) {
                    handler.postDelayed( clickRunnable, CLICK_DELAY );
                }else if( doubleClickFlag == 2 ) {
                    doubleClickFlag = 0;
                    Intent intent_sub = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent_sub);
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
                Intent intent1 = new Intent(MainActivity.this, Message.class);
                startActivity(intent1);
                break;
            case R.id.call:
                Intent intent2 = new Intent(MainActivity.this, Number.class);
                startActivity(intent2);
                break;
            case R.id.gps:
                Intent intent3 = new Intent(MainActivity.this, Gps.class);
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}