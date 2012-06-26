package com.github;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.github.helper.AppStatus;
import com.github.repository.RepositoryActivity;



public class SplashScreen extends Activity {
	AppStatus mAppStatus;
	private ProgressDialog loading;
	Handler mhandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		mAppStatus = AppStatus.getInstance(this);
		mhandler = new Handler();
		loading = new ProgressDialog(this);

		startApp();
	}

	private void startApp() {
		// TODO Auto-generated method stub
		// try {
		// this.wait(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				showLoading(true, "Loading", "In Process please wait...");
				if (mAppStatus.isOnline(SplashScreen.this)) {
					Log.v("SPLASH_SCREEN", "App is online");
					if (mAppStatus.isRegistered()) {
						Log.v("SPLASH_SCREEN", "App is registered!");
						Intent i = new Intent(SplashScreen.this,
								RepositoryActivity.class);
						i.putExtra("LOGIN_FLAG", false);
						startActivity(i);
						finish();
					} else {
						Log.v("SPLASH_SCREEN", "You are not registered!!!!");
						Intent intent_login = new Intent(SplashScreen.this,
								LoginInActivity.class);
						startActivity(intent_login);
						finish();
					}
				} else {
					Log.v("SPLASH_SCREEN", "You are not online!!!!");
					showLoading(false, "", "");
					message("Please check you internet connection!!");
					finish();
				}
				showLoading(false, "", "");
			}
		});
		t.start();
	}

	void showLoading(final boolean show, final String title, final String msg) {
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				if (show) {
					if (loading != null) {
						loading.setTitle(title);
						loading.setMessage(msg);
						loading.show();
					}
				} else {
					loading.cancel();
					loading.dismiss();
				}

			}
		});
	}

	void message(String msg) {
		final String mesage = msg;
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(SplashScreen.this, mesage, 8000);
				toast.show();
			}
		});
	}

}
