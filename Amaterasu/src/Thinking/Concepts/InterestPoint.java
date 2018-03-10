/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking.Concepts;

import java.awt.Point;

/**
 *
 * @author James
 */
public class InterestPoint {
          
          Point p;
          float Interest;

          public InterestPoint(Point p, float Interest) {
                    this.p = p;
                    this.Interest = Interest;
          }
          
          public InterestPoint()
          {
                    
          }
          public InterestPoint(InterestPoint ip)
          {
                    this.p = ip.getP();
                    this.Interest = ip.getInterest();
                    
          }
          

          public Point getP() {
                    return p;
          }

          public float getInterest() {
                    return Interest;
          }
          
}
