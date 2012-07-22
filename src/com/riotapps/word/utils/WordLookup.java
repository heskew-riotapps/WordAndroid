package com.riotapps.word.utils;

import java.util.List;
import net.jeremybrooks.knicker.AccountApi;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Definition;
import net.jeremybrooks.knicker.dto.TokenStatus;

public class WordLookup {


    public static void Get(String[] args) throws Exception {
        // use your API key here
        System.setProperty("WORDNIK_API_KEY", Constants.WN_KEY);


	// check the status of the API key
	TokenStatus status = AccountApi.apiTokenStatus();
	if (status.isValid()) {
	    System.out.println("API key is valid.");
	} else {
	    System.out.println("API key is invalid!");
	    System.exit(1);
	}


	// get a list of definitions for a word
	List<Definition> def = WordApi.definitions("siren");
	System.out.println("Found " + def.size() + " definitions.");

	int i = 1;
	for (Definition d : def) {
	    System.out.println((i++) + ") " + d.getPartOfSpeech() + ": " + d.getText());
	}

    }
}