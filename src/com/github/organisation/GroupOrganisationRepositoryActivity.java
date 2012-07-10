package com.github.organisation;

import android.content.Intent;
import android.os.Bundle;
import com.github.GroupActivity;

public class GroupOrganisationRepositoryActivity extends GroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}	

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIdList.clear();
		startChildActivity("Org Repository", new Intent(GroupOrganisationRepositoryActivity.this,OrganisationActivity.class));
	}

}
