package com.github.commits;

import android.database.Cursor;

public class CommitsDataModel {
	
	String name;
	String date;
	String message;
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	public CommitsDataModel() {
		// TODO Auto-generated constructor stub
	}
	
	
	public CommitsDataModel(Cursor cursor){
		 super();

        this.name = cursor.getString(cursor.getColumnIndex(CommitsDBAdapter.NAME));
		this.date=cursor.getString(cursor.getColumnIndex(CommitsDBAdapter.DATE));
		this.message=cursor.getString(cursor.getColumnIndex(CommitsDBAdapter.MESSAGE));
	}
	
}
