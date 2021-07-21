package com.example.jigsaw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.jigsaw.view.MainView;

public class MainActivity extends AppCompatActivity {
    public static int screenWidth;
    public static int screenHeight;


    /*
    * 优化游戏：
    * 1.将游戏难度暴露给玩家
    * 2.拼图模型可选
    * 3.最高分记录，分享
    * * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      WindowManager.LayoutParams.FLAG_FULLSCREEN ==  0x00000400 == 1024
        getWindow().setFlags(1024,1024);

        DisplayMetrics metrics = new DisplayMetrics();
//        getRealMetrics可获得完整屏幕宽高，不受虚拟返回键影响。
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        setContentView(new MainView(this));
    }

    public static int getScreenWidth(){
        return screenWidth;
    }
    public static int getScreenHeight(){
        return screenHeight;
    }
}