package com.alex_2048;

import android.alex.utils.TouchEventDetection;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class Handler2048 extends Handler {

  public static int INIT = 204820;

  private TextView tv[][] = new TextView[4][4];
  private TextView tv_score;
  private TextView tv_addScore;
  int data[][] = new int[4][4];

  Algorithm2048 algorithm2048;

  private int score = 0;

  public Handler2048(TextView[][] tv, TextView tv_score, TextView tv_addScore, int[][] data,
      Algorithm2048 algorithm2048, int score) {
    super();
    this.tv = tv;
    this.tv_score = tv_score;
    this.data = data;
    this.algorithm2048 = algorithm2048;
    this.score = score;
    this.tv_addScore = tv_addScore;
  }

  @Override public void handleMessage(Message msg) {
    Animation2048 ma = new Animation2048();

    if (msg.what == Handler2048.INIT) {
      score = 0;
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          data[i][j] = 0;
        }
      }
      algorithm2048.setAddScore(0);
      data = ma.addNew(data, tv);
      data = ma.addNew(data, tv);
    }

    if (msg.what == TouchEventDetection.ACTION_UP) {
      algorithm2048.getResult(data, tv, TouchEventDetection.ACTION_UP);
      data = ma.addNew(data, tv);
    }
    if (msg.what == TouchEventDetection.ACTION_DOWN) {
      algorithm2048.getResult(data, tv, TouchEventDetection.ACTION_DOWN);
      data = ma.addNew(data, tv);
    }

    if (msg.what == TouchEventDetection.ACTION_LEFT) {
      algorithm2048.getResult(data, tv, TouchEventDetection.ACTION_LEFT);
      data = ma.addNew(data, tv);
    }

    if (msg.what == TouchEventDetection.ACTION_RIGHT) {
      algorithm2048.getResult(data, tv, TouchEventDetection.ACTION_RIGHT);
      data = ma.addNew(data, tv);
    }

    tv_addScore.setText("+" + algorithm2048.getAddScore());
    tv_addScore.setVisibility(View.GONE);

    score = score + algorithm2048.getAddScore();

    tv_score.setText("" + score);
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (data[i][j] == 0) {
          tv[i][j].setText(" ");
          tv[i][j].setBackgroundColor(Color.WHITE);
          tv[i][j].setVisibility(View.INVISIBLE);
        } else {
          tv[i][j].setBackgroundColor(Color2048.setColorByData(data[i][j]));
          tv[i][j].setVisibility(View.VISIBLE);
          tv[i][j].setText(data[i][j] + "");
        }
      }
    }

    if (algorithm2048.checkGameOver(data)) {
      tv_score.setText("Gama Over" + score);
    }
  }
}
