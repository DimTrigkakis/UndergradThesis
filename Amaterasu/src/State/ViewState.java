/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import Concepts.Person;
import Concepts.Skeleton;
import ImageAnalyzers.ImageManipulator;
import Core.Actuator;
import Utilities.Mathematician;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class ViewState {
                             
          
          public static ArrayList<Skeleton> EnemyMinions = new ArrayList<>();
          public static ArrayList<Skeleton> AllyMinions = new ArrayList<>();
          public static ArrayList<Skeleton> EnemyBuildings = new ArrayList<>();
          public static ArrayList<Skeleton> AllyBuildings = new ArrayList<>();
          
          public static ArrayList<Skeleton> Characters = new ArrayList<>(); // view initial estimations   
          
          public static ArrayList<Person> EnemyCharacters = new ArrayList<>(); // information after map processing goes here for comparisons
          public static ArrayList<Person> AllyCharacters = new ArrayList<>();          
          
          public static ArrayList<Skeleton> FinalCharacters = new ArrayList<>();    
          public static ArrayList<Skeleton> PreviousFinalCharacters = new ArrayList<>();    
          
          // these are used in riskmaps, and are mapped from a perspective projection of the map onto the view          
          public static ArrayList<Point> EnemyMinionsRisk = new ArrayList<>();
          public static ArrayList<Point> EnemyBuildingsRisk = new ArrayList<>();
          public static ArrayList<Point> EnemyCharactersRisk = new ArrayList<>();  
          public static ArrayList<Point> AllyCharactersRisk = new ArrayList<>();           
          public static ArrayList<Point> AllyMinionsRisk = new ArrayList<>();
          public static ArrayList<Point> WallRisk = new ArrayList<>();    
          public static ArrayList<Point> AllyBuildingsRisk = new ArrayList<>();
          
          public static boolean updated = false;
          
          public static int level = 1;
          public static boolean dead = false;
          public static int hp = 0;
          public static int mp = 0;
          
          public static void addLevel()
          {
                    level++;
                    if (level > 18)
                              level = 18;
          }
          
          public static void updateState()
          {                                      
                    // translocate the characters from view to proper coordinates
                    // you only have the view coordinates and want to transform to new view coordinates based on the perspective
                    // transformation
                                          
                    
                     for (Skeleton s : Characters) 
                     {
                              if (s.getName().equals("Ahri"))
                              {
                                        
                                                  FinalCharacters.add(s);
                                                  Characters.remove(s);
                                                  for (Person p : AllyCharacters) 
                                                  {
                                                            if (p.getName().equals("Ahri"))
                                                            {
                                                                      AllyCharacters.remove(p);
                                                                      break;
                                                            }
                                                  }
                                                  
                                                  break;
                              }
                     }
                                        
                   // view information
                     for (Skeleton s : Characters) 
                    {
                              boolean used = false;
                                                  
                              for (Person p : AllyCharacters) // map information
                              {
                                        //System.out.println("Character is at "+s.getLocation()+" but in map he's at "+p.getLocation());
                                        if (p.getLocation().distance(s.getLocation()) < 300 && !s.isEnemy())
                                        {
                                            
                                                  // keep your position but change the names
                                                  s.setName(p.getName());
                                                  FinalCharacters.add(s);
                                                  AllyCharacters.remove(p);
                                                  used = true;
                                                  break;
                                        }
                              }
                              
                              
                              for (Person p : EnemyCharacters)
                              {                          
                                        if  (p.getLocation().distance(s.getLocation()) < 300 && s.isEnemy())
                                        {
                                                  s.setName(p.getName());
                                                  FinalCharacters.add(s);                                                  
                                                  EnemyCharacters.remove(p);
                                                  
                                                  used = true;
                                                  break;
                                        }
                              } 
                              
                              if (!used)
                                        FinalCharacters.add(s);
                    }
                    //*/
                    for (Skeleton s : FinalCharacters)
                    {
                              for (Skeleton s2 : PreviousFinalCharacters)
                              {
                                        int x = (int)s.getLocation().getX();
                                        int y = (int)s.getLocation().getY();
                                        int xx = (int)s.getLocation().getX();
                                        int yy = (int)s.getLocation().getY();
                                        
                                        if (s2.getName().equals(s.getName()))
                                                  s.setVelocityDirection(new Point(x-(xx-x),y-(yy-y)));
                                        
                                        
                              }
                    }
                              
                              
                    /*
                    for (Skeleton s2 : PreviousFinalCharacters)
                    {                              
                              boolean found = false;
                              for (Skeleton s : FinalCharacters)
                              {
                                        if (s.getName().equals(s2.getName()))
                                                  found = true;
                              }
                              
                              if (!found)
                              {
                                        if ((new Random()).nextBoolean())
                                        FinalCharacters.add(s2);
                              }
                    }*/
                    
                    //draw();
                    updateRisk();
                    
          }
          
          public static void updateRisk()
          {     
                    
                    for (Skeleton s : FinalCharacters)
                   {
                             int x = (int)(s.getLocation().x*60f/1600f) ;
                             int y =(int)(s.getLocation().y*60f/900f);                                                     
                                                          
                             if (s.isEnemy())
                             EnemyCharactersRisk.add(new Point(x,y));
                             else                                       
                             {
                                       if (!s.getName().equals("Ahri"))
                                       AllyCharactersRisk.add(new Point(x,y));
                             }
                   }
                    
                   for (Skeleton s : EnemyMinions)
                   {
                             int x = (int)(s.getLocation().x*60f/1600f) ;
                             int y =(int)(s.getLocation().y*60f/900f);
                             
                             EnemyMinionsRisk.add(new Point(x,y));
                   }
                   
                   for (Skeleton s : AllyMinions)
                   {
                             int x = (int)(s.getLocation().x*60f/1600f) ;
                             int y =(int)(s.getLocation().y*60f/900f);
                             
                             AllyMinionsRisk.add(new Point(x,y));
                   }
                   for (Skeleton s : EnemyBuildings)
                   {
                             int x = (int)(s.getLocation().x*60f/1600f) ;
                             int y =(int)(s.getLocation().y*60f/900f);
                             
                             EnemyBuildingsRisk.add(new Point(x,y)); // not for inhibitors
                   }
                   for (Skeleton s : AllyBuildings)
                   {
                             int x = (int)(s.getLocation().x*60f/1600f) ;
                             int y =(int)(s.getLocation().y*60f/900f);
                             
                             AllyBuildingsRisk.add(new Point(x,y)); // shouldn't be done for inhibitors
                   } 
                      
                   
                    int width = Actuator.viewWidth;
                    int height = Actuator.viewHeight;
                    int length = width*height;               
                    int viewx = MapState.view.x; // position of the view
                    int viewy = MapState.view.y-2;
                    int valuex = (int)(220/12); // how the view is mapped to the view inside the map (proper view width)
                    int valuey = (int)(220/12.5f);
                    for (int i=0;i<MapData.valueMap.length;i++)
                    for (int j=0;j<MapData.valueMap[0].length;j++)
                    {
                              if (MapData.valueMap[i][j] == 1)      
                              {
                                        float xmod = -1f*(i-(viewx))/valuex;                                               
                                        float ymod = 1f* Math.abs(j-(viewy-valuey))/(valuey*2);
                                        
                                        int xx = (int) Math.round((i - (viewx - valuex))*(width*1f/(2*valuex))); // anything but width is normalization to relevant 0-1 range inside map view
                                        int yy = (int) Math.round((j - (viewy - valuey))*(height*1f/(2*valuey)));
                                        ymod = 1-ymod;
                                        
                                        int modifier = 70;
                                        xx += xmod*ymod*modifier;
                                        
                                        if (Mathematician.valid(xx,yy, width, height))
                                                  WallRisk.add(new Point(xx,yy));
                              }
                    }
                    
          }
          
          public static void resetState()
          {
                    PreviousFinalCharacters = (ArrayList<Skeleton>) FinalCharacters.clone();
                    
                    AllyCharacters.clear();
                    EnemyCharacters.clear();
                    Characters.clear();
                    
                    EnemyMinions.clear();
                    AllyMinions.clear();
                    EnemyBuildings.clear();
                    AllyBuildings.clear();
                    FinalCharacters.clear();
                    
                    AllyCharactersRisk.clear();
                    EnemyCharactersRisk.clear();
                    
                    EnemyMinionsRisk.clear();
                    AllyMinionsRisk.clear();          
                    EnemyBuildingsRisk.clear();
                    AllyBuildingsRisk.clear();
                    WallRisk.clear();
                    
                    dead = false;
                    hp = 0;
                    mp = 0;
                    updated = false;
          }
          
          
          public static void draw()
          {
                    int width = Actuator.viewWidth/5;
                    int height = Actuator.viewHeight/5;
                    int length = width*height;                    
                    
                    BufferedImage bi = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);                    
                    BufferedImage bi2 = new BufferedImage(220,220, BufferedImage.TYPE_INT_RGB);
                                          
                   for (int i = 0; i < length; i++) {
                                                          
                             
                             int c = Color.black.getRGB();                             
                             bi.setRGB(i % width, i / width, c);
                    } 
                   
                   // Translate from coordinates in 220/220 to new image coordinates (view)
                    int viewx = MapState.view.x; // position of the view
                    int viewy = MapState.view.y-2;
                    int valuex = (int)(220/12); // how the view is mapped to the view inside the map (proper view width)
                    int valuey = (int)(220/12.5f);
                    
                    /*
                    for (Person p : MapState.AllyMembers)
                    {
                              float xx = -1f*(p.getLocation().x-(viewx))/valuex;                                        
                              float yy = 1f* Math.abs(p.getLocation().y-(viewy-valuey))/(valuey*2);
                              yy = 1-yy;
                              
                              int dislocation = (int) (xx*yy*10);         
                              p.getLocation().x += dislocation;   
                    }*/
                    
                    
                    
                    for (int i=0;i<MapData.valueMap.length;i++)
                    for (int j=0;j<MapData.valueMap[0].length;j++)
                    {
                              if (MapData.valueMap[i][j] == 1)      
                              {
                                        float xmod = -1f*(i-(viewx))/valuex;                                               
                                        float ymod = 1f* Math.abs(j-(viewy-valuey))/(valuey*2);
                                        
                                        int xx = (int) Math.round((i - (viewx - valuex))*(width*1f/(2*valuex))); // anything but width is normalization to relevant 0-1 range inside map view
                                        int yy = (int) Math.round((j - (viewy - valuey))*(height*1f/(2*valuey)));
                                        ymod = 1-ymod;
                                        
                                        int modifier = 70;
                                        xx += xmod*ymod*modifier;
                                        
                                        if (Mathematician.valid(xx,yy, width, height))
                                        {                                            
                                                  paint(bi,xx,yy,Color.GRAY);   
                                                  WallRisk.add(new Point(xx,yy));
                                        }
                                        
                                        
                                        
                              if (Math.abs(viewx - i) < valuex && Math.abs(viewy - j) < valuey)                
                              {     
                                        
                                        int c = Color.YELLOW.getRGB();                                              
                                        bi2.setRGB(i,j, c);
                              }
                                       else
                              {
                                        int c = Color.PINK.getRGB();      
                                       bi2.setRGB(i,j, c);
                              }
                              }
                              else   if (Math.abs(viewx - i) < valuex && Math.abs(viewy - j) < valuey)  
                              {
                                        
                                        int c = Color.DARK_GRAY.getRGB();      
                                       bi2.setRGB(i,j, c);
                              }
                               
                              if (MapState.AllyMembers.size() >0)
                             if (MapState.AllyMembers.get(0).getLocation().distance(new Point(i,j))<5)
                             {
                                        int c = Color.ORANGE.getRGB();    
                                        bi2.setRGB(i,j, c);      
                             }                                                          
                    }
                    
                    
                 
                     // ImageManipulator.show(bi2,6);
                   
                   // we need to save this list of things somewhere in this place
                   // so we can use it in the risk map
                   
                   
                   for (Skeleton s : FinalCharacters)
                   {
                             int x = s.getLocation().x/5;
                             int y = s.getLocation().y/5;                                                          
                             
                             paint(bi,x,y,Color.yellow);
                             
                             if (s.isEnemy())
                             EnemyCharactersRisk.add(new Point(x,y));
                             else                                       
                             {
                                       if (!s.getName().equals("Ahri"))
                                       AllyCharactersRisk.add(new Point(x,y));
                             }
                   }
                    
                   for (Skeleton s : EnemyMinions)
                   {
                             int x = s.getLocation().x /5;
                             int y = s.getLocation().y/5;
                             paint(bi,x,y,Color.red);
                             EnemyMinionsRisk.add(new Point(x,y));
                   }
                   
                   for (Skeleton s : AllyMinions)
                   {
                             int x = s.getLocation().x /5;
                             int y = s.getLocation().y/5;
                             paint(bi,x,y,Color.blue);
                             AllyMinionsRisk.add(new Point(x,y));
                   }
                   for (Skeleton s : EnemyBuildings)
                   {
                             int x = s.getLocation().x /5;
                             int y = s.getLocation().y/5;
                             paint(bi,x,y,Color.pink);
                             EnemyBuildingsRisk.add(new Point(x,y)); // not for inhibitors
                   }
                   for (Skeleton s : AllyBuildings)
                   {
                             int x = s.getLocation().x /5;
                             int y = s.getLocation().y/5;
                             paint(bi,x,y,Color.cyan);
                             AllyBuildingsRisk.add(new Point(x,y)); // shouldn't be done for inhibitors
                   }
                    
               //   ImageManipulator.show(bi,7);
                    try {
                              Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                              Logger.getLogger(ViewState.class.getName()).log(Level.SEVERE, null, ex);
                    }
          }
          
          private static void paint(BufferedImage bi, int x, int y, Color c)
          {
                    int colour = c.getRGB();
                    double radius = 4;
                    
                    for (int i=-5;i<=5;i++)
                    for (int j=-5;j<=5;j++)
                    {
                              double r = Math.sqrt(i*i+j*j);
                              if (r <= radius)
                              if (x+i > 0 && x+i < bi.getWidth() && y+j > 0 && y+j < bi.getHeight())
                                        bi.setRGB(x+i,y+j,colour);
                    }
          }
}
