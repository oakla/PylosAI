package search;

import interfaces.Action;
import interfaces.State;
import player.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;




import player.ActionImp;


import game.Grid;
import game.StaticTypes;

public class PylosState implements State {


	char[][] charGrid;
	
	private LinkedList<ActionImp> choices;
	
	private ArrayList<Integer> spaces;
	private ArrayList<Integer> freeBalls;
	
	char colorToPlay, colorNextPlay; 
	int nMyRes , nOpponentRes;
	
	
	public PylosState(Grid grid){
		colorToPlay = grid.getColorToPlay();
		init(grid.getCharArray());
	}
	
	private void init(char[][] charGrid2) {
		charGrid = charGrid2;
		spaces = new ArrayList<Integer>();
		freeBalls = new ArrayList<Integer>();
		if(colorToPlay == 'b') colorNextPlay = 'w'; 
		else if(colorToPlay == 'w') colorNextPlay = 'b';
		else throw new RuntimeException("unexpected player color symbol recieved by Grid constructor");
		
		nMyRes = 15;
		nOpponentRes = 15;
		
		parseGrid();  //populates spaces and freeballs, sets nMyRes and nOpponentRes.
	}

	//populates spaces and freeballs, sets nMyRes and nOpponentRes.
	private void parseGrid() {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				analyzePosition(i,j);
			}
		}
		for(int i = 4; i < 7; i++){
			for(int j = 0; j < 3; j++){
				analyzePosition(i,j);
			}
		}
		for(int i = 7; i < 9; i++){
			for(int j=0; j<2; j++){
				analyzePosition(i,j);
			}
		}
		for(int i = 9; i < 10; i++){
			for(int j=0; j<1; j++){
				analyzePosition(i,j);
			}
		}
	}
	
	//checks color of ball, and whether it's a free ball or space at position i,j
	public void analyzePosition(int i, int j){
		if(!Util.isValidPosition(i,j)) throw new RuntimeException(); //sanity check
		
		if(charGrid[i][j] == colorNextPlay) nOpponentRes--;
		else if(charGrid[i][j] == colorToPlay) {
			nMyRes--;
			if(isFreeBall(i,j,charGrid)) freeBalls.add(compressPos(i,j));
		}	
		else if(charGrid[i][j] != '_') 
			throw new RuntimeException("unexpected symbol found in charGrid: "+charGrid[i][j]+"at i= "+i+", j= "+j);
		else if (isUsableSpace(i,j,charGrid)) spaces.add(compressPos(i,j));
	}
	
	private boolean isFreeBall(int i, int j, char[][] tempCharGrid) {
		
		int offset = 0;
		int iMin = 0;
		if(tempCharGrid[i][j] != 'b' && tempCharGrid[i][j] != 'w') 
			throw new RuntimeException("non-ball position passed to isFreeBall: "+charGrid[i][j]+"at i= "+i+", j= "+j);

		if(Util.levelAt(i) == 0) {
			offset = 4; iMin = 3;
		}else if(Util.levelAt(i) == 1) {
			offset = 3; iMin = 6;
		}else if(Util.levelAt(i) == 2) {
			offset = 2; iMin = 8;
		}else if(Util.levelAt(i) == 3) return true;
			
		for(int k = 0; k < 2; k++){
			for(int l = 0; l < 2; l++){
				if(i+offset-k > iMin && i+offset-k < iMin+offset 
				&& j-l < offset-1 && j-l >= 0){
						if(!Util.isValidPosition(i+offset-k, j-l)){
							System.out.println("INVALID");
						}
						if(tempCharGrid[i+offset-k][j-l] != '_'){
							return false;
		}}}}
		return true;
	}
	
	public boolean isUsableSpace(int i, int j, char[][] tempCharGrid) {
		
		int offset = 0;
		if(tempCharGrid[i][j] != '_') return false;

		if(Util.levelAt(i) == 0) return true;
		offset = 5 - Util.levelAt(i);
		
		for(int k = 0; k < 2; k++){
			for(int l = 0; l < 2; l++){
				if(tempCharGrid[k+i-offset][l+j] == '_'){
					return false;
		}}}
		return true;
	}

	public LinkedList<ActionImp> getActions() {
		choices = new LinkedList<ActionImp>();
		
		//sanity check
		if(nMyRes < 0 || nOpponentRes < 0) {
			System.out.print("mis count of balls. nMyRes ="+nMyRes+", nOpponentRes ="+nOpponentRes); 
			return null;
		}
		
		createResToBoardMoves();
		createBoardToBoardMoves();
		
		return choices;
	}
	//for each free ball, make one move for every available space THAT IS ALSO at a higher level
	private void createBoardToBoardMoves() {
		ListIterator<Integer> FBIt = freeBalls.listIterator();
		ListIterator<Integer> SIt = spaces.listIterator();
		Integer temp, temp1;

		while(FBIt.hasNext()){
			temp = FBIt.next(); //move ball from this position
			while(SIt.hasNext()){
				temp1 = SIt.next(); //move ball to this position
				
				//check to see if space is still usable after ball is moved
				char[][] tempG = Util.cloneCharGrid(charGrid.clone());
				tempG[decodeI(temp)][decodeJ(temp)] = '_';
				if(Util.levelAt(decodeI(temp)) < Util.levelAt(decodeI(temp1)) && isUsableSpace(decodeI(temp1), decodeJ(temp1), tempG)) { 
					createAction(decodeI(temp), decodeJ(temp), decodeI(temp1), decodeJ(temp1));
				}	
			}
			SIt = spaces.listIterator();	
		}	
	}
	
	//if there is at least one ball in my reserve, make one move for every available space
	private void createResToBoardMoves() {
		Integer tempPos;
		ListIterator<Integer> SIt = spaces.listIterator();
		if(nMyRes > 0){
			while(SIt.hasNext()){
				tempPos = SIt.next(); //ball to this position
				createAction(-1, -1, decodeI(tempPos), decodeJ(tempPos));
			}
		}
	}
	
	private void createAction(int i, int j, int x, int y){

		ActionImp tempAction;
		tempAction = new ActionImp(i, j, x, y);
		if(checkForLine(i, j, x, y, colorToPlay) || checkForSquare(i, j, x, y, colorToPlay)){
			for(int r = StaticTypes.MAX_BALL_REMOVES; r > 0; r--){
				if(x == -1 || y == -1) System.out.println("Alert: pattern detected on remove");
				ListIterator<ActionImp> tempList = createRecursiveActions(tempAction, this, r).listIterator();
				while(tempList.hasNext()){
					choices.add(tempList.next());
				}
			}
		}else choices.add(tempAction);
	}

	private LinkedList<ActionImp> createRecursiveActions(ActionImp tempAction, PylosState currentState, int nRecursions) {
		nRecursions--;
		LinkedList<ActionImp> actionsList = new LinkedList<ActionImp>();
		PylosState stateAfterMove;
		ActionImp headAction, tailAction;
		
		stateAfterMove = currentState.clone();
		stateAfterMove.update(tempAction);
		ListIterator<ActionImp> It = stateAfterMove.getPossibleRemoves().listIterator();
		
		while(It.hasNext()){
			headAction = tempAction.clone();
			tailAction = It.next();

			if(nRecursions > 0){
				ListIterator<ActionImp> It2 = createRecursiveActions(tailAction, stateAfterMove, nRecursions).listIterator();
				while(It2.hasNext()){
					headAction = headAction.clone();
					headAction.setRemove(It2.next());
					actionsList.add(headAction);
				}
			}else {
				headAction.setRemove(tailAction);
				actionsList.add(headAction);
			}
		}
		return actionsList;
	}

	private boolean checkForSquare(int x, int y, int x1, int y1, char c) {
		if(x1 < 0 || y1 < 0){
			return false;
		}
		char[][] tempCharGrid = Util.cloneCharGrid(charGrid);
		tempCharGrid[x1][y1] = c;
		if(x != -1 || y != -1){
			tempCharGrid[x][y] = '_';	
		}
		int counter, iMin, iMax, jMin, jMax;
		int level = Util.levelAt(x1);
		if(level == 0){
			iMin = 0;
			iMax = 3;
			jMin = 0;
			jMax = 3;
		}
		else if(level == 1){
			iMin = 4;
			iMax = 6;
			jMin = 0;
			jMax = 2;
		}
		else if(level == 2){
			iMin = 7;
			iMax = 8;
			jMin = 0;
			jMax = 1;
		}
		else{
			return false;
		}
		if(x1+1 <= iMax && y1+1 <= jMax){
			counter = 0;
			for(int i = 0; i < 2; i++){
				for(int j = 0; j < 2; j++){
					if(tempCharGrid[x1+i][y1+j] != c){
						break;
					}
					counter++;
				}
				
			}
			if(counter == 4) {
				System.out.println("Square Found for"+x1+", "+y1);
				return true;
			}
		}
		if(x1-1 >= iMin && y1+1 <= jMax){
			counter = 0;
			for(int i = 0; i < 2; i++){
				for(int j = 0; j < 2; j++){
					if(tempCharGrid[x1-i][y1+j] != c){
						break;
					}
					counter++;
				}
				
			}
			if(counter == 4) {
				System.out.println("Square Found for"+x1+", "+y1);
				return true;
			}
		}
		if(x1+1 <= iMax && y1-1 >= jMin){
			counter = 0;
			for(int i = 0; i < 2; i++){
				for(int j = 0; j < 2; j++){
					if(tempCharGrid[x1+i][y1-j] != c){
						break;
					}
					counter++;
				}
			}
			if(counter == 4) {
				System.out.println("Square Found for"+x1+", "+y1);
				return true;
			}
		}
		if(x1-1 >= iMin && y1-1 >= jMin){
			counter = 0;
			for(int i = 0; i < 2; i++){
				for(int j = 0; j < 2; j++){
					if(tempCharGrid[x1-i][y1-j] != c){
						break;
					}
					counter++;
				}
			}
			if(counter == 4) {
				System.out.println("Square Found for"+x1+", "+y1);
				return true;
			}
		}
		return false;
	}

	private boolean checkForLine(int x, int y, int x1, int y1, char c) {
		if(x1 < 0 || y1 < 0){
			return false;
		}
		char[][] tempCharGrid = Util.cloneCharGrid(charGrid);
		tempCharGrid[x1][y1] = c;
		if(x != -1 || y != -1){
			tempCharGrid[x][y] = '_';	
		}
		int i;
		int level = Util.levelAt(x1);
		if(level == 0){
			for(i = 0; i < 4; i++){
				if(tempCharGrid[i][y1] != c){
					break;
				}
			}
			if(i == 4) return true;
			for(i = 0; i < 4; i++){
				if(tempCharGrid[x1][i] != c){
					break;
				}
			}
			if(i == 4) return true;
		}
		if(level == 1){
			for(i = 0; i < 3; i++){
				if(tempCharGrid[i+4][y1] != c){
					break;
				}
			}
			if(i == 3) return true;
			for(i = 0; i < 3; i++){
				if(tempCharGrid[x1][i] != c){
					break;
				}
			}
			if(i == 3) return true;
		}
		return false;
	}
	
	private LinkedList<ActionImp> makeListWithRemoves(ActionImp tempAction) {
		PylosState tempState;
		LinkedList<ActionImp> tempActionList, possibleRemoves;
		tempActionList = new LinkedList<ActionImp>();
		
		//get list of possible removes for state of grid after tempAction
		tempState = this.clone();
		tempState.update(tempAction);
		possibleRemoves = tempState.getPossibleRemoves();
		
		ListIterator<ActionImp> it = possibleRemoves.listIterator();
		while(it.hasNext()){
			ActionImp newAction = tempAction.clone();
			newAction.setRemove(it.next());
			tempActionList.add(newAction);
		}
		return tempActionList;
	}
	
	public PylosState clone(){
		int nBlackRes, nWhiteRes;
		if(colorToPlay == 'b'){
			nBlackRes = nMyRes;
			nWhiteRes = nOpponentRes;
		}else{
			nBlackRes = nOpponentRes;
			nWhiteRes = nMyRes;
		}
		return new PylosState(new Grid(charGrid, colorToPlay, nBlackRes, nWhiteRes));
	}

	public char[][] getGrid() {return charGrid;}

	public int compressPos(int i, int j){return ((i +1) * 100 + j + 1);}
	private int decodeI(int a){return ((a/100)-1);}
	private int decodeJ(int a){return ((a%100)-1);}
	
	public void printStateInfo(){
		System.out.println("charGrid represents current Grid for this state;");
		System.out.println("	 _ _ _ _		 _ _ _		 _ _		 _");
		System.out.println("	|"+charGrid[0][0]+"|"+charGrid[1][0]+"|"+charGrid[2][0]+"|"+charGrid[3][0]+"|		|"+charGrid[4][0]+"|"+charGrid[5][0]+"|"+charGrid[6][0]+"|		|"+charGrid[7][0]+"|"+charGrid[8][0]+"|		|"+charGrid[9][0]+"|");
		System.out.println("	|"+charGrid[0][1]+"|"+charGrid[1][1]+"|"+charGrid[2][1]+"|"+charGrid[3][1]+"|		|"+charGrid[4][1]+"|"+charGrid[5][1]+"|"+charGrid[6][1]+"|		|"+charGrid[7][0]+"|"+charGrid[8][0]+"|");
		System.out.println("	|"+charGrid[0][2]+"|"+charGrid[1][2]+"|"+charGrid[2][2]+"|"+charGrid[3][2]+"|		|"+charGrid[4][2]+"|"+charGrid[5][2]+"|"+charGrid[6][2]+"|");
		System.out.println("	|"+charGrid[0][3]+"|"+charGrid[1][3]+"|"+charGrid[2][3]+"|"+charGrid[3][3]+"|");

		System.out.println();
		System.out.println("RESERVE BALLS - Current player; "+nMyRes+"  Opponent; "+nOpponentRes);
		System.out.println("POSSIBLE MOVES - ");
		ListIterator<ActionImp> it = this.getActions().listIterator();
		while(it.hasNext()){
			ActionImp a = (ActionImp) it.next();
			a.printAction();
		}
		System.out.print("FREE SPACES -   ");
		System.out.print(spaces.toString());
		System.out.println();
		System.out.print("FREE BALLS -   ");
		System.out.print(freeBalls.toString());
		System.out.println();
	}
	
	//returns a list of actions that remove a free balls
	public LinkedList<ActionImp> getPossibleRemoves() {
		ListIterator<Integer> FBIt = freeBalls.listIterator();
		LinkedList<ActionImp> rmActionList = new LinkedList<ActionImp>();
		Integer temp;
		ActionImp tempAction;

		while(FBIt.hasNext()){
			temp = FBIt.next(); //move ball from this position	
			tempAction = new ActionImp(decodeI(temp), decodeJ(temp), -1, -1);
			rmActionList.add(tempAction);		
		}
		return rmActionList;
	}
/*
	private void createRemoveActions() {
		//no symmetry
		ActionImp tempAction, tempAction1, tempAction2;
		Integer[] tempFB = freeBalls.toArray(new Integer[10]);
		tempFB.add(compressPos(decodeI(temp1), decodeJ(temp1)));
		FBIt = freeBalls.listIterator();
		tempAction = choices.removeLast();
		while(FBIt.hasNext()){
			temp = FBIt.next();
			FBIt.remove();
			tempAction1 = tempAction.clone();
			tempAction1.setNextPart((new ActionImp(decodeI(temp), decodeJ(temp), -1, -1)));
			choices.add(tempAction1);
			FBIt = freeBalls.listIterator();
			while(FBIt.hasNext()){
				temp = FBIt.next();
				FBIt.remove();
				tempAction2 = tempAction1.clone();
				tempAction2.setNextPart((new ActionImp(decodeI(temp), decodeJ(temp), -1, -1)));
				choices.add(tempAction2);
			}
		}
		
	}*/
	
	@Override
	public void update(Action action) throws RuntimeException {
		int fromX = action.getFromX();
		int fromY = action.getFromY();
		if(fromX != -1 && fromY != -1){
			charGrid[fromX][fromY] = '_';
		}
		int toX = action.getToX();
		int toY = action.getToY();
		if(toX != -1 && toY != -1){
			charGrid[toX][toY] = colorToPlay;
		}
		init(charGrid);
		
	}
	


}
