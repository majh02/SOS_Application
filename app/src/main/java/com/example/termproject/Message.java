package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Message extends AppCompatActivity {

    private EditText memoWrite;
    private Button save, reset;
    TextView t1;
    InputMethodManager imm;
    public ArrayList<String> memo = new ArrayList<String>();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        memoWrite = (EditText) findViewById(R.id.edit_message);
        save = (Button) findViewById(R.id.button3);
        reset = (Button) findViewById(R.id.button4);
        t1 = (TextView) findViewById(R.id.textView);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        load();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memoWrite.getText().toString().equals("")) {
                    Toast.makeText(Message.this,
                            "값이 없습니다.",
                            Toast.LENGTH_LONG).show();
                } else { // 공백이 아닐때
                    String s1 = memoWrite.getText().toString();//1번 값 가져오기(xml->java)
                    t1.setText(s1);
                    Toast.makeText(Message.this,
                            "저장 완료!",
                            Toast.LENGTH_LONG).show();
                    hideKeyboard();
                }
                save();
                memoWrite.setText("");
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
                t1.setText("도움이 필요해요!!!!!! SOS");
                memoWrite.setText("");
            }
        });

    }

    protected void save(){
        SharedPreferences sharedPreferences = getSharedPreferences("memo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String text = memoWrite.getText().toString();
        editor.putString("key1",text); // key, value를 이용하여 저장하는 형태
        //다양한 형태의 변수값을 저장할 수 있다.
        //editor.putString();
        //editor.putBoolean();
        //editor.putFloat();
        //editor.putLong();
        //editor.putInt();
        //editor.putStringSet();
        editor.apply();
        editor.commit();
    }
    protected void load(){
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("memo",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("key1","");
        if(text.equals("")){
            t1.setText("도움이 필요해요!!!!!! SOS");
        }else{
            t1.setText(text);
        }
    }
    protected void remove(){
        SharedPreferences sharedPreferences = getSharedPreferences("memo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("key1");
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.msg_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent1 = new Intent(Message.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.call:
                Intent intent2 = new Intent(Message.this, Number.class);
                startActivity(intent2);
                break;
            case R.id.gps:
                Intent intent3 = new Intent(Message.this, Gps.class);
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(memoWrite.getWindowToken(), 0);

    }
}