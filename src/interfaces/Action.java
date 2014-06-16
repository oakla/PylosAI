package interfaces;

import java.util.LinkedList;

import player.ActionImp;

public interface Action {
	
	public double getCost();
	
	public int getFromX();
	
	public int getFromY();
	
	public int getToX();
	
	public int getToY();
	
	public boolean hasRemove();
	
	public ActionImp getRemove();
}
