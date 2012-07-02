package com.github.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.github.commits.CommitsDataModel;

public class CommitsParserResult {

	public CommitsParserResult()
	{
		
	}
	
	public ArrayList<CommitsDataModel> parseCommitsData(String strJsonReponse)
	{
	     ArrayList<CommitsDataModel> commitsObjArray = new ArrayList<CommitsDataModel>();
	     Log.i("index.....", "inside commit parser");
		try {
			
			JSONObject mJsonObjectCommits = new JSONObject(strJsonReponse);
			
			String strCommits = mJsonObjectCommits.getString("commits");
			
			JSONArray commitsJsonArray = new JSONArray(strCommits);
			
			for (int i = 0; i < commitsJsonArray.length(); i++) {
				
				CommitsDataModel mCommitsDataModel=new CommitsDataModel();
				
				//Name
				String strName = commitsJsonArray.getJSONObject(i).getString("name");
				Log.i("Name.....", String.valueOf(strName));
				mCommitsDataModel.setName(strName);
						
				//Message
				String strMessage = commitsJsonArray.getJSONObject(i).getString("message");
				Log.i("Message.....", String.valueOf(strMessage));
				mCommitsDataModel.setMessage(strMessage);
				
				//Date
				String strDate = commitsJsonArray.getJSONObject(i).getString("date");
				Log.i("Date.....", String.valueOf(strDate));
				mCommitsDataModel.setDate(strDate);
				
				commitsObjArray.add(mCommitsDataModel);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return commitsObjArray;
	}

}
