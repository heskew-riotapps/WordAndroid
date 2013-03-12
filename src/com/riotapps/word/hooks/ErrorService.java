package com.riotapps.word.hooks;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.hooks.Error.ErrorType;

public class ErrorService {
	
	public static ErrorType translateError(String result){
		
		Gson gson = new Gson(); //wrap json return into a single call that takes a type
	        
      //  Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
        
        Type type = new TypeToken<Error>() {}.getType();
        Error error = gson.fromJson(result, type);
        
        return error.getErrorType();
	}

}
