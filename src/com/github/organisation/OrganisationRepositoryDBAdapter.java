package com.github.organisation;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.github.database.DbAdapter;


public class OrganisationRepositoryDBAdapter extends DbAdapter {

	public static final String ROWID = "_id";
	public static final String NAME = "name";
	public static final String OWNER = "owner";
	public String strTableName;

	public OrganisationRepositoryDBAdapter(Context context, String strTableName) {
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
		this.dbColumns = new String[] { ROWID,NAME,OWNER };
		Log.i("Db Comolmn Set", dbColumns.toString());
	}

	@Override
	public long create(ContentValues repositoryValues) {
		return super.create(repositoryValues);
	}

	public boolean update(long rowId, ContentValues repositoryValues) {

		return super.update(rowId, repositoryValues);
	}

	ContentValues createContentValues(OrganisationRepositoryDataModel repository) {
        ContentValues values = new ContentValues();
        values.put("name", repository.getName());
        values.put("owner", repository.getName());
    	
        return values;
	}
	public ArrayList<OrganisationRepositoryDataModel> getRepositoryList(Context context) {
		Cursor repositoryCursor = this.fetchAll(null, null);
		ArrayList<OrganisationRepositoryDataModel> repositoryList = new ArrayList<OrganisationRepositoryDataModel>();

		while (repositoryCursor.moveToNext()) {
			OrganisationRepositoryDataModel repository_data = new OrganisationRepositoryDataModel(
					repositoryCursor);

			repositoryList.add(repository_data);

		}
		repositoryCursor.close();
		return repositoryList;
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
