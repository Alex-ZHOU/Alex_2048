package com.alex_2048;

import android.view.animation.ScaleAnimation;
import android.widget.TextView;

//2048算法类
public class Algorithm2048 {
  private int a[][] = new int[4][4];// 数组,作为参数记录2048数字矩阵
  private TextView tv[][] = new TextView[4][4];// TextView二维数组,记录2048的View矩阵
  private int addScore;// 增加的分数,每次滑动计算增加的分数
  private boolean soonIn = true;// 判断是否要方法TextView动画

  // 每次滑动调用此函数,用于计算数组矩阵各个点的数值并执行一定的动画
  public int[][] getResult(int[][] a, TextView tv[][], int type) {
    this.a = a;
    this.tv = tv;
    this.addScore = 0;
    this.soonIn = true;// 调用此函数默认将soonIn设置为放大,因为滑动产生的相同合并,合并时放大
    // type 为Activity中传入的操作类型识别,对于不同的类型输入调用不同的实现方式
    switch (type) {
      case 1:
        right();
        break;
      case 2:
        left();
        break;
      case 3:
        up();
        break;
      case 4:
        down();
        break;
      default:
        break;
    }
    return a;
  }

  // 这个方法是用于检测游戏是否已经结束,总体结构和getResult()方法相似,只是多了一个soonIn参数
  private int[][] checkEnd(int[][] a, TextView tv[][], int type, boolean soonIn) {
    this.a = a;
    this.tv = tv;
    this.addScore = 0;
    this.soonIn = soonIn;
    switch (type) {
      case 1:
        right();
        break;
      case 2:
        left();
        break;
      case 3:
        up();
        break;
      case 4:
        down();
        break;
      default:
        break;
    }
    return a;
  }

  // 去除空白,空白只的是相对一维数组被置为0的那部分
  private void up_remove_blank() {
    for (int i = 0; i < 4; i++) {// 该方法是向上操作,则将每一列都作为一维数组
      for (int j = 1; j < 4; j++) {// 向上,则是把所以数都忘事移动,相对数组来说就是向前移动
        int k = j;
        while (k - 1 >= 0 && a[k - 1][i] == 0) {// 如果发现空白,则向前移动,直到最靠后的那个数不为0
          int temp;
          temp = a[k][i];
          a[k][i] = a[k - 1][i];
          a[k - 1][i] = temp;
          k--;
        }
      }
    }
  }

  // 向上移动时调用的操作
  private void up() {
    up_remove_blank();// 开始前先调用改函数,将所有数往上移动
    for (int i = 0; i < 4; i++) {// 按列分割为一维数组,每列计算
      for (int j = 0; j < 3; j++) {
        if (a[j][i] == a[j + 1][i] && a[j][i] != 0) {// 如果相邻的相同,靠前的一个加为两倍,靠后一个置为0
          a[j][i] = a[j][i] + a[j + 1][i];
          a[j + 1][i] = 0;
          addScore = addScore + a[j][i];// 增加分数增加
          if (soonIn) {// 对靠前的那一个TextView执行放大动画
            ScaleAnimation scaleAnimation =
                new ScaleAnimation(1, 1.3f, 1, 1.3f, Animation2048.RELATIVE_TO_SELF, 0.5f,
                    Animation2048.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(100);
            tv[j][i].startAnimation(scaleAnimation);
          }
          up_remove_blank();// 每次执行万了之后要进行一次清除空白操作
        }
      }
    }
  }

  // 另外3种操作方式与向上的操作方式相似,不再一一打注释

  private void down_remove_blank() {
    for (int i = 0; i < 4; i++) {
      for (int j = 2; j >= 0; j--) {
        int k = j;
        while (k + 1 <= 3 && a[k + 1][i] == 0) {
          int temp;
          temp = a[k][i];
          a[k][i] = a[k + 1][i];
          a[k + 1][i] = temp;
          k++;
        }
      }
    }
  }

  private void down() {
    down_remove_blank();
    for (int i = 0; i < 4; i++) {
      for (int j = 3; j > 0; j--) {
        if (a[j][i] == a[j - 1][i] && a[j][i] != 0) {
          a[j][i] = a[j][i] + a[j - 1][i];
          a[j - 1][i] = 0;
          addScore = addScore + a[j][i];

          if (soonIn) {
            ScaleAnimation scaleAnimation =
                new ScaleAnimation(1, 1.3f, 1, 1.3f, Animation2048.RELATIVE_TO_SELF, 0.5f,
                    Animation2048.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(100);
            tv[j][i].startAnimation(scaleAnimation);
          }

          down_remove_blank();
        }
      }
    }
  }

  private void left_remove_blank() {
    for (int i = 0; i < 4; i++) {
      for (int j = 1; j < 4; j++) {
        int k = j;
        while (k - 1 >= 0 && a[i][k - 1] == 0) {
          int temp;
          temp = a[i][k];
          a[i][k] = a[i][k - 1];
          a[i][k - 1] = temp;
          k--;
        }
      }
    }
  }

  private void left() {
    left_remove_blank();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 3; j++) {
        if (a[i][j] == a[i][j + 1] && a[i][j] != 0) {
          a[i][j] = a[i][j] + a[i][j + 1];
          a[i][j + 1] = 0;
          addScore = addScore + a[i][j];
          if (soonIn) {
            ScaleAnimation scaleAnimation =
                new ScaleAnimation(1, 1.3f, 1, 1.3f, Animation2048.RELATIVE_TO_SELF, 0.5f,
                    Animation2048.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(100);
            tv[i][j].startAnimation(scaleAnimation);
          }

          left_remove_blank();
        }
      }
    }
  }

  private void right_remove_blank() {
    for (int i = 0; i < 4; i++) {
      for (int j = 2; j >= 0; j--) {
        int k = j;
        while (k + 1 <= 3 && a[i][k + 1] == 0) {
          int temp;
          temp = a[i][k];
          a[i][k] = a[i][k + 1];
          a[i][k + 1] = temp;
          k++;
        }
      }
    }
  }

  private void right() {
    right_remove_blank();
    for (int i = 0; i < 4; i++) {
      for (int j = 3; j >= 1; j--) {
        if (a[i][j] == a[i][j - 1] && a[i][j] != 0) {
          a[i][j] = a[i][j] + a[i][j - 1];
          a[i][j - 1] = 0;
          addScore = addScore + a[i][j];
          if (soonIn) {

            ScaleAnimation scaleAnimation =
                new ScaleAnimation(1, 1.3f, 1, 1.3f, Animation2048.RELATIVE_TO_SELF, 0.5f,
                    Animation2048.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(100);
            tv[i][j].startAnimation(scaleAnimation);
          }
          right_remove_blank();
        }
      }
    }
  }

  // 暴露一个获得加分的方法
  public int getAddScore() {
    return addScore;
  }

  // 暴露一个设置加分的方法
  public void setAddScore(int addScore) {
    this.addScore = addScore;
  }

  // 检测是否游戏结束
  public boolean checkGameOver(int[][] data) {
    // true为游戏结束,false为还没结束
    int empty = 0;// 2048面板剩余的空格数
    // 检测剩余的空格数
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (data[i][j] == 0) {
          empty++;
        }
      }
    }

    if (empty == 0) {
      boolean flag = false;// 设置标志,一开始设置为false
      int temp[][] = new int[4][4];// 创建一个临时二维数组,并将2048矩阵的值赋给这个临时数组
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          temp[i][j] = data[i][j];
        }
      }
      // 然后调用checkEnd()方法进行检测
      for (int i = 1; i <= 4; i++) {
        checkEnd(temp, tv, i, false);
      }
      // 并对返回的结果进行检测,如果找到空白的存在,flag设置为true并跳出循环
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (temp[i][j] == 0) {
            flag = true;
            break;
          }
        }
      }

      // flag为true则返回false,游戏继续,不然返回true,游戏结束
      if (!flag) {
        return true;
      } else {
        return false;
      }
    }
    return false;// empty不为0,还有剩余的空格数,直接返回false,游戏继续
  }
}
