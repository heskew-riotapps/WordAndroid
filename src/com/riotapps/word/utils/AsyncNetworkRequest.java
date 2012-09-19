package com.riotapps.word.utils;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Enums.ResponseHandlerType;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncNetworkRequest extends AsyncTask<String, Void, ServerResponse> {

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
		this.shownOnProgressDialog = shownOnProgressDialog;
	}
	
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
	  * 1st Constructor   
	  * ====================================================================================*/   
	 public AsyncNetworkRequest(Context ctx, RequestType requestType, ResponseHandlerType responseHandleBy, String shownOnProgressDialog, Class<?> goToClass){
		this.ctx = ctx;
		this.requestType = requestType;
		this.responseHandleBy = responseHandleBy;
		this.shownOnProgressDialog = shownOnProgressDialog;
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
		 progress = new CustomProgressDialog(ctx);
		 progress.setMessage(shownOnProgressDialog);
		 progress.show();
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
	//	 progress.dismiss();

		 //new ResponseHandler().handleResponse(ctx, responseHandleBy, serverResponseObject, goToClass);			
	 }//end method onPostExecute

}//end class RequestSentToServerAsyncTask
