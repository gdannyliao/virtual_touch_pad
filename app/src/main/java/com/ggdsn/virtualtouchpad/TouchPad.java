package com.ggdsn.virtualtouchpad;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by LiaoXingyu on 3/20/16.
 */
public class TouchPad {
	private static final String TAG = "TouchPad";
	private final int height;
	private final int width;
	Context context;
	WindowManager windowManager;

	public TouchPad(Context context) {
		this.context = context;
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		width = point.x;
		height = point.y;
		Log.d(TAG, "TouchPad: w:" + width + " h:" + height);

		View view = LayoutInflater.from(context).inflate(R.layout.layout_touch_pad, null).findViewById(R.id.frameLayout);
		view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {

			}
		});

		LayoutParams layoutParams = new LayoutParams();
		layoutParams.type = LayoutParams.TYPE_PHONE;
		layoutParams.format = PixelFormat.RGBA_8888;
		layoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		layoutParams.width = 250;
		layoutParams.height = 250;
		windowManager.addView(view, layoutParams);

	}
}
