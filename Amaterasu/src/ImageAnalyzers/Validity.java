/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageAnalyzers;

import Core.Actuator;
import Core.Gate;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public class Validity {
          
          public void process(BufferedImage bi) throws AWTException
          {
                 //   ImageManipulator.show(bi, 0);
                    int[] pixel = new int[3];
                    float[] hsv = new float[3];
                    float mix = 0;
                    float vectorIdentity;
                       for (int i = 0; i < Actuator.bactionWidth*Actuator.bactionHeight; i++) {

                              bi.getRaster().getPixel(i % Actuator.bactionWidth, i / Actuator.bactionWidth, pixel);
                              Color.RGBtoHSB(pixel[0],pixel[1],pixel[2], hsv);
                              mix += hsv[0]+hsv[1]+hsv[2];
                              
                    }
                      // System.out.println(mix);
                       vectorIdentity = mix;
                       
                       if (vectorIdentity == 367.06567f)
                       {
                                 // normal
                                // System.out.println("normal");
                                 
                       }
                       else if (vectorIdentity == 421.99738f)
                       {
                                 Robot robot =  new Robot();
                                 // wrong lock
                                 //System.out.println("lock"); // need to press Y 
                                        robot.keyPress(KeyEvent.VK_Y);       
                                                  try {Thread.sleep(15);   } catch (InterruptedException ex) {}
                                        robot.keyRelease(KeyEvent.VK_Y);  
                                        Gate.continueValidity = true;
                       }
                       else
                       {
                                 // wrong view
                                 Robot robot = new Robot();
                                 
                                // System.out.println("abnormal"); // need to press I 
                                        robot.keyPress(KeyEvent.VK_I);       
                                                  try {Thread.sleep(15);   } catch (InterruptedException ex) {}
                                        robot.keyRelease(KeyEvent.VK_I);  
                                        Gate.continueValidity = true;
                       }
          }
          
}
