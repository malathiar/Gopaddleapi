package com.gopaddle.utils;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.json.JSONException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.gopaddle.suite.TestClass;
import com.gopaddle.suite.TestSuite;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


@Listeners(org.uncommons.reportng.HTMLReporter.class)
	public class Multitier implements ITestListener{
	
	TestClass utility=new TestClass();
	String workspace=System.getProperty("user.dir");
	ExtentReports report=TestSuite.report;
	ExtentTest test=TestSuite.test;
	@Parameters({"suiteName"})
	@BeforeMethod
	 public void createreport(ITestContext arg0,@Optional String suiteName){
		try{
			test = report.startTest(arg0.getName());
	    test.assignCategory(suiteName);
		}
		catch(Exception e){
		e.printStackTrace();
		}
	 }
	
	@Parameters("designname")
	@Test
	public void createDesign(@Optional String designname) throws IOException, JSONException{
		utility.createDesign(designname);
	}
	@Parameters({"designname","repopath","auth","repoprovider","build","builder","platform","ports","install","postinstall","preinstall","startscript"})
	@Test
	public void CreateComponent(@Optional String designname,@Optional String repopath,@Optional String auth,@Optional String repoprovider,@Optional String build,@Optional String builder,@Optional String platform,@Optional String ports,@Optional String install,@Optional String postinstall,@Optional String preinstall,@Optional String startscript) throws  IOException, AWTException, InterruptedException{
		utility.CreateComponent(designname,repopath,auth,repoprovider, build, builder, platform, ports, install,postinstall, preinstall, startscript);
	
	}
	@Parameters({"designname","path","version","ports","accesssublink"})
	@Test
	public void createTwotierComponent(@Optional String name,@Optional String path,@Optional String version,@Optional String port,@Optional String accesssublink) throws IOException, JSONException{
		utility.createTwotierComponent(name,path,version,port,accesssublink);
	}
	
	@Parameters({"sourcelink","targetlink"})
	@Test
	public void linkComponent(@Optional String sourcelink,@Optional String targetlink) throws IOException, JSONException{
		utility.linkComponent(sourcelink,targetlink);
	}
	@Parameters({"sourcelink","targetlink"})
	@Test
	public void MultiLinkComponent(@Optional String sourcelink,@Optional String targetlink) throws IOException, JSONException{
		utility.linkComponent(sourcelink,targetlink);
	}
	@Parameters()
	@Test
	public void publishDesign() throws IOException, JSONException{
		utility.publishDesign();
	}
	@Parameters({"appname","ports","altport"})
	@Test
	public void launchApp(@Optional String appname,@Optional String ports,@Optional String altport) throws IOException, JSONException, InterruptedException{
		utility.launchApp(appname,ports,altport);
	}
	@Parameters({"accesssublink"})
	@Test
	public void accessValidation(@Optional String accesssublink) throws IOException, JSONException, InterruptedException{
		utility.accessValidation(accesssublink);
	}
	@Parameters("designname")
	@Test
	public void deleteDesign(@Optional String designname) throws IOException, JSONException{
		utility.deleteDesign(designname);
	}
	@Parameters()
	@Test
	public void deleteApp() throws IOException, JSONException{
		utility.deleteApp();
	}
	
	@Parameters({"suiteName"})
	@AfterMethod
		public void screenshot(ITestResult arg0,@Optional String suiteName) {
		
			int result=arg0.getStatus();
		      String testcase =suiteName+arg0.getName().toString(); 
		     Properties prop = new Properties();
		     if(result ==1){
		      prop.put(testcase, "PASS");
		      test.log(LogStatus.PASS,test.getDescription(),suiteName+arg0.getName());
		      test.getDescription();
		     }else if(result ==2){
		      prop.put(testcase, "FAIL"); 
		      test.log(LogStatus.FAIL,arg0.getThrowable());
		     }else if(result ==3){
		      prop.put(testcase, "SKIP"); 
		      test.log(LogStatus.SKIP,arg0.getThrowable());
		     }
			report.endTest(test);
			report.flush();
		}
		
		@Override
		public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTestFailure(ITestResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTestSkipped(ITestResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTestStart(ITestResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTestSuccess(ITestResult arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStart(ITestContext context) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onFinish(ITestContext context) {
			// TODO Auto-generated method stub
			
		}

}
	
