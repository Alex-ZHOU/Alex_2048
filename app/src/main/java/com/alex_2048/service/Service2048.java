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

package com.alex_2048.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alex_2048.R;

/**
 * Created by Alex on 18/4/16.
 * <p>
 * 服务,在用户打开应用的同时启动服务,定时推送通知,若是用户正常结束,
 * 即可推出服务,不然就一直在后台跑
 * (换来苹果电脑,偶尔懒得切中午输入法就直接打英文注释)
 * (只是为了应付作业而添加多余的服务)
 */
public class Service2048 extends Service {

    private static final String TAG = "Service2048";
    /**
     * Service sleep time. Two hour.
     */
    private static final int SLEEPTIME = 1000*60*60*2;

    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            while (true){

                // 通知实现代码 点击通知跳转到本项目的Github界面
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                String urlText = "https://github.com/Alex-ZHOU/Alex_2048";

                Uri uri = Uri.parse(urlText);
                Intent  resultIntent = new  Intent(Intent.ACTION_VIEW, uri);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentTitle("Alex 2048");
                builder.setContentText("Alex 2048 是开源软件,点击看代码!欢迎修改!!");
                builder.setSmallIcon(R.drawable.ic_alex2048);
                builder .setTicker("Alex 2048");
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);

                Notification notification = builder.build();

                notificationManager.notify(0, notification);

                // 线程睡眠,2小时通知一次
                try {
                    Thread.sleep(SLEEPTIME);
                    Log.i(TAG, "handleMessage");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + "service create");

        // Just new a thread in background.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper serviceLooper;

        serviceLooper = thread.getLooper();

        mServiceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + "service starting");

        // Send a massage to handler.
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");

        //Don't provide binding,so return null.

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service done
        Log.i(TAG, "onDestroy: " + "service done");
    }
}
