package com.example.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 分别为每张图片设置点击处理
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setOnClickListener((View.OnClickListener) this);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener((View.OnClickListener) this);
        imageView = (ImageView) findViewById(R.id.imageView3);
        imageView.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View view){
        // 准备利用Intent存放用户点击的图片编号，传递经第二个页面
        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
        int id = view.getId();
        switch (id){
            case R.id.imageView1:
                intent.putExtra("PicNo", "1");
                break;
            case R.id.imageView2:
                intent.putExtra("PicNo", "2");
                break;
            case R.id.imageView3:
                intent.putExtra("PicNo", "3");
                break;
        }

        // 调用第二个页面
        startActivity(intent);
    }
}