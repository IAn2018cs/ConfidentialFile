package cn.ian2018.testsoftwareclass.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具
 */
public class ToastUtli {
	public static void show(Context context, String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
