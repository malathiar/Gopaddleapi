package com.gopaddle.json.jsonmap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonCompare
{
	private static Logger log = Logger.getLogger(JsonCompare.class);
	
	private JsonObjectMap objMap;
	
	private JsonArrayMap arrMap;
		
	public JsonCompare() {
		 objMap = JsonObjectMap.newInstance();
		 arrMap = JsonArrayMap.newInstance();
	}
	
	public boolean compTwoJson(String actual, String expect) throws JSONException, MisMatch
	{
		  boolean isItWorked = false;
		   try {
			   log.debug("Found the json object : "  );
			   JSONObject jsonExpObj = new JSONObject(expect);
			   isItWorked = true;
			   JSONObject jsonActObj = new JSONObject(actual);
			   objMap.jsonObject(jsonActObj, jsonExpObj);
		   }
		   catch(JSONException jsonEx)
		   {
			   if (!isItWorked) {
				   			   try {
				   				   log.debug("Found the json array :  " );
								   JSONArray jsonActArr = new JSONArray(actual);
								   JSONArray jsonExpArr = new JSONArray(expect);
								   arrMap.jsonArray(jsonActArr, jsonExpArr);
							    }
							   catch(JSONException fatalEx) {
								   log.error("Invalid JSON : " + fatalEx.getLocalizedMessage());
								   throw new JSONException(fatalEx.getMessage());
							  }
		       }
			   else {
				   log.error("Formation Error : " + jsonEx.getLocalizedMessage());
				   throw new JSONException(jsonEx.getMessage());
			   }
		   }
		   return true;
	 }
}
