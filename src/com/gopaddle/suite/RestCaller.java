package com.gopaddle.suite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.gopaddle.suite.ResponseBody;
import com.gopaddle.utils.Readjson;

public class RestCaller {

	String responseStr;
	int statuscode;
	ResponseBody rb = new ResponseBody();
	String token;
	JSONObject js;
	String jsonFile = Readjson.readFileAsString("config");
	
	public void RestCaller() throws JSONException {
		js = new JSONObject(jsonFile);
		   token=js.getString("token");
		
	}
	
	

	public ResponseBody httpPost(String endpoint, String payload, String format) {
		String responseStr = null;
		try {
			
			HttpClient client = new DefaultHttpClient();
			final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		    client = new DefaultHttpClient(httpParams);		    
			String url = endpoint;
			HttpPost httppost = new HttpPost(url);
			js = new JSONObject(jsonFile);
			   token=js.getString("token");
			 if(token.isEmpty()){
			if (format.equalsIgnoreCase("JSON")) {
				httppost.addHeader("Content-Type", "application/json");
				StringEntity setcontent = new StringEntity(payload);
				setcontent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httppost.setEntity(setcontent);
			} else {
				MultipartEntity entity = new MultipartEntity();
				JSONObject obj = new JSONObject(payload);
				@SuppressWarnings("unchecked")
				Iterator<String> keys = obj.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					if (key.equalsIgnoreCase("p12File")
							|| key.equalsIgnoreCase("pemFile")) {
						File uploadFile1 = new File(
								System.getenv("GOPADDLE_HOME")
										+ obj.get(key).toString());
						if (key.equalsIgnoreCase("p12File"))
							entity.addPart(key, new FileBody(uploadFile1,"application/x-pkcs12"));
						else
							entity.addPart(key, new FileBody(uploadFile1,
									"application/x-pem-file"));
					} else {
						entity.addPart(key, new StringBody(obj.get(key)
								.toString()));
					}
				}
				httppost.setEntity(entity);
			}
			
			HttpResponse response = client.execute(httppost);
			statuscode = response.getStatusLine().getStatusCode();
			InputStream in = response.getEntity().getContent();
			responseStr = Readjson.readInputStreamAsString(in);
			rb.setResponseCode(statuscode);
			rb.setResponseBody(responseStr);
			 }
			 else
			 {
					if (format.equalsIgnoreCase("JSON")) {
						httppost.addHeader("Content-Type", "application/json");
						  
						StringEntity setcontent = new StringEntity(payload);
						setcontent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
								"application/json"));
						httppost.setEntity(setcontent);
					} else {
						MultipartEntity entity = new MultipartEntity();
						JSONObject obj = new JSONObject(payload);
						@SuppressWarnings("unchecked")
						Iterator<String> keys = obj.keys();
						while (keys.hasNext()) {
							String key = (String) keys.next();
							if (key.equalsIgnoreCase("p12File")
									|| key.equalsIgnoreCase("pemFile")) {
								File uploadFile1 = new File(
										System.getenv("GOPADDLE_HOME")
												+ obj.get(key).toString());
								if (key.equalsIgnoreCase("p12File"))
									entity.addPart(key, new FileBody(uploadFile1,
											"application/x-pkcs12"));
								else
									entity.addPart(key, new FileBody(uploadFile1,
											"application/x-pem-file"));
							} else {
								entity.addPart(key, new StringBody(obj.get(key)
										.toString()));
							}
						}
						httppost.setEntity(entity);
					}
					 httppost.addHeader("Authorization", "Bearer "+token);
					
					HttpResponse response = client.execute(httppost);
					statuscode = response.getStatusLine().getStatusCode();
					InputStream in = response.getEntity().getContent();
					responseStr = Readjson.readInputStreamAsString(in);
					rb.setResponseCode(statuscode);
					rb.setResponseBody(responseStr);
			 }

		} catch (ClientProtocolException cp) {
			System.out.println("Connection failed " + cp.getMessage());
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JSONException je) {
			System.out.println(je.getMessage());
		}
		return rb;

	}

	public ResponseBody httpGet(String endpoint, String format){
		try {
			HttpClient client = new DefaultHttpClient();
			String url = endpoint;
			HttpGet httpget = new HttpGet(url);
			HttpResponse response;
			int statuscode = 0;
			   JSONObject rB;
			   String status;
			   Thread.sleep(15000);
			  
				   if(format.equalsIgnoreCase("status"))
				   {
					  do{
						  js = new JSONObject(jsonFile);
						   token=js.getString("token");
						   httpget.addHeader("Authorization", "Bearer "+token);
						  response=client.execute(httpget);
						  statuscode=response.getStatusLine().getStatusCode();
						  InputStream in = response.getEntity().getContent();
						  responseStr = Readjson.readInputStreamAsString(in);
						  rB = new JSONObject(responseStr);
						  rB = rB.getJSONObject("result");
						  status = rB.getString("status");
						  System.out.println("Migration/Agent Installation status once 30sec : " + rB);
						  if(status.equalsIgnoreCase("running") || status.equalsIgnoreCase("true"))
							  Thread.sleep(30000);
					   }while(status.equalsIgnoreCase("running") || status.equalsIgnoreCase("true"));
					  System.out.println("Migration/Agent Installation completed with response: "+rB);
				   }else
				   {
					   js = new JSONObject(jsonFile);
					   token=js.getString("token");
					  httpget.addHeader("Authorization", "Bearer "+token);
					response = client.execute(httpget);
				statuscode = response.getStatusLine().getStatusCode();
				InputStream in = response.getEntity().getContent();
				responseStr = Readjson.readInputStreamAsString(in);
				   }
			   
			rb.setResponseCode(statuscode);
			rb.setResponseBody(responseStr);
			return rb;
		} catch (ClientProtocolException cp) {
			System.out.println("Connection failed : "
					+ cp.getLocalizedMessage());
		}catch(InterruptedException ie)
		{
			System.out.println("Error in Slepping: " + ie.getMessage());
		}
		catch (IOException ioe) {
			System.out.println("Invalid Data : " + ioe.getLocalizedMessage());
		} catch (JSONException ex) {
			System.out.println("Error in Json object handling: " + ex.getMessage());
		}
		rb.setResponseCode(500);
		rb.setResponseBody("Internal Error");
		return rb;
	}
	
	//code for PUT Command
	public ResponseBody httpPut(String aPI, String body)
	{
		String errorMsg = null;
		try
		{	
			HttpClient client=new DefaultHttpClient();
			String url=aPI;
			org.apache.http.client.methods.HttpPut httpput = new HttpPut(url);
			httpput.addHeader("Content-Type", "application/json");
			StringEntity setcontent = new StringEntity(body);
			setcontent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpput.setEntity(setcontent);
			js = new JSONObject(jsonFile);
			 token=js.getString("token");
			 httpput.addHeader("Authorization", "Bearer "+token);
			HttpResponse response=client.execute(httpput);
			int statuscode=response.getStatusLine().getStatusCode();
			InputStream in = response.getEntity().getContent();
			String responseStr = Readjson.readInputStreamAsString(in);
			rb.setResponseCode(statuscode);
			rb.setResponseBody(responseStr);
			return rb;
		}
		catch(ClientProtocolException cp)
		{
			  errorMsg = cp.getLocalizedMessage();
			  System.out.println("Connection failed : " + cp.getLocalizedMessage());
		}
		catch(IOException ioe)
		{
			  errorMsg = ioe.getLocalizedMessage();
			  System.out.println("Invalid Data : " + ioe.getLocalizedMessage());
		}
		catch(Exception ex)
		{
			  errorMsg = ex.getLocalizedMessage();
			  System.out.println( ex.getLocalizedMessage());
		}
		rb.setResponseCode(500);
		rb.setResponseBody("Internal Error : " + errorMsg);
		return rb;
	}
	public ResponseBody httpDelete(String aPI)
	{
		  try
		  {
			  
		   HttpClient client=new DefaultHttpClient();
		   String url=aPI;
		   HttpDelete httpdelete = new HttpDelete(url);
		   js = new JSONObject(jsonFile);
			 token=js.getString("token");
		   httpdelete.addHeader("Authorization", "Bearer "+token);
		   HttpResponse response=client.execute(httpdelete);
		   int statuscode=response.getStatusLine().getStatusCode();
		   rb.setResponseCode(statuscode);
		   return rb;
		  }
		  catch(ClientProtocolException cp)
		  {
			  System.out.println("Connection failed : " + cp.getLocalizedMessage());
		  }
		  catch(IOException ioe)
		  {
			  System.out.println("Invalid Data : " + ioe.getLocalizedMessage());
		  }
		  catch(Exception ex)
		  {
			  System.out.println( ex.getLocalizedMessage());
		  }
		 rb.setResponseCode(500);
		 rb.setResponseBody("Internal Error");
		return rb;		
	}
}
