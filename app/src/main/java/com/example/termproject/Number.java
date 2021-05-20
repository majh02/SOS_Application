package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class Number extends AppCompatActivity {

    dbHelper mydbHelper;
    private String Number;
    private String Name;
    private ListView listview ;
    private ArrayAdapter adapter ;
    public ArrayList<String> items = new ArrayList<String>();
    public ArrayList<String> viewitems = new ArrayList<String>();
    EditText name, num; //TextView?

    public class dbHelper extends SQLiteOpenHelper{
        public dbHelper(Context context){
            super(context, "telDB", null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE contacts(Name CHAR(20), Phone CHAR(20))");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        name = (EditText) findViewById(R.id.edit_message3);
        num = (EditText) findViewById(R.id.edit_message2);
        listview = (ListView) findViewById(R.id.listView) ;
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items) ;

        MyApplication myapp2 = (MyApplication) getApplication();
        myapp2.setPhone(viewitems);
        viewitems = myapp2.getPhone();

        mydbHelper = new dbHelper(this);
        listview.setAdapter(adapter) ;

        ShowDB();

        Button buttonAdd = (Button) findViewById(R.id.button5) ;
        buttonAdd.setEnabled(false) ;
        buttonAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.length() > 0 && num.length() > 0) {
                    viewitems.add(name.getText().toString());
                    viewitems.add(num.getText().toString());
                    items.add(name.getText().toString()+"   "+num.getText().toString());
                    name.setText("");
                    num.setText("") ;
                    adapter.notifyDataSetChanged();

                    Toast.makeText(Number.this,
                            "저장되었습니다.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button buttonDel = (Button) findViewById(R.id.button6) ;
        buttonDel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count ;
                int checkedIndex ;

                count = adapter.getCount() ;

                if (count > 0) {
                    checkedIndex = listview.getCheckedItemPosition();
                    if (checkedIndex > -1 && checkedIndex < count) {
                        viewitems.remove(checkedIndex*2);
                        viewitems.remove(checkedIndex*2);
                        items.remove(checkedIndex);
                        listview.clearChoices();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Number.this,
                                "삭제되었습니다.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button buttonDirectory = (Button) findViewById(R.id.button7) ;
        buttonDirectory.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        EditText editTextNew = (EditText) findViewById(R.id.edit_message2) ;
        editTextNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                Button buttonAdd = (Button) findViewById(R.id.button5) ;
                if (edit.toString().length() > 0) {
                    buttonAdd.setEnabled(true) ;
                } else {
                    buttonAdd.setEnabled(false) ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

    }
    private void ShowDB(){
        SQLiteDatabase mysqlDB = mydbHelper.getWritableDatabase();
        Cursor cursor;
        cursor=mysqlDB.rawQuery("SELECT * FROM contacts;",null);
        String strname = null;
        String strphone= null;
        items.clear();
        viewitems.clear();
        while(cursor.moveToNext()){
            strname = cursor.getString(0);
            strphone = cursor.getString(1);
            if(strname != null || strphone != null) {
                items.add(strname + "   " + strphone);
                viewitems.add(strname);
                viewitems.add(strphone);
            }
        }
        adapter.notifyDataSetChanged();
        cursor.close();
        mysqlDB.close();
    }
    private void SaveDB(){
        SQLiteDatabase mysqlDB = mydbHelper.getWritableDatabase();
        mydbHelper.onUpgrade(mysqlDB,1,2);
        int index = 0;
        String sname, snum;
        System.out.println(viewitems);
        while(index + 1 < viewitems.size()) {
            sname = viewitems.get(index);
            snum = viewitems.get(index+1);
            mysqlDB.execSQL("INSERT INTO contacts VALUES('" + sname + "','" + snum + "');");
            index+=2;
        }
        mysqlDB.close();
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                Cursor cursor = getContentResolver().query(data.getData(),
                        new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

                cursor.moveToFirst();
                Name = cursor.getString(0);
                Number = cursor.getString(1);
                cursor.close();
            }
            name.setText(Name);
            num.setText(Number);
            Number.super.onActivityResult(requestCode, resultCode, data);
        }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(resultCode == RESULT_OK) {
//
//            Cursor cursor = getContentResolver().query(data.getData(),
//                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
//
//            cursor.moveToFirst();
//            number = cursor.getString(1);
//            cursor.close();
//        }
//
//        Number.super.onActivityResult(requestCode, resultCode, data);
//        num.setText(number);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.call_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SaveDB();
        switch(item.getItemId()){
            case R.id.home:
                Intent intent1 = new Intent(Number.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.msg:
                Intent intent2 = new Intent(Number.this, Message.class);
                startActivity(intent2);
                break;
            case R.id.gps:
                Intent intent3 = new Intent(Number.this, Gps.class);
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}