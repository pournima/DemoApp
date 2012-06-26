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
	     Log.i("index.....", "inside parser");
		try {
			JSONArray repositoryJsonArray = new JSONArray(strJsonReponse);
			
			for (int i = 0; i < repositoryJsonArray.length(); i++) {
				
				CommitsDataModel mCommitsDataModel=new CommitsDataModel();
				
				Log.i("index.....", String.valueOf(i));

				String strCommit = repositoryJsonArray.getJSONObject(i)
						.getString("commit");
				Log.i("commit.......", strCommit);
				
				JSONObject mJsonObjectCommit = new JSONObject(strCommit);

				
				//Message
				String strMessage = mJsonObjectCommit.getString("message");
				Log.i("Commiter Name.......", strMessage);
				mCommitsDataModel.setMessage(strMessage);
				
//				//Message
//				String strMessage = repositoryJsonArray.getJSONObject(i).getString("message");
//				Log.i("Message.....", String.valueOf(strMessage));
//				mCommitsDataModel.setMessage(strMessage);
				
				
//				//Commitor data
//				String strCommiter = repositoryJsonArray.getJSONObject(i)
//						.getString("committer");
//				Log.i("Commiter.......", strCommiter);
				
				String strCommiter = mJsonObjectCommit.getString("committer");
				Log.i("Commiter.......", strCommiter);

				JSONObject mJsonObject = new JSONObject(strCommiter);
				
				//Commiter name
				String strName = mJsonObject.getString("name");
				Log.i("Commiter Name.......", strName);
				mCommitsDataModel.setName(strName);
				
				//commiter date
				String strDate = mJsonObject.getString("date");
				Log.i("Commiter Date.......", strDate);
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
