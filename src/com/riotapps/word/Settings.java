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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends FragmentActivity implements View.OnClickListener{
		private static final String TAG = Settings.class.getSimpleName();
		
	    final Context context = this;		
		Button bSave;
		EditText tEmail;
		EditText tNickname;
		EditText tPassword;
		
		//EditText 
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.settings);
	        
	        Player player = PlayerService.getPlayerFromLocal();		 
	        PlayerService.loadPlayerInHeader(this);
	        
	        tEmail = (EditText)findViewById(R.id.tEmail);
	        tNickname = (EditText)findViewById(R.id.tNickname);
	        tPassword = (EditText)findViewById(R.id.tPassword);	        

	        tEmail.setText(player.getEmail());
	        tNickname.setText(player.getNickname());
	        
	        bSave = (Button) findViewById(R.id.bSave);
	        bSave.setOnClickListener(this);
	  
	    }

	    @Override 
	    public void onClick(View v) {
	    	switch(v.getId()){  
	        case R.id.bSave:  
	            this.processPlayer();
	            break;  
	        }  
	      }
	    
	    private void processPlayer(){
	    	
	    	try {
	    		EditText tEmail = (EditText) findViewById(R.id.tEmail);
				EditText tNickname = (EditText) findViewById(R.id.tNickname);
				EditText tPassword = (EditText) findViewById(R.id.tPassword);
				
				String json = PlayerService.setupConnectViaEmail(this, tEmail.getText().toString(), tNickname.getText().toString(), tPassword.getText().toString());
				
				//kick off thread
				new NetworkTask(this, RequestType.POST, getString(R.string.progress_connecting), json).execute(Constants.REST_CREATE_PLAYER_URL);
				
			} 
			catch (DesignByContractException e) {
				//e.printStackTrace();
				DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			}
	    }
	    
	    private class NetworkTask extends AsyncNetworkRequest{
			
	    	Settings context;
			
			public NetworkTask(Settings ctx, RequestType requestType,
					String shownOnProgressDialog, String jsonPost) {
				super(Settings.this, requestType, shownOnProgressDialog, jsonPost);
				this.context = ctx;
			 
			}

			@Override
			protected void onPostExecute(NetworkTaskResult result) {
				// TODO Auto-generated method stub
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
			            	 Player player = PlayerService.handleCreatePlayerResponse(result.getResult());

			                 // Toast t = Toast.makeText(this.context, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
			         	    // t.show();
			         	     Intent intent = new Intent(this.context, com.riotapps.word.MainLanding.class);
			      	      
			      	      	 this.context.startActivity(intent);
			                 break;  

			             }//end of case 200 & 201 
			             case 401:
			            	 ErrorType errorType = ErrorService.translateError(result.getResult());
			            	 
			            	 String errorMessage;
			            	 
			            	 switch (errorType){
			            	 	case INCORRECT_PASSWORD:
					            	errorMessage = this.context.getString(R.string.validation_incorrect_password);
			            	 		break; 
			            	 	case EMAIL_NOT_SUPPLIED:
			            	 		errorMessage = this.context.getString(R.string.validation_email_already_taken);
			            	 		break;
			            	 	case NICKNAME_IN_USE:
			            	 		errorMessage = this.context.getString(R.string.validation_nickname_already_taken);
			            	 		break;
			            	 		
			            	 	default:
			            	 		errorMessage = this.context.getString(R.string.validation_unspecified_error);
			            	 		break;		            	 		
			            	 }
			            	 
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), errorMessage);
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
