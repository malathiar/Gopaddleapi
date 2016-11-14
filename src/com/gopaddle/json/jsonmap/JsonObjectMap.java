package com.gopaddle.json.jsonmap;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectMap
{
	private static Logger log = Logger.getLogger(JsonObjectMap.class);
	
	private static JsonObjectMap objMap;
	
	protected JsonObjectMap() {	}
	
	public static JsonObjectMap newInstance() {
		objMap = new JsonObjectMap();
		return objMap;
	}
	
	public static JsonObjectMap getInstance() {
		if (objMap == null){
			objMap = new JsonObjectMap();
			return objMap;
		}
		return objMap;
	}	
	
	protected String getBoolean(String json, String key) {
	 	try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getBoolean(key));
		} catch (JSONException e) {
			return null;
		}
	}
	protected String getInt(String json, String key) {
	 	try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getInt(key));
		} catch (JSONException e) {
			return null;
		}
	}
	protected String getLong(String json, String key) {
	 	try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getLong(key));
		} catch (JSONException e) {
			return null;
		}
	}
	protected String getDouble(String json, String key) {
	 try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getDouble(key));
		} catch (JSONException e) {
			return null;
		}
	}
	protected String getString(String json, String key) {
	 	try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getString(key));
		} catch (JSONException e) {
			return null;
		}
	}
	protected String getJsonObj(String json, String key) {
	 	try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getJSONObject(key));
		} catch (JSONException e) {
			return null;
		}
	}
	protected String getjsonArr(String json, String key) {
	 	try {
	 		JSONObject jsonObj = new JSONObject(json);
			return String.valueOf( jsonObj.getJSONArray(key));
		} catch (JSONException e) {
			return null;
		}
	}
	
	private void isBoolean(JSONObject actual, String expectJsonKey, boolean expectJsonValue)
			throws MisMatch, JSONException 
	{
		boolean actualJsonValue = actual.getBoolean(expectJsonKey);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Boolean : " + actualJsonValue + " <-> Expected Boolean : " + expectJsonValue);
    }
	
	private void isInteger(JSONObject actual, String expectJsonKey, int expectJsonValue)
			throws MisMatch, JSONException 
	{
		int actualJsonValue = actual.getInt(expectJsonKey);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Integer : " + actualJsonValue + " <-> Expected Integer : " + expectJsonValue);
    }
	
	private void isLong(JSONObject actual, String expectJsonKey, long expectJsonValue)
			throws MisMatch, JSONException 
	{
		long actualJsonValue = actual.getLong(expectJsonKey);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Long : " + actualJsonValue + " <-> Expected Long : " + expectJsonValue);
    }
	
	private void isDouble(JSONObject actual, String expectJsonKey, double expectJsonValue)
			throws MisMatch, JSONException 
	{
		double actualJsonValue = actual.getDouble(expectJsonKey);
    	if ( actualJsonValue != expectJsonValue) {
    			log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
    			throw new MisMatch(" Actual values does not match with Expected values " + expectJsonValue );
    	}
    	log.info("Actual Double : " + actualJsonValue + " <-> Expected Double : " + expectJsonValue);
    }
	
	private void isString(JSONObject actual, String expectJsonKey, String expectJsonValue)
			throws MisMatch, JSONException
	{
		switch(expectJsonValue){
			case JsonType.JSON_BOOLEAN:
				actual.getBoolean(expectJsonKey);
				break;
			case JsonType.JSON_INT:
				actual.getInt(expectJsonKey);
				break;
			case JsonType.JSON_LONG:
				actual.getLong(expectJsonKey);
				break;
			case JsonType.JSON_STRING:
				actual.getString(expectJsonKey);
				break;
			case JsonType.JSON_DOUBLE:
				actual.getDouble(expectJsonKey);
				break;
			case JsonType.JSON_OBJECT:
				actual.getJSONObject(expectJsonKey);
				break;
			case JsonType.JSON_ARRAY:
				actual.getJSONArray(expectJsonKey);
				break;
	     	default:
				String actualJsonValue = actual.getString(expectJsonKey);
				if ( !actualJsonValue.equalsIgnoreCase(expectJsonValue)) {
				   log.error("Actual values : " + actualJsonValue + " but expected value : " + expectJsonValue);
				   throw new MisMatch(" Actual values does not match with Expected values : " + expectJsonValue );
				}
				log.info("Actual String : " + actualJsonValue + " <-> Expected String : " + expectJsonValue);
				return;
		}
		log.info("Found dynamic values");
	}
	
	private void isJsonObject(JSONObject actual, String expectJsonKey ) throws JSONException {
		 log.info("Check is it Json Object : " + actual.getJSONObject(expectJsonKey));    	
    }
	
	private void isJsonArray(JSONObject actual, String expectJsonKey)throws  JSONException 	{
		log.info("Check is it Json Array : " + actual.getJSONArray(expectJsonKey));    	
    }
	
	/**
	 * 
	 * @param actual Actual json object
	 * @param expect Expected json object
	 * @throws JSONException Return Json exception if invalid Json
	 * @throws MisMatch Return MisMathch exception if actual values doesn't match with expected value
	 */
	public void jsonObject(JSONObject actual, JSONObject expect) throws JSONException, MisMatch
	{
		  Iterator<?> expectJsonKeys = expect.keys();
		  while(expectJsonKeys.hasNext())
		  {
				   	String expectJsonKey = (String) expectJsonKeys.next();
				   	 if(getBoolean(String.valueOf(expect), expectJsonKey) != null){
				    	boolean expectJsonValue =  Boolean.parseBoolean(getBoolean(String.valueOf(expect),expectJsonKey));
				    	log.debug("Key : " + expectJsonKey + " - Boolean Value: " + expectJsonValue);
				    	isBoolean(actual, expectJsonKey, expectJsonValue);
				     }
				    else if(getInt(String.valueOf(expect), expectJsonKey) != null){
				    	int expectJsonValue =  Integer.parseInt(getInt(String.valueOf(expect),expectJsonKey));
				    	log.debug("Key : " + expectJsonKey + " - Integer Value: " + expectJsonValue);
				    	isInteger(actual, expectJsonKey, expectJsonValue);
				    }
				    else if(getLong(String.valueOf(expect), expectJsonKey) != null){
				    	Long expectJsonValue = Long.parseLong(getLong(String.valueOf(expect),expectJsonKey));
				    	log.debug("Key : " + expectJsonKey +  " - Long Value : " + expectJsonValue );
				    	isLong(actual, expectJsonKey, expectJsonValue);
				    }
				    else if(getDouble(String.valueOf(expect), expectJsonKey) != null){
				    	double expectJsonValue =  Double.parseDouble(getDouble(String.valueOf(expect),expectJsonKey));
				    	log.debug("Key : " + expectJsonKey +  " - Double Value : " + expectJsonValue);
				    	isDouble(actual, expectJsonKey, expectJsonValue);
				    }
				    else if(getString(String.valueOf(expect), expectJsonKey) != null){
				    	String expectJsonValue = getString(String.valueOf(expect),expectJsonKey);
				    	log.debug("Key : " + expectJsonKey +  " - String Value : " + expectJsonValue );
				    	isString(actual, expectJsonKey, expectJsonValue);
				    }
				    else if(getJsonObj(String.valueOf(expect), expectJsonKey) != null){
				    	String expectJsonValue = getJsonObj(String.valueOf(expect),expectJsonKey);
				    	log.debug("Key : " + expectJsonKey +  " - Json object : " + expectJsonValue);
				    	isJsonObject(actual, expectJsonKey);
				       	jsonObject(new JSONObject(getJsonObj(String.valueOf(actual),expectJsonKey)),
				    			new JSONObject(getJsonObj(String.valueOf(expect),expectJsonKey)));
				    }
				    else if(getjsonArr(String.valueOf(expect), expectJsonKey) != null){
				    	String expectJsonValue = getjsonArr(String.valueOf(expect),expectJsonKey);
				    	log.debug("Key : " + expectJsonKey +  " - Json Array : " + expectJsonValue);
				    	isJsonArray(actual, expectJsonKey);
				    	JsonArrayMap arrMap = JsonArrayMap.getInstance();
				    	arrMap.jsonArray(new JSONArray(getjsonArr(String.valueOf(actual),expectJsonKey)),			
				    				new JSONArray (getjsonArr(String.valueOf(expect),expectJsonKey)));
				    }
				    else   {
				    	throw new JSONException("Invalid Json Object");
				    }
			 }
	}
}
