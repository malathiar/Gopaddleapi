package com.gopaddle.suite;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadTestCase {

	private String input;
	private String expectedOutput;
	public ReadTestCase dataparser(String jsonFile)
	{
		try
		{
			 JSONObject js = new JSONObject(jsonFile);			
			 setInput(js.getJSONObject("input").toString());			
			 setExpectedOutput(js.getJSONObject("output").toString());
		}catch(JSONException je)
		{
			System.out.println(je.getMessage());
		}
		
		return null;		
	}
	public String getExpectedOutput() {
		return expectedOutput;
	}
	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
}
