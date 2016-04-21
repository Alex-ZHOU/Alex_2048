/*
 * Copyright  2016 AlexZHOU
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
 *
 */

package com.alex_2048.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Alex on 2016/4/14.
 *
 * 保存棋盘数据
 */

@ModelContainer
@Table(database = Database2048.class)
public class CheckerboardData2048 extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    //Current Scores
    @Column
    public int currentScores;

    //Best Scores
    @Column
    public int bastScores;

    //The checkerboard's data if the user never finished the game back he quit
    @Column(defaultValue = "")
    public String checkerboard;

    @Column(defaultValue = "")
    public String useName;

    @Column(defaultValue = "")
    public String deviceUuid;

}
