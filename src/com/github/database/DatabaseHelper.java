package com.github.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "githubDB";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {	
			
			String createSql = "CREATE TABLE Repository (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT UNIQUE);";
			
			Log.v("GitHub", "Creating Repository: " + createSql);
			db.execSQL(createSql);

			createSql = null;
			createSql = "CREATE TABLE Branch (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "branch_name TEXT );";
			Log.v("GitHub", "Creating Branch: " + createSql);
			db.execSQL(createSql);
			
			createSql = null;
			createSql = "CREATE TABLE Commits (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT , "
					+ "date TEXT, "
					+ "message TEXT );";
			Log.v("GitHub", "Creating Commits: " + createSql);
			db.execSQL(createSql);
	
			createSql = null;
			createSql = "CREATE TABLE Organisation (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "org_name TEXT );";
			Log.v("GitHub", "Creating Organisation: " + createSql);
			db.execSQL(createSql);
		
			createSql=null;
			createSql = "CREATE TABLE OrganisationRepository (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT UNIQUE, "
					+ "owner TEXT);";
			
			Log.v("GitHub", "Creating Organisation Repository: " + createSql);
			db.execSQL(createSql);
			
			createSql=null;
			createSql = "CREATE TABLE Members (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT);";
			
			Log.v("GitHub", "Creating Members: " + createSql);
			db.execSQL(createSql);
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("GitHub", "db creation failed: " + e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w("GitHub", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS victories");
		onCreate(db);
	}
}
