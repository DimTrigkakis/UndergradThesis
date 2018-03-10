/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

import Abstractor.LearningAbstraction.ActiveMood;
import ImageAnalyzers.ImageManipulator;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public class Mood {
          
          static float aggressive = 0; // enemy is low
          static float scared = 0; // danger by anything attacking us other than enemies
          static float greedy = 0; // greed for creeps or building damages
          static float confused = 0; // outside view
          static float interested =0;
          
          public static void showMood(ActiveMood am)
          {
                           BufferedImage bi2 = new BufferedImage(30, 120, BufferedImage.TYPE_INT_RGB);
                   for (int choice = 0;choice < 5;choice++)
                   {
                             float amount = 0;
                             Color color = null;
                             if (choice == 0)
                             {
                                       color = Color.RED;
                                       if (am == ActiveMood.AGGRESSIVE)
                                                 color = Color.PINK;
                                       amount = aggressive;
                             }
                             else if (choice == 1)
                             {
                                       color = Color.BLUE;
                                       if (am == ActiveMood.SCARED)
                                                 color = Color.CYAN;
                                       amount = scared;
                             }
                             else if (choice == 2)
                             {
                                       color = Color.GREEN;
                                       if (am == ActiveMood.GREEDY)
                                                 color = Color.LIGHT_GRAY;
                                       amount = greedy;
                             }
                             else if (choice == 3)
                             {
                                       color = Color.YELLOW;
                                       if (am == ActiveMood.CONFUSED)
                                                 color = Color.WHITE;
                                       amount = confused;
                             }
                             else if (choice == 4)
                             {
                                       color = Color.MAGENTA;
                                       if (am == ActiveMood.INTERESTED)
                                                 color = Color.ORANGE;
                                       amount = interested;
                             }
                             else if (choice ==5 )
                             {                                       
                                       color = Color.BLACK;
                                       if (am == ActiveMood.NOTHING)
                                                 color = Color.GRAY;
                                       amount = 30;
                             }
                             
                                       
                             for (int j = 0+choice*20;j < 20+choice*20; j++)  
                                       for (int i = 0; i < 30; i++)  
                                       {
                                                 int c;
                                                 if (i <= amount)
                                                   c = color.getRGB();
                                                 else
                                                   c = (Color.BLACK).getRGB();
                                                           
                                                  bi2.setRGB(i , j, c);
                                                 
                                                 
                                                 
                                                 
                                                 
                                       }
                             ImageManipulator.show(bi2, 20);
                    } 
          }
          
          public static void reset()
          {
                    aggressive = 0;
                    scared = 0;
                    greedy = 0;
                    confused = 0;
          }
                    
}
