package com.github.organisation;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.github.database.DbAdapter;

public class OrganisationDBAdapter extends DbAdapter {

	public static final String ROWID = "_id";
	public static final String ORG_NAME = "org_name";
	
	public String strTableName;

	public OrganisationDBAdapter(Context context, String strTableName) {
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
		this.dbColumns = new String[] { ROWID,ORG_NAME };
		Log.i("Db Comolmn Set", dbColumns.toString());
	}

	@Override
	public long create(ContentValues repositoryValues) {
		return super.create(repositoryValues);
	}

	public boolean update(long rowId, ContentValues organisationValues) {

		return super.update(rowId, organisationValues);
	}

	ContentValues createContentValues(OrganisationDataModel organisation) {
        ContentValues values = new ContentValues();
        values.put("name", organisation.getOrgName());
    	
        return values;
	}
	public ArrayList<OrganisationDataModel> getOrganisationList(Context context) {
		Cursor orgCursor = this.fetchAll(null, null);
		ArrayList<OrganisationDataModel> orgList = new ArrayList<OrganisationDataModel>();

		while (orgCursor.moveToNext()) {
			OrganisationDataModel org_data = new OrganisationDataModel(
					orgCursor);

			orgList.add(org_data);

		}
		orgCursor.close();
		return orgList;
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
