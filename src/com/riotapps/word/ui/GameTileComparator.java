package com.riotapps.word.ui;

import java.util.Comparator;

public class GameTileComparator  implements Comparator<GameTile> {

	@Override
	public int compare(GameTile tile1, GameTile tile2) {
		return ((Integer)tile1.getId()).compareTo((Integer)tile2.getId());
	}

}
 