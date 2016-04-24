package com.alex_2048.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 24/4/16.
 * <p/>
 * 网络请求工具类
 */
public abstract class HttpUtils extends AsyncTask<Void, Void, List<String>> {

    private final static String TAG = "HttpUtils";

    private static String mResult;

    private static boolean isFinish;


    @Override
    protected List<String> doInBackground(Void... voids) {
        mResult = "";

        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] data = stringBuffer.toString().getBytes();//获得请求体


        URL url = null;

        try {
            url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 下面啥发送数据
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            urlConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            urlConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            urlConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(data);

            // 下面数据接收
            if (urlConnection.getResponseCode() == 200) {

                InputStreamReader inr = new InputStreamReader(urlConnection.getInputStream());

                BufferedReader br = new BufferedReader(inr);

                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    mResult += readLine;
                }
                Log.i(TAG, "run: " + "获取的数据" + mResult);
                inr.close();
                urlConnection.disconnect();
            }

        } catch (IOException e) {
            Log.e(TAG, "run: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    Map<String, String> params;
    String encode;
    String urlString;


    @Override
    protected void onPostExecute(List<String> strings) {
        finish();
    }

    @Override
    protected void onPreExecute() {
        begin();

    }

    public String post(final String urlString, final Map<String, String> params, final String encode) {

        this.params = params;
        this.encode = encode;
        this.urlString = urlString;

        return mResult;

    }

    public String getResult(){
        return mResult;
    }

    public abstract void finish();
    public abstract void begin();



//    public static boolean isFinish() {
//        return isFinish;
//    }
}
