package game;

import interfaces.Action;
import interfaces.Percept;

public class Pylos {

	final static char UNOCCUPIED = '_';
	final static char WHITE = 'W';
	final static char BLACK = 'B';
	
	private char[][] grid;
	private char colorToPlay;
	int nBlackRes, nWhiteRes;
	
	
	//create new Pylos game with a pyramid grid of base 4x4
	public Pylos(){
		colorToPlay = WHITE;
		nBlackRes = 15;
		nWhiteRes = 15;
		
		grid = new char[10][10];
		for(int i=1; i<10; i++) {
			for(int j=1; j<10; j++){
				grid[i][j] = UNOCCUPIED;
			}
		}
	}
	
	//create Pylos game with predefined grid (
	//Note: current agent implementation does not support grids other than 10x10 char array
	public Pylos(char[][] grid){
		this.grid = grid;
	}
	

	public Percept getPercept() {
		return new Grid(grid,colorToPlay,nBlackRes,nWhiteRes);
	}

	public void update(Action arg0) throws RuntimeException {
		// TODO Auto-generated method stub

	}
	
	//should many use Object.clone() instead?
	public Pylos clone(){
		return new Pylos(grid);
	}

}
