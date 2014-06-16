//
//  State.java
//  javaAgents
//
//  Created by Cara MacNish on 28/02/07.
//  Copyright 2007 CSSE, UWA. All rights reserved.
//

package interfaces;

import java.util.LinkedList;

import player.ActionImp;

public interface State {
    
  public void update (Action action) throws RuntimeException;
  
  public LinkedList<ActionImp> getActions ();
  
  public Object clone ();

  public char[][] getGrid();
  
}
