/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

import Core.Actuator;
import java.awt.Point;

/**
 *
 * @author James
 */
public class PointValue {
          
          Point p;
          int value;
          
          
          int finalX = 60; // final risk map
          int finalY = 60;

          public PointValue(Point p, int value) {
                              
                    this.p = new Point(p);
                    this.value = value;
          }

          public int getValue() {
                    return value;
          }
          
          public int getX()
          {
                    return (int)p.getX();
          }
          
          public int getY()
          {
                    return (int)p.getY();
          }
          
          
}
