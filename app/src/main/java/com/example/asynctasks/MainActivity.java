package com.example.asynctasks;

import android.Manifest;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    Button btnAdd;
    Button btnRead;
    Button btnClear;
    String strName;
    String strEmail;
    DBHelper DB_helper;
    SQLiteDatabase database;
    long row_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.txt_name);
        email = findViewById(R.id.txt_email);
        btnAdd = findViewById(R.id.btn_add);
        btnRead = findViewById(R.id.btn_read);
        btnClear = findViewById(R.id.btn_clear);
        DB_helper = new DBHelper(MainActivity.this, null,null,1);
    }

    public void onclick(View view) {

        strName = name.getText().toString();
        strEmail = email.getText().toString();
        ContentValues values = new ContentValues();
        database = openOrCreateDatabase("myDatabase", MODE_PRIVATE, null);

        switch (view.getId()){
            case R.id.btn_add:
                values.put("name",strName);
                values.put("email",strEmail);
                row_ID = database.insert("myTable",null, values);
                //database.execSQL("INSERT INTO myTable VALUES('oksana', 'email');");
                Log.d("debug",""+row_ID);
                break;

            case R.id.btn_clear:
                int count = database.delete("myTable","id=1",null);
                Log.d("debug","deleted "+count);
                break;

            case R.id.btn_read:
                Cursor cursor = database.query("myTable",null,null,null,null,null,null);
                int cID = cursor.getColumnIndex("id");
                int nameColumn = cursor.getColumnIndex("name");
                int emailColumn = cursor.getColumnIndex("email");
                if(cursor.moveToFirst()){
                    do {
                        Log.d("debug","Id = "+ cursor.getInt(cID));
                        Log.d("debug","name = "+ cursor.getString(nameColumn));
                        Log.d("debug","email = "+ cursor.getString(emailColumn));
                    }
                    while (cursor.moveToNext());
                }
                else{
                    Log.d("debug","count 0");
                }
                cursor.close();
                break;

            case R.id.btn_getCount:
                Cursor c = database.rawQuery("SELECT COUNT(*) FROM myTable",null);
                c = database.rawQuery("SELECT DISTINCT myTable",null);
                if(c !=null && c.getCount() != 0 ){
                    Log.d("debug","Rows count = "+c.getCount());
                }
                c.close();
                break;

            case R.id.btn_update:
                values.put("name",strName);
                values.put("email",strEmail);
                int id = 0;
                int updateCount = database.update("myTable",values,"id=?",new String[] {String.valueOf(id)});
                Log.d("debug","updated "+ updateCount);
                break;
        }
        DB_helper.close();
    }
}
