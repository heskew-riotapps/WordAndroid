package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

 
import com.riotapps.word.hooks.Error.ErrorType;
import com.riotapps.word.hooks.ErrorService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FBAccountSettings extends FragmentActivity implements View.OnClickListener{
		private static final String TAG = FBAccountSettings.class.getSimpleName();
		
	    final Context context = this;		

		EditText tNickname;
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fbaccountsettings);
	        
	        Player player = PlayerService.getPlayerFromLocal();		 
	        PlayerService.loadPlayerInHeader(this);
	        
	       
	        tNickname = (EditText)findViewById(R.id.tNickname);
	        tNickname.setText(player.getNickname());
	        
	        Button bSave = (Button) findViewById(R.id.bSave);
	        bSave.setOnClickListener(this);
	       
	    }

	    @Override 
	    public void onClick(View v) {
	    	switch(v.getId()){  
	        case R.id.bSave:  
	            this.processAccountUpdate();
	            break; 
  
	    	}  
	      }
	    
	    private void processAccountUpdate(){
	    	
	    	try {
				EditText tNickname = (EditText) findViewById(R.id.tNickname);
				
				String json = PlayerService.setupFBAccountUpdate(this, tNickname.getText().toString());
				
				//kick off thread
				new NetworkTask(this, RequestType.POST, getString(R.string.progress_updating), json).execute(Constants.REST_PLAYER_UPDATE_FB_ACCOUNT);
				
			} 
			catch (DesignByContractException e) {
				//e.printStackTrace();
				DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			}
	    }
	    
	    private class NetworkTask extends AsyncNetworkRequest{
			
	    	FBAccountSettings context;
 
			
			public NetworkTask(FBAccountSettings ctx, RequestType requestType,
					String shownOnProgressDialog, String jsonPost) {
				super(ctx, requestType, shownOnProgressDialog, jsonPost);
				this.context = ctx;
			}

			@Override
			protected void onPostExecute(NetworkTaskResult result) {
				 
				super.onPostExecute(result);
				
				this.handleResponse(result);
			}
	 
			private void handleResponse(NetworkTaskResult result){
			     Exception exception = result.getException();   

			     if(result.getResult() != null){  
			         switch(result.getStatusCode()){  
			             case 200:  
			             case 201: {   
			                //update text
			            	 
		            		 Player player = PlayerService.handleUpdateAccountResponse(this.context, result.getResult());
			     			 DialogManager.SetupAlert(this.context, this.context.getString(R.string.success), this.context.getString(R.string.fb_account_changed_successfully), true);
	 
			                 break;  

			             }//end of case 200 & 201 
			             case 401:
			            	 ErrorType errorType = ErrorService.translateError(this.context, result.getResult());
			            	 
			            	 String errorMessage;
			            	 
			            	 switch (errorType){
			            	 	case INCORRECT_PASSWORD:
					            	errorMessage = this.context.getString(R.string.validation_incorrect_password);
			            	 		break; 
			            	 	case NICKNAME_IN_USE:
			            	 		errorMessage = this.context.getString(R.string.validation_nickname_already_taken);
			            	 		break;
			            	 	case NICKNAME_NOT_SUPPLIED:
			            	 		errorMessage = this.context.getString(R.string.validation_nickname_not_supplied);
			            	 		break;
			            	 	case UNAUTHORIZED:
			            	 		errorMessage = this.context.getString(R.string.validation_unauthorized);
			            	 		break;
			            	 	default:
			            	 		errorMessage = this.context.getString(R.string.validation_unspecified_error);
			            	 		break;		            	 		
			            	 }
			            	 
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), errorMessage);
			            	 break;
			            	 
			             case 404:
			             //case Status code == 422
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_404_error), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			            	 break;
			             case 422: 
			             case 500:

			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), 0);  
			         }  
			     }else if (exception instanceof ConnectTimeoutException ||  exception instanceof java.net.SocketTimeoutException) {
			    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_connection_timeout), 0);
			     }else if(exception != null){  
			    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected), 0);  

			     }  
			     else{  
			         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

			     }//end of else  
			}
			
	 
		}
	    
	    
	}
