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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginInActivity2048 extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = "LoginInActivity";

  private Button mLoginInButton;
  private EditText mPasswordEditText;
  private EditText mAccountEditText;
  private String mPassword;
  private String mAccount;

  private RelativeLayout mTitleBar;
  private RelativeLayout mInput;
  private RelativeLayout mCommit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);


    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    String DEVICE_ID = tm.getDeviceId();


    initView();
  }

  //初始化各个View
  private void initView() {
    mLoginInButton = (Button) findViewById(R.id.id_login_btn);
    mPasswordEditText = (EditText) findViewById(R.id.id_password_ed);
    mAccountEditText = (EditText) findViewById(R.id.id_account_ed);
    mTitleBar = (RelativeLayout) findViewById(R.id.id_titlebar_rl);
    mInput = (RelativeLayout) findViewById(R.id.id_input_rl);
    mCommit = (RelativeLayout) findViewById(R.id.id_commit_id);

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
  }

  /**
   * 点击监听事件
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.id_login_btn:
        Log.i(TAG, "onClick: " + "id_login_btn");
        mAccount = mAccountEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        Toast.makeText(getApplicationContext(),DEVICE_ID,Toast.LENGTH_SHORT).show();

        Log.i(TAG, "onClick: " + "mPassword:" + mPassword + " mAccount:" + mAccount);
        Intent intent = new Intent(LoginInActivity2048.this, MainActivity2048.class);
        startActivity(intent);
        finish();
        break;
    }
  }
}
