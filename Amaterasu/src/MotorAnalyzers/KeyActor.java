/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MotorAnalyzers;

import Concepts.KeyAction;
import Concepts.MouseAction;
import Concepts.SpecialKeyAction;
import Core.Actuator;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class KeyActor extends Thread{
          
          private static ArrayList<KeyAction> keyActions = new ArrayList<>();
              
          @Override
          public void run()
          {
                    try {
                              while(true)
                              process();
                    } catch (InterruptedException ex) {
                              Logger.getLogger(MotorAnalyzers.MouseActor.class.getName()).log(Level.SEVERE, null, ex);
                    }
          }
          
          public static void addEvent(int delay, int duration,int key)
          {
                    keyActions.add(new KeyAction(delay,duration,key));
          }
          
          public static void addEvent(KeyAction k)
          {
                    keyActions.add(k);
          }
          
          public static void addSpecialEvent(KeyAction outside, int keyInside)
          {
                    keyActions.add(new SpecialKeyAction(outside.getDelay(),outside.getDuration(),outside.getKey(),keyInside));
          }
          
          public static void addSpecialEvent(int delay, int duration,int key, int keyInside)
          {
                    keyActions.add(new SpecialKeyAction(delay,duration,key,keyInside));
          }
          
          private static void process() throws InterruptedException
          {
                    for (int i=0;i<keyActions.size();i++)
                    {
                              if (keyActions.get(i).getClass().equals(new SpecialKeyAction(0,0,0,0).getClass()))
                              {
                                        new KeyRealization(new SpecialKeyAction((SpecialKeyAction)keyActions.get(i))).start();  
                              }
                              else
                              {                                        
                                        new KeyRealization(new KeyAction(keyActions.get(i))).start();  
                              }
                                      
                              keyActions.remove(i);
                    }
                    
                    Thread.sleep(5);
          }
               
          
}
