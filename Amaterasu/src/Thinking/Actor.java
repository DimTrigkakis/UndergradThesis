/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.Structs;
import MotorAnalyzers.MotionActor;
import Core.Actuator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class Actor extends Thread {
          
          @Override
          public void run()
          {                    
                    try {
                              Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                              Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    while(true)
                    {
                              try {
                                        Thread.sleep(500);
                              } catch (InterruptedException ex) {
                                        Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
                              }
                              
                              selectRandomAction();
                    }
          }
          
          private void selectRandomAction()
          {
                    Random r = new Random();
                    if (r.nextBoolean()) // mouse input
                    {
                              if (r.nextBoolean()) // click on minimap to move
                                        MotionActor.clickView(new Random().nextInt(220),new Random().nextInt(220),true);   
                              else // just move the mouse somewhere
                                        MotionActor.moveMouse(r.nextInt(Actuator.viewWidth),r.nextInt(Actuator.viewHeight),false);                                                            
                    }
                    else // keyboard input
                    {
                              if (r.nextBoolean()) // move the view in random direction
                                        MotionActor.moveView(Structs.direction[r.nextInt(4)]);            
                              else
                                        MotionActor.useAction("Q");    
                    }        
                    
          }
}
