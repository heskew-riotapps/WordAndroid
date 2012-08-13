package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

public class TileLayout {

	public List<StarterTile> StarterTiles = new ArrayList<StarterTile>();
	public List<BonusTile> BonusTiles = new ArrayList<BonusTile>();
	
	class StarterTile {
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	class BonusTile {
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getMultiplier() {
			return M;
		}
		public void setMultiplier(int multiplier) {
			this.M = multiplier;
		}
		public String getScope() {
			return S;
		}
		public void setScope(String scope) {
			this.S = scope;
		}
		private int id;
		private int M;
		private String S;
	}
	
}
