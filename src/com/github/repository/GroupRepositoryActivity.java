package com.github.repository;

import com.github.GroupActivity;

import android.content.Intent;
import android.os.Bundle;

public class GroupRepositoryActivity extends GroupActivity {

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
		startChildActivity("Repository", new Intent(GroupRepositoryActivity.this,RepositoryActivity.class));
	}

}
