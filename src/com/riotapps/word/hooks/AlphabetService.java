package com.riotapps.word.hooks;

import java.lang.reflect.Type;

import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.FileUtils;

public class AlphabetService {
	
	Alphabet alphabet = new Alphabet();
	
	public AlphabetService(Context context){
		 Gson gson = new Gson();
		 Type type = new TypeToken<Alphabet>() {}.getType();
		 alphabet = gson.fromJson(FileUtils.ReadRawTextFile(context, R.raw.alphabet), type);
	}

	public int GetLetterValue(String character){
		  for (Letter letter : this.alphabet.Letters){
			  if (letter.getCharacter() == character){
				  return letter.getValue();
			  }
		  }
	      return 0;
	}
}
