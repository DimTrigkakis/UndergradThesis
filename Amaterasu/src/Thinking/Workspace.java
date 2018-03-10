/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.Person;
import Concepts.Skeleton;
import State.ViewState;
import Workspace.Elements.Entity;
import java.util.ArrayList;

/**
 *
 * @author James
 */
class Workspace {
          
          static ArrayList<Entity> entities = new ArrayList<>();
          
          public static void initiateWorkspace()
          {
                    entities.clear();
                    for (Skeleton s : ViewState.FinalCharacters)
                    {
                        if (s.isEnemy())
                              entities.add(new Entity("enemy character",ViewState.FinalCharacters.indexOf(s)));
                        else      
                              entities.add(new Entity("ally character",ViewState.FinalCharacters.indexOf(s)));
                        
                    }
                    
                    for (Skeleton s : ViewState.EnemyMinions)
                    {
                              entities.add(new Entity("enemy minion",ViewState.EnemyMinions.indexOf(s)));
                              
                    }
                    
                    for (Skeleton s : ViewState.AllyMinions)
                    {
                              entities.add(new Entity("ally minion",ViewState.AllyMinions.indexOf(s)));
                    }
                    
          }
          
          public static void resetWorkspace()
          {
                    entities.clear();
          }

          public static void print()
          {
                    for (Entity e : entities)
                              e.print();
          }
          
          
}
