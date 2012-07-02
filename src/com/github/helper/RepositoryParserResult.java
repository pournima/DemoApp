package com.github.helper;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.repository.RepositoryDataModel;
import android.util.Log;

public class RepositoryParserResult {

	
	public RepositoryParserResult()
	{
		
	}
	
	public ArrayList<RepositoryDataModel> parseRepositoryData(String strJsonReponse)
	{
	     ArrayList<RepositoryDataModel> repositoryObjArray = new ArrayList<RepositoryDataModel>();
	     Log.i("index.....", "inside parser");
		try {
			
			JSONObject mJsonObjectRepository = new JSONObject(strJsonReponse);
			
			String strRepository = mJsonObjectRepository.getString("repository");
			
			JSONArray repositoryJsonArray = new JSONArray(strRepository);
			
			for (int i = 0; i < repositoryJsonArray.length(); i++) {
				
				RepositoryDataModel mRepositoryDataModel=new RepositoryDataModel();
				
//				Log.i("index.....", String.valueOf(i));
//
//				String strId = repositoryJsonArray.getJSONObject(i).getString("id");
//				Log.i("Id.....", String.valueOf(strId));
//				
//				mRepositoryDataModel.setId(strId);

				String strName = repositoryJsonArray.getJSONObject(i).getString("name");
				Log.i("repo name.....", String.valueOf(strName));
				mRepositoryDataModel.setName(strName);
				
				repositoryObjArray.add(mRepositoryDataModel);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return repositoryObjArray;
	}

}
