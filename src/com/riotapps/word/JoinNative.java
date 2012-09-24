package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.ApplicationContext;
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
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinNative extends Activity implements View.OnClickListener{
		private static final String TAG = JoinNative.class.getSimpleName();
		
	    final Context context = this;		
		Button bCancel; 
		Button bSave;
		EditText tEmail;
		EditText tNickname;
		EditText tPassword;
		
		//EditText 
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.joinnative);
	        
	        bCancel = (Button) findViewById(R.id.bCancel);
	        bSave = (Button) findViewById(R.id.bSave);
	       
	    //    TextView tvTitle =(TextView)findViewById(R.id.title_join_native);
	    //    TextView tvSubTitle =(TextView)findViewById(R.id.sub_title_join_native);
	     ////   TextView tvEmailLabel =(TextView)findViewById(R.id.email_label);
	    //    TextView tvEmailGuarantee =(TextView)findViewById(R.id.email_guarantee);
	      //  TextView tvPassword =(TextView)findViewById(R.id.password_label);
	      //  TextView tvNickname =(TextView)findViewById(R.id.nickname_label);
 
	        bCancel.setOnClickListener(this);
	        bSave.setOnClickListener(this);
	        bCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();      
				}
			});
	        
	        
	        bSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PlayerService playerSvc = new PlayerService();
					
					EditText tEmail = (EditText) findViewById(R.id.tEmail);
					EditText tNickname = (EditText) findViewById(R.id.tNickname);
					EditText tPassword = (EditText) findViewById(R.id.tPassword);
					
					try{
						//show spinner
						playerSvc.CreatePlayer(context, 
								tEmail.getText().toString(), 
								tNickname.getText().toString(), 
								tPassword.getText().toString());
					} 
					catch (DesignByContractException dbEx) {
					 	DialogManager.SetupAlert(context, getString(R.string.oops), dbEx.getMessage(), 0);					}
					catch (Exception ex) {
						//log this, figure out logging
					 	DialogManager.SetupAlert(context, getString(R.string.error_title), getString(R.string.error_message), 0);						
					}
				 
				}
			});
	    }

	    @Override 
	    public void onClick(View v) {
	    	switch(v.getId()){  
	        case R.id.bCancel:  
	        	finish();  
	        	break;
	        case R.id.bSave:  
	            // action to preform on button 1  
				Intent goToFirstActivity = new Intent(getApplicationContext(), JoinNative.class);
				startActivity(goToFirstActivity);
//	            Toast.makeText(context, "Button 2 pressed ", Toast.LENGTH_SHORT).show();  
	  //          break;  
	        }  
	      }
	    
	    private class NetworkTask extends AsyncNetworkRequest{
			
	    	JoinNative context;
			
			public NetworkTask(JoinNative ctx, RequestType requestType,
					String shownOnProgressDialog, String jsonPost) {
				super(ctx, requestType, shownOnProgressDialog, jsonPost);
				this.context = ctx;
			 
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
			         
			         Log.i(JoinNative.TAG, "StatusCode: " + statusCode);

			         switch(statusCode){  
			             case 200:  
			             case 201: {  
			                //update text
			            	 Player player = playerSvc.HandleFindPlayerByNicknameResponse(this.context, iStream);

			                // Toast t = Toast.makeText(this.context, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
			         	   // t.show();
			         	   Intent intent = new Intent(this.context, com.riotapps.word.FindPlayerResults.class);
			      	      //  intent.putExtra("gameId", game.getId());
			      	      //	intent.putExtra("game", s);
			         	    intent.putExtra(Constants.EXTRA_GAME, this.context.game);
			      	      	intent.putExtra(Constants.EXTRA_PLAYER, player);
			      	      	this.context.startActivity(intent);
			                 break;  

			             }//end of case 200 & 201  
			             case 404:
			             //case Status code == 422
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.find_player_opponent_not_found), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			            	 break;
			             case 422: 
			             case 500:

			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
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
