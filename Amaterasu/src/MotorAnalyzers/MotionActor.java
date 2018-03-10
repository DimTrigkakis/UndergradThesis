/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MotorAnalyzers;

import Core.Actuator;
import java.awt.Point;
import java.awt.event.KeyEvent;

/**
 *
 * @author James
 */
public class MotionActor {
          
          final static int mouseDelay = 5;
          final static int keyboardDelay =20;
          
          public static void moveMouse(int x, int y, boolean rightClick) // right click, or no click
          {                    
                    if (rightClick)
                    MouseActor.addEvent(new Point(x+Actuator.viewx, y+Actuator.viewy),mouseDelay,100,false,2);
                    else
                    MouseActor.addEvent(new Point(x+Actuator.viewx, y+Actuator.viewy),mouseDelay,100,false,0);
          }
          
          
          public static void moveMouseLeft(int x, int y, boolean leftClick) // left click, or no click
          {                    
                    if (leftClick)
                    MouseActor.addEvent(new Point(x+Actuator.viewx, y+Actuator.viewy),mouseDelay,100,false,1);
                    else
                    MouseActor.addEvent(new Point(x+Actuator.viewx, y+Actuator.viewy),mouseDelay,100,false,0);
          }
          
          public static void clickView(int x, int y, boolean rightClick) // right click or left click
          {
                    Point mouse = new Point(MouseActor.getMouse());
                    
                    if (rightClick)
                    MouseActor.addEvent(new Point(x+Actuator.mapx, y+Actuator.mapy),mouseDelay,100,false,2);
                    else
                    MouseActor.addEvent(new Point(x+Actuator.mapx, y+Actuator.mapy),mouseDelay,100,false,1);
                    
                    MouseActor.addEvent(mouse,mouseDelay,99,false,0);
          }
          
          public static void moveView(String direction)
          {
                    switch(direction) // unfortunately, these work only for the numpad arrow keys      
                    {  
                              case "up":
                                        KeyActor.addEvent(keyboardDelay,50,KeyEvent.VK_UP);
                                        break;
                              case "down":
                                        KeyActor.addEvent(keyboardDelay,50,KeyEvent.VK_DOWN);
                                        break;
                              case "right":
                                        KeyActor.addEvent(keyboardDelay,50,KeyEvent.VK_RIGHT);
                                        break;
                              case "left":
                                        KeyActor.addEvent(keyboardDelay,50,KeyEvent.VK_LEFT);
                                        break;
                    }
          }
                    
          public static void useAction(String skill)
          {
                    switch(skill)
                    {
                              case "cQ":
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_Q);
                                        break;
                              case "cW":
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_W);
                                        break;
                              case "cE":
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_E);
                                        break;
                              case "cR":
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_R);
                                        break;
                              case "Q":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_Q);
                                        break;
                              case "W":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_W);
                                        break;                                      
                              case "E":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_E);
                                        break;                                       
                              case "R":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_R);
                                        break;                                       
                              case "D":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_D);
                                        break;                                    
                              case "F":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_F);
                                        break;
                              case "S":
                                        KeyActor.addEvent(keyboardDelay,20,KeyEvent.VK_S);
                                        break;    
                              case "B":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_B);
                                        break;        
                              case "Y":                              
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_Y);
                                        KeyActor.addEvent(keyboardDelay+20,10,KeyEvent.VK_Y);                                        
                                        break;                                      
                              case "P":
                                        KeyActor.addEvent(keyboardDelay,40,KeyEvent.VK_P);
                                        break;                      
                    }
          }
          
          public static void useItem(String item)
          {
                    switch(item)
                    {
                              case "1":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_1);
                                        break;
                              case "2":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_2);
                                        break;                                      
                              case "3":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_3);
                                        break;                                       
                              case "4":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_4);
                                        break;                                       
                              case "5":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_5);
                                        break;                                    
                              case "6":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_6);
                                        break;
                              case "7":
                                        KeyActor.addEvent(keyboardDelay,10,KeyEvent.VK_7);
                                        break;                          
                    }
          }
          
          public static void raiseLevel(String skill)
          {
                    switch(skill){                                                                       
                              case "Q":                         
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_Q);
                                        break;                                        
                              case "W":               
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_W);
                                        break;                                            
                              case "E":               
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_E);
                                        break;      
                              case "R":               
                                        KeyActor.addSpecialEvent(keyboardDelay,10,KeyEvent.VK_CONTROL,KeyEvent.VK_R);
                                        break;         
                            }
          }
}
