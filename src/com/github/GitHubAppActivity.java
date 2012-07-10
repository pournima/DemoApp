package com.github;

import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.repository.RepositoryActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GitHubAppActivity extends Activity {
	
	
	private WebView webView;
	AppStatus mAppStatus;
	LoginInActivity context;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_authorise);
 
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("https://github.com/login/oauth/authorize?client_id=81e06b06df8444dfc400&scope=repo");
		//https://github.com/login/oauth/authorize?client_id=81e06b06df8444dfc400
		
		webView.setWebViewClient(new AuthClient());
			
		webView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
		         Log.i("******", "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
		         
				return false;
			}
	    });

    }
	
	
	  private class AuthClient extends WebViewClient 
	  {
          @Override
          public void onPageFinished(WebView v, String url) 
          {
              v.clearHistory();
              Log.d("clear web view", "************");
          }
	  }

	  
	  @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				Log.i("Backup Button", "Pressed");
				
				mAppStatus = AppStatus.getInstance(this);
				
				String name=getIntent().getExtras().getString("username");
				
				Constants.gitflag=true;
				Intent i= new Intent(GitHubAppActivity.this, LoginInActivity.class); //RepositoryActivity
				i.putExtra("username", name);
				startActivity(i);				
				finish();
				
			}
			//super.onKeyDown(keyCode, event)
			return true;
		}

	
}