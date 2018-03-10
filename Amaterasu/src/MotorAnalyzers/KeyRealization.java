/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MotorAnalyzers;

import Concepts.KeyAction;
import Concepts.SpecialKeyAction;
import Core.Actuator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class KeyRealization extends Thread{
          
          KeyAction k = null;
          
          @Override
          public void run()
          {
                    try {
                              useEvent(k);
                    } catch (InterruptedException ex) {
                              Logger.getLogger(KeyRealization.class.getName()).log(Level.SEVERE, null, ex);
                    }
          }
          
          public KeyRealization(KeyAction k)
          {
                    this.k= k;
          }
          
          
          private static void useEvent(KeyAction k) throws InterruptedException
          {                    
                    if (k != null) 
                    {
                              if (k.getClass().equals(new SpecialKeyAction(0,0,0,0).getClass()))
                              {
                                        SpecialKeyAction sk = (SpecialKeyAction)k;                                        
                                        Thread.sleep(k.getDelay()*10);  // maybe reduce this
                                        pressSpecialKey(k.getKey(),sk.getKeyInside(),k.getDuration());
                              }
                              else
                              {                        
                                        Thread.sleep(k.getDelay()*10);      // maybe reduce this
                                        pressKey(k.getKey(),k.getDuration());
                              }
                    }                    
          }     
          
          private static void pressKey(int value,int duration) throws InterruptedException
          {
                    Actuator.pressKey(value,duration);
          }
          
          private static void pressSpecialKey(int valueOutside,int valueInside,int duration) throws InterruptedException
          {
                    Actuator.pressSpecialKey(valueOutside,valueInside,duration);
          }
          
}
