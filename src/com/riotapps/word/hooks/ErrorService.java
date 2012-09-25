package com.riotapps.word.hooks;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.hooks.Error.ErrorType;

import android.content.Context;

public class ErrorService {
	
	public static ErrorType translateError(final Context ctx, InputStream iStream){
		
		Gson gson = new Gson(); //wrap json return into a single call that takes a type
	        
        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
        
        Type type = new TypeToken<Error>() {}.getType();
        Error error = gson.fromJson(reader, type);
        
        return error.getErrorType();
	}

}
