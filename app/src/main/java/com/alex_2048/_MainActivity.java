package com.alex_2048;
//package com.alex_2048;
//
//import android.support.v7.app.ActionBarActivity;
//import android.alex.utils.TouchEventDetection;
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.MotionEvent;
//
//import android.widget.TextView;
//
//public class MainActivity extends ActionBarActivity {
//
//	private TextView[][] tv = new TextView[4][4];
//
//	private TextView tv_score;
//
//	private int[][] data = new int[4][4];
//
//	private int score = 0;
//
//	private TouchEventDetection touchEventDetection;
//	private Result_2048 result_2048;
//
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(android.os.Message msg) {
//
//			score = 0;
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					score = score + data[i][j];
//				}
//			}
//
//			tv_score.setText("Score:" + score);
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					if (data[i][j] == 0) {
//						tv[i][j].setText(" ");
//					} else {
//						tv[i][j].setText(data[i][j] + "");
//					}
//				}
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		touchEventDetection = new TouchEventDetection(this);
//
//		for (int i = 0; i < data.length; i++) {
//			for (int j = 0; j < data[i].length; j++) {
//				data[i][j] = 0;
//			}
//		}
//		result_2048 = new Result_2048();
//
//		tv_score = (TextView) findViewById(R.id.tv_score);
//
//		tv[0][0] = (TextView) findViewById(R.id.tv_0_0);
//		tv[0][1] = (TextView) findViewById(R.id.tv_0_1);
//		tv[0][2] = (TextView) findViewById(R.id.tv_0_2);
//		tv[0][3] = (TextView) findViewById(R.id.tv_0_3);
//		tv[1][0] = (TextView) findViewById(R.id.tv_1_0);
//		tv[1][1] = (TextView) findViewById(R.id.tv_1_1);
//		tv[1][2] = (TextView) findViewById(R.id.tv_1_2);
//		tv[1][3] = (TextView) findViewById(R.id.tv_1_3);
//		tv[2][0] = (TextView) findViewById(R.id.tv_2_0);
//		tv[2][1] = (TextView) findViewById(R.id.tv_2_1);
//		tv[2][2] = (TextView) findViewById(R.id.tv_2_2);
//		tv[2][3] = (TextView) findViewById(R.id.tv_2_3);
//		tv[3][0] = (TextView) findViewById(R.id.tv_3_0);
//		tv[3][1] = (TextView) findViewById(R.id.tv_3_1);
//		tv[3][2] = (TextView) findViewById(R.id.tv_3_2);
//		tv[3][3] = (TextView) findViewById(R.id.tv_3_3);
//		init();
//	}
//
//	public void init() {
//		int a = (int) (0 + Math.random() * 16);
//		int b = (int) (0 + Math.random() * 16);
//		if (a == b) {
//			init();
//			return;
//		} else {
//			int flag = 0;
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					if (flag == a || flag == b) {
//						data[i][j] = 2;
//						flag++;
//					} else {
//						flag++;
//					}
//				}
//			}
//
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					handler.sendEmptyMessage(1);
//				}
//			}).start();
//		}
//
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (touchEventDetection.getAction(event)) {
//		case TouchEventDetection.ACTION_UP:
//			result_2048.getResult(data, TouchEventDetection.ACTION_UP);
//			change();
//			break;
//		case TouchEventDetection.ACTION_DOWN:
//			result_2048.getResult(data, TouchEventDetection.ACTION_DOWN);
//			change();
//			break;
//		case TouchEventDetection.ACTION_LEFT:
//			result_2048.getResult(data, TouchEventDetection.ACTION_LEFT);
//			change();
//			break;
//		case TouchEventDetection.ACTION_RIGHT:
//			result_2048.getResult(data, TouchEventDetection.ACTION_RIGHT);
//			change();
//			break;
//		default:
//			break;
//		}
//
//		return super.onTouchEvent(event);
//	}
//
//	public void change() {
//		int empty = 0;
//		for (int i = 0; i < 4; i++) {
//			for (int j = 0; j < 4; j++) {
//				if (data[i][j] == 0) {
//					empty++;
//				}
//			}
//		}
//
//		if (empty == 0) {
//			boolean flag = false;
//			int temp[][] = new int[4][4];
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					temp[i][j] = data[i][j];
//				}
//			}
//			for (int i = 1; i <= 4; i++) {
//				result_2048.getResult(temp, i);
//			}
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					if (temp[i][j] == 0) {
//						flag = true;
//						break;
//					}
//				}
//			}
//
//			if (!flag) {
//				tv_score.setText("GameOver");
//			}
//
//		} else {
//			inputNew();
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					handler.sendEmptyMessage(1);
//				}
//			}).start();
//		}
//
//		// int inputNew = (int) (0 + Math.random() * empty);
//		//
//		// int flag = 0;
//		// for (int i = 0; i < 4; i++) {
//		// for (int j = 0; j < 4; j++) {
//		// if (flag == inputNew) {
//		// data[i][j] = 2;
//		// flag++;
//		// break;
//		// } else {
//		// flag++;
//		// }
//		// }
//		// if (flag <= inputNew) {
//		// break;
//		// }
//		// }
//
//		
//	}
//
//	private void inputNew() {
//		int a = (int) (0 + Math.random() * 4);
//		int b = (int) (0 + Math.random() * 4);
//		if (data[a][b] == 0) {
//			data[a][b] = 2;
//		} else {
//			inputNew();
//		}
//	}
//
//}
