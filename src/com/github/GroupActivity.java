package com.github;

import java.util.ArrayList;

import com.github.R;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.organisation.GroupOrganisationRepositoryActivity;
import com.github.organisation.OrganisationActivity;
import com.github.repository.GroupRepositoryActivity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

/**
 * The purpose of this Activity is to manage the activities in a tab.
 * Note: Child Activities can handle Key Presses before they are seen here.
 */
public class GroupActivity extends ActivityGroup {

    public ArrayList<String> mIdList;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        if (mIdList == null) mIdList = new ArrayList<String>();
    }
 
    /**
     * This is called when a child activity of this one calls its finish method.
     * This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
     * and starts the previous activity.
     * If the last child activity just called finish(),this activity (the parent),
     * calls finish to finish the entire group.
     */
  @Override
  public void finishFromChild(Activity child) {
      LocalActivityManager manager = getLocalActivityManager();
      int index = mIdList.size()-1;
     
      if (index < 1) {
          finish();
          return;
      }
         
      manager.destroyActivity(mIdList.get(index), true);
      mIdList.remove(index); index--;
      String lastId = mIdList.get(index);
      Intent lastIntent = manager.getActivity(lastId).getIntent();
      Window newWindow = manager.startActivity(lastId, lastIntent);
      setContentView(newWindow.getDecorView());
  }
 
  /**
   * Starts an Activity as a child Activity to this.
   * @param Id Unique identifier of the activity to be started.
   * @param intent The Intent describing the activity to be started.
   * @throws android.content.ActivityNotFoundException.
   */
  public void startChildActivity(String Id, Intent intent) {    
      Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
      if (window != null) {
          mIdList.add(Id);
          Log.v("log_tag", "startchildactivity: " + mIdList.size() + ":" + Id);
          setContentView(window.getDecorView());
      }   
  }
 
  /**
   * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
   * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      Log.v("log_tag", "  this function involve when press-down " );
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
          onBackPressed();
          return true;
      }
      return super.onKeyDown(keyCode, event);
  }

  /**
   * Overrides the default implementation for KeyEvent.KEYCODE_BACK
   * so that all systems call onBackPressed().
   */
  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
     
      Log.v("log_tag", "  this function involve when press-up ");
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          onBackPressed();
          return true;
      }
      return super.onKeyUp(keyCode, event);
  }
 
  /**
   * If a Child Activity handles KeyEvent.KEYCODE_BACK.
   * Simply override and add this method.
   */
  @Override
  public void  onBackPressed() {
      Log.v("log_tag", "this function involve when press back");
      int length = mIdList.size();
      Log.v("log_tag", "length: " + length);
      if (length > 1) {
          Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
          Log.v("log_tag", "the child view :"+current);
       //  this.finishFromChild(current);
         
         if (length < 1){
        	 warningDialogBox();
         }
         else{
        	 current.finish();
         }
        
      }
 
  }
  

	private void warningDialogBox() {
		Log.i("Warning......Dialog", "ssssss");
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			Log.i("Menu", "----- menu");
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu, menu);
			return true;

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			Log.i("########feed", String.valueOf(item.getItemId()));


			 if(item.getItemId()==R.id.menuLogOut){
					//***LogOut

					AppStatus mAppStatus=new AppStatus();
					mAppStatus.clearAuthKey(Constants.AUTH_KEY);
					
					Intent intent=new Intent(GroupActivity.this,LoginInActivity.class);
					startActivity(intent);
					finish();
					
				}else if(item.getItemId()==R.id.menuOrganisation){
										
					Intent intent=new Intent(GroupActivity.this,GroupOrganisationRepositoryActivity.class);
					startActivity(intent);
					//startChildActivity("organisation", intent);
					finish();
				}
				else if(item.getItemId()==R.id.menuUser){
					
					Intent intent=new Intent(GroupActivity.this,GroupRepositoryActivity.class);
					startActivity(intent);
					finish();
				}
			return true;

		}

}
