/*
 * Copyright 2016 AlexZHOU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alex_2048;

import android.alex.utils.TouchEventDetection;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2048 extends Activity implements OnClickListener {

  private TextView[][] tv = new TextView[4][4];

  private TextView tv_score;

  private TextView tv_addScore;

  private Button btn_restart;

  private Button btn_back;

  private int[][] data = new int[4][4];


  private int score = 0;

  private TouchEventDetection touchEventDetection;
  private Algorithm2048 algorithm2048;

  private Handler2048 handler2048;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tv_score = (TextView) findViewById(R.id.tv_score);
    tv_addScore = (TextView) findViewById(R.id.tv_addScore);
    tv[0][0] = (TextView) findViewById(R.id.tv_0_0);
    tv[0][1] = (TextView) findViewById(R.id.tv_0_1);
    tv[0][2] = (TextView) findViewById(R.id.tv_0_2);
    tv[0][3] = (TextView) findViewById(R.id.tv_0_3);
    tv[1][0] = (TextView) findViewById(R.id.tv_1_0);
    tv[1][1] = (TextView) findViewById(R.id.tv_1_1);
    tv[1][2] = (TextView) findViewById(R.id.tv_1_2);
    tv[1][3] = (TextView) findViewById(R.id.tv_1_3);
    tv[2][0] = (TextView) findViewById(R.id.tv_2_0);
    tv[2][1] = (TextView) findViewById(R.id.tv_2_1);
    tv[2][2] = (TextView) findViewById(R.id.tv_2_2);
    tv[2][3] = (TextView) findViewById(R.id.tv_2_3);
    tv[3][0] = (TextView) findViewById(R.id.tv_3_0);
    tv[3][1] = (TextView) findViewById(R.id.tv_3_1);
    tv[3][2] = (TextView) findViewById(R.id.tv_3_2);
    tv[3][3] = (TextView) findViewById(R.id.tv_3_3);

    btn_restart = (Button) findViewById(R.id.btn_restart);
    btn_back = (Button) findViewById(R.id.btn_back);

    btn_restart.setOnClickListener(this);
    btn_back.setOnClickListener(this);

    touchEventDetection = new TouchEventDetection(this);
    algorithm2048 = new Algorithm2048();
    handler2048 = new Handler2048(tv, tv_score, tv_addScore, data, algorithm2048, score);

    init2();
  }

  public void init2() {
    handler2048.sendEmptyMessage(Handler2048.INIT);
    score = 0;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    // ViewDragHelper.

    switch (touchEventDetection.getAction(event)) {

      case TouchEventDetection.ACTION_UP:

        Animation2048 ma = new Animation2048(handler2048, MainActivity2048.this, tv);
        ma.animationUp(data, getResources().getDimension(R.dimen.margin_top_height));
        // Toast.makeText(getApplicationContext(), "up", Toast.LENGTH_SHORT)
        // .show();

        break;
      case TouchEventDetection.ACTION_DOWN:

        final Animation2048 ma2 = new Animation2048(handler2048, MainActivity2048.this, tv);
        ma2.animationDown(data, getResources().getDimension(R.dimen.margin_top_height));
        // Toast.makeText(getApplicationContext(), "down",
        // Toast.LENGTH_SHORT).show();

        break;
      case TouchEventDetection.ACTION_LEFT:

        final Animation2048 ma3 = new Animation2048(handler2048, MainActivity2048.this, tv);
        ma3.animationLeft(data, getResources().getDimension(R.dimen.margin_top_height));
        // Toast.makeText(getApplicationContext(), "left",
        // Toast.LENGTH_SHORT)
        // .show();

        break;
      case TouchEventDetection.ACTION_RIGHT:
        final Animation2048 ma4 = new Animation2048(handler2048, MainActivity2048.this, tv);
        ma4.animationRight(data, getResources().getDimension(R.dimen.margin_top_height));

        // Toast.makeText(getApplicationContext(), "right",
        // Toast.LENGTH_SHORT)
        // .show();
        break;
      default:
        break;
    }

    return super.onTouchEvent(event);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_restart:
        init2();
        break;
      case R.id.btn_back:
        Toast.makeText(getApplicationContext(), "按钮无效", Toast.LENGTH_SHORT).show();
        break;
    }
    //if (v.equals(btn_restart)) {
    //  init2();
    //}
    //if (v.equals(btn_back)) {
    //  Toast.makeText(getApplicationContext(), "按钮无效", Toast.LENGTH_SHORT).show();
    //}
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
