package com.github.commits;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.github.database.DbAdapter;
import com.github.repository.RepositoryDataModel;

public class CommitsDBAdapter extends DbAdapter {

	public static final String ROWID = "_id";
	public static final String NAME = "name";
	public static final String DATE = "date";
	public static final String MESSAGE = "message";
	
	public String strTableName;

	public CommitsDBAdapter(Context context, String strTableName) {
		super(context, strTableName);
		this.strTableName = strTableName;
		setDbName();
		setDbColumns();
	}

	@Override
	protected void setDbName() {
		// TODO Auto-generated method stub
		this.dbName = strTableName;
		Log.i("DB Name Set", dbName);
	}

	@Override
	protected void setDbColumns() {
		// TODO Auto-generated method stub
		this.dbColumns = new String[] { ROWID,NAME,DATE,MESSAGE};
		Log.i("Db Comolmn Set", dbColumns.toString());
	}

	@Override
	public long create(ContentValues commitsValues) {
		return super.create(commitsValues);
	}

	public boolean update(long rowId, ContentValues commitsValues) {

		return super.update(rowId, commitsValues);
	}

//	ContentValues createContentValues(RepositoryDataModel repository) {
//        ContentValues values = new ContentValues();
//        values.put("id", repository.getId());
//        values.put("name", repository.getName());
//    	
//        return values;
//	}
	public ArrayList<CommitsDataModel> getCommitsList(Context context) {
		Cursor categoriesCursor = this.fetchAll(null, null);
		ArrayList<CommitsDataModel> categoriesList = new ArrayList<CommitsDataModel>();

		while (categoriesCursor.moveToNext()) {
			CommitsDataModel commits_data = new CommitsDataModel(
					categoriesCursor);

			categoriesList.add(commits_data);

		}
		categoriesCursor.close();
		return categoriesList;
	}

	
	public void deleteAll() {
		try {
			db.beginTransaction();
			this.delete();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}
	
}
