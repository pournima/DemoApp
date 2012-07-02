package com.github.helper;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.github.organisation.OrganisationDataModel;

public class OrganisationParserResult {

	public OrganisationParserResult()
	{
		
	}
	
	public ArrayList<OrganisationDataModel> parseRepositoryData(String strJsonReponse)
	{
	     ArrayList<OrganisationDataModel> organisationObjArray = new ArrayList<OrganisationDataModel>();
	     Log.i("index.....", "inside org parser");
		try {
			
			JSONObject mJsonObjectOrganisation = new JSONObject(strJsonReponse);
			
			String strRepository = mJsonObjectOrganisation.getString("organizations");
			
			JSONArray organisationJsonArray = new JSONArray(strRepository);
			
			for (int i = 0; i < organisationJsonArray.length(); i++) {
				
				OrganisationDataModel mOrganisationDataModel=new OrganisationDataModel();
				
				Log.i("index.....", String.valueOf(i));

				String strName = organisationJsonArray.getJSONObject(i).getString("name");
				Log.i("organisation name.....", String.valueOf(strName));
				mOrganisationDataModel.setOrgName(strName);
				
				organisationObjArray.add(mOrganisationDataModel);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return organisationObjArray;
	}	
	
}
