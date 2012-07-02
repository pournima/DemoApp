package com.github.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.github.organisation.OrganisationRepositoryDataModel;


public class OrganisationRepositoryParseResult {

	public OrganisationRepositoryParseResult()
	{
		
	}
	
	public ArrayList<OrganisationRepositoryDataModel> parseRepositoryData(String strJsonReponse)
	{
	     ArrayList<OrganisationRepositoryDataModel> repositoryObjArray = new ArrayList<OrganisationRepositoryDataModel>();
	     Log.i("index.....", "inside parser");
		try {
			
			JSONObject mJsonObjectRepository = new JSONObject(strJsonReponse);
			
			String strRepository = mJsonObjectRepository.getString("repository");
			
			JSONArray repositoryJsonArray = new JSONArray(strRepository);
			
			for (int i = 0; i < repositoryJsonArray.length(); i++) {
				
				OrganisationRepositoryDataModel mRepositoryDataModel=new OrganisationRepositoryDataModel();

				String strOwner = repositoryJsonArray.getJSONObject(i).getString("owner");
				Log.i("owner name.....", String.valueOf(strOwner));
				mRepositoryDataModel.setOwner(strOwner);

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
