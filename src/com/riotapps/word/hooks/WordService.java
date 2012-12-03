package com.riotapps.word.hooks;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
 
	Type type = new TypeToken<SortedSet<String>>() {}.getType();
	Context context;
	
	
	public WordService(Context context){
		this.context = context;
	}
	

	public boolean isWordValid(String word){
		word = word.toLowerCase();
		String startWith = (word.substring(0, 1));
		
		this.loadList(startWith);
 	
		if (startWith.equals("a")){ return this.words_a.contains(word); }
		if (startWith.equals("b")){ return this.words_b.contains(word); }
		if (startWith.equals("c")){ return this.words_c.contains(word); }
		if (startWith.equals("d")){ return this.words_d.contains(word); }
		if (startWith.equals("e")){ return this.words_e.contains(word); }
		if (startWith.equals("f")){ return this.words_f.contains(word); }
		if (startWith.equals("g")){ return this.words_g.contains(word); }
		if (startWith.equals("h")){ return this.words_h.contains(word); }
		if (startWith.equals("i")){ return this.words_i.contains(word); }
		if (startWith.equals("j")){ return this.words_j.contains(word); }
		if (startWith.equals("k")){ return this.words_k.contains(word); }
		if (startWith.equals("l")){ return this.words_l.contains(word); }
		if (startWith.equals("m")){ return this.words_m.contains(word); }
		if (startWith.equals("n")){ return this.words_n.contains(word); }
		if (startWith.equals("o")){ return this.words_o.contains(word); }
		if (startWith.equals("p")){ return this.words_p.contains(word); }
		if (startWith.equals("q")){ return this.words_q.contains(word); }
		if (startWith.equals("r")){ return this.words_r.contains(word); }
		if (startWith.equals("s")){ return this.words_s.contains(word); }
		if (startWith.equals("t")){ return this.words_t.contains(word); }
		if (startWith.equals("u")){ return this.words_u.contains(word); }
		if (startWith.equals("v")){ return this.words_v.contains(word); }
		if (startWith.equals("w")){ return this.words_w.contains(word); }
		if (startWith.equals("x")){ return this.words_x.contains(word); }
		if (startWith.equals("y")){ return this.words_y.contains(word); }
		if (startWith.equals("z")){ return this.words_z.contains(word); }

		return false;
	}
	
	public void loadAll(){
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
	}
	
	private void loadList(String letter){

		if (letter.equals("a") && this.words_a == null){ 
			this.words_a = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_a), this.type);
			return;
		}
		if (letter.equals("b") && this.words_b == null){ 
			this.words_b = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_b), this.type);
			return;
		}
		if (letter.equals("c") && this.words_c == null){ 
			this.words_c = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_c), this.type); 
			return;
		}
		if (letter.equals("d") && this.words_d == null){ 
			this.words_d = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_d), this.type); 
			return;
		}
		if (letter.equals("e") && this.words_e == null){ 
			this.words_e = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_e), this.type); 
			return;
		}
		if (letter.equals("f") && this.words_f == null){ 
			this.words_f = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_f), this.type);  
			return;
		}
		if (letter.equals("g") && this.words_g == null){ 
			this.words_g = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_g), this.type); 
			return;
		}
		if (letter.equals("h") && this.words_h == null){ 
			this.words_h = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_h), this.type);  
			return;
		}
		if (letter.equals("i") && this.words_i == null){ 
			this.words_i = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_i), this.type); 
			return;
		}
		if (letter.equals("j") && this.words_j == null){ 
			this.words_j = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_j), this.type);  
			return;
		}
		if (letter.equals("k") && this.words_k == null){ 
			this.words_k = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_k), this.type);  
			return;
		}
		if (letter.equals("l") && this.words_l == null){ 
			this.words_l = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_l), this.type);
			return;
		}
		if (letter.equals("m") && this.words_m == null){ 
			this.words_m = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_m), this.type); 
			return;
		}
		if (letter.equals("n") && this.words_n == null){ 
			this.words_n = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_n), this.type);
			return;
		}
		if (letter.equals("o") && this.words_o == null){ 
			this.words_o = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_o), this.type);
			return;
		}
		if (letter.equals("p") && this.words_p == null){ 
			this.words_p = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_p), this.type);
			return;
		}
		if (letter.equals("q") && this.words_q == null){ 
			this.words_q = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_q), this.type);  
			return;
		}
		if (letter.equals("r") && this.words_r == null){ 
			this.words_r = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_r), this.type); 
			return;
		}
		if (letter.equals("s") && this.words_s == null){ 
			this.words_s = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_s), this.type);
			return;
		}
		if (letter.equals("t") && this.words_t == null){ 
			this.words_t = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_t), this.type); 
			return;
		}
		if (letter.equals("u") && this.words_u == null){ 
			this.words_u = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_u), this.type); 
			return;
		}
		if (letter.equals("v") && this.words_v == null){ 
			this.words_v = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_v), this.type); 
			return;
		}
		if (letter.equals("w") && this.words_w == null){ 
			this.words_w = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_w), this.type);
			return;
		}
		if (letter.equals("x") && this.words_x == null){ 
			this.words_x = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_x), this.type); 
			return;
		}
		if (letter.equals("y") && this.words_y == null){ 
			this.words_y = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_y), this.type); 
			return;
		}
		if (letter.equals("z") && this.words_z == null){ 
			this.words_z = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_z), this.type); 
			return;
		}
	 
	}

}
