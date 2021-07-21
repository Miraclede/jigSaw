package com.example.jigsaw.model;


import android.util.Log;

import com.example.jigsaw.bean.Position;

import java.util.Random;

public class Board {
    private int col;
    private int row;
    int mArray[][];
    int dir[][] = {
            {-1, 0},//向左移动
            {0, -1},//向上
            {1, 0},//向右
            {0, 1}//向下
    };

    private void createIntegerBoard(int row, int col) {
        mArray = new int[row][col];
        int dex = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mArray[i][j] = dex++;
            }
        }
    }

    public int[][] createRandomBoard(int row, int col) {
        if (col < 2 || row < 2) {
            new IllegalArgumentException("行列值必须大于2 ");
        }
        this.row = row;
        this.col = col;
//        创造原始矩阵和位图矩阵一一对应
        createIntegerBoard(row, col);
//        空白位图位置   x存储col信息,y存储row信息
        Position mPoint = new Position(col - 1, row - 1);
//        模仿人随机打乱rd次
        int rd = new Random().nextInt(100) + 20;
        for (int i = 0; i < rd; i++) {
            mPoint = findNext(mPoint);
        }
        return mArray;
    }

    private Position findNext(Position src) {

        int random = new Random().nextInt(4);
        int offsetX = dir[random][0];
        int offsetY = dir[random][1];
        Position newTemp = next(src.getX(), src.getY(), offsetX, offsetY);
        if (newTemp.getX() != -1 && newTemp.getY() != -1) {
            return newTemp;
        }
        return findNext(src);
    }

    private Position next(int x, int y, int offsetX, int offsetY) {
        int sx = x + offsetX;
        int sy = y + offsetY;
        if (sx < 0 || sx >= col || sy < 0 || sy >= row) {
            return new Position(-1, -1);
        }
//        ∵  int dir[][] ={{},{},{},{}}
//        ∴  mArray[row][col]  y值：row；x值:col
        int temp = mArray[sy][sx];
        mArray[sy][sx] = mArray[y][x];
        mArray[y][x] = temp;
        return new Position(sx, sy);
    }

    public boolean isSuccess(int[][] arr) {
        int dex = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length && dex < row * col - 1; j++) {
                    if (arr[dex / col][dex % col] > arr[(dex + 1) / col][(dex + 1) % col]) {
                        return false;
                    }
                dex++;
            }
        }
        return true;
    }
}
