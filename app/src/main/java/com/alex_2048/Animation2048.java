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

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

//动画实现类
public class Animation2048 extends Animation {

  private TextView[][] tv = new TextView[4][4];

  private Handler2048 handler;
  private Context context;

  public Animation2048() {
    super();
  }

  // 构造方法,传入对应的参数
  public Animation2048(Handler2048 handler, Context context, TextView[][] tv) {
    super();
    this.handler = handler;
    this.tv = tv;
    this.context = context;
  }

  public void animationUp(int[][] a, float moveDp) {
    TranslateAnimation translateAnimation[][] =
        new TranslateAnimation[4][4];// 每个View都定义了对应的TranslateAnimation
    int temp[] = new int[5];// temp 0 到 3 用于记录对应i列的数据 ,temp4用户数据交互的临时数据
    int time[] = new int[4];// 用于记录当期列每个数据要移动距离的倍数

    boolean flag = false;// 标志是否有View需要执行动画

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        temp[j] = a[j][i];
      }

      // 每次执行前初始化time,用于记录单列要移动的长度的倍数
      for (int j = 0; j < time.length; j++) {
        time[j] = 0;
      }

      for (int j = 0; j < 4; j++) {

        if (temp[j] == 0) {
          for (int j2 = j + 1; j2 < 4; j2++) {
            time[j2]++;
          }
          continue;
        }
        int k = j;
        while (k + 1 <= 3) {
          if (temp[k + 1] == 0) {
            k++;
            continue;
          } else if (temp[k + 1] != temp[j]) {
            k++;
            break;
          } else if (temp[j] == temp[k + 1]) {
            temp[k + 1] = -1;
            for (int j2 = k + 1; j2 < 4; j2++) {

              time[j2]++;
            }
            k++;
            break;
          }
        }
      }

      // 下面的计算次数方法偶尔会出Bug
      // for (int j = 1; j < a.length; j++) {
      // time[j] = 0;// 每次使用要进行初始化,不然数据会越来越大,影响后续的列
      //
      // int k = j;// 设置k用于控制while的循环次数
      // while (k - 1 >= 0) {
      // // if (a[k - 1][i] == 0) {
      // // temp[4] = a[k - 1][i];
      // // a[k - 1][i] = a[k][i];
      // // a[k][i] = temp[4];// 交换2个数的值
      // // time[j]++;// 统计动画要执行的倍数
      // // }
      // if (temp[k - 1] == 0) {
      // temp[4] = temp[k - 1];
      // temp[k - 1] = temp[k];
      // temp[k] = temp[4];// 交换2个数的值
      // time[j]++;// 统计动画要执行的倍数
      // }
      // k--;
      // }
      // }
      // // 不能用排序后的数组进行运算,所以用原来二维数组的数据进行计数
      // for (int j = 0; j < 3; j++) {
      // if (a[j][i] == a[j + 1][i] && a[j + 1][i] != 0) {
      //
      // for (int j3 = j + 1; j3 < time.length; j3++) {
      // time[j3]++;
      // }
      // j++;
      // }
      // }

      for (int j = 0; j < 4; j++) {
        translateAnimation[j][i] = new TranslateAnimation(0, 0, 0, -time[j] * moveDp);
        translateAnimation[j][i].setFillAfter(false);// 动画执行完成后返回初始位置,下面那句话是相同的效果
        translateAnimation[j][i].setFillBefore(true);

        translateAnimation[j][i].setDuration(100);// 动画执行的时间

        if (!flag
            && tv[j][i].getVisibility() == View.VISIBLE
            && time[j] != 0) {// 此判断语句主要目的是将一个需要的移动的View设置监听,在View滑动执行结束后在调用Handler
          flag = true;
          translateAnimation[j][i].setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              handler.sendEmptyMessage(TouchEventDetection.ACTION_UP);
            }
          });
        }
      }
    }

    if (flag) {// flag为true表示有需要移动的动画
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (tv[j][i].getVisibility() == View.VISIBLE) {// 如果view不为INVISIBLE状态时,执行动画
            tv[j][i].startAnimation(translateAnimation[j][i]);
          }
        }
      }
    } else {// 没有需要执行的动画,直接返回调用handler执行数组逻辑操作以及更新UI
      // handler.sendEmptyMessage(TouchEventDetection.ACTION_UP);
    }
  }

  public void animationDown(final int[][] a, float moveDp) {
    TranslateAnimation translateAnimation[][] =
        new TranslateAnimation[4][4];// 每个View都定义了对应的TranslateAnimation
    int temp[] = new int[5];// temp 0 到 3 用于记录对应i列的数据 ,temp4用户数据交互的临时数据
    int time[] = new int[4];// 用于记录当期列每个数据要移动距离的倍数

    boolean flag = false;// 标志是否有View需要执行动画

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        temp[j] = a[j][i];
      }

      // 每次执行前初始化time,用于记录单列要移动的长度的倍数
      for (int j = 0; j < time.length; j++) {
        time[j] = 0;
      }
      for (int j = 3; j >= 0; j--) {

        if (temp[j] == 0) {
          for (int j2 = j - 1; j2 >= 0; j2--) {
            time[j2]++;
          }
          continue;
        }
        int k = j;
        while (k - 1 >= 0) {
          if (temp[k - 1] == 0) {
            k--;
            continue;
          } else if (temp[k - 1] != temp[j]) {
            k--;
            break;
          } else if (temp[j] == temp[k - 1]) {
            temp[k - 1] = -1;
            for (int j2 = k - 1; j2 >= 0; j2--) {

              time[j2]++;
            }
            k--;
            break;
          }
        }
      }

      for (int j = 0; j < 4; j++) {
        translateAnimation[j][i] = new TranslateAnimation(0, 0, 0, time[j] * moveDp);
        translateAnimation[j][i].setFillAfter(false);// 动画执行完成后返回初始位置,下面那句话是相同的效果
        translateAnimation[j][i].setFillBefore(true);

        translateAnimation[j][i].setDuration(100);// 动画执行的时间

        if (!flag
            && tv[j][i].getVisibility() == View.VISIBLE
            && time[j] != 0) {// 此判断语句主要目的是将一个需要的移动的View设置监听,在View滑动执行结束后在调用Handler
          flag = true;
          translateAnimation[j][i].setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              handler.sendEmptyMessage(TouchEventDetection.ACTION_DOWN);
            }
          });
        }
      }
    }

    if (flag) {// flag为true表示有需要移动的动画
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (tv[j][i].getVisibility() == View.VISIBLE) {// 如果view不为INVISIBLE状态时,执行动画
            tv[j][i].startAnimation(translateAnimation[j][i]);
          }
        }
      }
    } else {// 没有需要执行的动画,直接返回调用handler执行数组逻辑操作以及更新UI
      // handler.sendEmptyMessage(TouchEventDetection.ACTION_DOWN);
    }
  }

  public void animationRight(final int[][] a, float moveDp) {
    TranslateAnimation translateAnimation[][] =
        new TranslateAnimation[4][4];// 每个View都定义了对应的TranslateAnimation
    int temp[] = new int[5];// temp 0 到 3 用于记录对应i列的数据 ,temp4用户数据交互的临时数据
    int time[] = new int[4];// 用于记录当期列每个数据要移动距离的倍数

    boolean flag = false;// 标志是否有View需要执行动画

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        temp[j] = a[i][j];
      }

      // 每次执行前初始化time,用于记录单列要移动的长度的倍数
      for (int j = 0; j < time.length; j++) {
        time[j] = 0;
      }
      for (int j = 3; j >= 0; j--) {

        if (temp[j] == 0) {
          for (int j2 = j - 1; j2 >= 0; j2--) {
            time[j2]++;
          }
          continue;
        }
        int k = j;
        while (k - 1 >= 0) {
          if (temp[k - 1] == 0) {
            k--;
            continue;
          } else if (temp[k - 1] != temp[j]) {
            k--;
            break;
          } else if (temp[j] == temp[k - 1]) {
            temp[k - 1] = -1;
            for (int j2 = k - 1; j2 >= 0; j2--) {

              time[j2]++;
            }
            k--;
            break;
          }
        }
      }

      for (int j = 0; j < 4; j++) {
        translateAnimation[i][j] = new TranslateAnimation(0, time[j] * moveDp, 0, 0);
        translateAnimation[i][j].setFillAfter(false);// 动画执行完成后返回初始位置,下面那句话是相同的效果
        translateAnimation[i][j].setFillBefore(true);

        translateAnimation[i][j].setDuration(100);// 动画执行的时间

        if (!flag
            && tv[i][j].getVisibility() == View.VISIBLE
            && time[j] != 0) {// 此判断语句主要目的是将一个需要的移动的View设置监听,在View滑动执行结束后在调用Handler
          flag = true;
          translateAnimation[i][j].setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              handler.sendEmptyMessage(TouchEventDetection.ACTION_RIGHT);
            }
          });
        }
      }
    }

    if (flag) {// flag为true表示有需要移动的动画
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (tv[j][i].getVisibility() == View.VISIBLE) {// 如果view不为INVISIBLE状态时,执行动画
            tv[j][i].startAnimation(translateAnimation[j][i]);
          }
        }
      }
    } else {// 没有需要执行的动画,直接返回调用handler执行数组逻辑操作以及更新UI
      // handler.sendEmptyMessage(TouchEventDetection.ACTION_DOWN);
    }
  }

  public void animationLeft(int[][] a, float moveDp) {
    TranslateAnimation translateAnimation[][] =
        new TranslateAnimation[4][4];// 每个View都定义了对应的TranslateAnimation
    int temp[] = new int[5];// temp 0 到 3 用于记录对应i列的数据 ,temp4用户数据交互的临时数据
    int time[] = new int[4];// 用于记录当期列每个数据要移动距离的倍数

    boolean flag = false;// 标志是否有View需要执行动画

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        temp[j] = a[i][j];
      }

      // 每次执行前初始化time,用于记录单列要移动的长度的倍数
      for (int j = 0; j < time.length; j++) {
        time[j] = 0;
      }

      for (int j = 0; j < 4; j++) {

        if (temp[j] == 0) {
          for (int j2 = j + 1; j2 < 4; j2++) {
            time[j2]++;
          }
          continue;
        }
        int k = j;
        while (k + 1 <= 3) {
          if (temp[k + 1] == 0) {
            k++;
            continue;
          } else if (temp[k + 1] != temp[j]) {
            k++;
            break;
          } else if (temp[j] == temp[k + 1]) {
            temp[k + 1] = -1;
            for (int j2 = k + 1; j2 < 4; j2++) {

              time[j2]++;
            }
            k++;
            break;
          }
        }
      }

      for (int j = 0; j < 4; j++) {
        translateAnimation[i][j] = new TranslateAnimation(0, -time[j] * moveDp, 0, 0);
        translateAnimation[i][j].setFillAfter(false);// 动画执行完成后返回初始位置,下面那句话是相同的效果
        translateAnimation[i][j].setFillBefore(true);

        translateAnimation[i][j].setDuration(100);// 动画执行的时间

        if (!flag
            && tv[i][j].getVisibility() == View.VISIBLE
            && time[j] != 0) {// 此判断语句主要目的是将一个需要的移动的View设置监听,在View滑动执行结束后在调用Handler
          flag = true;
          translateAnimation[i][j].setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              handler.sendEmptyMessage(TouchEventDetection.ACTION_LEFT);
            }
          });
        }
      }
    }

    if (flag) {// flag为true表示有需要移动的动画
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (tv[j][i].getVisibility() == View.VISIBLE) {// 如果view不为INVISIBLE状态时,执行动画
            tv[j][i].startAnimation(translateAnimation[j][i]);
          }
        }
      }
    } else {// 没有需要执行的动画,直接返回调用handler执行数组逻辑操作以及更新UI
      // handler.sendEmptyMessage(TouchEventDetection.ACTION_UP);
    }
  }

  // 新增加一个数据项的方法
  public int[][] addNew(int[][] data, TextView[][] tv) {
    // 检测矩阵是否有空格,没有的话直接返回原来的数据
    int empty = 0;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (data[i][j] == 0) {
          empty++;
        }
      }
    }
    if (empty == 0) {
      return data;
    }

    // 矩阵上还有空白的格子
    // 生成两个0到3的随机数
    int a = (int) (0 + Math.random() * 4);
    int b = (int) (0 + Math.random() * 4);
    // 如果当前的格子为0(即空白)
    if (data[a][b] == 0) {

      // 随机加入2或者4,生成2的概率为0.75,4的概率为0.25
      int newData = (int) (0 + Math.random() * 4);

      if (newData < 3) {
        data[a][b] = 2;
      } else {
        data[a][b] = 4;
      }

      // 动画,产生新格子的位置由小到大弹出
      ScaleAnimation scaleAnimation =
          new ScaleAnimation(0, 1, 0, 1, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
      scaleAnimation.setDuration(100);
      tv[a][b].startAnimation(scaleAnimation);
    } else {
      // 如果当前格子不为空,重新调用此方法
      addNew(data, tv);
    }
    return data;
  }

  public void soomIn() {

  }
}
