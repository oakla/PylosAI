package player;

import interfaces.NodeInfo;
import search.*;



public class NodeReader implements NodeInfo {
	
	final static int L1_ROWS = 4;
	final static int L2_ROWS = 3;
	final static int L3_ROWS = 2;
	final static int L4_ROWS = 1;
	
	char color;
	
	
	public NodeReader(char c){
		color = c;
	}
	
	@Override
	public boolean isGoal(Node node) {
		isColorSet();
		return (node.getState().getGrid()[9][0] == color);
	}

	@Override
	public boolean isTerminal(Node node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double utility(Node node) {
		isColorSet();
		
		double utility = 15;
		
		char[][] grid = node.getState().getGrid();
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j< 4; j++){
				if(grid[i][j] == color) {utility--;}
			}
		}
		for(int i = 4; i < 7; i++){
			for(int j = 0; j< 4; j++){
				if(grid[i][j] == color) {utility--;}
			}
		}
		for(int i = 7; i < 9; i++){
			for(int j = 0; j< 4; j++){
				if(grid[i][j] == color) {utility--;}
			}
		}
		if(isGoal(node)) {utility = Double.POSITIVE_INFINITY;}
		return utility;
	}

	@Override
	public void setDepthLimit(double limit) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getDepthLimit() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isColorSet(){
		if (color == 'w' || color == 'b') return true;
		else System.out.println("You forgot to set your color"); return false;
		
	}

}
