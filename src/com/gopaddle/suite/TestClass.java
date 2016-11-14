package com.gopaddle.suite;

import java.io.File;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SuiteRunner;
import org.testng.TestNG;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import com.gopaddle.json.jsonmap.JsonCompare;
import com.gopaddle.json.jsonmap.MisMatch;
import com.gopaddle.json.parser.JsonFormatter;
import com.gopaddle.suite.ResponseBody;
import com.gopaddle.utils.ConfigReader;
import com.gopaddle.utils.Readjson;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

@Listeners(org.uncommons.reportng.HTMLReporter.class)
public class TestClass extends TestNG {

	String jsonFile;
	static Logger log = Logger.getLogger(SuiteRunner.class);
	RestCaller rc = new RestCaller();
	ResponseBody responseBody;
	static int c=0;
	String endPoint;
	String payloadPath = "payload/";
	String token,handle;
	String bpId,appId,compId,codebaseId;
	boolean respCompare = false;
	ReadTestCase RTC = new ReadTestCase();
	ConfigReader con = ConfigReader.getConfig();
	JSONObject js,js1;
	static int conncount=1;
	String rootPath="log/log.log/";
	ExtentReports report=TestSuite.report;
	ExtentTest test=TestSuite.test;
	String kubeId = "";
	String appid="";
	int length;
	public TestClass() {

		String jsonFile = Readjson.readFileAsString("config");
		try {
			js = new JSONObject(jsonFile);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.setProperty("file.log","./log/log.log");
		PropertyConfigurator.configure("./log4j.properties");
		
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		
		try {
			endPoint = js.getString("url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(TestClass.conncount==1)
		{			
			try {
				connection();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonFile = Readjson.readFileAsString("config");
			try {
				js = new JSONObject(jsonFile);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				con.Put("##HANDLE", js.getString("handle"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			TestClass.conncount++;
			}		
	}
	
	@Test
	public void connection() throws IOException, JSONException{
		
		String p12fileLocation="credentials/Bluemeric-fb2209fc4af8.p12";
		String emailId="145053954158-compute@developer.gserviceaccount.com";
		  String apiURL="http://api.stage.gopaddle.io/api/auth/google/sdk";
		  
		  File originalFile = new File(p12fileLocation);
		                
		        String encodedBase64 = null;
		        FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
		        byte[] bytes = new byte[(int)originalFile.length()];
		        fileInputStreamReader.read(bytes);
		        encodedBase64 = new String(Base64.encodeBase64(bytes));
			        JSONObject json=new JSONObject();
		        json.put("p12file", encodedBase64);
		        json.put("email_id", emailId);
		              
		        responseBody = rc.httpPost(apiURL, json.toString(), "JSON");
		       
		         int statusCompare = responseBody.getResponseCode() == 200
		           || responseBody.getResponseCode() == 202 ? 0 : 1;
		         fileInputStreamReader.close();
		         if (statusCompare == 0) {
		        	 Reporter.log("get Response body: = " + responseBody.getResponseBody());
		        	 JSONObject resp = new JSONObject(responseBody.getResponseBody());
		 			token=resp.getString("token");
		 			handle=resp.getString("handle");
		 			String jsonFile = Readjson.readFileAsString("config");
		 			js = new JSONObject(jsonFile);
		        	 js.put("token", token);
		        	 Readjson.stringWriteAsFile(js.toString(), "config");
		        	 jsonFile = Readjson.readFileAsString("config");
			 			js = new JSONObject(jsonFile);
		        	 js.put("handle", handle);
		        	 Readjson.stringWriteAsFile(js.toString(), "config");
		     	}
			}
	
	//Creating Design
	public void createDesign(String name) throws JSONException {
		handle = con.Get("##HANDLE");
		String apiUrl = endPoint + "/api/" + handle+"/design";
		jsonFile = Readjson.readFileAsString(payloadPath
				+ "design/createDesign.json");
		try {
			RTC.dataparser(jsonFile);
			RTC.setInput(RTC.getInput().replaceAll("#DESIGNNAME#", name));
			RTC.setExpectedOutput(RTC.getInput().replaceAll("#DESIGNNAME#", name));
			responseBody = rc.httpPost(apiUrl, RTC.getInput(), "JSON");
			int statusCompare = responseBody.getResponseCode() == 200
					|| responseBody.getResponseCode() == 202 ? 0 : 1;
			if (statusCompare == 0) {
				JSONObject resp = new JSONObject(responseBody.getResponseBody());
				bpId=resp.getString("id");
				String design_name=resp.getString("name");
				c++;
				Reporter.log("Design Name : " +design_name);
				Reporter.log("Design Id   : " +bpId);

				//update the bpid in the config file
				String jsonFile = Readjson.readFileAsString("config");
				js = new JSONObject(jsonFile);
				js.put("bpId", bpId);
				Readjson.stringWriteAsFile(js.toString(), "config");
				
				//comparing
				JsonCompare jc = new JsonCompare();
				
				JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
				JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
				try {
					respCompare = jc
							.compTwoJson(temp2.toString(), temp1.toString());
				} catch (MisMatch mm) {
					System.out.println(mm.getMessage());
				}
				
			}
			Reporter.log("Resource URI : "+apiUrl);
			Reporter.log("Response Status = " + responseBody.getResponseCode());
			Reporter.log("get Response body: = " + responseBody.getResponseBody());
		if (respCompare) {
				log.info("create the Ant  Design is successfully");
			} else {
				log.info("Creating Ant Design get failed");
			
			Assert.assertEquals((respCompare && statusCompare == 0), true);
		}
		}catch (JSONException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage(), e.getCause());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage(), e.getCause());
		}
		
	}
	//create component
		public void CreateComponent(String name,String repopath,String auth,String repoprovider,String build,String ibuilder,String iplatform,String iports,String install,String postinstall,String preinstall,String startscript) {
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try
			{
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId+"/component";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "component/createComponent.json");
			try {
					RTC.dataparser(jsonFile);
					RTC.setInput(RTC.getInput().replaceAll("#REPO_PATH#", repopath));
					RTC.setInput(RTC.getInput().replaceAll("#BUILD#", build));
					RTC.setInput(RTC.getInput().replaceAll("#BUILDER#", ibuilder));
					RTC.setInput(RTC.getInput().replaceAll("#PLATFORM#", iplatform));
					RTC.setInput(RTC.getInput().replaceAll("#PORT#", iports));
					RTC.setInput(RTC.getInput().replaceAll("#INSTALL#", install));
					RTC.setInput(RTC.getInput().replaceAll("#POST_INSTALL#", postinstall));
					RTC.setInput(RTC.getInput().replaceAll("#PRE_INSTALL#", preinstall));
					RTC.setInput(RTC.getInput().replaceAll("#REPO_HANDLE#", auth));
					RTC.setInput(RTC.getInput().replaceAll("#REPO_PROVIDER#", repoprovider));
					RTC.setInput(RTC.getInput().replaceAll("#START_SCRIPT#", startscript));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#REPO_PATH#", repopath));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#BUILD#", build));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#BUILDER#", ibuilder));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#PLATFORM#", iplatform));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#PORT#", iports));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#INSTALL#", install));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#POST_INSTALL#", postinstall));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#PRE_INSTALL#", preinstall));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#REPO_HANDLE#", auth));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#REPO_PROVIDER#", repoprovider));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#START_SCRIPT#", startscript));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#DESIGNNAME#", name));
					responseBody = rc.httpPost(apiUrl, RTC.getInput(), "JSON");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					String component_type=resp.getString("component_type");
					String platform=resp.getString("platform");
						JSONObject repo = resp.getJSONObject("repo");
						String	repo_path=repo.getString("repo_path");
						String repo_provider=repo.getString("repo_provider");
						JSONObject script1 = resp.getJSONObject("script");
						if(ibuilder !=""){
						String	builder=script1.getString("builder");
						Reporter.log("Builder              = " + builder);
						}
						if(startscript !=""){
						String	startscript1=script1.getString("start_script");
						Reporter.log("startscript1		   = " + startscript1);
						}
						if(postinstall !=""){
						String	postinstall1=script1.getString("post_install");
						Reporter.log("postinstall1		   = " + postinstall1);
						}
						if(preinstall !=""){
						String	preinstall1=script1.getString("pre_install");
						Reporter.log("preinstall1		   = " + preinstall1);
						}
						JSONObject network1 = resp.getJSONObject("networks");
						String ports=network1.getString("ports");
						c++;
					Reporter.log("Component Type       = " + component_type);
					Reporter.log("Component Path       = " + repo_path);
					Reporter.log("Repository Provider  = " + repo_provider);
					Reporter.log("Platform			   = " + platform);
					Reporter.log("Ports				   = " + ports);
					
					if(responseBody.getResponseBody().toString().contains(ports)){
						Reporter.log("Resource URI : "+apiUrl);
						Reporter.log("Expected Status =  200");
						Reporter.log("Response Status = " + responseBody.getResponseCode());
						Reporter.log("get Response body: = " + responseBody.getResponseBody());
					}else{
						Assert.assertFalse(true, "response data not valid");
					}
				}
				
				
					if (respCompare) {
					log.info("Create Design from existing repo get Success");
				} else {
					log.info("Create Design from existing repo is failed");
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(),e.getCause());
			
			}
		}
		public void linkComponent(String sourcelink,String targetlink) {
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			try
			{
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId;
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "design/linkComponent.json");
			c++;
			try {
				RTC.dataparser(jsonFile);
				RTC.setInput(RTC.getInput().replaceAll("5", sourcelink));
				RTC.setInput(RTC.getInput().replaceAll("6", targetlink));
		System.out.println("link details"+RTC.getInput().toString());
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				Reporter.log("Actual Response body: = " + RTC.getExpectedOutput());
				
					Assert.assertEquals(( statusCompare == 0 ), true);
			
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(JSONException e)
			{
				e.getLocalizedMessage();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void publishDesign() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId;
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "design/publish.json");
			c++;
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					//comparing
					JsonCompare jc = new JsonCompare();
					
					JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
					JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
					
					try {
						respCompare = jc
								.compTwoJson(temp2.toString(), temp1.toString());
					} catch (MisMatch mm) {
						System.out.println(mm.getMessage());
					}
										
				}
				
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Expected Status =  200");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());Reporter.log("Actual Response body: = " + RTC.getExpectedOutput());
				
				if (respCompare) {
					log.info("Link Component get success");
				} else {
					log.info("Link Component get failed");
				
				Assert.assertEquals((respCompare && statusCompare == 0), true);
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}

		}
		public void createKubes(String kubname) {
			handle = con.Get("##HANDLE");
			String apiUrl = endPoint + "/api/" + handle+"/kube?operation=create";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "kube/createKubes.json");
			try {
				RTC.dataparser(jsonFile);
				RTC.setInput(RTC.getInput().replaceAll("#kUBNAME#", kubname));
				responseBody = rc.httpPost(apiUrl, RTC.getInput(), "JSON");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
								
					//comparing
					JsonCompare jc = new JsonCompare();
					
					JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
					JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
					System.out.println(responseBody.getResponseBody());
					
					try {
						respCompare = jc
								.compTwoJson(temp2.toString(), temp1.toString());
					} catch (MisMatch mm) {
						System.out.println(mm.getMessage());
					}
					
				}
				
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("Request Body = " + JsonFormatter.format(RTC.getInput()));
				Reporter.log("Response Message = "
						+ JsonFormatter.format(responseBody.getResponseBody()));
				if (respCompare) {
					log.info("creating kubernates is successfully");
				} else {
					log.info("Creating kubernates get failed");
					TimeUnit.MINUTES.sleep(5);
				Assert.assertEquals((respCompare && statusCompare == 0), true);
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			
		}
		public void getkubId(String kubname) {
			handle = con.Get("##HANDLE");
			String apiUrl = endPoint + "/api/" + handle+"/kube?force=true";
			
			JSONArray kubeId_temp = new JSONArray();
			
			try {
					responseBody = rc.httpGet(apiUrl,"Normal");
					apiUrl = endPoint + "/api/" + handle+"/kube?force=false";
					responseBody = rc.httpGet(apiUrl,"Normal");
					int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					kubeId_temp=resp.getJSONArray("result");
					String machine_name=kubname;
					int i;
					for(i=0;i<kubeId_temp.length();i++)
					{
						JSONObject value=kubeId_temp.getJSONObject(i);
						String kubename=value.getString("name");
						if(machine_name.equals(kubename))
						{
							System.out.println("kub is availab");
							Reporter.log("Kube Name : "+kubename);
							String kubeId = value.getString("id");
							Reporter.log("Kube ID   : "+kubeId);
							con.Put("##KUBEID", kubeId);
							log.info("KubeId get Success");
						}
						
					}	
					if(con.Get("##KUBEID") == null){
						Reporter.log("Kub is not existing");
						Assert.fail();
					}
				
				}
				Assert.assertEquals(statusCompare == 0, true);

			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void launchApp(String appname,String ports,String altport) throws InterruptedException {
			handle = con.Get("##HANDLE");
		String	kubeId=con.Get("##KUBEID");
		Thread.sleep(20000);
			String jsonFile1 = Readjson.readFileAsString("config");
			try
			{
			js = new JSONObject(jsonFile1);
				bpId=js.getString("bpId");
				System.out.println(bpId);
			String apiUrl = endPoint + "/api/" + handle+"/app/"+bpId;
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "launch/launch.json");
			
			try {
					RTC.dataparser(jsonFile);
					RTC.setInput(RTC.getInput().replaceAll("#PORTS#", ports));
					RTC.setInput(RTC.getInput().replaceAll("#ALTPORTS#", altport));
					RTC.setInput(RTC.getInput().replaceAll("##KUBEID", kubeId));
					RTC.setInput(RTC.getInput().replaceAll("#APPNAME#", appname));
					responseBody = rc.httpPost(apiUrl, RTC.getInput(), "JSON");
					c++;
					int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				
				if (statusCompare == 0) {
					
					JSONObject resp = new JSONObject(RTC.getInput());
					String appliname=resp.getString("app_name");
					JSONObject repo = resp.getJSONObject("expose_ports");
					String ports1=repo.getString(ports);
					Reporter.log("Launched Application ID : " +appliname);
					Reporter.log("External Port			  : " +ports1);
				 
					JsonCompare jc = new JsonCompare();
					
					JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
					JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
					JSONObject trans1 = new JSONObject(responseBody.getResponseBody());
					String transid=trans1.getString("tx_id");
					System.out.println(transid);
					js.put("transid", transid);
					Readjson.stringWriteAsFile(js.toString(), "config");
					try {
						respCompare = jc
								.compTwoJson(temp2.toString(), temp1.toString());
					} catch (MisMatch mm) {
						System.out.println(mm.getMessage());
					}
				}
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Expected Status =  200");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				if (respCompare) {
					log.info("Launching Design get Success");
				} else {
					log.info("Launching Design is failed");
				}
				Assert.assertEquals((statusCompare == 0 && respCompare), true);
			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			
		}
		public void accessValidation(String accesssublink) {
			handle = con.Get("##HANDLE");
			try
			{
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jsonFile = Readjson.readFileAsString("config");
				js1= new JSONObject(jsonFile);
				String transid=js1.getString("transid");
				boolean flag=true;
				String accessurl ="access";
				JSONArray accessurl1;
				String status="";
				while(flag){
				String apiUrl = endPoint + "/api/" +handle+"/transaction/"+transid;
				responseBody=rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 500 || responseBody.getResponseCode() == 404 ? 0 : 1;
				JSONObject trans1 = new JSONObject(responseBody.getResponseBody());
				JSONObject trans2=trans1.getJSONObject("result");
				appid=trans2.getString("id");
				js1.put("appid", appid);
				Readjson.stringWriteAsFile(js1.toString(), "config");
				status=trans2.getString("status");
				System.out.println("status"+status);
				if(status.equals("Success")){
					String apiUrl1 = endPoint + "/api/" +handle+"/app/"+appid+"?format=true";
					responseBody=rc.httpGet(apiUrl1, "Normal");
				int statusCompare1 = responseBody.getResponseCode() == 200 || responseBody.getResponseCode() == 404 ? 0 : 1;
				JSONObject resp = new JSONObject(responseBody.getResponseBody());
				JSONObject resp1=resp.getJSONObject("result");
			  accessurl1=resp1.getJSONArray("access_links");
			   accessurl=accessurl1.getString(0);
				 Assert.assertEquals(statusCompare == 0, true);
				 Assert.assertEquals(statusCompare1 == 0, true);
					flag=false;
					}else if(status.equals("Failed")){
						flag=false;
						Reporter.log("Application creation failed");
					}else{
						flag=true;
					}
			   }
				Reporter.log("Application :"+ status);
			String accesslink=accessurl+accesssublink;
			Thread.sleep(10000);
			responseBody=rc.httpGet(accesslink, "Normal");
			int statusCompare1 = responseBody.getResponseCode() == 200 || responseBody.getResponseCode() == 404 ? 0 : 1;
			Reporter.log("Response Status = " + responseBody.getResponseBody());
			Reporter.log("Response Status = " + responseBody.getResponseCode());
			Assert.assertEquals(statusCompare1 == 0, true);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage(), e.getCause());
		}
		}
		public void deleteDesign(String name) {
			try
			{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			String apiUrl = endPoint + "/api/" +handle+"/design/"+bpId;
			try {
				responseBody=rc.httpDelete(apiUrl);
				int statusCompare = responseBody.getResponseCode() == 204
						|| responseBody.getResponseCode() == 500 ? 0 : 1;
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Assert.assertEquals(statusCompare == 0, true);
			}catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}catch(JSONException e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		} 
		public void deleteApp() throws JSONException {
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			appid=js.getString("appid");
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String apiUrl = endPoint + "/api/" +handle+"/app/"+appid;
			try {
				responseBody=rc.httpDelete(apiUrl);
				int statusCompare = responseBody.getResponseCode() == 204
						|| responseBody.getResponseCode() == 200 ? 0 : 1;
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log(appid+" Application Deleted ");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Assert.assertEquals(statusCompare == 0, true);	
			}catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void listAllDesigns() {
			handle = con.Get("##HANDLE");
			String apiUrl = endPoint + "/api/" + handle+"/design";
			try {
				responseBody = rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					 length = resp.getInt("count");
					}
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Expected Status =  200");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				if (respCompare) {
					log.info("Listing the Design is successfully");
				} else {
					log.info("Listing the Design get failed");
				}
				Assert.assertEquals((statusCompare == 0 && length > 0), true);

			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void searchSingleDesign() {
			jsonFile = Readjson.readFileAsString("config");
			try	
			{
				js = new JSONObject(jsonFile);
				handle = con.Get("##HANDLE");
				bpId=js.getString("bpId");
				String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId;
				try {
						responseBody = rc.httpGet(apiUrl, "Normal");
						int statusCompare = responseBody.getResponseCode() == 200
								|| responseBody.getResponseCode() == 202 ? 0 : 1;
						Reporter.log("Resource URI : "+apiUrl);
						Reporter.log("Response Status = " + responseBody.getResponseCode());
						Reporter.log("get Response body: = " + responseBody.getResponseBody());
						Reporter.log("Response Message = "
								+ JsonFormatter.format(responseBody.getResponseBody()));
						if (respCompare) {
							log.info("Search the Design is successfully");
						} else {
							log.info("Search the Design get failed");
							Assert.assertEquals(statusCompare == 0, true);
							}
				} catch (Exception e) {
					e.printStackTrace();
					Assert.fail(e.getLocalizedMessage(), e.getCause());
					}
			}
			catch(JSONException e)
			{
				e.getLocalizedMessage();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void deleteComponent() {
			try
			{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			compId=js.getString("componentId");
			String apiUrl = endPoint + "/api/" +handle+"/design/"+bpId+"/component/"+compId;
				try {
				responseBody=rc.httpDelete(apiUrl);
				int statusCompare = responseBody.getResponseCode() == 204
						|| responseBody.getResponseCode() == 500 ? 0 : 1;
				
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Assert.assertEquals(statusCompare == 0, true);

			
			}catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}catch(JSONException e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		} 
		public void registerKubes(@Optional("default") String param1) {
			handle = con.Get("##HANDLE");
			String apiUrl = endPoint + "/api/" + handle+"/kube?operation=register";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "kube/registerKubes.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPost(apiUrl, RTC.getInput(), "JSON");
				int statusCompare = responseBody.getResponseCode() == 201
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
								
					//comparing
					JsonCompare jc = new JsonCompare();
					
					JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
					JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
					
					try {
						respCompare = jc
								.compTwoJson(temp2.toString(), temp1.toString());
					} catch (MisMatch mm) {
						System.out.println(mm.getMessage());
					}
					
				}
				
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("Request Body = " + JsonFormatter.format(RTC.getInput()));
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				Reporter.log("Actual Response body: = " + RTC.getExpectedOutput());
				if (respCompare) {
					log.info("creating kubernates is successfully");
				} else {
					log.info("Creating kubernates get failed");
				
				Assert.assertEquals((respCompare && statusCompare == 0), true);
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void listKubes(String kubname) {
			handle = con.Get("##HANDLE");
			String apiUrl = endPoint + "/api/" + handle+"/kube";
			try {
				responseBody = rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					length = resp.getInt("count");
					}
				JSONObject res=new JSONObject(responseBody.getResponseBody());
				Reporter.log("Response Message = "
								+ JsonFormatter.format(responseBody.getResponseBody()));
				JSONArray values = res.getJSONArray("result");
				 String name="";
				 String ip = "";
			//	 Thread.sleep(100000);
				 boolean flag= true;
				 int i;
				
				
				while(flag){
					apiUrl = endPoint + "/api/" + handle+"/kube";
					responseBody = rc.httpGet(apiUrl, "Normal");
					int statusCompare1 = responseBody.getResponseCode() == 200
							|| responseBody.getResponseCode() == 202 ? 0 : 1;
					if (statusCompare1 == 0) {
						JSONObject resp = new JSONObject(responseBody.getResponseBody());
						length = resp.getInt("count");
						}
					System.err.println(flag);
					 res=new JSONObject(responseBody.getResponseBody());
					JSONArray values1 = res.getJSONArray("result");
					for ( i = 0; i < values1.length(); i++) {
					    JSONObject jsonobject = values1.getJSONObject(i);
					     name = jsonobject.getString("name");
					     ip = jsonobject.getString("ip");
					    System.out.println(name +":"+ip);
					    outer:
					if(name.equals(kubname)){
						System.out.println(i);
					
			     name = jsonobject.getString("name");
			     ip = jsonobject.getString("ip");
			    System.out.println("outside" +":"+ip);
			    if(ip.contains(".")){
			    	System.out.println("insideif"+ip);
			    	flag=false;
			    	break outer;
			    }
			    else{
			    	System.out.println("insideelse:"+ip);
			    	flag=true;
			    }
					}
			   /* else{
			    	System.out.println("insideelse:"+ip);
			    	flag=true;
			    }*/
				}
				 }
				 System.out.println(ip);
			Assert.assertEquals((statusCompare == 0 && length > 0), true);

			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}	
		public void listApps() {
			handle = con.Get("##HANDLE");
			String apiUrl = endPoint + "/api/" + handle+"/app";
				
			try {
				String jsonFile1 = Readjson.readFileAsString("appaccess");
				js1 = new JSONObject(jsonFile1);
				String jsonFile = Readjson.readFileAsString("config");
				js = new JSONObject(jsonFile);
				responseBody = rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					length = resp.getInt("count");
					JSONArray result  = resp.getJSONArray("result");
					int temp;
					String app_name="mw";
					
				for (temp=0;temp<result.length();temp++){
					JSONObject value=result.getJSONObject(temp);
						String apname=value.getString("name");
						if(app_name.equals(apname))
						{
							appId=value.getString("id");
							js1.put("maven_appid", appId);
							Readjson.stringWriteAsFile(js1.toString(), "appaccess");
							js.put("appId", appId);
							Readjson.stringWriteAsFile(js.toString(), "config");					
						}
					} 
				}
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				if (respCompare) {
					log.info("Listing the App is successfully");
				} else {
					log.info("Listing the App was failed");
				}
				Assert.assertEquals((statusCompare == 0 && length > 0), true);

			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void searchApp() {
			handle = con.Get("##HANDLE");
			try{
			appId=js.getString("appId");
			String apiUrl = endPoint + "/api/" + handle+"/app/"+appId;
			try {
				responseBody = rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
				}
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				Reporter.log("Actual Response body: = " + RTC.getExpectedOutput());
				if (respCompare) {
					log.info("Listing the the single app is successfully");
				} else {
					log.info("Reading the single app get failed");
				
				Assert.assertEquals(statusCompare == 0, true);

			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void updateApplicationStateOn() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			appid=js.getString("appid");
			String apiUrl = endPoint + "/api/" + handle+"/app/"+appid+"?action=start";
			
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "application/updateAppState.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				Assert.assertEquals((statusCompare == 0), true);
			
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}

		}
		public void updateAppStateOff() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			appid=js.getString("appid");
			String apiUrl = endPoint + "/api/" + handle+"/app/"+appid+"?action=stop";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "application/updateAppState.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				Assert.assertEquals((statusCompare == 0), true);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void updateApplicationDeliveryON() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			appid=js.getString("appid");
			String apiUrl = endPoint + "/api/" + handle+"/app/"+appid+"?cDelivery=true&autoScale=true";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "application/updateAppContinueDelivery.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				
				Assert.assertEquals((statusCompare == 0), true);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void updateApplicationDeliveryOff() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			appid=js.getString("appid");
			String apiUrl = endPoint + "/api/" + handle+"/app/"+appid+"?cDelivery=false&autoScale=false";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "application/updateAppContinueDelivery.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				
					Assert.assertEquals((statusCompare == 0), true);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}

		}
		public void listAllComponent(@Optional("default") String param1) {
			handle = con.Get("##HANDLE");
			try{
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId+"/component";
			try {
				responseBody = rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Expected Status =  200");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				
				if (respCompare) {
					log.info("Listing the component is successfully");
				} else {
					log.info("Listing the component get failed");
				Assert.assertEquals(statusCompare == 0, true);
			}
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		public void searchComponent() {
			handle = con.Get("##HANDLE");
			try{
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			compId=js.getString("componentId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId+"/component/"+compId;
			try {
				responseBody = rc.httpGet(apiUrl, "Normal");
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Expected Status =  200");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				if (respCompare) {
					log.info("Listing a single component is completed successfully");
				} else {
					log.info("Listing a single component get failed");
				
				Assert.assertEquals(statusCompare == 0, true);
			}
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			}
		}
		public void updateComponent() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			compId=js.getString("componentId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId+"/component/"+compId;
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "component/updateComponent.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				String component_name = "";
				int component_id = 0;
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					 component_name=resp.getString("name");
					 component_id=resp.getInt("id");
				//comparing
					 JSONObject script1 = resp.getJSONObject("script");
					 JSONObject network1 = resp.getJSONObject("networks");
					       String ports=network1.getString("ports");
					 if(responseBody.getResponseBody().toString().contains(ports)){
							Reporter.log("Resource URI : "+apiUrl);
							Reporter.log("Expected Status =  200");
							Reporter.log("Response Status = " + responseBody.getResponseCode());
							Reporter.log("get Response body: = " + responseBody.getResponseBody());
						}else{
							Assert.assertFalse(true, "response data not valid");
						}
										
				}
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Component Name  : " +component_name);
				Reporter.log("Component Id    : " +component_id);
				Reporter.log("Expected Status =  200");
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
			
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}

		}
		public void updateDesign() {
			try{
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			compId=js.getString("componentId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId;
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "design/updateDesign.json");
			try {
				RTC.dataparser(jsonFile);
				responseBody = rc.httpPut(apiUrl, RTC.getInput());
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					//comparing
					JsonCompare jc = new JsonCompare();
					
					JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
					JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
					
					try {
						respCompare = jc
								.compTwoJson(temp2.toString(), temp1.toString());
					} catch (MisMatch mm) {
						System.out.println(mm.getMessage());
					}
										
				}
				Reporter.log("Resource URI : "+apiUrl);
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				if (respCompare) {
					log.info("Design name update complete successfully");
				} else {
					log.info("Design name update get failed");
				
				Assert.assertEquals((respCompare && statusCompare == 0), true);
			}
			}catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
		}
		
		public void createTwotierComponent(String name,String ipath,String iversion,String port,String accesssublink) {
			handle = con.Get("##HANDLE");
			jsonFile = Readjson.readFileAsString("config");
			try
			{
			js = new JSONObject(jsonFile);
			bpId=js.getString("bpId");
			String apiUrl = endPoint + "/api/" + handle+"/design/"+bpId+"/component";
			jsonFile = Readjson.readFileAsString(payloadPath
					+ "component/createTTierComponent.json");
			try {
					RTC.dataparser(jsonFile);
					RTC.setInput(RTC.getInput().replaceAll("#NAME#", name));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#NAME#", name));
					RTC.setInput(RTC.getInput().replaceAll("#PATH#", ipath));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#PATH#", ipath));
					RTC.setInput(RTC.getInput().replaceAll("#VERSION#", iversion));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#VERSION#", iversion));
					RTC.setInput(RTC.getInput().replaceAll("#PORT#", port));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#PORT#", port));
					RTC.setExpectedOutput(RTC.getExpectedOutput().replaceAll("#sublink#", accesssublink));
					responseBody = rc.httpPost(apiUrl, RTC.getInput(), "JSON");
						
				int statusCompare = responseBody.getResponseCode() == 200
						|| responseBody.getResponseCode() == 202 ? 0 : 1;
				if (statusCompare == 0) {
					JSONObject resp = new JSONObject(responseBody.getResponseBody());
					String component_type=resp.getString("component_type");
					String data_store=resp.getString("data_store");
					String version=resp.getString("version");
					String path=resp.getString("path");
						JSONObject network = resp.getJSONObject("networks");
							String ports=network.getString("ports");
							
							c++;
							Reporter.log("Resource URI : "+apiUrl);
					Reporter.log("Component Type       = " + component_type);
					Reporter.log("DataStore		       = " + data_store);
					Reporter.log("Version			   = " + version);
					Reporter.log("Path		           = " + path);
					Reporter.log("Ports					   = " + ports);
					
					JsonCompare jc = new JsonCompare();
					
					JSONObject temp1 = new JSONObject(RTC.getExpectedOutput());
					JSONObject temp2 = new JSONObject(responseBody.getResponseBody());
					
					try {
						respCompare = jc
								.compTwoJson(temp2.toString(), temp1.toString());
					} catch (MisMatch mm) {
						System.out.println(mm.getMessage());
					}
				}
				Reporter.log("Response Status = " + responseBody.getResponseCode());
				Reporter.log("get Response body: = " + responseBody.getResponseBody());
				if (respCompare) {
					log.info("Create Mysql component get Success");
				} else {
					log.info("Create Mysql component is failed");
				}
				Assert.assertEquals((statusCompare == 0 && respCompare), true);

			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(), e.getCause());
			}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
				Assert.fail(e.getLocalizedMessage(),e.getCause());
			
			}
		}
}