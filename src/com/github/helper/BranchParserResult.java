package com.github.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.github.branch.BranchDataModel;

public class BranchParserResult {

	public BranchParserResult()
	{
		
	}
	
	public ArrayList<BranchDataModel> parseBranchData(String strJsonReponse)
	{
	     ArrayList<BranchDataModel> branchObjArray = new ArrayList<BranchDataModel>();
	     Log.i("index.....", "inside parser");
		try {
			JSONArray branchJsonArray = new JSONArray(strJsonReponse);
			
			for (int i = 0; i < branchJsonArray.length(); i++) {
				
				BranchDataModel mBranchDataModel=new BranchDataModel();
				
				Log.i("index.....", String.valueOf(i));

				String strName = branchJsonArray.getJSONObject(i).getString("name");
				Log.i("branch name.....", String.valueOf(strName));
				mBranchDataModel.setBranchName(strName);
				
				branchObjArray.add(mBranchDataModel);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return branchObjArray;
	}
}
