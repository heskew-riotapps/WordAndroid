package com.riotapps.word.hooks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Opponent  implements Parcelable{
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getNumWins() {
		return numWins;
	}

	public void setNumWins(int numWins) {
		this.numWins = numWins;
	}

	public int getNumLosses() {
		return numLosses;
	}

	public void setNumLosses(int numLosses) {
		this.numLosses = numLosses;
	}

	public int getNumDraws() {
		return numDraws;
	}

	public void setNumDraws(int numDraws) {
		this.numDraws = numDraws;
	}

	private Player player; 
	
	@SerializedName("n_w")
	private int numWins = 0; //num wins
	
	@SerializedName("n_l")
	private int numLosses = 0; //num losses
	
	@SerializedName("n_d")
	private int numDraws = 0; //num draws

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(player, flags);
		out.writeInt(this.numWins);
		out.writeInt(this.numLosses);
		out.writeInt(this.numDraws);
	}
	
	public static final Parcelable.Creator<Opponent> CREATOR
    			= new Parcelable.Creator<Opponent>() {
			public Opponent createFromParcel(Parcel in) {
				return new Opponent(in);
			}

			public Opponent[] newArray(int size) {
				return new Opponent[size];
			}
	};

	private Opponent(Parcel in) {
		// same order as writeToParcel
		this.player = in.readParcelable(Player.class.getClassLoader());
		this.numWins = in.readInt();
		this.numLosses = in.readInt();
		this.numDraws = in.readInt();
	}

	
	
}
