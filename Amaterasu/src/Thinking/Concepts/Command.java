/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking.Concepts;

import MotorAnalyzers.MotionActor;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class Command {
          
          String name;
          int type; // move mouse = 1, right click at spot  = 2, press key = 3, press special key combination = 4
          int priority;
          Point p;
          int parameter; //3 -  0 = Q  
          // 4 - 0 to 3 = control + Q, control + W, control + E, control + R

          public Command(String name, int type, int priority, Point p, int parameter) {
                    this.name = name;
                    this.type = type;
                    this.priority = priority;
                    this.p = p;
                    this.parameter = parameter;
          }
          
          public void execute()
          {
                    System.out.println("executed");
                    if (type == 1)
                    {                       
                              MotionActor.moveMouse((int)p.getX(),(int)p.getY(), false);
                              System.out.println("i'm moving towards a place"+p.getX()+" "+p.getY());
                    }
                    if (type == 2)
                    {                       
                              MotionActor.moveMouse((int)p.getX(),(int)p.getY(), true);
                    }if (type == 3)
                    {                     
                              if (parameter == 0)
                              {
                                        System.out.println("HIT Q");
                                        MotionActor.moveMouse((int)p.getX(),(int)p.getY(), false);
                                        
                                        
                                        MotionActor.useAction("Q");
                              }
                    }
                    
                    if (type == 4)
                    {
                              if (parameter == 0)
                              {                        
                                        MotionActor.useAction("cQ");
                              }if (parameter == 1)
                              {                                        
                                        MotionActor.useAction("cW");
                              }if (parameter == 2)
                              {                                        
                                        MotionActor.useAction("cE");
                              }if (parameter == 3)
                              {                                        
                                        MotionActor.useAction("cR");
                              }
                    }
          }

          public String getName() {
                    return name;
          }

          public int getType() {
                    return type;
          }

          public int getPriority() {
                    return priority;
          }

          public Point getPoint() {
                    return p;
          }

          public int getParameter() {
                    return parameter;
          }
          
          
}
