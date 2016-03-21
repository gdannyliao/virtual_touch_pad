package com.ggdsn.virtualtouchpad;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by LiaoXingyu on 3/20/16.
 */
public class TouchPad implements View.OnClickListener, View.OnTouchListener {
	private static final String TAG = "TouchPad";
	private final int height;
	private final int width;
	private final LayoutParams layoutParams;

	private float currentx;
	private float currenty;
	private float lastx;
	private float lasty;

	Context context;
	WindowManager windowManager;
	private final View view;


	public TouchPad(final Context context) {
		this.context = context;
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		width = point.x;
		height = point.y;
		Log.d(TAG, "TouchPad: w:" + width + " h:" + height);

		view = LayoutInflater.from(context).inflate(R.layout.layout_touch_pad, null);
		view.findViewById(R.id.button).setOnClickListener(this);
		view.setOnTouchListener(this);

		layoutParams = new LayoutParams();
		layoutParams.type = LayoutParams.TYPE_PHONE;
		layoutParams.format = PixelFormat.RGBA_8888;
		layoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		layoutParams.width = 250;
		layoutParams.height = 250;
		windowManager.addView(view, layoutParams);
	}

	@Override public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(i);
	}

	@Override public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		float x = event.getRawX();
		float y = event.getRawY();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				currentx = x;
				currenty = y;
				lastx = x;
				lasty = y;
				break;
			case MotionEvent.ACTION_MOVE:
				currentx = x;
				currenty = y;
				updateViewPosition(currentx, currenty);
				lastx = x;
				lasty = y;
				break;
			case MotionEvent.ACTION_UP:

				break;
		}

		return true;
	}
//TODO 添加对鼠标的控制
	private void updateViewPosition(float x, float y) {
		int dx = (int) (x - lastx);
		int dy = (int) (y - lasty);
		layoutParams.x += dx;
		layoutParams.y += dy;
		windowManager.updateViewLayout(view, layoutParams);
	}
}
