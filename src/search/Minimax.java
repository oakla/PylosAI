package search;

import game.*;

import interfaces.Action;
import interfaces.NodeInfo;

import java.util.*;

import player.ActionImp;




/**
 * This file contains a template for getting started writing a minimax 
 * search algorithm. Before editing the class it should first be
 * written to a file called Minimax.java.
 * <p>
 * It requires an application specific NodeInfo object which tailors the
 * search to the particular application - see the NodeInfo documentation
 * for more details.
 * <p>
 * Note that the algorithm is recursive, with maxValue calling minValue,
 * and vice versa.
 *
 * @author Cara MacNish
 */

public class Minimax {

  NodeInfo nodeInfo;
  ArrayList<Node> visited;

  public Minimax (NodeInfo nodeInfo) {
    this.nodeInfo = nodeInfo;
    visited = new ArrayList<Node>();
  }

  /**
   * @return the highest value Max can achieve at this node with optimal play
   */
  public double maxValue (Node visit) {
    double maxSoFar = visit.utility;
    ListIterator<ActionImp> li;
    Action arc;
    Node child;
    double childValue;
    if (nodeInfo.isTerminal(visit)) {
    	return maxSoFar;
    }
    else {
      li = visit.getState().getActions().listIterator();
      while (li.hasNext()) {
    	  arc = li.next();
    	  child = (Node) visit.clone();
    	  child.update(arc);
    	  visited.add(child);
    	  
    	  childValue = nodeInfo.utility(child);
    	  if(childValue > maxSoFar){
    		  maxSoFar = childValue;
    	  }
    	  minValue(child);
      }
      return maxSoFar;
    }
  }

  /**
   * @return the lowest value Min can achieve at this node with optimal play
   */
  public double minValue (Node visit) {
    double minSoFar = visit.utility;
    ListIterator<ActionImp> li;
    Action arc;
    Node child;
    double childValue;
    if (nodeInfo.isTerminal(visit)) {
    	return minSoFar;
    }
    else {
      li = visit.getState().getActions().listIterator();
      while (li.hasNext()) {
    	  arc = li.next();
    	  child = (Node) visit.clone();
    	  child.update(arc);
    	  visited.add(child);
    	  
    	  childValue = nodeInfo.utility(child);
    	  child.setUtility(childValue);
    	  if(childValue < minSoFar){
    		  minSoFar = childValue;
    	  }
    	  maxValue(child);
      }
      return minSoFar;
    }
  }

}
