package player;

import java.util.LinkedList;
import java.util.ListIterator;

import search.PylosState;
import game.Grid;

public class AgentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		char[][] charGrid = new char[10][10];
		int nB, nW;
		nB = 15;
		nW = 15;
		
		if(args.length > 0){
			if(args[0] == "empty"){
				nB = 15;
				nW = 15;
				charGrid[0] = ("____").toCharArray();
				charGrid[1] = ("____").toCharArray();
				charGrid[2] = ("____").toCharArray();
				charGrid[3] = ("____").toCharArray();
				charGrid[4] = ("___").toCharArray();
				charGrid[5] = ("___").toCharArray();
				charGrid[6] = ("___").toCharArray();
				charGrid[7] = ("__").toCharArray();
				charGrid[8] = ("__").toCharArray();
				charGrid[9] = ("_").toCharArray();
			}
			
			if(args[0].equals("2")){
				nB = 15;
				nW = 15;
				charGrid[0] = ("bbbw").toCharArray();
				charGrid[1] = ("wwwb").toCharArray();
				charGrid[2] = ("__wb").toCharArray();
				charGrid[3] = ("____").toCharArray();
				charGrid[4] = ("__w").toCharArray();
				charGrid[5] = ("__b").toCharArray();
				charGrid[6] = ("___").toCharArray();
				charGrid[7] = ("__").toCharArray();
				charGrid[8] = ("__").toCharArray();
				charGrid[9] = ("_").toCharArray();
			}
		}
		
		else if(args.length == 0){
			nB = 14;
			nW = 13;
			charGrid[3] = ("bbbw").toCharArray();
			charGrid[2] = ("wbwb").toCharArray();
			charGrid[1] = ("wwbw").toCharArray();
			charGrid[0] = ("bwwb").toCharArray();
			charGrid[6] = ("___").toCharArray();
			charGrid[5] = ("bb_").toCharArray();
			charGrid[4] = ("_b_").toCharArray();
			charGrid[8] = ("__").toCharArray();
			charGrid[7] = ("__").toCharArray();
			charGrid[9] = ("_").toCharArray();
		}
		
		Grid grid = new Grid(charGrid, 'b', nB, nW);
		
		DrPylos dr = new DrPylos('b');
		PylosState testState = dr.makeState(grid);
		
		testState.printStateInfo();
		
		ListIterator<ActionImp> it = testState.getPossibleRemoves().listIterator();
		System.out.println("removes:");
		while(it.hasNext()){
			it.next().printAction();
		}
		it = testState.getPossibleRemoves().listIterator();
		
		testState.update(it.next());
		
		it = testState.getPossibleRemoves().listIterator();
		System.out.println("removes:");
		while(it.hasNext()){
			it.next().printAction();
		}
		
		System.out.println("random move:");
		dr.getAction(grid).printAction();

	}

}
