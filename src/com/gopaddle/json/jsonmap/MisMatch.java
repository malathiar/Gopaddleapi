package com.gopaddle.json.jsonmap;

import org.testng.Reporter;

public class MisMatch extends Exception
{
		
	private static final long serialVersionUID = 1L;

		public MisMatch(String msg) {
			super(msg);
			Reporter.log(msg);
			
		}
}
