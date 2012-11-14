package com.riotapps.word.hooks;

import java.util.Comparator;

public class PlayedTileComparator implements Comparator<PlayedTile> {

	@Override
	public int compare(PlayedTile tile1, PlayedTile tile2) {
		return ((Integer)tile1.getBoardPosition()).compareTo((Integer)tile2.getBoardPosition());
	}

}
 