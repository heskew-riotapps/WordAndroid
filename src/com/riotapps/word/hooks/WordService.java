package com.riotapps.word.hooks;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.FileUtils;

public class WordService {
	
	List<Word> wordsA = null;
	List<Word> wordsB = null;
	List<Word> wordsC = null;
	List<Word> wordsD = null;
	List<Word> wordsE = null;
	List<Word> wordsF = null;
	List<Word> wordsG = null;
	List<Word> wordsH = null;
	List<Word> wordsI = null;
	List<Word> wordsJ = null;
	List<Word> wordsK = null;
	List<Word> wordsL = null;
	List<Word> wordsM = null;
	List<Word> wordsN = null;
	List<Word> wordsO = null;
	List<Word> wordsP = null;
	List<Word> wordsQ = null;
	List<Word> wordsR = null;
	List<Word> wordsS = null;
	List<Word> wordsT = null;
	List<Word> wordsU = null;
	List<Word> wordsV = null;
	List<Word> wordsW = null;
	List<Word> wordsX = null;
	List<Word> wordsY = null;
	List<Word> wordsZ = null;
	
	Type type = new TypeToken<List<Word>>() {}.getType();
	Context context;
	
	
	public WordService(Context context){
		this.context = context;
	}
	

	public boolean isWordValid(String word){
		word = word.toLowerCase();
		String startWith = (word.substring(0, 1));
		
		this.loadList(startWith);

		if (startWith.equals("a")){ return this.wordsA.contains(word); }
		if (startWith.equals("b")){ return this.wordsB.contains(word); }
		if (startWith.equals("c")){ return this.wordsC.contains(word); }
		if (startWith.equals("d")){ return this.wordsD.contains(word); }
		if (startWith.equals("e")){ return this.wordsE.contains(word); }
		if (startWith.equals("f")){ return this.wordsF.contains(word); }
		if (startWith.equals("g")){ return this.wordsG.contains(word); }
		if (startWith.equals("h")){ return this.wordsH.contains(word); }
		if (startWith.equals("i")){ return this.wordsI.contains(word); }
		if (startWith.equals("j")){ return this.wordsJ.contains(word); }
		if (startWith.equals("k")){ return this.wordsK.contains(word); }
		if (startWith.equals("l")){ return this.wordsL.contains(word); }
		if (startWith.equals("m")){ return this.wordsM.contains(word); }
		if (startWith.equals("n")){ return this.wordsN.contains(word); }
		if (startWith.equals("o")){ return this.wordsO.contains(word); }
		if (startWith.equals("p")){ return this.wordsP.contains(word); }
		if (startWith.equals("q")){ return this.wordsQ.contains(word); }
		if (startWith.equals("r")){ return this.wordsR.contains(word); }
		if (startWith.equals("s")){ return this.wordsS.contains(word); }
		if (startWith.equals("t")){ return this.wordsT.contains(word); }
		if (startWith.equals("u")){ return this.wordsU.contains(word); }
		if (startWith.equals("v")){ return this.wordsV.contains(word); }
		if (startWith.equals("w")){ return this.wordsW.contains(word); }
		if (startWith.equals("x")){ return this.wordsX.contains(word); }
		if (startWith.equals("y")){ return this.wordsY.contains(word); }
		if (startWith.equals("z")){ return this.wordsZ.contains(word); }

		return false;
	}
	
	private void loadList(String letter){

		if (letter.equals("a") && this.wordsA == null){ 
			this.wordsA = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_a), this.type);
			return;
		}
		if (letter.equals("b") && this.wordsB == null){ 
			this.wordsB = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_b), this.type);
			return;
		}
		if (letter.equals("c") && this.wordsC == null){ 
			this.wordsC = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_c), this.type); 
			return;
		}
		if (letter.equals("d") && this.wordsD == null){ 
			this.wordsD = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_d), this.type); 
			return;
		}
		if (letter.equals("e") && this.wordsE == null){ 
			this.wordsE = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_e), this.type); 
			return;
		}
		if (letter.equals("f") && this.wordsF == null){ 
			this.wordsF = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_f), this.type);  
			return;
		}
		if (letter.equals("g") && this.wordsG == null){ 
			this.wordsG = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_g), this.type); 
			return;
		}
		if (letter.equals("h") && this.wordsH == null){ 
			this.wordsH = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_h), this.type);  
			return;
		}
		if (letter.equals("i") && this.wordsI == null){ 
			this.wordsI = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_i), this.type); 
			return;
		}
		if (letter.equals("j") && this.wordsJ == null){ 
			this.wordsJ = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_j), this.type);  
			return;
		}
		if (letter.equals("k") && this.wordsK == null){ 
			this.wordsK = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_k), this.type);  
			return;
		}
		if (letter.equals("l") && this.wordsL == null){ 
			this.wordsL = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_l), this.type);
			return;
		}
		if (letter.equals("m") && this.wordsM == null){ 
			this.wordsM = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_m), this.type); 
			return;
		}
		if (letter.equals("n") && this.wordsN == null){ 
			this.wordsN = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_n), this.type);
			return;
		}
		if (letter.equals("o") && this.wordsO == null){ 
			this.wordsO = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_o), this.type);
			return;
		}
		if (letter.equals("p") && this.wordsP == null){ 
			this.wordsP = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_p), this.type);
			return;
		}
		if (letter.equals("q") && this.wordsQ == null){ 
			this.wordsQ = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_q), this.type);  
			return;
		}
		if (letter.equals("r") && this.wordsR == null){ 
			this.wordsR = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_r), this.type); 
			return;
		}
		if (letter.equals("s") && this.wordsS == null){ 
			this.wordsS = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_s), this.type);
			return;
		}
		if (letter.equals("t") && this.wordsT == null){ 
			this.wordsT = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_t), this.type); 
			return;
		}
		if (letter.equals("u") && this.wordsU == null){ 
			this.wordsU = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_u), this.type); 
			return;
		}
		if (letter.equals("v") && this.wordsV == null){ 
			this.wordsV = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_v), this.type); 
			return;
		}
		if (letter.equals("w") && this.wordsW == null){ 
			this.wordsW = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_w), this.type);
			return;
		}
		if (letter.equals("x") && this.wordsX == null){ 
			this.wordsX = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_x), this.type); 
			return;
		}
		if (letter.equals("y") && this.wordsY == null){ 
			this.wordsY = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_y), this.type); 
			return;
		}
		if (letter.equals("z") && this.wordsZ == null){ 
			this.wordsZ = new Gson().fromJson(FileUtils.ReadRawTextFile(context, R.raw.words_z), this.type); 
			return;
		}
	 
		
	}
	
	private int getResourceId(String letter){
		if (letter == "a"){ return R.raw.words_a; }
		if (letter == "b"){ return R.raw.words_b; }
		if (letter == "c"){ return R.raw.words_c; }
		if (letter == "d"){ return R.raw.words_d; }
		if (letter == "e"){ return R.raw.words_e; }
		if (letter == "f"){ return R.raw.words_f; }
		if (letter == "g"){ return R.raw.words_g; }
		if (letter == "h"){ return R.raw.words_h; }
		if (letter == "i"){ return R.raw.words_i; }
		if (letter == "j"){ return R.raw.words_j; }
		if (letter == "k"){ return R.raw.words_k; }
		if (letter == "l"){ return R.raw.words_l; }
		if (letter == "m"){ return R.raw.words_m; }
		if (letter == "n"){ return R.raw.words_n; }
		if (letter == "o"){ return R.raw.words_o; }
		if (letter == "p"){ return R.raw.words_p; }
		if (letter == "q"){ return R.raw.words_q; }
		if (letter == "r"){ return R.raw.words_r; }
		if (letter == "s"){ return R.raw.words_s; }
		if (letter == "t"){ return R.raw.words_t; }
		if (letter == "u"){ return R.raw.words_u; }
		if (letter == "v"){ return R.raw.words_v; }
		if (letter == "w"){ return R.raw.words_w; }
		if (letter == "z"){ return R.raw.words_x; }
		if (letter == "y"){ return R.raw.words_y; }
		if (letter == "z"){ return R.raw.words_z; }
		
		return 0;
	}
}
