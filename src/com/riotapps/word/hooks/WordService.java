package com.riotapps.word.hooks;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashSet;
import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Logger;

public class WordService {
	private static final String TAG = WordService.class.getSimpleName();
	
	 
	private static HashSet<String> words_a = null;
	private static HashSet<String> words_b = null;
	private static HashSet<String> words_c = null;
	private static HashSet<String> words_d = null;
	private static HashSet<String> words_e = null;
	private static HashSet<String> words_f = null;
	private static HashSet<String> words_g = null;
	private static HashSet<String> words_h = null;
	private static HashSet<String> words_i = null;
	private static HashSet<String> words_j = null;
	private static HashSet<String> words_k = null;
	private static HashSet<String> words_l = null;
	private static HashSet<String> words_m = null;
	private static HashSet<String> words_n = null;
	private static HashSet<String> words_o = null;
	private static HashSet<String> words_p = null;
	private static HashSet<String> words_q = null;
	private static HashSet<String> words_r = null;
	private static HashSet<String> words_s = null;
	private static HashSet<String> words_t = null;
	private static HashSet<String> words_u = null;
	private static HashSet<String> words_v = null;
	private static HashSet<String> words_w = null;
	private static HashSet<String> words_x = null;
	private static HashSet<String> words_y = null;
	private static HashSet<String> words_z = null;
	
	 
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
	 /*
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
	 */
 
//	Type type = new TypeToken<ArrayList<String>>() {}.getType();
	Type type = new TypeToken<HashSet<String>>() {}.getType();
	//Context context;
	
	
	public WordService(){
		//this.context = context;
	}
	

	public static boolean isWordValid(String word){
		word = word.toLowerCase();
		String startWith = (word.substring(0, 1));
		
		WordService.loadList(startWith);
 	
		if (startWith.equals("a")){ return words_a.contains(word); }
		else if (startWith.equals("b")){ return words_b.contains(word); }
		else if (startWith.equals("c")){ return words_c.contains(word); }
		else if (startWith.equals("d")){ return words_d.contains(word); }
		else if (startWith.equals("e")){ return words_e.contains(word); }
		else if (startWith.equals("f")){ return words_f.contains(word); }
		else if (startWith.equals("g")){ return words_g.contains(word); }
		else if (startWith.equals("h")){ return words_h.contains(word); }
		else if (startWith.equals("i")){ return words_i.contains(word); }
		else if (startWith.equals("j")){ return words_j.contains(word); }
		else if (startWith.equals("k")){ return words_k.contains(word); }
		else if (startWith.equals("l")){ return words_l.contains(word); }
		else if (startWith.equals("m")){ return words_m.contains(word); }
		else if (startWith.equals("n")){ return words_n.contains(word); }
		else if (startWith.equals("o")){ return words_o.contains(word); }
		else if (startWith.equals("p")){ return words_p.contains(word); }
		else if (startWith.equals("q")){ return words_q.contains(word); }
		else if (startWith.equals("r")){ return words_r.contains(word); }
		else if (startWith.equals("s")){ return words_s.contains(word); }
		else if (startWith.equals("t")){ return words_t.contains(word); }
		else if (startWith.equals("u")){ return words_u.contains(word); }
		else if (startWith.equals("v")){ return words_v.contains(word); }
		else if (startWith.equals("w")){ return words_w.contains(word); }
		else if (startWith.equals("x")){ return words_x.contains(word); }
		else if (startWith.equals("y")){ return words_y.contains(word); }
		else if (startWith.equals("z")){ return words_z.contains(word); }

		return false;
	}
	
	/*
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
	*/
	
	
	public static void loadList(String letter){
	//	Context context = ApplicationContext.getAppContext();
	//	Type type = new TypeToken<HashSet<String>>() {}.getType();
		
		if (letter.equals("a")){
			if (words_a == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_a = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_a)), type);//FileUtils.ReadRawTextFile(context, R.raw.words_a), type);
				context = null;
			}
			
			return;
		}	
		else if (letter.equals("b")){
			if (words_b == null){ 
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_b = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_b)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_b), type);
				context = null;
			}
		//	context = null;
			return;
		}
		else if (letter.equals("c")){
			if (words_c == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_c = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_c)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_c), type); 
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("d")){
			if (words_d == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_d = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_d)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_d), type); 
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("e")){ 
			if (words_e == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_e = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_e)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_e), type); 
				context = null;
			}
	//		context = null;
			return;
		}
		else if (letter.equals("f")) {
			if (words_f == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_f = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_f)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_f), type);  
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("g")){ 
			if (words_g == null){ 
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_g = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_g)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_g), type); 
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("h")){ 
			if (words_h == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_h = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_h)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_h), type);  
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("i")) { 
			if (words_i == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_i = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_i)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_i), type); 
				context = null;
			}
	//		context = null;
			return;
		}
		else if (letter.equals("j")) { 
			if (words_j == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_j = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_j)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_j), type);  
				context = null;
			}
	//		context = null;
			return;
		}
		else if (letter.equals("k")){ 
			if (words_k == null){ 
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_k = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_k)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_k), type);  
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("l")) { 
			if (words_l == null){ 
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_l = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_l)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_l), type);
				context = null;
			}
	//		context = null;
			return;
		}
		else if (letter.equals("m")) { 
			if (words_m == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_m = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_m)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_m), type); 
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("n")) { 
			if (words_n == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_n = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_n)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_n), type);
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("o")){ 
			if (words_o == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_o = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_o)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_o), type);
				context = null;
			}
		//	context = null;
			return;
		}
		else if (letter.equals("p")) { 
			if (words_p == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_p = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_p)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_p), type);
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("q")) { 
			if (words_q == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_q = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_q)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_q), type);  
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("r")) { 
			if (words_r == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_r = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_r)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_r), type); 
				context = null;
			}
//			context = null;
			return;
		}
		else if (letter.equals("s")) { 
			if (words_s == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_s = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_s)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_s), type);
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("t")) { 
			if (words_t == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_t = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_t)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_t), type); 
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("u")) { 
			if (words_u == null){ 
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_u = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_u)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_u), type); 
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("v")) { 
			if (words_v == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_v = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_v)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_v), type); 
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("w")) { 
			if (words_w == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_w = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_w)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_w), type);
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("x")) { 
			if (words_x == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_x = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_x)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_x), type); 
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("y")) { 
			if (words_y == null){
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_y = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_y)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_y), type); 
				context = null;
			}
			//context = null;
			return;
		}
		else if (letter.equals("z")) { 
			if (words_z == null){ 
				Context context = ApplicationContext.getAppContext();
				Type type = new TypeToken<HashSet<String>>() {}.getType();
				Logger.d(TAG, "loading from disk letter=" + letter);
				words_z = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(R.raw.words_z)), type);//(FileUtils.ReadRawTextFile(context, R.raw.words_z), type); 
				context = null;
			}
			
			return;
		}
		 
 
	 
	}

}
