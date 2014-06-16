package player;

import java.util.LinkedList;
import java.util.ListIterator;

import interfaces.Action;

public class ActionImp implements Action{
	
	private int i, j, x, y;
	
	private ActionImp remove;
	
	
	public ActionImp(int i, int j, int x, int y){
		
		//from
		this.i = i; // -1 for removal from reserve (i.e. not being taken from board)
		this.j = j; //
		
		//to
		this.x = x; // -1 for removal from board (i.e. not being placed on board)
		this.y = y; //
		
		remove = null;
	}
	
	//Constructor for move with more parts (i.e. ball remove action). Similar to linked list.
	public ActionImp(int i, int j, int x, int y, ActionImp remove){
		
		//from
		this.i = i; // -1 for removal from reserve (i.e. not being taken from board)
		this.j = j; //
		
		//to
		this.x = x; // -1 for removal from board (i.e. not being placed on board)
		this.y = y; //
		
		this.remove = remove;
		
	}

	public double getCost() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getFromX(){
		return i;
	}
	
	public int getFromY(){
		return j;
	}
	
	public int getToX(){
		return x;
	}
	
	public int getToY(){
		return y;
	}
	
	public boolean hasRemove(){
		return (remove != null);
	}
	
	public ActionImp getRemove(){
		if(remove == null){
			throw new RuntimeException("getRemove called on a Action without remove turn");
		}
		return remove;
	}
	
	public void setRemove(ActionImp action){
		remove = action;
	}
	
	public ActionImp clone(){
		ActionImp removeCopy;
		if(remove != null){
			removeCopy = remove.clone();
		}else removeCopy = null;
		return new ActionImp(i,j,x,y, removeCopy);
	}

	public void printAction(){
		System.out.print("From: "+i+", "+j+"	To: "+x+", "+y);
		if(remove != null) {
			System.out.print("-> remove:");
			remove.printAction();
		}else System.out.println();
	}
}
