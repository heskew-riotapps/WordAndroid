package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.riotapps.word.hooks.ErrorService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.Error.ErrorType;
import com.riotapps.word.ui.CustomDialog;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.DialogManager;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Options extends FragmentActivity implements View.OnClickListener{
		private static final String TAG = Options.class.getSimpleName();
	 
	
		TextView tvAccountSettings;
		TextView tvQuickRules;
		TextView tvFullRules;
		Button bLogout;
		
		final FragmentActivity context = this;

			
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.options);

	        Player player = PlayerService.getPlayerFromLocal();		
	        
	        tvAccountSettings = (TextView) findViewById(R.id.tvAccountSettings);
	        if (player.isFacebookUser()){
	        	tvAccountSettings.setVisibility(View.GONE);
	        }
	        else {
	        	tvAccountSettings.setOnClickListener(this);
	        }
	        tvQuickRules = (TextView) findViewById(R.id.tvQuickRules);
	        tvQuickRules.setOnClickListener(this); 
	        tvFullRules = (TextView) findViewById(R.id.tvFullRules);
	        tvFullRules.setOnClickListener(this); 
	        
	        bLogout = (Button) findViewById(R.id.bLogout);
	        bLogout.setOnClickListener(this); 
			PlayerService.loadPlayerInHeader(this);

	       
	    }
	
	    @Override 
	    public void onClick(View v) {
	    	Intent intent;
	    	
	    	switch(v.getId()){  
	        case R.id.tvAccountSettings:  
	        	intent = new Intent(this, AccountSettings.class);
				startActivity(intent);
				break;
	       	case R.id.tvQuickRules:  
	       		intent = new Intent(this, QuickRules.class);
        		startActivity(intent);
        		break;
	       	
	       	case R.id.tvFullRules:  
	       		intent = new Intent(this, FullRules.class);
        		startActivity(intent);
        		break;
	       	case R.id.bLogout:  
	       		this.handleLogout();
	       		break;


    	}
	    	
        }  
	    
	    private void handleLogout(){
	    	final CustomDialog dialog = new CustomDialog(this, this.getString(R.string.dialog_title_are_you_sure), this.getString(R.string.dialog_text_logout));
	    	
	    	dialog.setOnOKClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
				//	PlayerService.logout(context);
				//	Intent intent = new Intent(context, Welcome.class);
	        	//	startActivity(intent);
	        		//kick off thread
		 			String json;
					try {
						json = PlayerService.setupAuthTokenTransport(context);
						new NetworkTask(context, RequestType.POST, getString(R.string.progress_logging_out), json).execute(Constants.REST_PLAYER_LOGOUT);
					} 
					catch (DesignByContractException e) {
		            	 DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());
					}
		 		}
			});

	    	dialog.show();	
	    }
	    
	    
  private class NetworkTask extends AsyncNetworkRequest{
			
	  		FragmentActivity context;
	    	//CustomDialog dialog;
			
			public NetworkTask(FragmentActivity ctx, RequestType requestType,
					String shownOnProgressDialog, String jsonPost) {
				super(ctx, requestType, shownOnProgressDialog, jsonPost);
				this.context = ctx;
				//this.dialog = dialog;
			 
			}

			@Override
			protected void onPostExecute(ServerResponse serverResponseObject) {
				// TODO Auto-generated method stub
				super.onPostExecute(serverResponseObject);
				
				this.handleResponse(serverResponseObject);
				
				
			}
	 
			private void handleResponse(ServerResponse serverResponseObject){
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
			         
			         Log.i(Options.TAG, "StatusCode: " + statusCode);

			         switch(statusCode){  
			             case 200:  
			             case 201: {  
			            	 //dialog.dismiss();
			            	 PlayerService.logout(this.context);
			            	 Intent intent = new Intent(this.context, com.riotapps.word.Welcome.class);
			      	      	 this.context.startActivity(intent);
			                 break;  

			             }//end of case 200 & 201 
			             case 401:
	                       	 String errorMessage = this.context.getString(R.string.validation_unauthorized);
			            	 
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), errorMessage);
			            	 break;
			             case 404:
			             	 //ignore player not found error for now
			            	 //DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_404_error), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			            	 Log.w(TAG, "handleResponse 404 error.");
			            	 break;
			             case 422: 
			             case 500:

			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
			            	 break;
			         }  
			     }else if (exception instanceof ConnectTimeoutException) {
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
