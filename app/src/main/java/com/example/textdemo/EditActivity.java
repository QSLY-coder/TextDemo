package com.example.textdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private String title;
    private int state;
    private File file;
    private ImageButton save_btn, cancel_btn;
    private EditText editText;
    private byte[] buffer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findID();
        intent = getIntent();
        state = intent.getIntExtra("state", 1);
        title = intent.getStringExtra("title");
        System.out.println("获取到的title为"+title);
        intent.removeExtra("title");
        intent.removeExtra("state");

        if (state == 0) {
            file = new File(Environment.getExternalStorageDirectory() + "/" + title + ".txt");
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                buffer = new byte[fin.available()];
                fin.read(buffer);
                String data = new String(buffer);
                editText.setText(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                file = new File(Environment.getExternalStorageDirectory() + "/" + title + ".txt");
                FileOutputStream fos = null;
                String text = editText.getText().toString();
                try {
                    fos = new FileOutputStream(file);
                    fos.write(text.getBytes());
                    fos.flush();
                    intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                            Toast.makeText(EditActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.cancel_btn:
                intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void findID() {
        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(this);
        cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(this);
        editText = findViewById(R.id.editText);
    }

    public void newText() {

    }

    public void editText() {

    }
}