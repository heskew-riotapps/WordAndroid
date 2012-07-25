package com.riotapps.word.hooks;

import com.riotapps.word.utils.Constants;

public class Player {
	private String id;
	private String nickname;
	private String email;
	private String password;
	private String fb = "";
	private String auth_token;
	private int n_w = 0; //num wins
	private int n_l = 0; //num losses
	private int n_d = 0; //num draws
	private String badge_drawable = "";

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}
	public String getNickname() {
		return this.nickname;
	}
	public void setEmail(String email) {
		this.email = email.trim();
	}
	public String getEmail() {
		return this.email;
	}
	public void setPassword(String password) {
		this.password = password.trim();
	}
	public String getPassword() {
		return this.password;
	}
	public void setFB(String fb) {
		this.fb = fb.trim();
	}
	public String getFB() {
		return this.fb;
	}
	public void setAuthToken(String auth_token) {
		this.auth_token = auth_token.trim();
	}
	public String getAuthToken() {
		return this.auth_token;
	}
	
	public boolean isFacebookUser(){
		return this.fb.length() > 0;
	}
	
	public int getNumGames(){
		return this.n_w + this.n_l + this.n_d;
	}
	
	public String getBadgeDrawable(){
		if (this.n_w == 0) {
			return Constants.BADGE_0;
		}
		if (this.n_w >= 1 && this.n_w <= 4) {
			return Constants.BADGE_1_4;
		}
		if (this.n_w >= 5 && this.n_w <= 9) {
			return Constants.BADGE_5_9;
		}
		if (this.n_w >= 10 && this.n_w <= 14) {
			return Constants.BADGE_10_14;
		}
		if (this.n_w >= 15 && this.n_w <= 19) {
			return Constants.BADGE_1_4;
		}
		if (this.n_w >= 1 && this.n_w <= 4) {
			return Constants.BADGE_15_19;
		}
		if (this.n_w >= 20 && this.n_w <= 24) {
			return Constants.BADGE_20_24;
		}
		if (this.n_w >= 25 && this.n_w <= 29) {
			return Constants.BADGE_25_29;
		}
		if (this.n_w >= 30 && this.n_w <= 39) {
			return Constants.BADGE_30_39;
		}
		if (this.n_w >= 40 && this.n_w <= 49) {
			return Constants.BADGE_40_49;
		}
		if (this.n_w >= 50 && this.n_w <= 59) {
			return Constants.BADGE_50_59;
		}
		if (this.n_w >= 60 && this.n_w <= 69) {
			return Constants.BADGE_60_69;
		}
		if (this.n_w >= 70 && this.n_w <= 79) {
			return Constants.BADGE_70_79;
		}
		if (this.n_w >= 80 && this.n_w <= 89) {
			return Constants.BADGE_80_89;
		}
		if (this.n_w >= 90 && this.n_w <= 99) {
			return Constants.BADGE_90_99;
		}
		if (this.n_w >= 100 && this.n_w <= 124) {
			return Constants.BADGE_100_124;
		}
		if (this.n_w >= 125 && this.n_w <= 149) {
			return Constants.BADGE_125_149;
		}
		if (this.n_w >= 150 && this.n_w <= 174) {
			return Constants.BADGE_150_174;
		}
		if (this.n_w >= 175 && this.n_w <= 199) {
			return Constants.BADGE_175_199;
		}
		if (this.n_w >= 200 && this.n_w <= 224) {
			return Constants.BADGE_200_224;
		}
		if (this.n_w >= 225 && this.n_w <= 249) {
			return Constants.BADGE_225_249;
		}
		if (this.n_w >= 250 && this.n_w <= 274) {
			return Constants.BADGE_250_274;
		}
		if (this.n_w >= 275 && this.n_w <= 299) {
			return Constants.BADGE_275_299;
		}
		if (this.n_w >= 300 && this.n_w <= 349) {
			return Constants.BADGE_300_349;
		}
		if (this.n_w >= 350 && this.n_w <= 399) {
			return Constants.BADGE_350_399;
		}
		if (this.n_w >= 400 && this.n_w <= 449) {
			return Constants.BADGE_400_449;
		}
		if (this.n_w >= 450 && this.n_w <= 499) {
			return Constants.BADGE_450_499;
		}
		if (this.n_w >= 500 && this.n_w <= 599) {
			return Constants.BADGE_500_599;
		}
		if (this.n_w >= 600 && this.n_w <= 699) {
			return Constants.BADGE_600_699;
		}
		if (this.n_w >= 700 && this.n_w <= 799) {
			return Constants.BADGE_700_799;
		}
		if (this.n_w >= 800 && this.n_w <= 899) {
			return Constants.BADGE_800_899;
		}
		if (this.n_w >= 900 && this.n_w <= 999) {
			return Constants.BADGE_900_999;
		}
		if (this.n_w >= 1000 && this.n_w <= 1249) {
			return Constants.BADGE_1000_1249;
		}
		if (this.n_w >= 1250 && this.n_w <= 1499) {
			return Constants.BADGE_1250_1499;
		}
		if (this.n_w >= 1500 && this.n_w <= 1749) {
			return Constants.BADGE_1500_1749;
		}
		if (this.n_w >= 1750 && this.n_w <= 1999) {
			return Constants.BADGE_1750_1999;
		}
		if (this.n_w >= 2000 && this.n_w <= 2499) {
			return Constants.BADGE_2000_2499;
		}
		if (this.n_w >= 2500 && this.n_w <= 2999) {
			return Constants.BADGE_2500_2999;
		}
		if (this.n_w >= 3000 && this.n_w <= 3999) {
			return Constants.BADGE_3000_3999;
		}
		if (this.n_w >= 4000 && this.n_w <= 4999) {
			return Constants.BADGE_4000_4999;
		}
		if (this.n_w >= 5000) {
			return Constants.BADGE_5000;
		}
		//fallthrough
		return Constants.BADGE_0;
	}
	

}
