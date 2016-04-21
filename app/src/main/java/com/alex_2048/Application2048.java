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

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.alex_2048.service.Service2048;

/**
 * Created by Alex on 18/4/16.
 * <p>
 * 整个App的生命周期
 */
public class Application2048 extends Application {

    private final String TAG = "Application2048";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: "+ "Alex 2048 app create");
        //Start Service
        Intent intent = new Intent(this, Service2048.class);
        startService(intent);
    }



    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate: "+ "Alex 2048 app terminate");

        super.onTerminate();
    }
}
