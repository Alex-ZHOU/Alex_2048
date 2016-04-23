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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.alex_2048.database.UserData2048;
import com.alex_2048.service.Service2048;
import com.alex_2048.utils.DeviceUuidFactory;
import com.alex_2048.utils.NetworkCheck;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;


public class LoginInActivity2048 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginInActivity";

    private Button mLoginInButton;
    private Button mTouristButton;
    private TextView mSignIn;
    private EditText mPasswordEditText;
    private EditText mAccountEditText;

    private RelativeLayout mInput;
    private RelativeLayout mCommit;

    private DeviceUuidFactory mDeviceUuidFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkCheck.check(this.getApplicationContext());
        mDeviceUuidFactory = new DeviceUuidFactory(getApplicationContext());
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
        if (!NetworkCheck.check(this.getApplicationContext())){
            return;
        }
        Intent intent;
        UserData2048 userData2048;
        switch (v.getId()) {
            //Click login button
            case R.id.id_login_btn:
                Log.i(TAG, "onClick: " + "id_login_btn" + R.id.id_login_btn);
                String mPassword;
                String mAccount;
                mAccount = mAccountEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();
                Log.i(TAG, "onClick: " + "mPassword:" + mPassword + " mAccount:" + mAccount);


                // FIXME: 2016/4/10
                intent = new Intent(LoginInActivity2048.this, MainActivity2048.class);
                startActivity(intent);

                finish();
                break;
            //点击了游客登录按钮
            case R.id.id_tourist_btn:
                Log.i(TAG, "onClick: " + "id_tourist_btn:" + R.id.id_tourist_btn);

                Log.i(TAG, "onClick: " + "UUID:" + mDeviceUuidFactory.getDeviceUuid());

                userData2048 = new Select().from(UserData2048.class).querySingle();

                if (userData2048==null){
                    userData2048 = new UserData2048();
                    userData2048.deviceUuid = mDeviceUuidFactory.getDeviceUuid().toString();
                    userData2048.isLogin = false;
                    userData2048.save();
                    Log.i(TAG, "onClick: "+"保存新的游客");
                }else {
                    if (userData2048.deviceUuid.equals(mDeviceUuidFactory.getDeviceUuid().toString())){
                        Log.i(TAG, "onClick: "+"UUid正确,普通登陆");
                    }else{
                        Log.i(TAG, "onClick: "+"UUid错误,重新初始化");
                        userData2048.delete();
                        userData2048 = new UserData2048();
                        userData2048.deviceUuid = mDeviceUuidFactory.getDeviceUuid().toString();
                        userData2048.isLogin = false;
                        userData2048.save();
                    }
                }

                //// FIXME: 23/4/16
                intent = new Intent(LoginInActivity2048.this, MainActivity2048.class);
                intent.putExtra("UUID",mDeviceUuidFactory.getDeviceUuid().toString());
                startActivity(intent);
                finish();
                break;
            case R.id.id_signin_tv:
                Log.i(TAG, "onClick: " + "id_signin_tv:" + R.id.id_signin_tv);
                intent = new Intent(LoginInActivity2048.this, SignInActivity2048.class);
                intent.putExtra("UUID",mDeviceUuidFactory.getDeviceUuid().toString());
                startActivity(intent);
                break;

        }

    }

    private UserData2048 mUserData2048;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
