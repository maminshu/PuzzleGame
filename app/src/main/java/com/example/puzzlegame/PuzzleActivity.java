package com.example.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<SmallPic> arrayOfPics_2 = new ArrayList<>();
    int secondClick=0;
    int firstClickedRowNo=0;
    int firstClickedColNo=0;
    int success = 0;
    private SoundPool soundPool;
    int soundIdMove;    // 移动图片时的提示音id
    int soundIdSuccess; // 成功时的提示音id
    Calendar cStart;

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

        // 初始化声音效果文件
        AudioAttributes abs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build() ;
        soundPool =  new SoundPool.Builder()
                .setMaxStreams(100)   //设置允许同时播放的流的最大值
                .setAudioAttributes(abs)   //完全可以设置为null
                .build() ;
        soundIdMove=soundPool.load(this,R.raw.move,1);//加载资源，得到soundId
        soundIdSuccess=soundPool.load(this,R.raw.success,1);//加载资源，得到soundId

        // 记录当前时间，作为起始时间，以便计算拼图所用时长
        cStart = Calendar.getInstance();
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

                // 判断是否已成功
                if(success == 1){
                    // 计算所用时间
                    // 获取当前时间（结束时间）
                    Calendar cEnd = Calendar.getInstance();
                    // 开始的时分秒
                    long timeStart = cStart.getTimeInMillis();
                    long timeEnd = cEnd.getTimeInMillis();
                    long timeUsed = timeEnd - timeStart;    // 计算相差的毫秒
                    long secondUsed = timeUsed / 1000;  // 转换为秒
                    long hourUsed = secondUsed / 3600;
                    long minuteUsed = (secondUsed % 3600) / 60;
                    secondUsed = secondUsed % 60;
                    // 拼接输入所用时长
                    String strTimeUsed = "本次用时：";
                    if (hourUsed > 0){
                        String strTemp = String.format("%d小时", hourUsed);
                        strTimeUsed += strTemp;
                    }
                    if (minuteUsed > 0){
                        String strTemp = String.format("%d分", minuteUsed);
                        strTimeUsed += strTemp;
                    }
                    String strTemp = String.format("%d秒", secondUsed);
                    strTimeUsed += strTemp;

                    TextView tvTime = findViewById(R.id.tvTimeUsed);
                    tvTime.setText(strTimeUsed);
                    tvTime.setVisibility(View.VISIBLE);

                    // 显示成功信息
                    TextView tv1 = findViewById(R.id.tvCongrtulation);
                    tv1.setVisibility(View.VISIBLE);
                    // 播放成功的音乐
                    int streamIdSuccess = soundPool.play(soundIdSuccess, 1, 1, 1,0, 1); // 播放成功音乐
                }

                // 播放声音效果
                // 播放移动图片的提示音
//                int streamId= soundPool.play(soundIdMove, 1,1,1,0,1);//播放，得到StreamId
                secondClick = 0;
            }
        }
    }
}