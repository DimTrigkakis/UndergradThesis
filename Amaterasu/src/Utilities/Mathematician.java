/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.Point;

/**
 *
 * @author James
 */
public class Mathematician {
          
          public static int vectorDistance(int[] a, int[] b)
          {
                    int distance = 0;
                    for (int i=0;i<a.length;i++)
                              distance += Math.abs(a[i] - b[i]);
                    
                    return distance;
          }          
          
          // hsv are the three dimensions, with each gradient compared for distance
          // maybe apply different weights to distance? 1.5 1 0.5?
          public static int vectorDistance3(int[][] a, int[][] b)
          {
                    int distance = 0;
                    for (int j=0;j<=2;j++)
                    for (int i=0;i<a[j].length;i++)
                    {
                              
                              distance += (int)(Math.abs(a[j][i] - b[j][i]));
                    }
                    
                    return distance;
          }       
          public static int vectorDistance2(Point a, Point b)
          {
                    int distance = (int) Math.round(Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)));
                    
                    return distance;
          }        
          
          public static int vectorDistance2(int[][] a, int[][] b)
          {
                    int distance = 0;
                    for (int j=0;j<=2;j++)
                    for (int i=0;i<a[j].length;i++)
                    {                              
                              distance += (int)(Math.abs(a[j][i] - b[j][i]));
                    }
                    
                    return distance;
          }          
          
          
          public static boolean valid(long x, long y, long width, long height) {
                    if (x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) {
                              return true;
                    }
                    return false;
          } // returns true if the values are withing the proper ranges
                 
          
          public static boolean valid(long x, long length) {
                    if (x >= 0 && x <= length - 1 ){
                              return true;
                    }
                    return false;
          } // returns true if the values are withing the proper ranges
         
}
