package com.alex_2048;
//package com.alex_2048;
//
//public class Result_2048 {
//	private int a[][] = new int[4][4];
//
//	public int[][] getResult(int[][] a, int type) {
//		this.a = a;
//		switch (type) {
//		case 1:
//			right();
//			break;
//		case 2:
//			left();
//			break;
//		case 3:
//			up();
//			break;
//		case 4:
//			down();
//			break;
//		default:
//			break;
//		}
//		return a;
//	}
//
//	private void up_remove_blank() {
//		for (int i = 0; i < 4; i++) {
//			for (int j = 1; j < 4; j++) {
//				int k = j;
//				while (k - 1 >= 0 && a[k - 1][i] == 0) {
//					int temp;
//					temp = a[k][i];
//					a[k][i] = a[k - 1][i];
//					a[k - 1][i] = temp;
//					k--;
//				}
//			}
//		}
//	}
//
//	private void up() {
//		up_remove_blank();
//		for (int i = 0; i < 4; i++) {
//			for (int j = 0; j < 3; j++) {
//				if (a[j][i] == a[j + 1][i] && a[j][i] != 0) {
//					a[j][i] = a[j][i] + a[j + 1][i];
//					a[j + 1][i] = 0;
//					up_remove_blank();
//				}
//			}
//		}
//	}
//
//	private void down_remove_blank() {
//		for (int i = 0; i < 4; i++) {
//			for (int j = 2; j >= 0; j--) {
//				int k = j;
//				while (k + 1 <= 3 && a[k + 1][i] == 0) {
//					int temp;
//					temp = a[k][i];
//					a[k][i] = a[k + 1][i];
//					a[k + 1][i] = temp;
//					k++;
//				}
//			}
//		}
//	}
//
//	private void down() {
//		down_remove_blank();
//		for (int i = 0; i < 4; i++) {
//			for (int j = 3; j > 0; j--) {
//				if (a[j][i] == a[j - 1][i] && a[j][i] != 0) {
//					a[j][i] = a[j][i] + a[j - 1][i];
//					a[j - 1][i] = 0;
//					down_remove_blank();
//				}
//			}
//		}
//	}
//
//	private void left_remove_blank() {
//		for (int i = 0; i < 4; i++) {
//			for (int j = 1; j < 4; j++) {
//				int k = j;
//				while (k - 1 >= 0 && a[i][k - 1] == 0) {
//					int temp;
//					temp = a[i][k];
//					a[i][k] = a[i][k - 1];
//					a[i][k - 1] = temp;
//					k--;
//				}
//			}
//		}
//	}
//
//	private void left() {
//		left_remove_blank();
//		for (int i = 0; i < 4; i++) {
//			for (int j = 0; j < 3; j++) {
//				if (a[i][j] == a[i][j + 1] && a[i][j] != 0) {
//					a[i][j] = a[i][j] + a[i][j + 1];
//					a[i][j + 1] = 0;
//					left_remove_blank();
//				}
//			}
//		}
//	}
//
//	private void right_remove_blank() {
//		for (int i = 0; i < 4; i++) {
//			for (int j = 2; j >= 0; j--) {
//				int k = j;
//				while (k + 1 <= 3 && a[i][k + 1] == 0) {
//					int temp;
//					temp = a[i][k];
//					a[i][k] = a[i][k + 1];
//					a[i][k + 1] = temp;
//					k++;
//				}
//			}
//		}
//	}
//
//	private void right() {
//		right_remove_blank();
//		for (int i = 0; i < 4; i++) {
//			for (int j = 3; j >= 1; j--) {
//				if (a[i][j] == a[i][j - 1] && a[i][j] != 0) {
//					a[i][j] = a[i][j] + a[i][j - 1];
//					a[i][j - 1] = 0;
//					right_remove_blank();
//				}
//			}
//		}
//	}
//
//}
