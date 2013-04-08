package com.riotapps.word.hooks;

import java.lang.reflect.Type;

import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.FileUtils;

public class AlphabetService {
	
	private static Alphabet alphabet = new Alphabet();
	
	public AlphabetService(Context context){
		 Gson gson = new Gson();
		 Type type = new TypeToken<Alphabet>() {}.getType();
		 alphabet = gson.fromJson(FileUtils.ReadRawTextFile(context, R.raw.alphabet), type);
	}

	public static int getLetterValue(String character){
		if (AlphabetService.alphabet == null){
			 Gson gson = new Gson();
			 Type type = new TypeToken<Alphabet>() {}.getType();
			 AlphabetService.alphabet = gson.fromJson(FileUtils.ReadRawTextFile( ApplicationContext.getAppContext(), R.raw.alphabet), type);
		}
		
		  for (Letter letter : AlphabetService.alphabet.Letters){
			  if (letter.getCharacter().equals(character)){
				  return letter.getValue();
			  }
		  }
	      return 0;
	}
}
