package game;

import player.Util;
import interfaces.Percept;

public class Grid implements Percept {
	
	char[][] grid;
	char colorToPlay;
	int nBlackRes, nWhiteRes;
	
	public Grid(char[][] grid, char c, int nBlack, int nWhite){
		colorToPlay = c;
		this.grid = grid;
		nBlackRes = nBlack;
		nWhiteRes = nWhite;
	}
	
	public char[][] getCharArray (){
		return Util.cloneCharGrid(grid);
	}

	public char getColorToPlay() {
		return colorToPlay;
	}
}
