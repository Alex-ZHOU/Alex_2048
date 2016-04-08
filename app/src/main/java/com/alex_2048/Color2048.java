package com.alex_2048;

import android.graphics.Color;

//颜色设置类
public class Color2048 extends Color {
  public static int COLOR_2 = 0xFFFFF8C8;
  public static int COLOR_4 = 0xFFFFFA8D;
  public static int COLOR_8 = 0xFFFF9D22;
  public static int COLOR_16 = 0xFF845000;
  public static int COLOR_32 = 0xFFCC2A00;
  public static int COLOR_64 = RED;
  public static int COLOR_128 = 0xFFFF8578;
  public static int COLOR_256 = 0xFFE2FF88;
  public static int COLOR_512 = 0xFF95FF66;
  public static int COLOR_1024 = 0xFF38FF42;
  public static int COLOR_2048 = 0xFF1FC800;
  public static int COLOR_4096 = 0xFF00CAD5;
  public static int COLOR_8192 = 0xFF005ADE;

  // 暴露一个静态方法,通过传入的值设置颜色
  public static int setColorByData(int tempData) {

    switch (tempData) {
      case 2:
        return COLOR_2;
      case 4:
        return COLOR_4;
      case 8:
        return COLOR_8;
      case 16:
        return COLOR_16;
      case 32:
        return COLOR_32;
      case 64:
        return COLOR_64;
      case 128:
        return COLOR_128;
      case 256:
        return COLOR_256;
      case 512:
        return COLOR_512;
      case 1024:
        return COLOR_1024;
      case 2048:
        return COLOR_2048;
      case 4096:
        return COLOR_4096;
      case 8192:
        return COLOR_8192;
    }
    return 0;
  }
}
