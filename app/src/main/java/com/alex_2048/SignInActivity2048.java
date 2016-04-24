package com.alex_2048;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex_2048.database.CheckerboardData2048;
import com.alex_2048.utils.HttpUtils;
import com.alex_2048.utils.NetworkCheck;
import com.alex_2048.utils.Url2048;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 23/4/16.
 * <p/>
 * 注册页面
 */
public class SignInActivity2048 extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "SignInActivity2048";

    private EditText mAccountEditText;

    private EditText mPasswordEditText;

    private EditText mEnterPasswordAgainEditText;

    private Button mSignInButton;

    private Context mContext;

    private String result = "";

    private ProgressDialog progressDialog = null;

    private List<CheckerboardData2048> mCheckerboardData2048List;

    private Activity self;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initView();
        mContext = this;
        self = this;

    }


    private void initView() {
        mAccountEditText = (EditText) findViewById(R.id.id_account_ed);
        mPasswordEditText = (EditText) findViewById(R.id.id_password_ed);
        mEnterPasswordAgainEditText = (EditText) findViewById(R.id.id_enterpasswordagain_ed);
        mSignInButton = (Button) findViewById(R.id.id_signin_btn);

        if (mSignInButton != null) {
            mSignInButton.setOnClickListener(this);
        }

    }

    String account;
    String password;

    @Override
    public void onClick(View view) {
        // 先检测一下是否有网络
        if (!NetworkCheck.check(this.getApplicationContext())) {
            return;
        }

        account = mAccountEditText.getText().toString();
        password = mPasswordEditText.getText().toString();
        String enterPasswordAgain = mEnterPasswordAgainEditText.getText().toString();

        if (account.equals("")) {
            Toast.makeText(this, "输入账号不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.equals("")) {
            Toast.makeText(this, "输入密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (enterPasswordAgain.equals("")) {
            Toast.makeText(this, "输入确认密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }


        if (!password.equals(enterPasswordAgain)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
            return;
        }


        Map<String, String> params = new HashMap<String, String>();
        params.put("Account", account);
        params.put("Password", password);
        params.put("Type", "SignIn");




        HttpUtils httpUtils = new HttpUtils() {
            @Override
            public void finish() {
                progressDialog.dismiss();
                Log.i(TAG, "finish");
                result = this.getResult();
                Log.i(TAG, "finish: "+result);
                if (result.equals("账号已经存在")) {
                    Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                } else if (result.equals("注册成功")) {
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_LONG).show();
                    isSuccee = true;
                    self.finish();
                } else {
                    Toast.makeText(mContext, "发生未知错误", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void begin() {
                Log.i(TAG, "begin");
            }
        };
        httpUtils.post(Url2048.URL, params, "utf-8");

        progressDialog = ProgressDialog.show(mContext, "请稍等...", "获取数据中...", true);
        httpUtils.execute();


    }

    boolean isSuccee;

    @Override
    protected void onDestroy() {
        if (isSuccee) {
            Intent data = new Intent();
            data.putExtra("Account", account);
            data.putExtra("Password", password);
            this.setResult(1, data);
        }
        super.onDestroy();
    }
}
