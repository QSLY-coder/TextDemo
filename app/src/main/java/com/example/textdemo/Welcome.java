package com.example.textdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;
//欢迎界面
public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //实例化SharedPreferences对象，用于判断是否为第一次启动
        SharedPreferences sp_state = getSharedPreferences("State",MODE_PRIVATE);
        String state = sp_state.getString("now",null);
        if (state == null){
            //第一次启动时，写入状态为1，表示第一次启动完成
            SharedPreferences.Editor editor = sp_state.edit();
            editor.putString("now","1");
            editor.apply();
            //设置定时器，定时跳转到MainActivity
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Welcome.this,MainActivity.class);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(intent);

                }
            };
            timer.schedule(timerTask,2000);
        }else{
            //不是第一次启动时直接跳转
            Intent intent = new Intent(Welcome.this,MainActivity.class);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);
        }



    }
}