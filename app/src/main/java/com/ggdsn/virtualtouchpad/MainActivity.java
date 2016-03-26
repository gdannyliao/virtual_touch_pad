package com.ggdsn.virtualtouchpad;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private static final int REQUEST_CODE = 100;
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				checkDrawOverlayPermission();
			}
		});
	}

	private void startTouchPadService() {
		Intent i = new Intent(MainActivity.this, TouchPadService.class);
		startService(i);
		finish();
	}

	public void checkDrawOverlayPermission() {
		Log.d(TAG, "checkDrawOverlayPermission: ");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!Settings.canDrawOverlays(this)) {
				Intent intent =
					new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
				startActivityForResult(intent, REQUEST_CODE);
			} else {
				onActivityResult(REQUEST_CODE, 0 , null);
			}
		} else {
			Log.d(TAG, "checkDrawOverlayPermission: else");
			onActivityResult(REQUEST_CODE, 0, null);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
		if (requestCode == REQUEST_CODE) {
			Log.d(TAG, "onActivityResult: ");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (Settings.canDrawOverlays(this)) {
					startTouchPadService();
				} else {
					Toast.makeText(MainActivity.this, "fuck", Toast.LENGTH_SHORT).show();
				}
			} else {
				startTouchPadService();
			}
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
