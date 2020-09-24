package com.example.textdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> fileName = new ArrayList<>();//储存文件名的集合
    private String key = ".txt";//文件后缀名
    private ListView mainList;//主菜单ListView
    private ArrayAdapter<String> arrayAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //文件路径
        File file = new File(Environment.getExternalStorageDirectory().getPath());//获取sd卡目录
        searchFile(file);//遍历搜索所有.txt文件

        findID();
        findID();
        //显示主菜单
        arrayAdapter = new ArrayAdapter<>(this, R.layout.listbuild, fileName);
        mainList.setAdapter(arrayAdapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("title", fileName.get(position));
                intent.putExtra("state", 0);
                finish();
                startActivity(intent);
            }
        });

        //注册上下文菜单到ListView中
        registerForContextMenu(mainList);
    }

    public void findID() {
        ImageButton add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(this);
        mainList = findViewById(R.id.mainList);
    }

    //点击监听
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            //弹出提示框，要求输入标题
            final EditText inputServer = new EditText(this);
            inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            AlertDialog.Builder builder = new AlertDialog.Builder(this);//实例化AlertDialog提示框对象
            builder.setTitle("请输入标题").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String title = inputServer.getText().toString();
                    if (!title.isEmpty()) {
                        intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("state", 1);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "标题为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        }
    }

    //搜索文件方法
    public void searchFile(File fileFold) {
        File[] fileList = fileFold.listFiles();
        if (fileList != null) {
            if (fileList.length > 0) {
                for (File file : fileList) {
                    if (file.getName().contains(key)) {
                        String name = delStr(file.getName(), key);//删除后缀名
                        fileName.add(name);
                    }
                }
            }
        }
    }

    //删除指定字符串，此处用于删除后缀.txt
    public String delStr(String str, String clear) {
        StringBuilder stringBuffer = new StringBuilder(str);
        while (true) {
            int index = stringBuffer.indexOf(clear);
            if (index == -1) {
                break;
            }
            stringBuffer.delete(index, index + clear.length());
        }
        return stringBuffer.toString();
    }

    //定义上下文菜单中的Item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "编辑");
        menu.add(0, 1, 0, "删除");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //上下文菜单Item点击事件
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            //点击编辑
            case 0:
                intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("title", fileName.get(menuInfo.position));
                intent.putExtra("state", 0);
                finish();
                startActivity(intent);
                break;
            //点击删除
            case 1:
                final int position = menuInfo.position;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否删除").setIcon(android.R.drawable.ic_dialog_info)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileDelName = fileName.get(position) + key;
                        File fileDel = new File(Environment.getExternalStorageDirectory() + "/" + fileDelName);
                        if (fileDel.exists()) {
                            if (fileDel.delete()) {
                                Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                fileName.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                            } else
                                Toast.makeText(MainActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, "删除失败！文件不存在", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;

        }
        return super.onContextItemSelected(item);
    }
}