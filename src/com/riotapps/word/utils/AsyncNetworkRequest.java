package com.riotapps.word.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;

import com.riotapps.word.MainLanding;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Enums.ResponseHandlerType;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncNetworkRequest extends AsyncTask<String, Void, NetworkTaskResult> {
	private static final String TAG = AsyncNetworkRequest.class.getSimpleName();
	Context ctx = null;
	RequestType requestType;
	ResponseHandlerType responseHandleBy;
	String shownOnProgressDialog = null;

	CustomProgressDialog progress = null;
	ArrayList<NameValuePair> nameValuePairs = null;
	String jsonPost = "";
	Class<?> goToClass = null;

	/**====================================================================================   
	  * main Constructor   
	  * ====================================================================================*/   
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, String shownOnProgressDialog){
		this.ctx = ctx;
		this.requestType = requestType;
		//this.responseHandleBy = responseHandleBy;
		if (shownOnProgressDialog != null && shownOnProgressDialog.length() > 0) {this.shownOnProgressDialog = shownOnProgressDialog;}
	}
	 
	 /**====================================================================================   
	  * main Constructor   
	  * ====================================================================================*/   
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, String shownOnProgressDialog, String jsonPost){
		this.ctx = ctx;
		this.requestType = requestType;
		//this.responseHandleBy = responseHandleBy;
		if (shownOnProgressDialog != null && shownOnProgressDialog.length() > 0) {this.shownOnProgressDialog = shownOnProgressDialog;}
		this.jsonPost = jsonPost;
	}
	
	/**====================================================================================   
	  * 1st Constructor   
	  * ====================================================================================*/   
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog){
		this.ctx = ctx;
		this.requestType = requestType;
		this.responseHandleBy = responseHandleBy;
		if (shownOnProgressDialog != null && shownOnProgressDialog.length() > 0) {this.shownOnProgressDialog = shownOnProgressDialog;}
	}

		/**====================================================================================   
	  * 1st Constructor   
	  * ====================================================================================*/   
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog, Class<?> goToClass){
		this.ctx = ctx;
		this.requestType = requestType;
		this.responseHandleBy = responseHandleBy;
		if (shownOnProgressDialog != null && shownOnProgressDialog.length() > 0) {this.shownOnProgressDialog = shownOnProgressDialog;}
		this.goToClass =  goToClass;
	}
	 /**====================================================================================   
	  * 2nd Constructor   
	  * ====================================================================================*/  
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog, ArrayList<NameValuePair> nameValuePairs){
		this(ctx,requestType, responseHandleBy, shownOnProgressDialog);
		this.nameValuePairs = nameValuePairs;
	 }

	 /**====================================================================================   
	  * 3rd Constructor   
	  * ====================================================================================*/  
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog, 
			 String jsonPost, Class<?> goToClass){
		this(ctx,requestType, responseHandleBy, shownOnProgressDialog);
		this.jsonPost = jsonPost;
		this.goToClass =  goToClass;
	 }
	 
	 /**====================================================================================   
	  * method onPreExecute   
	  * ====================================================================================*/  
	 @Override
	 protected void onPreExecute() {
		 if (this.shownOnProgressDialog != null){
			 progress = new CustomProgressDialog(ctx);
			 progress.setMessage(shownOnProgressDialog);
			 progress.show();
		 }

	 }

	 /**====================================================================================   
	  * method doInBackground   
	  * ====================================================================================*/   
	 @Override   
	 protected NetworkTaskResult doInBackground(String... urlArray) {   

	     String urlString = urlArray[0];   
	     ServerResponse serverResponseObject = null;   
  
	     NetworkTaskResult result = new NetworkTaskResult();
		 
		 Logger.d(TAG, "doInBackground url=" + urlString + " json=" + this.jsonPost);
	     
	     switch(this.requestType){  

	         case GET:  
	             serverResponseObject = new WebClient().createGetRequest(urlString);  
	             break;  
	         case POST:  
	        	 if (this.jsonPost.length() > 0){
	        		 serverResponseObject = new WebClient().createPostRequest(urlString, this.jsonPost); 
	        	 }
	        	 else {
	        		 serverResponseObject = new WebClient().createPostRequest(urlString, nameValuePairs); 
	        	 }
	        	 break;
	         case PUT:  
	        	 if (this.jsonPost.length() > 0){
	        		 serverResponseObject = new WebClient().createPutRequest(urlString, this.jsonPost); 
	        	 }
	        	 else {
	        		 serverResponseObject = new WebClient().createPutRequest(urlString, nameValuePairs); 
	        	 }
	        	 break;
	         case DELETE:  
	        	 if (this.jsonPost.length() > 0){
	        		 serverResponseObject = new WebClient().createDeleteRequest(urlString, this.jsonPost); 
	        	 }
	        	 else {
	        		 serverResponseObject = new WebClient().createDeleteRequest(urlString, nameValuePairs); 
	        	 }
	             break;  
	     }//end of switch  
 
	     result.setException(serverResponseObject.exception);
	     result.setStatusCode(serverResponseObject.response.getStatusLine().getStatusCode());
	    
	     Logger.d(TAG, "StatusCode: " + result.getStatusCode()); 
	     InputStream iStream = null; 
	     try{
	    	 result.setStatusReason(serverResponseObject.response.getStatusLine().getReasonPhrase());
	     }
	     catch(Exception e){
	     }
	     
         if (serverResponseObject.response != null ) {
       
            try {  
	             iStream = serverResponseObject.response.getEntity().getContent();  
	         } catch (IllegalStateException e) {  
	             Logger.e(TAG,"doInBackground IllegalStateException " + e);
	             throw e;
	         } catch (IOException e) {  
	        	 Logger.e(TAG,"doInBackground IOException " + e);  
	         
	         }  
         }  
         if (iStream != null) {
             try {
                 BufferedReader reader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
                 StringBuilder sb = new StringBuilder();
                 String line = null;
                 while ((line = reader.readLine()) != null) {
                     sb.append(line + "\n");
                 }
                 iStream.close();

                 result.setResult(sb.toString());
             } catch (Exception e) {
            	 Logger.e(TAG,"doInBackground error converting result to string= " + e); 
             }
         }

	     return result;  

	 }//end method doInBackground()


	 /**====================================================================================   
	  * method onPostExecute   
	  * ====================================================================================*/  
	 @Override
	 protected void onPostExecute(NetworkTaskResult result){
		 if (this.shownOnProgressDialog != null){
			 progress.dismiss();
		 }

		 //new ResponseHandler().handleResponse(ctx, responseHandleBy, serverResponseObject, goToClass);			
	 }//end method onPostExecute

	 public void dismiss(){
		 if (this.shownOnProgressDialog != null){
			 progress.dismiss();
		 }
	 }
 
	 
}//end class RequestSentToServerAsyncTask
