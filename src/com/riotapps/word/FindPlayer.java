package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.ServerResponse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class FindPlayer extends FragmentActivity implements View.OnClickListener{

	private static final String TAG = FindPlayer.class.getSimpleName();
	private PlayerService playerSvc = new PlayerService();
	private Context context = this;
	private EditText etFindPlayer;
	private Button bSearch;
	private Game game;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findplayer);
		
		PlayerService.loadPlayerInHeader(this);
		
		etFindPlayer = (EditText)findViewById(R.id.etFindPlayer);
		etFindPlayer.setPressed(true);
		bSearch = (Button)findViewById(R.id.bSearch);
		bSearch.setOnClickListener(this);
		
		//etFindPlayer.setFocusable(true);
		etFindPlayer.requestFocus();
		this.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		Intent i = getIntent();
		this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		
		//to do
		//if there are already more than one players in the game, display the other players in list view
		
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
			String url = PlayerService.setupFindPlayerByNickname(context, etFindPlayer.getText().toString());
			
			//kick off thread
			new NetworkTask(this, RequestType.GET, getString(R.string.progress_searching)).execute(url);
			
		} 
		catch (DesignByContractException e) {
			//e.printStackTrace();
			DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		}
		
	}
	
	private class NetworkTask extends AsyncNetworkRequest{
		
		FindPlayer context;
		
		public NetworkTask(FindPlayer ctx, RequestType requestType,
				String shownOnProgressDialog) {
			super(ctx, requestType, shownOnProgressDialog);
			this.context = ctx;
			// TODO Auto-generated constructor stub
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
		            	 Player player = PlayerService.handleFindPlayerByNicknameResponse(this.context, result.getResult());

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

		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), 0);  
		         }  
		     }else if (exception instanceof ConnectTimeoutException) {
		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_connection_timeout), 0);
		     }else if(exception != null){  
		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected), 0);  

		     }  
		     else{  
		         Log.d("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

		     }//end of else  
		}
		
 
	}

}
