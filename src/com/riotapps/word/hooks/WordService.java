package com.riotapps.word.hooks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.GameSurface;
import com.riotapps.word.utils.FileUtils;
import com.riotapps.word.utils.Logger;

public class WordService {
	private static final String TAG = WordService.class.getSimpleName();
	
	boolean isLoaded = false;
	/*
	 SortedSet<String> words_a = null;
	SortedSet<String> words_b = null;
	SortedSet<String> words_c = null;
	SortedSet<String> words_d = null;
	SortedSet<String> words_e = null;
	SortedSet<String> words_f = null;
	SortedSet<String> words_g = null;
	SortedSet<String> words_h = null;
	SortedSet<String> words_i = null;
	SortedSet<String> words_j = null;
	SortedSet<String> words_k = null;
	SortedSet<String> words_l = null;
	SortedSet<String> words_m = null;
	SortedSet<String> words_n = null;
	SortedSet<String> words_o = null;
	SortedSet<String> words_p = null;
	SortedSet<String> words_q = null;
	SortedSet<String> words_r = null;
	SortedSet<String> words_s = null;
	SortedSet<String> words_t = null;
	SortedSet<String> words_u = null;
	SortedSet<String> words_v = null;
	SortedSet<String> words_w = null;
	SortedSet<String> words_x = null;
	SortedSet<String> words_y = null;
	SortedSet<String> words_z = null;
	
	*/
	/*
	ArrayList<String> words_a = null;
	ArrayList<String> words_b = null;
	ArrayList<String> words_c = null;
	ArrayList<String> words_d = null;
	ArrayList<String> words_e = null;
	ArrayList<String> words_f = null;
	ArrayList<String> words_g = null;
	ArrayList<String> words_h = null;
	ArrayList<String> words_i = null;
	ArrayList<String> words_j = null;
	ArrayList<String> words_k = null;
	ArrayList<String> words_l = null;
	ArrayList<String> words_m = null;
	ArrayList<String> words_n = null;
	ArrayList<String> words_o = null;
	ArrayList<String> words_p = null;
	ArrayList<String> words_q = null;
	ArrayList<String> words_r = null;
	ArrayList<String> words_s = null;
	ArrayList<String> words_t = null;
	ArrayList<String> words_u = null;
	ArrayList<String> words_v = null;
	ArrayList<String> words_w = null;
	ArrayList<String> words_x = null;
	ArrayList<String> words_y = null;
	ArrayList<String> words_z = null;
	*/
	
	HashSet<String> words_a = null;
	HashSet<String> words_b = null;
	HashSet<String> words_c = null;
	HashSet<String> words_d = null;
	HashSet<String> words_e = null;
	HashSet<String> words_f = null;
	HashSet<String> words_g = null;
	HashSet<String> words_h = null;
	HashSet<String> words_i = null;
	HashSet<String> words_j = null;
	HashSet<String> words_k = null;
	HashSet<String> words_l = null;
	HashSet<String> words_m = null;
	HashSet<String> words_n = null;
	HashSet<String> words_o = null;
	HashSet<String> words_p = null;
	HashSet<String> words_q = null;
	HashSet<String> words_r = null;
	HashSet<String> words_s = null;
	HashSet<String> words_t = null;
	HashSet<String> words_u = null;
	HashSet<String> words_v = null;
	HashSet<String> words_w = null;
	HashSet<String> words_x = null;
	HashSet<String> words_y = null;
	HashSet<String> words_z = null;
	
 
//	Type type = new TypeToken<ArrayList<String>>() {}.getType();
	Type type = new TypeToken<HashSet<String>>() {}.getType();
	Context context;
	
	
	public WordService(Context context){
		this.context = context;
	}
	

	public boolean isWordValid(String word){
		word = word.toLowerCase();
		String startWith = (word.substring(0, 1));
		
		this.loadList(startWith);
 	
		if (startWith.equals("a")){ return this.words_a.contains(word); }
		else if (startWith.equals("b")){ return this.words_b.contains(word); }
		else if (startWith.equals("c")){ return this.words_c.contains(word); }
		else if (startWith.equals("d")){ return this.words_d.contains(word); }
		else if (startWith.equals("e")){ return this.words_e.contains(word); }
		else if (startWith.equals("f")){ return this.words_f.contains(word); }
		else if (startWith.equals("g")){ return this.words_g.contains(word); }
		else if (startWith.equals("h")){ return this.words_h.contains(word); }
		else if (startWith.equals("i")){ return this.words_i.contains(word); }
		else if (startWith.equals("j")){ return this.words_j.contains(word); }
		else if (startWith.equals("k")){ return this.words_k.contains(word); }
		else if (startWith.equals("l")){ return this.words_l.contains(word); }
		else if (startWith.equals("m")){ return this.words_m.contains(word); }
		else if (startWith.equals("n")){ return this.words_n.contains(word); }
		else if (startWith.equals("o")){ return this.words_o.contains(word); }
		else if (startWith.equals("p")){ return this.words_p.contains(word); }
		else if (startWith.equals("q")){ return this.words_q.contains(word); }
		else if (startWith.equals("r")){ return this.words_r.contains(word); }
		else if (startWith.equals("s")){ return this.words_s.contains(word); }
		else if (startWith.equals("t")){ return this.words_t.contains(word); }
		else if (startWith.equals("u")){ return this.words_u.contains(word); }
		else if (startWith.equals("v")){ return this.words_v.contains(word); }
		else if (startWith.equals("w")){ return this.words_w.contains(word); }
		else if (startWith.equals("x")){ return this.words_x.contains(word); }
		else if (startWith.equals("y")){ return this.words_y.contains(word); }
		else if (startWith.equals("z")){ return this.words_z.contains(word); }

		return false;
	}
	
	public void loadAll(){
		
		if (this.isLoaded){return;}
		this.loadList("a");
		this.loadList("b");
		this.loadList("c");
		this.loadList("d");
		this.loadList("e");
		this.loadList("f");
		this.loadList("g");
		this.loadList("h");
		this.loadList("i");
		this.loadList("j");
		this.loadList("k");
		this.loadList("l");
		this.loadList("m");
		this.loadList("n");
		this.loadList("o");
		this.loadList("p");
		this.loadList("q");
		this.loadList("r");
		this.loadList("s");
		this.loadList("t");
		this.loadList("u");
		this.loadList("v");
		this.loadList("w");
		this.loadList("x");
		this.loadList("y");
		this.loadList("z");
		this.isLoaded = true;
	}
	
	public void loadList(String letter){

		if (letter.equals("a")){
			if (this.words_a == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_a = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_a)), this.type);//FileUtils.ReadRawTextFile(context, R.raw.words_a), this.type);
			}
			return;
		}	
		else if (letter.equals("b")){
			if (this.words_b == null){ 
				Logger.d(TAG, "letter=" + letter);
				this.words_b = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_b)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_b), this.type);
			}
			return;
		}
		else if (letter.equals("c")){
			if (this.words_c == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_c = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_c)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_c), this.type); 
			}
			return;
		}
		else if (letter.equals("d")){
			if (this.words_d == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_d = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_d)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_d), this.type); 
			}
			return;
		}
		else if (letter.equals("e")){ 
			if (this.words_e == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_e = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_e)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_e), this.type); 
			}
			return;
		}
		else if (letter.equals("f")) {
			if (this.words_f == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_f = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_f)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_f), this.type);  
			}
			return;
		}
		else if (letter.equals("g")){ 
			if (this.words_g == null){ 
				Logger.d(TAG, "letter=" + letter);
				this.words_g = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_g)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_g), this.type); 
			}
			return;
		}
		else if (letter.equals("h")){ 
			if (this.words_h == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_h = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_h)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_h), this.type);  
			}
			return;
		}
		else if (letter.equals("i")) { 
			if (this.words_i == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_i = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_i)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_i), this.type); 
			}
			return;
		}
		else if (letter.equals("j")) { 
			if (this.words_j == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_j = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_j)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_j), this.type);  
			}
			return;
		}
		else if (letter.equals("k")){ 
			if (this.words_k == null){ 
				Logger.d(TAG, "letter=" + letter);
				this.words_k = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_k)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_k), this.type);  
			}
			return;
		}
		else if (letter.equals("l")) { 
			if (this.words_l == null){ 
				Logger.d(TAG, "letter=" + letter);
				this.words_l = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_l)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_l), this.type);
			}
			return;
		}
		else if (letter.equals("m")) { 
			if (this.words_m == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_m = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_m)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_m), this.type); 
			}
			return;
		}
		else if (letter.equals("n")) { 
			if (this.words_n == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_n = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_n)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_n), this.type);
			}
			return;
		}
		else if (letter.equals("o")){ 
			if (this.words_o == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_o = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_o)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_o), this.type);
			}
			return;
		}
		else if (letter.equals("p")) { 
			if (this.words_p == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_p = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_p)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_p), this.type);
			}
			return;
		}
		else if (letter.equals("q")) { 
			if (this.words_q == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_q = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_q)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_q), this.type);  
			}
			return;
		}
		else if (letter.equals("r")) { 
			if (this.words_r == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_r = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_r)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_r), this.type); 
			}
			return;
		}
		else if (letter.equals("s")) { 
			if (this.words_s == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_s = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_s)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_s), this.type);
			}
			return;
		}
		else if (letter.equals("t")) { 
			if (this.words_t == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_t = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_t)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_t), this.type); 
			}
			return;
		}
		else if (letter.equals("u")) { 
			if (this.words_u == null){ 
				Logger.d(TAG, "letter=" + letter);
				this.words_u = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_u)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_u), this.type); 
			}
			return;
		}
		else if (letter.equals("v")) { 
			if (this.words_v == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_v = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_v)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_v), this.type); 
			}
			return;
		}
		else if (letter.equals("w")) { 
			if (this.words_w == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_w = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_w)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_w), this.type);
			}
			return;
		}
		else if (letter.equals("x")) { 
			if (this.words_x == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_x = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_x)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_x), this.type); 
			}
			return;
		}
		else if (letter.equals("y")) { 
			if (this.words_y == null){
				Logger.d(TAG, "letter=" + letter);
				this.words_y = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_y)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_y), this.type); 
			}
			return;
		}
		else if (letter.equals("z")) { 
			if (this.words_z == null){ 
				Logger.d(TAG, "letter=" + letter);
				this.words_z = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_z)), this.type);//(FileUtils.ReadRawTextFile(context, R.raw.words_z), this.type); 
			}
		}
	 
	}

}
