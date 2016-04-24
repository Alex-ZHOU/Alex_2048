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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alex_2048.database.TouristData2048;
import com.raizlabs.android.dbflow.sql.language.Select;

public class MainActivity2048 extends Activity implements OnClickListener {

    private final String TAG = "MainActivity2048";

    private TextView[][] tv = new TextView[4][4];

    private TextView tv_score;

    private TextView tv_addScore;

    private TextView tv_BestScore;

    private TextView tv_type;

    private Button btn_restart;

    private Button btn_back;

    private int[][] data = new int[4][4];


    private int CurrentScores = 0;
    private int BastScorses = 0;

    private TouchEventDetection touchEventDetection;
    private Algorithm2048 algorithm2048;

    private Handler2048 handler2048;

    private String mType;
    private String mCheckerBoard;
    private int mCurrentScores;
    private int mBastScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mType = this.getIntent().getStringExtra("Type");
        mCheckerBoard = this.getIntent().getStringExtra("CheckerBoard");
        mCurrentScores = this.getIntent().getIntExtra("CurrentScores",0);
        mBastScores = this.getIntent().getIntExtra("BastScores",0);

        if (mCheckerBoard != null) {
            String[] checkerBoardData = mCheckerBoard.split("\\|");
            int temp = 0;
//            for (int i = 0;i<checkerBoardData.length;i++){
//                Log.i(TAG, "onCreate: "+checkerBoardData[i]);
//            }

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {

                    data[i][j] = Integer.parseInt(checkerBoardData[temp]);
                    temp++;
                }
            }
        }


        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_addScore = (TextView) findViewById(R.id.tv_addScore);
        tv_type = (TextView) findViewById(R.id.id_type_tv);
        tv_BestScore = (TextView) findViewById(R.id.tv_bestscore);
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

        CurrentScores =mCurrentScores;

        BastScorses = mBastScores;

        tv_BestScore.setText(BastScorses+"");
        tv_type.setText(mType);


        handler2048 = new Handler2048(tv, tv_score, tv_BestScore,tv_addScore, data, algorithm2048, CurrentScores, BastScorses);
        if (CurrentScores == 0) {
            init2();
        }else{
            handler2048.sendEmptyMessage(Handler2048.ToutistRestart);
        }

    }

    public void init2() {
        handler2048.sendEmptyMessage(Handler2048.INIT);
        CurrentScores = 0;
    }

    @Override
    protected void onPause() {

        if (mType.equals("Tourist")) {
            handler2048.sendEmptyMessage(Handler2048.ToutistSaveData);
        }
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2048.this);
                builder.setMessage("确认退出重新开始吗？");

                builder.setTitle("提示");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        init2();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();

                break;
            case R.id.btn_back:
                dialog();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
        }
        return false;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2048.this);
        builder.setMessage("确认退出游戏吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                MainActivity2048.this.finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
