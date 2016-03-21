package com.ggdsn.virtualtouchpad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by LiaoXingyu on 3/20/16.
 */
public class FlowFrameLayout extends FrameLayout {

	private static final String TAG = FlowFrameLayout.class.getSimpleName();
	private static final float MAX_SHAKE_DEVIATION = 4;
	private OnTouchListener onTouchListener;
	private float downx;
	private float downy;
	private float currentx;
	private float currenty;

	public FlowFrameLayout(Context context) {
		super(context);
	}

	public FlowFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override public void setOnTouchListener(OnTouchListener l) {
		//super.setOnTouchListener(l);
		this.onTouchListener = l;
	}

	@Override public boolean dispatchTouchEvent(MotionEvent ev) {
		if (onTouchListener != null) {
			onTouchListener.onTouch(this, ev);
		}

		float rawX = ev.getRawX();
		float rawY = ev.getRawY();

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downx = rawX;
				downy = rawY;
				break;
			case MotionEvent.ACTION_MOVE:
				currentx = rawX;
				currenty = rawY;
				break;
			case MotionEvent.ACTION_UP:
				if (Math.abs(currentx - downx) > MAX_SHAKE_DEVIATION || Math.abs(currenty - downy) > MAX_SHAKE_DEVIATION) {
					//如果在抖动允许的范围内，则截断该事件（防止按钮被点击）
					return true;
				}
		}
		return super.dispatchTouchEvent(ev);
	}
}
