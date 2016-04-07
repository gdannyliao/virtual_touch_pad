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
import java.io.File;
import java.io.IOException;

/**
 * Created by LiaoXingyu on 3/20/16.
 */
public class TouchPad implements View.OnClickListener, View.OnTouchListener {
	public static final String DEVICE_PATH = "/dev/uinput";
	private static final float IGNORE_RANGE = 5;

	static {
		System.loadLibrary("virtual_mouse");
	}

	private static final String TAG = "TouchPad";
	private final int height;
	private final int width;
	private final LayoutParams layoutParams;

	private float currentx;
	private float currenty;
	private float lastx;
	private float lasty;

	private int speedTimes = 1;

	Context context;
	WindowManager windowManager;
	private final View view;
	private float startx;
	private float starty;

	public TouchPad(final Context context) {
		File dev = new File(DEVICE_PATH);

		boolean pmsGot = false;
		try {
			pmsGot = getPermission(dev);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int res = open();
		Log.d(TAG, "TouchPad() called with: " + "open res = [" + res + "]");
		if (res != 1) {
			throw new IllegalStateException("open failed");
		}

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

	private boolean getPermission(File dev) throws IOException, InterruptedException {
		Process su = Runtime.getRuntime().exec("su");
		String cmd = "chmod 666 " + dev.getAbsolutePath() + "\n" + "exit\n";
		su.getOutputStream().write(cmd.getBytes());
		if ((su.waitFor() != 0) || !dev.canRead() || !dev.canWrite()) {
			return false;
		}
		return true;
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
				startx = x;
				starty = y;
				break;
			case MotionEvent.ACTION_MOVE:
				currentx = x;
				currenty = y;
				updateViewPosition(currentx, currenty);
				lastx = x;
				lasty = y;
				break;
			case MotionEvent.ACTION_UP:
				if (Math.abs(startx - currentx) < IGNORE_RANGE && Math.abs(starty - currenty) < IGNORE_RANGE) {

				}
				break;
		}

		return true;
	}

	private void updateViewPosition(float x, float y) {
		float dx = (x - lastx);
		float dy = (y - lasty);
		layoutParams.x += dx;
		layoutParams.y += dy;
		Log.d(TAG, "updateViewPosition() called with: " + "dx = [" + dx + "], dy = [" + dy + "]");
		windowManager.updateViewLayout(view, layoutParams);
		mouseMove(dx * speedTimes, dy * speedTimes);
	}

	private native void click(float x, float y);
	private native void mouseMove(float x, float y);

	private native int open();
}
