package com.github.organisation;

import android.database.Cursor;

public class OrganisationDataModel {

	String orgName;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	public OrganisationDataModel(Cursor cursor){
		 super();

       this.orgName = cursor.getString(cursor
                       .getColumnIndex(OrganisationDBAdapter.ORG_NAME));
		
		
	}
	
	public OrganisationDataModel() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return orgName;
	}
}
