package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

public class TileLayout {

	List<StarterTile> StarterTiles = new ArrayList<StarterTile>();
	List<BonusTile> BonusTiles = new ArrayList<BonusTile>();
	
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
			return multiplier;
		}
		public void setMultiplier(int multiplier) {
			this.multiplier = multiplier;
		}
		public String getScope() {
			return scope;
		}
		public void setScope(String scope) {
			this.scope = scope;
		}
		private int id;
		private int multiplier;
		private String scope;
	}
	
}
