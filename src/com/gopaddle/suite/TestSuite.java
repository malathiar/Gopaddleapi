package com.gopaddle.suite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class TestSuite implements ITestListener {

	static WebDriver driver;
	//static ChromeDriver driver;
	static boolean status = true;
	static String projectHome = System.getProperty("user.dir");
	public static ExtentReports report;
	public static ExtentTest test;
	static String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	List<XmlSuite> xmlSuites;
	static String workspace = System.getProperty("user.dir");
	public static final String USERNAME = "v_m_k_r";
	 public static final String ACCESS_KEY = "b2379be9-08d0-41fa-b821-d99437e63d7d";
	 public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";
	//static String cloud;
	public TestSuite() {
		xmlSuites = new ArrayList<XmlSuite>();
	}

	public WebDriver newDriver() {
		if (driver == null) {
			return new ChromeDriver();
		}
		return driver;
	}
	public void method(boolean stat) {
		status = stat;
	}

	public static void login() throws IOException {
		try {
			File file = new File(workspace + "/file.Properties");
System.out.println(workspace + "/file.Properties");
			if (file.delete()) {
				System.out.println("file not available");
			} else {
				System.out.println("file not availableelse");
			}
			
			file.createNewFile();
			Thread.sleep(10000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	

	private void runTests(TestNG tng) throws JAXBException, Exception {
		XmltoJava xmltojava;

		xmltojava = (XmltoJava) com.gopaddle.suite.GenericClass
				.unmarshallClass(projectHome + "/" + "config.xml", XmltoJava.class);
		for (int k = 0; k < xmltojava.getSuite().length; k++) {

			XmlSuite suite = new XmlSuite();

			ArrayList<XmlTest> tests = new ArrayList<XmlTest>();

			for (int l = 0; l < xmltojava.getSuite()[k].getParameter().length; l++) {

				Map<String, String> parameters = new HashMap<String, String>();

				suite.setName(xmltojava.getSuite()[k].getClassname() + " - "
						+ xmltojava.getSuite()[k].getSuitename());
				List<XmlTest> xmlTest = new ArrayList<XmlTest>();
				XmlTest test = new XmlTest(suite);
				test.setName(xmltojava.getSuite()[k].getParameter()[l].getTestcase());

				parameters.put("suiteName", xmltojava.getSuite()[k].getSuitename());
				parameters.put("Testcase", xmltojava.getSuite()[k].getParameter()[l].getTestcase());
				parameters.put("sourcelink", xmltojava.getSuite()[k].getParameter()[l].getSourcelink());
				parameters.put("targetlink", xmltojava.getSuite()[k].getParameter()[l].getTargetlink());
				parameters.put("designname", xmltojava.getSuite()[k].getParameter()[l].getDesignname());
				parameters.put("repopath", xmltojava.getSuite()[k].getParameter()[l].getRepopath());
				parameters.put("repoprovider", xmltojava.getSuite()[k].getParameter()[l].getRepoprovider());
				parameters.put("build", xmltojava.getSuite()[k].getParameter()[l].getBuild());
				parameters.put("builder", xmltojava.getSuite()[k].getParameter()[l].getBuilder());
				parameters.put("platform", xmltojava.getSuite()[k].getParameter()[l].getPlatform());
				parameters.put("ports", xmltojava.getSuite()[k].getParameter()[l].getPorts());
				parameters.put("postinstall", xmltojava.getSuite()[k].getParameter()[l].getPostinstall());
				parameters.put("preinstall", xmltojava.getSuite()[k].getParameter()[l].getPreinstall());
				parameters.put("startscript", xmltojava.getSuite()[k].getParameter()[l].getStartscript());
				parameters.put("kubname", xmltojava.getSuite()[k].getParameter()[l].getKubname());
				parameters.put("altport", xmltojava.getSuite()[k].getParameter()[l].getAltport());
				parameters.put("appname", xmltojava.getSuite()[k].getParameter()[l].getAppname());
				parameters.put("accesssublink", xmltojava.getSuite()[k].getParameter()[l].getAccesssublink());
				parameters.put("path", xmltojava.getSuite()[k].getParameter()[l].getPath());
				parameters.put("version", xmltojava.getSuite()[k].getParameter()[l].getVersion());
				parameters.put("install", xmltojava.getSuite()[k].getParameter()[l].getInstall());
				parameters.put("auth", xmltojava.getSuite()[k].getParameter()[l].getAuth());
				parameters.put("Depends", xmltojava.getSuite()[k].getParameter()[l].getDepends());
				/*System.out.println("path"+xmltojava.getSuite()[k].getParameter()[l].getRepopath());
				System.out.println("provider"+xmltojava.getSuite()[k].getParameter()[l].getRepoprovider());
				System.out.println("build"+xmltojava.getSuite()[k].getParameter()[l].getBuild());
				System.out.println("builder"+xmltojava.getSuite()[k].getParameter()[l].getBuilder());
				System.out.println("platform"+xmltojava.getSuite()[k].getParameter()[l].getPlatform());
				System.out.println("port"+xmltojava.getSuite()[k].getParameter()[l].getPorts());
				System.out.println("postin"+xmltojava.getSuite()[k].getParameter()[l].getPostinstall());
				System.out.println("preinst"+xmltojava.getSuite()[k].getParameter()[l].getPreinstall());
				System.out.println("starts"+xmltojava.getSuite()[k].getParameter()[l].getStartscript());
				System.out.println("preinst"+xmltojava.getSuite()[k].getParameter()[l].getSourcelink());
				System.out.println("starts"+xmltojava.getSuite()[k].getParameter()[l].getTargetlink());*/
				test.setParameters(parameters);

				ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
				ArrayList<XmlClass> classes1 = new ArrayList<XmlClass>();
				XmlClass classes = new XmlClass();

				classes.setName("com.gopaddle.utils." + xmltojava.getSuite()[k].getClassname());

				methodsToRun.add(new XmlInclude(xmltojava.getSuite()[k].getParameter()[l].getTestcase()));
				classes.setIncludedMethods(methodsToRun);
				classes1.add(classes);
				test.setXmlClasses(classes1);
				xmlTest.add(test);
				tests.addAll(xmlTest);
			}
			suite.setTests(tests);

			xmlSuites.add(suite);
		}
		tng.setXmlSuites(xmlSuites);
		
		tng.run();
	}

	public static void main(String[] args) throws JAXBException, Exception {
		
		TestSuite rtest = new TestSuite();
		TestNG tng = new TestNG();
		report = new ExtentReports(projectHome+"/test-output"+timeStamp+"/ExtendedReports"+timeStamp+".html");
		report.loadConfig(new File(projectHome+"/extent-config.xml"));
		tng.setOutputDirectory("test-output"+timeStamp+"/");
		login();
		rtest.runTests(tng);
		
		}	
	

	@Override
	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub

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

}
