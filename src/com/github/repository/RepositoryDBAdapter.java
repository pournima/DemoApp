package com.github.repository;

import java.util.ArrayList;

import com.github.database.DbAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


public class RepositoryDBAdapter extends DbAdapter {

	public static final String ROWID = "_id";
	public static final String ID = "id";
	public static final String NAME = "name";
	public String strTableName;

	public RepositoryDBAdapter(Context context, String strTableName) {
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
		this.dbColumns = new String[] { ROWID, ID, NAME };
		Log.i("Db Comolmn Set", dbColumns.toString());
	}

	@Override
	public long create(ContentValues repositoryValues) {
		return super.create(repositoryValues);
	}

	public boolean update(long rowId, ContentValues repositoryValues) {

		return super.update(rowId, repositoryValues);
	}

	ContentValues createContentValues(RepositoryDataModel repository) {
        ContentValues values = new ContentValues();
        values.put("id", repository.getId());
        values.put("name", repository.getName());
    	
        return values;
	}
	public ArrayList<RepositoryDataModel> getRepositoryList(Context context) {
		Cursor categoriesCursor = this.fetchAll(null, null);
		ArrayList<RepositoryDataModel> categoriesList = new ArrayList<RepositoryDataModel>();

		while (categoriesCursor.moveToNext()) {
			RepositoryDataModel repository_data = new RepositoryDataModel(
					categoriesCursor);

			categoriesList.add(repository_data);

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
