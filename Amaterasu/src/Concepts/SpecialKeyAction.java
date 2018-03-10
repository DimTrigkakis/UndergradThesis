/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts;

/**
 *
 * @author James
 */
public class SpecialKeyAction extends KeyAction{
          
          int keyInside;
          
          public SpecialKeyAction(int delay,int duration, int key,int keyInside)
          {
                    super(delay,duration,key);
                    this.keyInside = keyInside;
          } 
          
          public SpecialKeyAction(SpecialKeyAction s)
          {
                    super(s.delay,s.duration,s.key);
                    this.keyInside = s.keyInside;
          }
          
          public int getKeyInside()                  
          {
                    return keyInside;
          }
          
}
