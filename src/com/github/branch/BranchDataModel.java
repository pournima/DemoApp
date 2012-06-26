package com.github.branch;

import android.database.Cursor;



public class BranchDataModel {

	String branchName;
	
	
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	
	public BranchDataModel(Cursor cursor){
		 super();

       this.branchName = cursor.getString(cursor.getColumnIndex(BranchDBAdapter.NAME));

	}

	public BranchDataModel() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String toString() {
		return branchName;
	}
	
}
