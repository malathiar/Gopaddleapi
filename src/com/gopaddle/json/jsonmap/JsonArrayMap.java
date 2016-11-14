package com.gopaddle.json.jsonmap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class JsonArrayMap 
{
	private static Logger log = Logger.getLogger(JsonArrayMap.class);
	
	private static JsonArrayMap arrMap;
	
	protected JsonArrayMap() { 	}
	
	public static JsonArrayMap newInstance() {
		arrMap = new JsonArrayMap();
		return arrMap;
	}
	
	public static JsonArrayMap getInstance() {
		if (arrMap == null){
			arrMap = new JsonArrayMap();
			return arrMap;
		}
		return arrMap;
	}	
	
	protected String getInt(String json, int index) {
	 	try {
	 		JSONArray jsonArr = new JSONArray(json);
			return String.valueOf( jsonArr.getInt(index));
		} catch (JSONException e) {
			return null;
		}
	}
	
	protected String getLong(String json, int index) {
	 	try {
	 		JSONArray jsonArr = new JSONArray(json);
			return String.valueOf( jsonArr.getLong(index));
		} catch (JSONException e) {
			return null;
		}
	}
	
	protected String getString(String json, int index) {
	 	try {
	 		JSONArray jsonArr = new JSONArray(json);
			return String.valueOf( jsonArr.getString(index));
		} catch (JSONException e) {
			return null;
		}
	}
	
	protected String getJsonObj(String json, int index) {
	 	try {
	 		JSONArray jsonObj = new JSONArray(json);
			return String.valueOf( jsonObj.getJSONObject(index));
		} catch (JSONException e) {
			return null;
		}
	}
	
	protected String getBoolean(String json, int index) {
	 	try {
	 		JSONArray jsonArr = new JSONArray(json);
			return String.valueOf(jsonArr.getBoolean(index));
		} catch (JSONException e) {
			return null;
		}
	}
	
	protected String getDouble(String json, int index) {
	 	try {
	 		JSONArray jsonArr = new JSONArray(json);
			return String.valueOf( jsonArr.getDouble(index));
		} catch (JSONException e) {
			return null;
		}
	}
	
	private void isBoolean(JSONArray actual, int index, boolean expectJsonValue)
			throws MisMatch, JSONException 
	{
		boolean actualJsonValue = actual.getBoolean(index);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Boolean : " + actualJsonValue + " <-> Expected Boolean : " + expectJsonValue);
    }
	
	private void isInteger(JSONArray actual, int index, int expectJsonValue)
			throws MisMatch, JSONException 
	{
		int actualJsonValue = actual.getInt(index);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Integer : " + actualJsonValue + " <-> Expected Integer : " + expectJsonValue);
    }
	
	private void isLong(JSONArray actual, int index, long expectJsonValue)
			throws MisMatch, JSONException 
	{
		long actualJsonValue = actual.getLong(index);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Long : " + actualJsonValue + " <-> Expected Long : " + expectJsonValue);
    }
	
	private void isDouble(JSONArray actual, int index, double expectJsonValue)
			throws MisMatch, JSONException 
	{
		double actualJsonValue = actual.getDouble(index);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Double : " + actualJsonValue + " <-> Expected Double : " + expectJsonValue);
    }
	
	private boolean isString(JSONArray actual, int index, String expectJsonValue)
			throws MisMatch, JSONException
	{
		switch(expectJsonValue){
			case JsonType.JSON_BOOLEAN:
				actual.getBoolean(index);
				return false;
				
			case JsonType.JSON_INT:
				actual.getInt(index);
				return false;
			
			case JsonType.JSON_LONG:
				actual.getLong(index);
				return false;
			
			case JsonType.JSON_STRING:
				actual.getString(index);
				return false;
			
			case JsonType.JSON_DOUBLE:
				actual.getDouble(index);
				return false;
			
			case JsonType.JSON_OBJECT:
				actual.getJSONObject(index);
				return false;
				
			default:
				   String actualJsonValue = actual.getString(index);
				   if ( !actualJsonValue.equalsIgnoreCase(expectJsonValue)) {
					log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
					throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
				}
				log.info("Actual String : " + actualJsonValue + " <-> Expected String : " + expectJsonValue);
				return true;
		}
	}
	
	private void isJsonObject(JSONArray actual, int index ) throws JSONException {
		 log.info("Check is it Json Object : " + actual.getJSONObject(index));    	
    }
	
	public  void jsonArray(JSONArray actual, JSONArray expect) throws JSONException, MisMatch {
		int i = 0;
		while ( i < expect.length() ) {
			if(getBoolean(String.valueOf(expect), i) != null){
				boolean expectJsonValue =  Boolean.parseBoolean(getBoolean(String.valueOf(expect),i));
		    	log.debug("Index : " + i + " - Boolean Value: " + expectJsonValue);
		    	isBoolean(actual, i, expectJsonValue);
		    }
			else if(getInt(String.valueOf(expect), i) != null){
				int expectJsonValue =  Integer.parseInt(getInt(String.valueOf(expect),i));
		    	log.debug("Index : " + i + " - Integer Value: " + expectJsonValue);
		    	isInteger(actual, i, expectJsonValue);
		    }
			else if(getLong(String.valueOf(expect), i) != null){
				long expectJsonValue =  Long.parseLong(getLong(String.valueOf(expect),i));
		    	log.debug("Index : " + i + " - Long Value: " + expectJsonValue);
		    	isLong(actual, i, expectJsonValue);
		   }
			else if(getDouble(String.valueOf(expect), i) != null){
				double expectJsonValue =   Double.parseDouble(getDouble(String.valueOf(expect),i));
		    	log.debug("Index : " + i + " - Double Value: " + expectJsonValue);
		    	isDouble(actual, i, expectJsonValue);
		    }
			else if(getString(String.valueOf(expect), i) != null){
				String expectJsonValue =   getString(String.valueOf(expect),i);
		    	log.debug("Index : " + i + " - String Value: " + expectJsonValue);
		    	if ( isString(actual, i, expectJsonValue) == false ) {
		    		log.debug("Found dynamic values");
		    		break;
		    	}
		    }
			else if(getJsonObj(String.valueOf(expect), i) != null){
				String  expectJsonValue =   getJsonObj(String.valueOf(expect),i);
		    	log.debug("Index : " + i + " - JSON Value: " + expectJsonValue);
		    	isJsonObject(actual, i);
		    	JsonObjectMap objMap =  JsonObjectMap.getInstance();
		    	objMap.jsonObject(new JSONObject(getJsonObj(String.valueOf(actual),i)),
		    			new JSONObject(getJsonObj(String.valueOf(expect),i)));
		    }
			else   {
		    	throw new JSONException("Invalid Json Array");
		    }
			i++;
		}
	}
}
