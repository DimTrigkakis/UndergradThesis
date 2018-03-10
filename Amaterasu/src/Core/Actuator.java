/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import ImageAnalyzers.ImageManipulator;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class Actuator extends Thread{
          
          static Robot robot;
          public static boolean lowerResolution = false;          
          String name;
          
          public static int XstartScreen = 0;
          public static int YstartScreen = 0;
          
          public static void positionAdjust()
          {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    double width = screenSize.getWidth();
                    double height = screenSize.getHeight();                    
                              
                    XstartScreen = (int) (width/2 - viewWidth/2);                             
                    YstartScreen = (int) (height/2 - viewHeight/2 + 10);     
                    
          }
          
          public Actuator(String name)
          {
                    this.name = name;
          }
          
          @Override
          public void run()
          {
                    switch(name)
                    {
                              case "view":
                    
                                        robot.keyPress(KeyEvent.VK_I);          
                                        try {Thread.sleep(15);   } catch (InterruptedException ex) {}
                                        robot.keyRelease(KeyEvent.VK_I);                                  
                                        Gate.viewImage = Actuator.getImage(4);     
                                        robot.keyPress(KeyEvent.VK_I);       
                                                  try {Thread.sleep(15);   } catch (InterruptedException ex) {}
                                        robot.keyRelease(KeyEvent.VK_I);  
                                        
                                        break;
                              case "map":
                                        Gate.mapImage = Actuator.getImage(2);
                                        break;
                              case "validity":
                                        Gate.validImage = Actuator.getImage(9);
                                        break;
                            /*  case "bar":
                                        Gate.barImage = Actuator.getImage(7);
                                        break;*/
                              case "action":
                                        Gate.actionImage = Actuator.getImage(6);
                                        break;
                              case "money":
                                        Gate.moneyImage = Actuator.getImage(8);
                                        break;
                              case "chat":
                                        Gate.chatImage = Actuator.getImage(5);
                                        break;
                    }
          }

          public static boolean hasLowerResolution() {
                    return lowerResolution;
          }
          
          public static void init() throws AWTException // this is necessary, since new Robot() throws an exception
          {
                    robot = new Robot();
                   // lowerResolution();
                    positionAdjust();
                    fixResolution();
                    
          }
          
          public static Point getMouse()
          {
                    Point a = new Point(MouseInfo.getPointerInfo().getLocation());
                    return a;
          }
          
          public static void pressSpecialKey(int valueOutside, int valueInside,int duration) throws InterruptedException
          {                   
                    if (duration > 100)
                              duration = 100;
                    
                    robot.keyPress(valueOutside);
                    Thread.sleep(30);
                    robot.keyPress(valueInside);
                    Thread.sleep(duration*3);
                    robot.keyRelease(valueInside);
                    robot.keyRelease(valueOutside);
          }
          
          public static void pressKey(int value,int duration) throws InterruptedException
          {
                    if (duration > 100)
                              duration = 100;
                    
                    robot.keyPress(value);
                    Thread.sleep(duration*3);
                    robot.keyRelease(value);
          }
                    
                    
          public static void moveMouse(Point p)
          {                        
                    Point mappedPoint = new Point((int)(p.getX()),(int)(p.getY()));
                    robot.mouseMove((int)mappedPoint.getX(),(int)mappedPoint.getY()); // maybe it needs a transformation in different resolution
          }
          
          public static void rClickMouse() throws InterruptedException
          {
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
          }
          
          public static void lClickMouse() throws InterruptedException
          {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
          }
          
          public static int mapWidth = 220;
          public static int mapHeight = 220;   
          public static int mapx = 1375;
          public static int mapy = 675;
          
          public static BufferedImage getImage(int index) 
          {
                              // 1 for actions
                              // 2 for map
                              // 3 for score image
                              // 4 for whole image
                    try {
                    switch(index)
                    {
                          //    case 1: return robot.createScreenCapture(new Rectangle(770,830,420,165));
                              case 2: return robot.createScreenCapture(new Rectangle(mapx,mapy,mapWidth,mapHeight)); // get the map image
                            //  case 3: return getScoreImage();//;robot.createScreenCapture(new Rectangle(565,370,800,500));
                  //;robot.createScreenCapture(new Rectangle(565,370,800,500));
                              case 4: return getWholeImage(); // get the full view
                              case 5: return getChatImage(); // do we need to chat
                              case 6: return getActionImage(); // do we need to level up actions
                            //  case 7: return getAction2Image();
                              case 8: return getMoneyImage(); // get how much money we have
                              case 9: return getImageState(); // useful for correcting Y and I issues
                              default: return null;
                    }      
                    } catch (InterruptedException ex) {
                              Logger.getLogger(Actuator.class.getName()).log(Level.SEVERE, null, ex);
                              return null;
                    }                
                    
          }
          
          // button actions need to be performed if the buttons are pressed or nonexistent
          public static int bactionHeight = 20;
          public static int bactionWidth = 20;
          public static int bactionx = 1560;
          public static int bactiony = 650;
          public static BufferedImage getImageState()
          {                       
                    return robot.createScreenCapture(new Rectangle(bactionx,bactiony,bactionWidth,bactionHeight));   
          }
          
          // actions
          public static int actionHeight = 40;
          public static int actionWidth = 220;
          public static int actionx = 655;
          public static int actiony = 730;
          public static BufferedImage getActionImage() throws InterruptedException
          {                                                  
                    return robot.createScreenCapture(new Rectangle(actionx,actiony,actionWidth,actionHeight));                    
          }
          
          /*
          // bars
          public static int action2Height = 33;
          public static int action2Width = 420;
          public static int action2x = 770;
          public static int action2y = 768;
          public static BufferedImage getAction2Image() throws InterruptedException
          {                                     
                    return robot.createScreenCapture(new Rectangle(action2x,action2y,action2Width,action2Height));
                   
          }*/
          
          
          // money
          public static int moneyHeight = 20;
          public static int moneyWidth = 80;
          public static int moneyx = 220;
          public static int moneyy = 870;
          public static BufferedImage getMoneyImage() throws InterruptedException
          {                  
                    return robot.createScreenCapture(new Rectangle(moneyx,moneyy,moneyWidth,moneyHeight));
                    
          }
          
          /*
          public static BufferedImage getScoreImage() throws InterruptedException
          {
                    robot.keyPress(KeyEvent.VK_TAB);                    
                    Thread.sleep(5);
                    BufferedImage bi = robot.createScreenCapture(new Rectangle(565,370,800,500));
                    Thread.sleep(5);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    
                    return bi;
          }*/
          
          // chat
          public static int chatHeight = 175/8;
          public static int chatWidth = 650;
          public static int chatx = 0;
          public static int chaty = 700;
          public static BufferedImage getChatImage() throws InterruptedException
          {                    
                    robot.keyPress(KeyEvent.VK_ENTER);       
                    Thread.sleep(15);
                    robot.keyRelease(KeyEvent.VK_ENTER);    
                    Thread.sleep(15);
                    robot.keyPress(KeyEvent.VK_ENTER);           
                    Thread.sleep(15);
                    robot.keyRelease(KeyEvent.VK_ENTER);   
                    BufferedImage bi = robot.createScreenCapture(new Rectangle(chatx,chaty,chatWidth,chatHeight));  
                    
                    return bi;
          }
          
          // view
          public static BufferedImage getWholeImage() throws InterruptedException
          { 
                    return robot.createScreenCapture(new Rectangle(viewx,viewy,viewWidth,viewHeight));    
                                        
          }
          
          
          // whole image coordinates
          public static int viewHeight = 900;    
          public static int viewWidth = 1600;
          public static int viewx = 160;
          public static int viewy = 105;
          
          /*
          public static int bactionHeight = 20;
          public static int bactionWidth = 20;
          public static int bactionx = 1720;
          public static int bactiony = 750;*/
           
          public static void lowerResolution()
          {
                    if (lowerResolution)
                    {
                    viewWidth  = 1024;
                    viewHeight = 768;
                    
                    // map
                    mapWidth = (mapWidth*1024)/1600;
                    mapHeight = (mapHeight*768)/900;
                    mapx = viewx+viewWidth-mapWidth;
                    mapy = viewy+viewHeight-mapHeight;
                    
                    // baction
                     bactionWidth = (bactionWidth*1024)/1600;
                     bactionHeight = ( bactionHeight*768)/900;
                     bactionx = ( bactionx*1024)/1600;
                     bactiony = ( bactiony*768)/900;
                     
                     // actions
                     actionWidth = (actionWidth*1024)/1600;
                     actionHeight = ( actionHeight*768)/900;
                     actionx = ( actionx*1024)/1600;
                     actiony = ( actiony*768)/900;
                     
                     // money
                    
                     moneyWidth = (moneyWidth*1024)/1600;
                     moneyHeight = ( moneyHeight*768)/900;
                     moneyx = (moneyx*1024)/1600;
                     moneyy = (moneyy*768)/900;
                     
                     // chat
                     
                     chatWidth = (chatWidth*1024)/1600;
                     chatHeight = ( chatHeight*768)/900;
                     chatx = (chatx*1024)/1600;
                     chaty = (chaty*768)/900;
                    }
                    
          }
          
          public static void fixResolution()
          {         
                    // view
                    viewx = XstartScreen;
                    viewy = YstartScreen;
                    
                    mapx += XstartScreen;
                    mapy += YstartScreen;
                    
                    bactionx += XstartScreen;
                    bactiony += YstartScreen;
                    
                    actionx += XstartScreen;
                    actiony += YstartScreen;
                    
                    
                    moneyx += XstartScreen;
                    moneyy += YstartScreen;
                    
                    chatx += XstartScreen;
                    chaty += YstartScreen;
                    
          }
          
          
          
          
          
          
}
