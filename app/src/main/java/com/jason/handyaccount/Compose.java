package com.jason.handyaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Compose extends AppCompatActivity {
    private static final String TAG = "ComposeSeg";
    private String todayStr = null;
    private String diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        //获取当前日期
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayStr = sdf.format(today);

        TextView viewtime = findViewById(R.id.editDate);
        viewtime.setText(todayStr);

    }

    public void submit(View view) {
        EditText editText = findViewById(R.id.editContext);
        diary = editText.getText().toString();
        Log.i(TAG, "submit: diary is==" + diary);
        if(!diary.equals("")){

            DiaryManager manager = new DiaryManager(this);
            manager.add(new DiaryItem(diary,todayStr+"          成都.西南财经大学柳林校区"));
            Log.i(TAG, "onOptionsItemSelected: 写入数据完毕");
        } else {
            Toast.makeText(this, "真的不写了吗大爷~~~ ", Toast.LENGTH_LONG).show();
        }
        Intent intent = getIntent();
        intent.putExtra("diary",diary);
        intent.putExtra("date",todayStr+"          成都.西南财经大学柳林校区");
        setResult(2,intent);

        finish();
    }
}
