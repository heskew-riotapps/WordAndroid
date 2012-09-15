package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;

import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.DialogManager;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Enums.ResponseHandlerType;
import com.riotapps.word.utils.ServerResponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPlayer extends Activity implements View.OnClickListener{

	private PlayerService playerSvc = new PlayerService();
	private Context context = this;
	private EditText etFindPlayer;
	private Button bSearch;
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findplayer);
		
		etFindPlayer = (EditText)findViewById(R.id.etFindPlayer);
		bSearch = (Button)findViewById(R.id.bSearch);
		bSearch.setOnClickListener(this);
		
		
	}
	@Override 
	    public void onClick(View v) {

	    	switch(v.getId()){  
	        case R.id.bSearch:  
	        	this.findPlayer();
				break;
	       	

	    	}
	    	
	    }  
	
	
	private void findPlayer(){
		try {
			String url = playerSvc.setupFindPlayerByNickname(context, etFindPlayer.getText().toString());
			
			//kick off thread
			new NetworkTask(this.context, RequestType.POST, getString(R.string.progress_searching)).execute(url);
			
		} catch (DesignByContractException e) {
			//e.printStackTrace();
			DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage());  
		}
		
	}
	
	private class NetworkTask extends AsyncNetworkRequest{
		
		Context context;
		
		public NetworkTask(Context ctx, RequestType requestType,
				String shownOnProgressDialog) {
			super(ctx, requestType, shownOnProgressDialog);
			this.context = ctx;
			// TODO Auto-generated constructor stub
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

		         switch(statusCode){  
		             case 200:  
		             case 201: {  
		                //update text
		            	 Player player = playerSvc.HandleFindPlayerByNicknameResponse(this.context, iStream);

		                 Toast t = Toast.makeText(this.context, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
		         	    t.show();
		                 break;  

		             }//end of case 200 & 201  
		             case 404:
		             //case Status code == 422
		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.find_player_opponent_not_found));  
		            	 
		             case 422: 
		             case 500:

		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase());  
		         }  
		     }else if(exception != null){  
		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected));  

		     }  
		     else{  
		         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

		     }//end of else  
		}
		
 
	}

}
