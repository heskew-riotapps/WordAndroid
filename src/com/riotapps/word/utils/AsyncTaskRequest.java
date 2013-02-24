package com.riotapps.word.utils;

import com.riotapps.word.interfaces.IAsyncTask;

import android.os.AsyncTask;

public class AsyncTaskRequest extends AsyncTask<Object, Object, Object>{
	IAsyncTask iAsyncTask;
	
	public AsyncTaskRequest(IAsyncTask iAsyncTask){
		this.iAsyncTask = iAsyncTask;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
