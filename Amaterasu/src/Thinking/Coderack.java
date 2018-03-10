/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.Person;
import Concepts.Skeleton;
import State.ViewState;
import Thinking.Concepts.Command;
import Workspace.Elements.Descriptor;
import Workspace.Elements.Entity;
import java.awt.Point;
import java.util.Random;

/**i
 *
 * @author James
 */
public class Coderack {
          
          static int[] probabilities = {0,5,5,3,0};// new int[2];
          static int size = 13;
          static final int agents = 5;
          
          
          // agent 1 cs
          // agent 2 low hp
          // agent 3 close target
          // agent 4 mark target
          // agent 5 hit target
                    
          public static void setProbability(int agent, int value)
          {
                    size +=(probabilities[agent]+value)/2 - probabilities[agent];
                    probabilities[agent]= (probabilities[agent]+value)/2;
          }
          
          public static void runCodelet() 
          {                                                           
                    int tempSize = size;
                    Random r = new Random();
                    for (int i=0;i<probabilities.length;i++)
                    {
                              if (r.nextInt(tempSize)+1 <= probabilities[i])
                              {
                                        Agent(i+1);
                                        break;
                              }
                              
                              
                              tempSize -= probabilities[i];
                    }                    
          }
          
          private static void Agent(int name)
          {
                    switch(name)
                    {
                              case 1 : agentCs();
                                        break;
                              case 2 : minionFindLowHp();
                                        break;
                              case 3 : closeTarget();
                                        break;
                              case 4 : markTarget();
                                        break;
                              case 5 : hitQ();
                                        break;
                    }                                        
          }     
          
          private static void closeTarget()
          {
                    
                    for (Person  p : ViewState.AllyCharacters)
                                        System.out.println("Ally is at "+p.getLocation().x+" "+p.getLocation().y);
                    for (Person  p : ViewState.EnemyCharacters)
                                        System.out.println("Enemy is at "+p.getLocation().x+" "+p.getLocation().y);
                    for (Skeleton s : ViewState.Characters)
                                        System.out.println(s.getName()+" "+s.getLocation().x+" "+s.getLocation().y);
                    
                    Point self = new Point(0,0);
                    for (Skeleton s : ViewState.FinalCharacters)
                    {
                              if (s.getName().equals("Ahri") || s.getName().equals("X"))
                              {
                                        self = s.getLocation();
                              
                                        break;
                              }                              
                    }
                    
                    for (Entity e : Workspace.entities)
                    {
                              if (e.getName().equals("enemy character"))
                              {
                                        System.out.println("Found an enemy character");
                                        
                                        System.out.println(ViewState.FinalCharacters.get(e.getPlace()).getLocation().distance(self));
                                        System.out.println("Enemy "+ViewState.FinalCharacters.get(e.getPlace()).getLocation().x+" "+ViewState.FinalCharacters.get(e.getPlace()).getLocation().y);
                                        System.out.println("Ahri is at "+self.x+" "+self.y);
                                        
                                        Point p = new Point(ViewState.FinalCharacters.get(e.getPlace()).getLocation());
                                        System.out.println(p.x+" "+p.y);
                                        Commander.addCommand(new Command("Hit character",1,15,p,0));
                                        
                                        if (self.distance(new Point(0,0))!=0)
                                        if (ViewState.FinalCharacters.get(e.getPlace()).getLocation().distance(self) < 1000)
                                        {
                                                  System.out.println("he's really close to me");
                                                  e.addDescriptor(new Descriptor("Close"));
                                        }
                              }
                    }
          }
          
          private static void markTarget()
          {
                    
                    for (Entity e : Workspace.entities)
                    {
                              if (e.getName().equals("enemy character") && e.hasDescriptor("Close"))
                                      {
                                                System.out.println("IM MARKED");
                                                Slipnet.createActivation(0, 10);
                                                e.addDescriptor(new Descriptor("Marked"));
                                                break;
                                      }
                    }
          }
          
          private static void hitQ()
          {                    
                    for (Entity e : Workspace.entities)
                    {
                               if (e.getName().equals("enemy character") && e.hasDescriptor("Marked"))
                              {
                                        System.out.println("NOW ILL HIT Q");
                                        Point p = new Point(ViewState.FinalCharacters.get(e.getPlace()).getLocation());
                                        System.out.println(p.x+" "+p.y);
                                      //  Commander.addCommand(new Command("Hit character",3,10,p,0));
                                        break;
                              }
                              
                    }
          }
                    
          private static void agentCs()                    
          {                                             
                    for (Entity e : Workspace.entities)
                    {
                              if (e.getName().equals("enemy minion") && e.hasDescriptor("Low Hp"))
                              {
                                        Point p = new Point(ViewState.EnemyMinions.get(e.getPlace()).getLocation());
                                        Commander.addCommand(new Command("Right click minion",2,5,p,0));
                                        break;
                              }
                                                                                
                    }
          }
          
          private static void minionFindLowHp()
          {
                    for (Entity e : Workspace.entities)
                    {
                              if (e.getName().equals("enemy minion"))
                              {
                                        if (ViewState.EnemyMinions.get(e.getPlace()).getHp() < 40)
                                        {
                                                  Slipnet.createActivation(3, 10);
                                                  e.addDescriptor(new Descriptor("Low Hp"));
                                        }
                              }
                    }
                    
          }
          
}
