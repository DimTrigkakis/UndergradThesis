/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts;

/**
 *
 * @author James
 */
public class KeyAction {

          int delay;
          int duration;
          int key;

          public KeyAction(int delay, int duration,int key) {
                    this.delay = delay;
                    this.duration = duration;
                    this.key = key;
          }
          
          public KeyAction(KeyAction k)
          {
                    this.delay = k.delay;
                    this.duration = k.duration;
                    this.key = k.key;
          }

          public int getDelay() {
                    return delay;
          }


          public int getDuration() {
                    return duration;
          }

          public int getKey() {
                    return key;
          }
}
