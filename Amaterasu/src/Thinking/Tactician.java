/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.BoardCharacter;
import Concepts.Skeleton;
import MotorAnalyzers.MotionActor;
import State.MapState;
import State.ViewState;
import Thinking.Concepts.Command;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author James
 */
public class Tactician {
          
          boolean debug,show;
          public static boolean interest = false;
          
          float[] perceptions = {0f}; // confident-scared
          
          
          public Tactician(boolean showStrategy, boolean debugStrategy) {

                    debug = debugStrategy;
                    show = showStrategy;

          }
          
          Point self = null;
          boolean proximity = false;        
          
                    
          public void process() throws InterruptedException {       
                    
                    // Targets for the tactician are
                    // 1. csing 2. managing opponent characters 3. managing opponent buildings 4. managing jungle 5. exploration
                    
                    // CSing first-combined with jungling
                    // IF you see any point that is important, try to hit it
                    
                    interest  = false; // remove at end
                    
                    // Action abstractions - attack a player / attack a minion wave / retreat / dodge
                    // Perception abstractions - enemy close, enemy far, minions close, turret close, cooldowns low, hp high, hp low
                    
                    
                    
                    
                    
                                          
                    Point point = null;
                    int minimum = 30;
                    int maximum = 60;
                    
                    for (Skeleton e : ViewState.EnemyMinions)
                    {
                              if (e.getHp() < minimum)                              
                              {
                                        
                                        minimum = e.getHp();
                                        point = e.getLocation();
                              }
                    }
                    
                    Random r = new Random();
                    
                    if (point == null)
                    {
                               for (Skeleton e : ViewState.EnemyMinions)
                              {
                              if (e.getHp() < maximum)                              
                              {
                                        
                                        maximum = e.getHp();
                                        point = e.getLocation();
                                        int dx = r.nextInt(30)+30;
                                        
                                        if (r.nextBoolean())
                                                  dx = -dx;
                                        
                                        int dy = r.nextInt(30)+30;
                                        if (r.nextBoolean())                                                  
                                                  dy= -dy;
                                        
                                        point.translate(dx, dy);
                              }
                              }
                    }
                                                 
                    if (point != null){                                               
                              Commander.addCommand(new Command("Right click minion",2,5,point,0));
                    }
                    
                    Commander.executeCommands();
                    
                    
          }

          
}
