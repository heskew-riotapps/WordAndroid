package com.riotapps.word.hooks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Opponent  implements Parcelable{
	


	private Player player; 

	@SerializedName("n_g")
	private int numGames = 0;  

	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	public int getNumGames() {
		return numGames;
	}

	public void setNumGames(int numGames) {
		this.numGames = numGames;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(player, flags);
		out.writeInt(this.numGames);
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
		this.numGames = in.readInt();
		}

	
	
}
