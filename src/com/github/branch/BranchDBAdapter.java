package com.github.branch;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.github.database.DbAdapter;

public class BranchDBAdapter extends DbAdapter {

	public static final String ROWID = "_id";
	public static final String NAME = "branch_name";

	
	public String strTableName;

	public BranchDBAdapter(Context context, String strTableName) {
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
		this.dbColumns = new String[] { ROWID,NAME};
		Log.i("Db Comolmn Set", dbColumns.toString());
	}

	@Override
	public long create(ContentValues commitsValues) {
		return super.create(commitsValues);
	}

	public boolean update(long rowId, ContentValues commitsValues) {

		return super.update(rowId, commitsValues);
	}

	public ArrayList<BranchDataModel> getBranchList(Context context) {
		Cursor branchCursor = this.fetchAll(null, null);
		ArrayList<BranchDataModel> branchList = new ArrayList<BranchDataModel>();

		while (branchCursor.moveToNext()) {
			BranchDataModel branch_data = new BranchDataModel(
					branchCursor);

			branchList.add(branch_data);

		}
		branchCursor.close();
		return branchList;
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
