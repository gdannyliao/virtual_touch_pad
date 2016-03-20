package com.ggdsn.virtualtouchpad;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by LiaoXingyu on 3/20/16.
 */
public class TouchPadService extends Service {

	private TouchPad touchPad;

	@Nullable @Override public IBinder onBind(Intent intent) {
		return null;
	}

	@Override public void onCreate() {
		super.onCreate();
		touchPad = new TouchPad(this);
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
}
