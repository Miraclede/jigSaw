package com.example.jigsaw.view;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.jigsaw.MainActivity;
import com.example.jigsaw.R;
import com.example.jigsaw.bean.Position;
import com.example.jigsaw.model.Board;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainView extends View {
    private Context context;
    private Bitmap mBitMap;
    private int tileWidth;
    private int tileHeight;
    private Bitmap[] mBitmapTiles;
    private int row = 3;
    private int col = 2;
    private Board mBoard;
    private boolean success;
    private int steps;
    int[][] mtileMatrix;
    Paint paint;

    int dir[][] = {
            {
                    -1, 0
            },//向左移动
            {
                    0, -1
            },//向上
            {
                    1, 0
            },//向右
            {
                    0, 1
            }//向下
    };

    public MainView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();
//        防锯齿化
        paint.setAntiAlias(true);
        init();
        startGame();
    }

//    创造屏幕大小的位图、分割图片并给每块图片赋值
    private void init() {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream stream = assetManager.open("draw.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            mBitMap = Bitmap.createScaledBitmap(bitmap, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tileHeight = mBitMap.getHeight() / row;
        tileWidth = mBitMap.getWidth() / col;
        mBitmapTiles = new Bitmap[row * col];
        int dex = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                /*
                *    2*2
                *   | 0  1 |
                *   | 2  3 |
                * */
                mBitmapTiles[dex++] = Bitmap.createBitmap(mBitMap,j * tileWidth ,i * tileHeight , tileWidth, tileHeight);
            }
        }
    }

//    随机交换相邻小位图顺序
    private void startGame() {
        mBoard = new Board();
        success = false;
        steps = 0;
        mtileMatrix = mBoard.createRandomBoard(row, col);
        invalidate();
    }

//    绘制位图
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#666666"));
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int dex = mtileMatrix[i][j];
//                拼图未成功，不绘制原始最后一块拼图
                if (dex == row * col - 1 && !success) {
                    continue;
                }
                canvas.drawBitmap(mBitmapTiles[dex], j * tileWidth, i * tileHeight, paint);
            }
        }
    }

//    点击移动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!success){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Position temp = xyIntoPos((int) event.getX(), (int) event.getY());
//            判断方向，移动到空白处col*row-1处
                for (int i = 0; i < dir.length; i++) {
                    int newX = temp.getX() + dir[i][0];
                    int newY = temp.getY() + dir[i][1];
                    if (newX >= 0 && newX < col && newY >= 0 && newY < row) {
                        if (mtileMatrix[newY][newX] == col * row - 1) {
                            steps++;
                            int tempInt = mtileMatrix[newY][newX];
                            mtileMatrix[newY][newX] = mtileMatrix[temp.getY()][temp.getX()];
                            mtileMatrix[temp.getY()][temp.getX()] = tempInt;
                            invalidate();
                            if (mBoard.isSuccess(mtileMatrix)) {
                                success = true;
//                        invalidate();
                                String str = String.format("恭喜完成,总共消耗%d步", steps);
                                new AlertDialog.Builder(context)
                                        .setTitle(str)
                                        .setCancelable(false)
                                        .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startGame();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNeutralButton("浏览成果", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                System.exit(0);
                                            }
                                        })
//                            alert.create();
                                        .show();
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private Position xyIntoPos(int x, int y) {
        int addx = x % tileWidth > 0 ? 1 : 0;
        int addy = y % tileHeight > 0 ? 1 : 0;
        int newX = x / tileWidth + addx;
        int newY = y / tileHeight + addy;
        return new Position(newX - 1, newY - 1);
    }
}
