package com.github.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.organisation.OrganisationUserCommitsDataModel;

import android.util.Log;


public class OrganisationUserCommitsParseResult {

	public OrganisationUserCommitsParseResult()
	{
		
	}
	
	public ArrayList<OrganisationUserCommitsDataModel> parseCommitsData(String strJsonReponse)
	{
	     ArrayList<OrganisationUserCommitsDataModel> commitsObjArray = new ArrayList<OrganisationUserCommitsDataModel>();
	     Log.i("index.....", "inside commit parser");
		try {
			
			JSONObject mJsonObjectCommits = new JSONObject(strJsonReponse);
			
			String strCommits = mJsonObjectCommits.getString("committer");
			
			JSONArray commitsJsonArray = new JSONArray(strCommits);
			
			for (int i = 0; i < commitsJsonArray.length(); i++) {
				
				OrganisationUserCommitsDataModel mCommitsDataModel=new OrganisationUserCommitsDataModel();
				
				//Name
				String strName = commitsJsonArray.getJSONObject(i).getString("name");
				Log.i("Name.....", String.valueOf(strName));
				mCommitsDataModel.setName(strName);
						
			
				commitsObjArray.add(mCommitsDataModel);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return commitsObjArray;
	}

}
