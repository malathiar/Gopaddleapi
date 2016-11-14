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
	public class Singletier implements ITestListener{
	
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
	@Parameters("kubname")
	@Test
	public void createKubes(@Optional String kubname) throws IOException, JSONException{
		utility.createKubes(kubname);
	}
	@Parameters()
	@Test
	public void connection() throws IOException, JSONException{
		utility.connection();
	}
	@Parameters("kubname")
	@Test
	public void getkubId(@Optional String kubname) throws IOException, JSONException{
		utility.getkubId(kubname);
	}
	@Parameters("designname")
	@Test
	public void createDesign(@Optional String designname) throws IOException, JSONException{
		utility.createDesign(designname);
	}
	@Parameters({"designname","repopath","auth","repoprovider","build","builder","platform","ports","install","postinstall","preinstall","startscript"})
	@Test
	public void CreateComponent(@Optional String designname,@Optional String repopath,@Optional String auth,@Optional String repoprovider,@Optional String build,@Optional String builder,@Optional String platform,@Optional String ports,@Optional String install,@Optional String postinstall,@Optional String preinstall,@Optional String startscript) throws  IOException, AWTException, InterruptedException{
		utility.CreateComponent(designname,repopath,auth,repoprovider, build, builder, platform, ports,install, postinstall, preinstall, startscript);
	
	}
	@Parameters({"sourcelink","targetlink"})
	@Test
	public void linkComponent(@Optional String sourcelink,@Optional String targetlink) throws IOException, JSONException{
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
	@Parameters()
	@Test
	public void updateAppStateOn() throws IOException, JSONException{
		utility.updateApplicationStateOn();
	}
	@Parameters()
	@Test
	public void updateAppStateOff() throws IOException, JSONException{
		utility.updateAppStateOff();
	}
	@Parameters()
	@Test
	public void updateAppDeliveryON() throws IOException, JSONException{
		utility.updateApplicationStateOn();
		utility.updateApplicationDeliveryON();
	}
	@Parameters()
	@Test
	public void updateAppDeliveryOff() throws IOException, JSONException{
		utility.updateApplicationDeliveryOff();
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
	@Parameters()
	@Test
	public void listAllDesigns() throws IOException, JSONException{
		utility.listAllDesigns();
	}
	@Parameters()
	@Test
	public void searchSingleDesign() throws IOException, JSONException{
		utility.searchSingleDesign();
	}
	@Parameters()
	@Test
	public void deleteComponent() throws IOException, JSONException{
		utility.deleteComponent();
	}
	@Parameters("kubname")
	@Test
	public void listKubes(@Optional String kubname) throws IOException, JSONException{
		utility.listKubes(kubname);
	}
	@Parameters()
	@Test
	public void listApps() throws IOException, JSONException{
		utility.listApps();
	}
	@Parameters()
	@Test
	public void searchApp() throws IOException, JSONException{
		utility.searchApp();
	}
	@Parameters()
	@Test
	public void searchComponent() throws IOException, JSONException{
		utility.searchComponent();
	}
	@Parameters()
	@Test
	public void updateComponent() throws IOException, JSONException{
		utility.updateComponent();
	}
	@Parameters()
	@Test
	public void updateDesign() throws IOException, JSONException{
		utility.updateDesign();
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
	
