/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import Concepts.Person;
import Core.Actuator;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author James
 */
public class MapState {
                    
          public static Point view = new Point(0,0);       
          
          // This point is made through the chat function
          // and is an interest point that should be taken into account
          // if taken into account it takes precedence for a short amount of time over
          // any other interest point
          public static String chatCommand = null;
          
          
          public static boolean[] purpleInhibitors = new boolean[3];
          public static boolean[] purpleTurrets = new boolean[11];
          public static boolean[] cyanInhibitors = new boolean[3];
          public static boolean[] cyanTurrets = new boolean[11];
          public static boolean[] ourJungle = new boolean[6];
          public static boolean[] theirJungle = new boolean[6];
          public static boolean dragon;
          public static boolean baron;
                    
          public static boolean mapAhri = false; // is she in base?
          
          
          public static ArrayList<Point> AllyMemberPositions = new ArrayList<>();
          public static ArrayList<Point> EnemyMemberPositions = new ArrayList<>();
          public static ArrayList<Person> AllyMembers = new ArrayList<>();
          public static ArrayList<Person> EnemyMembers = new ArrayList<>();
          public static ArrayList<Point> CyanMinions = new ArrayList<>();
          public static ArrayList<Point> RedMinions = new ArrayList<>();
          
          public static boolean updated = false;
          
          static int mapViewWidth = 20;
          static int mapViewHeight = 16;
          
          private static boolean insideView(Point p)
          {
                    if (p.x > view.getX() - mapViewWidth && p.x < view.getX() + mapViewWidth)
                    if (p.y > view.getY() - mapViewHeight && p.y < view.getY() + mapViewHeight)
                    {
                              return true;
                    }
                    
                    return false;
          }
          
          public static void compareViewState()
          {
                    // for characters mainly                    
                    // if u see a building in view, do this for mapState as well                                             
                    int viewx = MapState.view.x; // position of the view
                    int viewy = MapState.view.y-2;
                    int valuex = (int)(220/12f); // how the view is mapped to the view inside the map (proper view width)
                    int valuey = (int)(220/12.5f);
                    
                    
                    
                    // transform map into view coordinates (needs the perspective trick to work better)
                    if (view != null)
                    {
                              for (Person  p :AllyMembers) // we found where the character is so we need to transform to view coordinates (proper ones)
                              {
                                        if (insideView(p.getLocation()))
                                        {                 
                                                  Person inViewP = new Person(p);
                                                  
                                                  inViewP.getLocation().x = (p.getLocation().x-(view.x));
                                                  inViewP.getLocation().y = (p.getLocation().y-(view.y-valuey))-4;
                                                  
                                                  float xx = -1f*(inViewP.getLocation().x)/(13);                                        
                                                  float yy = 1f* Math.abs(inViewP.getLocation().y)/(26);
                                                  yy = 1-yy;
                                                  int modifier = 400;
                                                  int dislocation = (int) (xx*yy*modifier);   
                    
                                                  Person moddedP = new Person(inViewP);
                                                  moddedP.setP(new Point((int)((inViewP.getLocation().x+13)*((1526-73)/25f)*1f+73),
                                                          (int)((inViewP.getLocation().y/26f)*(945-93)+93))); // hardwired values for y
                                                  moddedP.getLocation().x += dislocation;                                         
                                                  
                                                  ViewState.AllyCharacters.add(moddedP);
                                                  
                                        }      
                              }
                              
                              for (Person  p :EnemyMembers) // we found where the character is so we need to transform to view coordinates (proper ones)
                              {
                                        if (insideView(p.getLocation()))
                                        {                 
                                                  Person inViewP = new Person(p);
                                                  
                                                  inViewP.getLocation().x = (p.getLocation().x-(view.x));
                                                  inViewP.getLocation().y = (p.getLocation().y-(view.y-valuey))-4;
                                                  
                                                  
                                                  float xx = -1f*(inViewP.getLocation().x)/(13);                                        
                                                  float yy = 1f* Math.abs(inViewP.getLocation().y)/(26);
                                                  yy = 1-yy;
                                                  int modifier = 400;
                                                  int dislocation = (int) (xx*yy*modifier);   
                    
                                                  Person moddedP = new Person(inViewP);
                                                  moddedP.setP(new Point((int)((inViewP.getLocation().x+13)*((1526-73)/25f)*1f+73),
                                                          (int)((inViewP.getLocation().y/26f)*(945-93)+93))); // hardwired values for y
                                                  moddedP.getLocation().x += dislocation;                                         
                                                  
                                                  ViewState.EnemyCharacters.add(moddedP);
                                                  
                                        }      
                              }
                              
                              
                              
                    }
                    
                    for (Person p : AllyMembers)
                    {
                              if (p.getName().equals("Ahri"))
                              {
                                        mapAhri = true; // ahri in base or in map?
                              }
                    }
                    
                    
          }
          
          public static void resetState()
          {
                     purpleInhibitors = new boolean[3];
                    purpleTurrets = new boolean[11];
                    cyanInhibitors = new boolean[3];
                    cyanTurrets = new boolean[11];                    
                    
                    ourJungle = new boolean[6];
                    theirJungle = new boolean[6];
                    
                    dragon = false;
                    baron = false;
          
                    AllyMembers = new ArrayList<>();
                    EnemyMembers = new ArrayList<>();          
                    AllyMemberPositions = new ArrayList<>();
                    EnemyMemberPositions = new ArrayList<>();
                    
                    EnemyMembers = new ArrayList<>();
                    CyanMinions = new ArrayList<>();
                    RedMinions = new ArrayList<>();
                    
                    updated = false;
                    
                    mapAhri = false;
          }
          
}
