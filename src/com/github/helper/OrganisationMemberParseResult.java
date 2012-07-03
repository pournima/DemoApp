package com.github.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.repository.RepositoryDataModel;

import android.util.Log;


public class OrganisationMemberParseResult {

	public OrganisationMemberParseResult()
	{
		
	}
	
	public ArrayList<RepositoryDataModel> parseRepositoryData(String strJsonReponse)
	{
	     ArrayList<RepositoryDataModel> repositoryObjArray = new ArrayList<RepositoryDataModel>();
	     Log.i("index.....", "inside member parser");
		try {
			
			JSONObject mJsonObjectRepository = new JSONObject(strJsonReponse);
			
			String strRepository = mJsonObjectRepository.getString("members");
			
			JSONArray repositoryJsonArray = new JSONArray(strRepository);
			
			for (int i = 0; i < repositoryJsonArray.length(); i++) {
				
				RepositoryDataModel mMemberDataModel=new RepositoryDataModel();

				String strMemberName = repositoryJsonArray.getJSONObject(i).getString("name");
				Log.i("member name.....", String.valueOf(strMemberName));
				mMemberDataModel.setName(strMemberName);
				
				repositoryObjArray.add(mMemberDataModel);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return repositoryObjArray;
	}

}
