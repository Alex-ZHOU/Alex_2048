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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alex_2048.database.TouristData2048;
import com.alex_2048.utils.DeviceUuidFactory;
import com.alex_2048.utils.HttpUtils;
import com.alex_2048.utils.NetworkCheck;
import com.alex_2048.utils.Url2048;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginInActivity2048 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginInActivity2048";

    private Button mLoginInButton;
    private Button mTouristButton;
    private TextView mSignIn;
    private EditText mPasswordEditText;
    private EditText mAccountEditText;

    private RelativeLayout mInput;
    private RelativeLayout mCommit;

    private DeviceUuidFactory mDeviceUuidFactory;

    private Activity self;

    private ProgressDialog progressDialog = null;

    private String result = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkCheck.check(this.getApplicationContext());
        mDeviceUuidFactory = new DeviceUuidFactory(getApplicationContext());
        self = this;
        initView();
    }



    //初始化各个View
    private void initView() {
        mLoginInButton = (Button) findViewById(R.id.id_login_btn);
        mTouristButton = (Button) findViewById(R.id.id_tourist_btn);
        mPasswordEditText = (EditText) findViewById(R.id.id_password_ed);
        mAccountEditText = (EditText) findViewById(R.id.id_account_ed);
        mInput = (RelativeLayout) findViewById(R.id.id_input_rl);
        mCommit = (RelativeLayout) findViewById(R.id.id_commit_id);
        mSignIn = (TextView) findViewById(R.id.id_signin_tv);

        // 淡出动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        mInput.startAnimation(alphaAnimation);

        // 滑动弹出动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 500f, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(1000);
        mCommit.startAnimation(translateAnimation);

        mLoginInButton.setOnClickListener(this);
        mTouristButton.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        TouristData2048 touristData2048;
        switch (v.getId()) {
            //Click login button
            case R.id.id_login_btn:
                if (!NetworkCheck.check(this.getApplicationContext())){
                    return;
                }
                Log.i(TAG, "onClick: " + "id_login_btn" + R.id.id_login_btn);

                String mPassword;
                String mAccount;
                mAccount = mAccountEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();

                if (mAccount.equals("")) {
                    Toast.makeText(this, "输入账号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mPassword.equals("")) {
                    Toast.makeText(this, "输入密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i(TAG, "onClick: " + "mPassword:" + mPassword + " mAccount:" + mAccount);

                HttpUtils httpUtils = new HttpUtils() {
                    @Override
                    public void finish() {
                        progressDialog.dismiss();
                        Log.i(TAG, "finish");
                        result = this.getResult();
                        Log.i(TAG, "finish: "+result);

                        //解析json
                        try {
                            JSONObject ojb =  new JSONObject(result);
                            ojb.getString("Login2048");
                            Log.i(TAG, "finish: "+ojb.getString("Login2048"));
                            if (ojb.getString("Login2048").equals("Succee")) {
                                Toast.makeText(self, "登陆成功", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LoginInActivity2048.this, MainActivity2048.class);intent.putExtra("Type","Tourist");
                                intent.putExtra("Type","User");
                                intent.putExtra("CheckerBoard",ojb.getString("CheckerBoard"));
                                intent.putExtra("CurrentScores",ojb.getInt("CurrentScores"));
                                intent.putExtra("BastScores",ojb.getInt("BastScores"));
                                intent.putExtra("Username",ojb.getString("Username"));
                                startActivity(intent);

                                self.finish();
                            } else if (ojb.getString("Login2048").equals("Failure")) {
                                Toast.makeText(self, "密码或者账号错误", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(self, "发生未知错误", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void begin() {
                        Log.i(TAG, "begin");
                    }
                };

                Map<String, String> params = new HashMap<String, String>();
                params.put("Account", mAccount);
                params.put("Password", mPassword);
                params.put("Type", "LoginIn");
                httpUtils.post(Url2048.URL, params, "utf-8");

                progressDialog = ProgressDialog.show(self, "请稍等...", "获取数据中...", true);
                httpUtils.execute();


                break;



            //点击了游客登录按钮
            case R.id.id_tourist_btn:
                Log.i(TAG, "onClick: " + "id_tourist_btn:" + R.id.id_tourist_btn);

                Log.i(TAG, "onClick: " + "UUID:" + mDeviceUuidFactory.getDeviceUuid());

                touristData2048 = new Select().from(TouristData2048.class).querySingle();

                if (touristData2048==null){
                    touristData2048 = new TouristData2048();
                    touristData2048.deviceUuid = mDeviceUuidFactory.getDeviceUuid().toString();
                    touristData2048.save();
                    Log.i(TAG, "onClick: "+"保存新的游客");

                }else {
                    if (touristData2048.deviceUuid.equals(mDeviceUuidFactory.getDeviceUuid().toString())){
                        Log.i(TAG, "onClick: "+"UUid正确,普通登陆");
                    }else{
                        Log.i(TAG, "onClick: "+"UUid错误,重新初始化");
                        touristData2048.delete();
                        touristData2048 = new TouristData2048();
                        touristData2048.deviceUuid = mDeviceUuidFactory.getDeviceUuid().toString();
                        touristData2048.save();
                    }
                }

                intent = new Intent(LoginInActivity2048.this, MainActivity2048.class);
                intent.putExtra("Type","Tourist");
                intent.putExtra("CheckerBoard",touristData2048.checkerboard);
                intent.putExtra("CurrentScores",touristData2048.currentScores);
                intent.putExtra("BastScores",touristData2048.bastScores);
                intent.putExtra("Username","");
                startActivity(intent);
                finish();
                break;



            // 点击注册
            case R.id.id_signin_tv:
                if (!NetworkCheck.check(this.getApplicationContext())){
                    return;
                }
                Log.i(TAG, "onClick: " + "id_signin_tv:" + R.id.id_signin_tv);
                intent = new Intent(LoginInActivity2048.this, SignInActivity2048.class);
                intent.putExtra("UUID",mDeviceUuidFactory.getDeviceUuid().toString());
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Bundle bundle = data.getExtras();
//
//        Log.i(TAG, "onActivityResult: "+bundle.getString("Account"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
