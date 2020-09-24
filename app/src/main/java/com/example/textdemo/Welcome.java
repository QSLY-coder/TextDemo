package com.example.textdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sp_state = getSharedPreferences("State",MODE_PRIVATE);
        String state = sp_state.getString("now",null);
        if (state == null){
            SharedPreferences.Editor editor = sp_state.edit();
            editor.putString("now","1");
            editor.commit();
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
            Intent intent = new Intent(Welcome.this,MainActivity.class);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);
        }



    }
}