package com.riotapps.word.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.riotapps.word.R;
import com.riotapps.word.hooks.*;
import com.riotapps.word.utils.Enums.ResponseHandlerType;
 
class ResponseHandler {

	 /**=======================================================================================   
	  * method handleResponse   
	  * =======================================================================================*/   
	// @SuppressWarnings("unchecked")
	void handleResponse(final Context ctx, ResponseHandlerType responseHandleBy, ServerResponse serverResponseObject) {   

	     HttpResponse response = serverResponseObject.response;   
	     Exception exception = serverResponseObject.exception;   

	     if(response != null){  

	         InputStream iStream = null;  

	         try {  
	             iStream = response.getEntity().getContent();  
	         } catch (IllegalStateException e) {  
	             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IllegalStateException " + e);  
	         } catch (IOException e) {  
	             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IOException " + e);  
	         }  

	         int statusCode = response.getStatusLine().getStatusCode();  

	         switch(statusCode){  
	             case 200:  
	             case 201: {  
	                 switch(responseHandleBy){  

	                     //case 1 Constants.USER_LISTING:  
	                     case CREATE_PLAYER: {  

	                         if(iStream == null){  
	                             Toast t = Toast.makeText(ctx, ctx.getString(R.string.response_in_error), Toast.LENGTH_LONG);  
	                             t.show();  
	                         }else{  
	                             new PlayerService().HandleCreatePlayerResponse(ctx, iStream);         
	                         }  
	                         break;  
	                     }
	                     case UPDATE_PLAYER:{
	                
	                    	 break;
	                     } 
	                     case CREATE_GAME: {  
	                         
	                         break;  
	                     }//end case USER_LISTING_DELETE:      
	                 }//end of switch response handle by  

	                 break;  

	             }//end of case 200 & 201  
	             case 404:
	             //case Status code == 422  
	             case 422://{   
	       //          Object arrayOfErrorsObject = new CreateXMLParser().creatingXmlParser(iStream, Constants.XML_PARSER_FOR_ERRORS);  
	       //          ArrayList<String> arrayOfErrors = (ArrayList<String>)arrayOfErrorsObject;  

	      //           String message="";  
	      //           for(String error : arrayOfErrors) {  
	      //               message+="\n* " + error + ".";  
	      //           }  
//
//	                 String headingOfAlertDialog = ctx.getString(R.string.alertDialogHeadingError);  
//	                 new ShowAlertDialog(ctx, headingOfAlertDialog, message).showDialog();  
//	                 break;  
//	             }//end case 422: 
	             case 500:
	         }  
	     }else if(exception != null){  
	         new ShowAlertDialog(ctx, ctx.getString(R.string.oops), ctx.getString(R.string.msg_not_connected)).showDialog();  

	     }  
	     else{  
	         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	     }//end of else  
	 }//end method handle response
}//end class ResponseHandler