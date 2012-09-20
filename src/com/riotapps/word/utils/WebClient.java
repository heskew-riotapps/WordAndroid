package com.riotapps.word.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

public class WebClient{

	static HttpClient httpClient;
	private HttpResponse httpResponseToCallingMethod;
	private Context ctx;
	private int connectionTimeout = Constants.DEFAULT_CONNECTION_TIMEOUT;
	private int socketTimeout =  Constants.DEFAULT_SOCKET_CONNECTION_TIMEOUT;
	
	public WebClient(){}
	
	public WebClient(int connectionTimeout, int socketTimeout){
		this.connectionTimeout = connectionTimeout;
		this.socketTimeout = socketTimeout;
	}
	
	private void setHttpClient(){
		 HttpParams httpParameters = new BasicHttpParams();
		  HttpConnectionParams.setConnectionTimeout(httpParameters, this.connectionTimeout);
		  HttpConnectionParams.setSoTimeout(httpParameters, this.socketTimeout);
		  httpClient = new DefaultHttpClient(httpParameters);
	}


	/*===================================================================   
     * method setHttpResponseToCallingMethod   
     * ==================================================================*/   
   void setHttpResponseToCallingMethod(HttpResponse res){
		httpResponseToCallingMethod = res;
	}

   /*===================================================================   
    * method getHttpResponseToCallingMethod   
    * ==================================================================*/   
	HttpResponse getHttpResponseToCallingMethod(){
		return httpResponseToCallingMethod;
	}


   /*===================================================================   
    * method createGetRequest()   
    * ==================================================================*/   
   public ServerResponse createGetRequest(String urlString){   
     
       if(httpClient == null){   
    	   setHttpClient(); //httpClient = new DefaultHttpClient();   
       }   
    
       ServerResponse serverResponseObject = new ServerResponse();  
       HttpResponse response = null;  
       Exception exception = null;  
    
       HttpGet httpGet = new HttpGet(urlString);  
       try {  
           response = httpClient.execute(httpGet);  
       } catch (ClientProtocolException cpe) {  
           Log.v("in HttpClient -> in createGetRequest(String urlString) -> in catch", "ClientProtocolException" + cpe);  
           response = null;  
           exception = cpe;  
       } catch (IOException ioe) {  
           Log.v("in HttpClient -> in createGetRequest(String urlString) -> in catch", "IOException" + ioe);  
           response = null;  
           exception = ioe;  
       }  
    
       serverResponseObject.response = response;  
       serverResponseObject.exception = exception;  
    
       return serverResponseObject;  
   }//end method createGetRequest


   
   /*===================================================================   
    * method createPutRequest()   
    * ==================================================================*/   
   ServerResponse createPutRequest(String urlString, ArrayList<NameValuePair> nameValuePairs){

       HttpPost httpPost = new HttpPost(urlString);
       httpPost.addHeader("X-Http-Method-Override","put");
       try {
    	   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	   return createPostRequest(httpPost);
       }
       catch (Exception e){
    	   return ReturnResponseError(e);
       }
    
   } 
   
   /*===================================================================   
    * method createPutRequest()   
    * ==================================================================*/   
   ServerResponse createPutRequest(String urlString, String jsonPost){

       HttpPost httpPost = new HttpPost(urlString);
       httpPost.addHeader("X-Http-Method-Override","put");
       httpPost.setHeader("Accept", "application/json");
       httpPost.setHeader("Content-Type", "application/json");

//       httpPost.setHeader("Content-Type","application/json; charset=utf-8");
       try {
    	   StringEntity json = new StringEntity(jsonPost);
    	   json.setContentType("application/json; charset=utf-8");
    	   httpPost.setEntity(json);
    	   return createPostRequest(httpPost);
       }
       catch (Exception e){
    	   return ReturnResponseError(e);
       }
    
   } 
   
   /*===================================================================   
    * method createPostRequest()   
    * ==================================================================*/   
   ServerResponse createPostRequest(String urlString, ArrayList<NameValuePair> nameValuePairs){

       HttpPost httpPost = new HttpPost(urlString);
       try {
    	   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	   return createPostRequest(httpPost);
       }
       catch (Exception e){
    	   return ReturnResponseError(e);
       }
    
   }//end of createPostRequest	
   
   
   /*===================================================================   
    * method createPostRequest()   
    * ==================================================================*/   
   ServerResponse createPostRequest(String urlString, String jsonPost){

       HttpPost httpPost = new HttpPost(urlString);
     //  httpPost.setHeader("Content-Type","application/json; charset=utf-8");
       httpPost.setHeader("Accept", "application/json");
       httpPost.setHeader("Content-Type", "application/json");

       try {
    	   StringEntity json = new StringEntity(jsonPost);
    	   json.setContentType("application/json; charset=utf-8");
    	   httpPost.setEntity(json);
    	   return createPostRequest(httpPost);
       }
       catch (Exception e){
    	   return ReturnResponseError(e);
       }
    
   }//end of createPostRequest	

   /*===================================================================   
    * method createDeleteRequest()   
    * ==================================================================*/   
   ServerResponse createDeleteRequest(String urlString, ArrayList<NameValuePair> nameValuePairs){

       HttpPost httpPost = new HttpPost(urlString);
       httpPost.addHeader("X-Http-Method-Override","delete");
       try {
    	   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	   return createPostRequest(httpPost);
       }
       catch (Exception e){
    	   return ReturnResponseError(e);
       }
    
   }//end of createPostRequest	
   
   /*===================================================================   
    * method createDeleteRequest()   
    * ==================================================================*/   
   ServerResponse createDeleteRequest(String urlString, String jsonPost){

       HttpPost httpPost = new HttpPost(urlString);
       httpPost.addHeader("X-Http-Method-Override","delete");
      // httpPost.setHeader("Content-Type","application/json; charset=utf-8");
       httpPost.setHeader("Accept", "application/json");
       httpPost.setHeader("Content-Type", "application/json");
       try {
    	   StringEntity json = new StringEntity(jsonPost);
    	   json.setContentType("application/json; charset=utf-8");
    	   httpPost.setEntity(json);
    	   return createPostRequest(httpPost);
       }
       catch (Exception e){
    	   return ReturnResponseError(e);
       }
    
   }//end of createPostRequest	
   
   private ServerResponse ReturnResponseError(Exception e) {
	   
		ServerResponse serverResponseObject = new ServerResponse();
		HttpResponse response = null;
		Exception exception = null;
		
		Log.v("in HttpClient -> in createPostRequest(String urlString) -> in catch", "IOException" + e);
   		response = null;
		exception = e;
		serverResponseObject.response = response;
	    serverResponseObject.exception = exception;
	       
	    return serverResponseObject;

   }
  
    private ServerResponse createPostRequest(HttpPost httpPost){
		if(httpClient == null){
			setHttpClient(); //httpClient = new DefaultHttpClient();
		}

	 
		
		ServerResponse serverResponseObject = new ServerResponse();
		HttpResponse response = null;
		Exception exception = null;

       //HttpPost httpPost = new HttpPost(urlString);
       //org.apache.http.client.entity.UrlEncodedFormEntity entity = new org.apache.http.client.entity.UrlEncodedFormEntity()
       try {
         //  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	   
    	 
    	   
           response = httpClient.execute(httpPost);
       } catch (ClientProtocolException e) {
       	Log.v("in HttpClient -> in createPostRequest(String urlString) -> in catch", "ClientProtocolException" + e);
			response = null;
			exception = e;
       } catch (IOException e) {
       	Log.v("in HttpClient -> in createPostRequest(String urlString) -> in catch", "IOException" + e);
       	response = null;
			exception = e;
       }
       
       serverResponseObject.response = response;
       serverResponseObject.exception = exception;
       
       return serverResponseObject;
   }//end of createPostRequest	

   
   /*===================================================================   
    * method createPutRequest()   
    * ==================================================================*/   
   //ServerResponse createPutRequest(String urlString, ArrayList<NameValuePair> nameValuePairs){
//
//		nameValuePairs.add(new BasicNameValuePair(Constants.REST_METHOD, Constants.PUT_VERB));
//		return createPostRequest(urlString, nameValuePairs);
//   }
   
   /*===================================================================   
    * method createDeleteRequest()   
    * ==================================================================*/   
 //  ServerResponse createDeleteRequest(String urlString, ArrayList<NameValuePair> nameValuePairs){
//
//		nameValuePairs.add(new BasicNameValuePair(Constants.REST_METHOD, Constants.DELETE_VERB));
//		return createPostRequest(urlString, nameValuePairs);
//   }
   
}//end class MyHttpClient
