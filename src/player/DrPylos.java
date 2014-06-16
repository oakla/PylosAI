package player;
import java.util.ListIterator;
import java.util.Random;

import interfaces.Action;
import interfaces.Agent;
import interfaces.Percept;
import game.Grid;
import search.PylosState;




public class DrPylos {
	
	private final char color;
	
	private PylosState currentState;

	public DrPylos(char color){
		this.color = color;
	}
	public ActionImp getAction(Percept arg0) {
		ActionImp action;
		currentState = makeState((Grid) arg0);
		ListIterator<ActionImp> li = currentState.getActions().listIterator();
		action = li.next();
		Random r = new Random();
		while(li.hasNext() && r.nextInt(6) != 5){
			action = li.next();
		}
		return action;
	}
	
	public PylosState makeState(Grid grid){
		return new PylosState(grid);
	}

}
