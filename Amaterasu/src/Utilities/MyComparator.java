/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Core.Actuator;
import java.awt.Point;
import java.util.Comparator;

/**
 *
 * @author James
 */
public class MyComparator implements Comparator<Point>{
      
          static int width = Actuator.viewWidth;
          @Override
         public int compare(Point p1, Point p2){
                   
                   return (int)((p1.getX() - p2.getX()) %width);
                   
         }
 }

