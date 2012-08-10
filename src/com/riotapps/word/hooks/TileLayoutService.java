package com.riotapps.word.hooks;

import java.lang.reflect.Type;

import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.FileUtils;

public class TileLayoutService {

	public TileLayout GetDefaultLayout(Context context){
		 Gson gson = new Gson();
		 Type type = new TypeToken<TileLayout>() {}.getType();
	       
		return gson.fromJson(FileUtils.ReadRawTextFile(context, R.raw.tile_layout), type);
	}
	
}
