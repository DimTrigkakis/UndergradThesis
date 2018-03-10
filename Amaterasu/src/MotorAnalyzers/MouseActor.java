/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MotorAnalyzers;

import Concepts.MouseAction;
import Core.Actuator;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class MouseActor extends Thread {

          private static Point lastMouse = null;
          private static ArrayList<MouseAction> mouseActions = new ArrayList<>();

          public static Point getMouse() {
                    return Actuator.getMouse();
          }
 
          // One of the most important calculations in this program, it checks for whether the user actually dislocated the mouse
          // If he did, it's the exit signal
          @Override
          public void run() {
                    try {
                              while (true) {
                                        process();


                                        if (lastMouse != null) 
                                                  if (MouseActor.lastMouse.x != MouseActor.getMouse().x || MouseActor.lastMouse.y != MouseActor.getMouse().y) 
                                                  {
                                                            System.exit(136);
                                                  }
                                        



                              }
                    } catch (InterruptedException ex) {
                              Logger.getLogger(MouseActor.class.getName()).log(Level.SEVERE, null, ex);
                    }
          }

          public static void addEvent(Point p, int delay, int priority, boolean relative, int click) // 1 left click, 2 right click
          {
                    mouseActions.add(new MouseAction(p, delay, priority, relative, click));
          }

          public static void addEvent(MouseAction m) {
                    mouseActions.add(m);
          }

          private static void process() throws InterruptedException {
                    int position = -1;
                    int max = -1;
                    for (int i = 0; i < mouseActions.size(); i++) {
                              MouseAction m = mouseActions.get(i);
                              if (m.getPriority() > max) {
                                        max = m.getPriority();
                                        position = i;
                              }
                    }

                    if (position > -1) {
                              useEvent(position);
                              mouseActions.remove(position);
                    } else {
                              Thread.sleep(20);
                    }
          }

          private static void useEvent(int i) throws InterruptedException {
                    if (i != -1) {
                              MouseAction m = mouseActions.get(i);

                              Point mouse = MouseActor.getMouse();

                              if (m.isRelative()) {
                                        mouse.x += m.getP().x;
                                        mouse.y += m.getP().y;
                              } else {
                                        mouse.x = m.getP().x;
                                        mouse.y = m.getP().y;
                              }

                              MouseActor.lastMouse = new Point(mouse.x,mouse.y); // set the new coordinates of the mouse
                              
                              Thread.sleep(m.getDelay() * 10);
                              moveMouse(new Point(mouse), m.getClick());
                    }
          }

          private static void moveMouse(Point p, int click) throws InterruptedException {
                    Actuator.moveMouse(p);
                    if (click == 1) {
                              Actuator.lClickMouse();
                    }
                    if (click == 2) {
                              Actuator.rClickMouse();
                    }
          }
}
