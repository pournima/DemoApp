package com.github.organisation;

import android.database.Cursor;

public class OrganisationRepositoryDataModel {

	String name;
	String owner;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public OrganisationRepositoryDataModel(){
		
	}
	

	public OrganisationRepositoryDataModel(Cursor cursor){
		 super();

        this.name = cursor.getString(cursor
                        .getColumnIndex(OrganisationRepositoryDBAdapter.NAME));
		
        this.owner = cursor.getString(cursor
                .getColumnIndex(OrganisationRepositoryDBAdapter.OWNER));
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
