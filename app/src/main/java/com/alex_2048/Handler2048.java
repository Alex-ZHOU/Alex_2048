package com.alex_2048;

import android.alex.utils.TouchEventDetection;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alex_2048.database.TouristData2048;
import com.alex_2048.utils.HttpUtils;
import com.alex_2048.utils.Url2048;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Handler2048 extends Handler {

    private final String TAG = "Handler2048";

    public static int INIT = 204820;
    public static int ToutistSaveData = 204820 - 1;
    public static int UserSaveData = 204820 - 3;
    public static int ToutistRestart = 204820 - 2;

    private TextView tv[][] = new TextView[4][4];
    private TextView tv_score;
    private TextView tv_addScore;
    private TextView tv_bestScore;
    int data[][] = new int[4][4];

    Algorithm2048 algorithm2048;

    private int score = 0;
    private int bestScore = 0;

    private String UserName;

    public void setUserName(String UserName){
        this.UserName = UserName;
    }

    public Handler2048(TextView[][] tv, TextView tv_score, TextView tv_bestScore, TextView tv_addScore, int[][] data,
                       Algorithm2048 algorithm2048, int score, int bestScore) {
        super();
        this.tv = tv;
        this.tv_score = tv_score;
        this.tv_bestScore = tv_bestScore;
        this.data = data;
        this.algorithm2048 = algorithm2048;
        this.score = score;
        this.bestScore = bestScore;
        this.tv_addScore = tv_addScore;
    }

    @Override
    public void handleMessage(Message msg) {
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

        if (msg.what == ToutistSaveData){
            TouristData2048 touristData2048 = new Select().from(TouristData2048.class).querySingle();
            touristData2048.currentScores = score;
            touristData2048.bastScores = bestScore;
            String temp = "";
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    temp += data[i][j] + "|";
                }
            }
            Log.i(TAG, "onPause: "+temp+" "+score+" " +bestScore);
            touristData2048.checkerboard = temp;
            touristData2048.update();
        }


        if (msg.what == UserSaveData){



            String temp = "";
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    temp += data[i][j] + "|";
                }
            }
            Log.i(TAG, "onPause: User"+temp+" "+score+" " +bestScore);
            JSONObject obj = new JSONObject();

            try {
                obj.put("CurrentScores",score);
                obj.put("BestScore",bestScore);
                obj.put("Username", this.UserName);
                obj.put("CheckerBoard", temp);

                HttpUtils httpUtils = new HttpUtils() {
                    @Override
                    public void finish() {

                    }

                    @Override
                    public void begin() {

                    }
                };

                Map<String, String> params = new HashMap<String, String>();
                params.put("UserData", obj.toString());
                params.put("Type", "UserData");
                httpUtils.post(Url2048.URL, params, "utf-8");
                httpUtils.execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        if (score > bestScore) {
            bestScore = score;
            tv_bestScore.setText("" + bestScore);
        }
        Log.i(TAG, "handleMessage: "+tv_bestScore.getVisibility());
        if (algorithm2048.checkGameOver(data)) {
            tv_score.setText("Gama Over" + score);
        }
    }
}
