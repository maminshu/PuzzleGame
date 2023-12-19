package com.example.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<SmallPic> arrayOfPics_2 = new ArrayList<>();
    int secondClick=0;
    int firstClickedRowNo=0;
    int firstClickedColNo=0;
    int success = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // 获取屏幕大小
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x;
        // 计算屏幕宽度的1/4，以备后面设置图片宽度和高度
        int picWidth = screenWidth / 4;

        // 获取上一个页面传递过来的参数PicNo，即点击了第几张图片
        Intent intent = getIntent();
        String picNo = intent.getStringExtra("PicNo");

        // 初始化一个按顺序排列的对象数组
        ArrayList<SmallPic> arrayOfPics_1 = new ArrayList<>();
        for(int i=0; i<=3; i++){
            for(int j=0; j<=3; j++){
                // 生成对应的图片文件名
                String picName = "pic" + picNo + "_" + j + i;
                // 在对象数组中加入新元素
                arrayOfPics_1.add(new SmallPic(i, j, picName));
            }
        }

        // 生成随机的数组
        // 初始化随机数生成器
        Random r = new Random();
        for(int i=0; i<=15; i++){
            // 生成下一个随机数
            int idx = r.nextInt(16-i);
            // 将原数组arrOfPic1中的相应元素加入新数组arrarOfPic2中
            arrayOfPics_2.add(new SmallPic(arrayOfPics_1.get(idx)));
            // 从原数组arrayOfPic1中去除相应元素
            arrayOfPics_1.remove(idx);
        }

        // 将随机生成的图片顺序展示
        // 双层循环，第一层为针对各行的循环，第二层为针对各列的循环
        for(int i=0; i<=3; i++){
            for(int j=0; j<=3; j++){
                // 获取第i行、第j列图片控件的id
                Resources resources = getResources();
                String imageName = "puzzlePic" + j + i;         // 形成图片控件名
                int imageID = resources.getIdentifier(imageName, "id", getPackageName());       // 获取图片控件id
                ImageView imageView = findViewById(imageID);                                            // 根据id确定控件名称
                // 获取相应图片id
                String picName = arrayOfPics_2.get(i*4 + j).getResourceName();                          // 获取图片名
                int pictureID = resources.getIdentifier(picName, "drawable", getPackageName()); // 根据图片名获取图片id
                // 将相应控件的资源设置为相应图片
                imageView.setImageResource(pictureID);

                // 设置图片大小
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = picWidth;
                params.height = picWidth;
                imageView.setLayoutParams(params);
                imageView.setOnClickListener((View.OnClickListener) this);
            }
        }

        success = 1;
        for(int i = 0; i<=15; i++){
            int rowNo = i/4;
            int colNo = i%4;

            if(rowNo != arrayOfPics_2.get(i).getRowNo() || colNo != arrayOfPics_2.get(i).getColNo()){
                success = 0;
                break;
            }
        }
        if(success == 1){
            TextView tv1 = findViewById(R.id.tvCongrtulation);
            tv1.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view){
        int currRowNo=0;
        int currColNo=0;
        int hasFound=0;

        if(success == 0){
            int id = view.getId();
            for(int i=0; i<=3; i++)
            {
                if(hasFound == 1){
                    break;
                }

                for(int j=0; j<=3; j++)
                {
                    // 获取第i行、第j列图片控件的id
                    Resources resources = getResources();
                    String imageName = "puzzlePic" + j + i;         // 形成图片控件名
                    int imageID = resources.getIdentifier(imageName, "id", getPackageName());       // 获取图片控件id

                    if(id == imageID){
                        currRowNo = i;
                        currColNo = j;

                        hasFound = 1;
                        break;
                    }
                }
            }

            if(secondClick == 0){
                firstClickedRowNo = currRowNo;
                firstClickedColNo = currColNo;
                secondClick = 1;
            }
            else{
                // 交换数组元素
                Collections.swap(arrayOfPics_2, firstClickedRowNo * 4 + firstClickedColNo, currRowNo * 4 + currColNo);

                // 交换显示的图片
                // 首先处理第一张点击的图片
                Resources resources = getResources();
                String imageName = "puzzlePic" + firstClickedColNo + firstClickedRowNo;         // 形成图片控件名
                int imageID = resources.getIdentifier(imageName, "id", getPackageName());       // 获取图片控件id
                ImageView imageView = findViewById(imageID);                                            // 根据id确定控件名称
                // 获取相应图片id
                String picName = arrayOfPics_2.get(firstClickedRowNo * 4 + firstClickedColNo).getResourceName();                          // 获取图片名
                int pictureID = resources.getIdentifier(picName, "drawable", getPackageName()); // 根据图片名获取图片id
                // 将相应控件的资源设置为相应图片
                imageView.setImageResource(pictureID);

                // 修改第二张图片
                imageView = findViewById(id);                                            // 根据id确定控件名称
                // 获取相应图片id
                picName = arrayOfPics_2.get(currRowNo * 4 + currColNo).getResourceName();                          // 获取图片名
                pictureID = resources.getIdentifier(picName, "drawable", getPackageName()); // 根据图片名获取图片id
                // 将相应控件的资源设置为相应图片
                imageView.setImageResource(pictureID);

                success = 1;
                for(int i = 0; i<=15; i++){
                    int rowNo = i/4;
                    int colNo = i%4;

                    if(rowNo != arrayOfPics_2.get(i).getRowNo() || colNo != arrayOfPics_2.get(i).getColNo()){
                        success = 0;
                        break;
                    }
                }
                if(success == 1){
                    TextView tv1 = findViewById(R.id.tvCongrtulation);
                    tv1.setVisibility(View.VISIBLE);
                }

                secondClick = 0;
            }
        }
    }
}