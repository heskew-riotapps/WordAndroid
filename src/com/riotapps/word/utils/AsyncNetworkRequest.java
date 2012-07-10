package com.riotapps.word.utils;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Enums.ResponseHandlerType;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class AsyncNetworkRequest extends AsyncTask<String, Void, ServerResponse> {

	Context ctx = null;
	RequestType requestType;
	ResponseHandlerType responseHandleBy;
	String shownOnProgressDialog = null;

	CustomProgressDialog dialogAccessingSpurstone = null;
	ArrayList<NameValuePair> nameValuePairs = null;
	String jsonPost = null;

	/**====================================================================================   
	  * 1st Constructor   
	  * ====================================================================================*/   
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog){
		this.ctx = ctx;
		this.requestType = requestType;
		this.responseHandleBy = responseHandleBy;
		this.shownOnProgressDialog = shownOnProgressDialog;
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
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog, String jsonPost){
		this(ctx,requestType, responseHandleBy, shownOnProgressDialog);
		this.jsonPost = jsonPost;
	 }
	 
	 /**====================================================================================   
	  * method onPreExecute   
	  * ====================================================================================*/  
	 @Override
	 protected void onPreExecute() {
		 dialogAccessingSpurstone = new CustomProgressDialog(ctx);
		 dialogAccessingSpurstone.setMessage(shownOnProgressDialog);	
		 dialogAccessingSpurstone.show();
	 }

	 /**====================================================================================   
	  * method doInBackground   
	  * ====================================================================================*/   
	 @Override   
	 protected ServerResponse doInBackground(String... urlArray) {   

	     String urlString = urlArray[0];   
	     ServerResponse serverResponseObject = null;   

	   
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
 
	     return serverResponseObject;  

	 }//end method doInBackground()


	 /**====================================================================================   
	  * method onPostExecute   
	  * ====================================================================================*/  
	 @Override
	 protected void onPostExecute(ServerResponse serverResponseObject){
		 dialogAccessingSpurstone.dismiss();

		 new ResponseHandler().handleResponse(ctx, responseHandleBy, serverResponseObject);			
	 }//end method onPostExecute

}//end class RequestSentToServerAsyncTask
