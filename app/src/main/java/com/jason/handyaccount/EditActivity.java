package com.jason.handyaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    String diary = "";
    String date = "";
    String id="";
    String Position;
    private static final String TAG = "EditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        diary = getIntent().getStringExtra("diary");
        date = getIntent().getStringExtra("date");
        id =  getIntent().getStringExtra("Id");
        Position = getIntent().getStringExtra("position");


        TextView dateView = findViewById(R.id.date_show);
        dateView.setText(date);
        EditText diaryView = findViewById(R.id.story_edit);
        diaryView.setText(diary);

    }

    public void reset_story(View view) {
        TextView date_change = findViewById(R.id.date_show);
        EditText story_change = findViewById(R.id.story_edit);
        date = date_change.getText().toString();
        diary = story_change.getText().toString();
        DiaryManager manager = new DiaryManager(this);
        DiaryItem item = new DiaryItem(diary,date);
        item.setId(Integer.parseInt(id));
        manager.update(item);

        //将数据打包参数回传
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putString("diary",diary);
        bdl.putString("date",date);
        bdl.putString("id",id);
        bdl.putString("position", Position);
        intent.putExtras(bdl);
        setResult(2,intent);


        finish();
    }

    public void deletebtn(View view) {
        DiaryManager manager = new DiaryManager(this);
        manager.delete(Integer.parseInt(id));
        Toast.makeText(this, "成功删除", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        intent.putExtra("position",Position);
        setResult(3,intent);
        finish();
    }
}
