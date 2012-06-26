package com.github.repository;

import android.database.Cursor;


public class RepositoryDataModel {
	String name;
	String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public RepositoryDataModel(){
		
	}
	

	public RepositoryDataModel(Cursor cursor){
		 super();

        this.name = cursor.getString(cursor
                        .getColumnIndex(RepositoryDBAdapter.NAME));
		
		
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
