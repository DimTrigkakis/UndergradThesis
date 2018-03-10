/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts;

import java.awt.Point;

/**
 *
 * @author James
 */
public class MouseAction {
          
          Point p;
          int delay;
          int priority;
          boolean relative;
          int click; // 0 = no click, 1 = left clik, 2 = right click
          public MouseAction(Point p, int delay, int priority, boolean relative, int click) {
                    this.p = p;
                    this.priority = priority;
                    this.delay = delay;
                    this.relative = relative;
                    this.click  = click;
          }

          public boolean isRelative() {
                    return relative;
          }
          

          public Point getP() {
                    return p;
          }

          public int getPriority() {
                    return priority;
          }

          public int getDelay() {
                    return delay;
          }

          public int getClick() {
                    return click;
          }          
}
