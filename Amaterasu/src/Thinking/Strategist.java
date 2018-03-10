/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Abstractor.LearningAbstraction;
import Concepts.Person;
import Core.Actuator;
import ImageAnalyzers.ImageManipulator;
import MotorAnalyzers.MotionActor;
import State.MapData;
import State.MapState;
import Thinking.Concepts.InterestPoint;
import Utilities.Mathematician;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class Strategist {

          private static boolean debug;
          private static boolean show;
          private static float[][] hsb = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          private static float[][] hsbFinal = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          private static Point self = null;
          int width = Actuator.mapWidth;
          int height = Actuator.mapHeight;
          int length = Actuator.mapHeight * Actuator.mapWidth;
          ArrayList<Point> minionCollisionPoints = new ArrayList<>();
          ArrayList<Point> tempPoints = new ArrayList<>();
          ArrayList<Point> characterPushedTurret = new ArrayList<>();
          ArrayList<Point> characterUnpushedTurret = new ArrayList<>();
          ArrayList<Point> minionPushedTurret = new ArrayList<>();
          ArrayList<Point> minionUnpushedTurret = new ArrayList<>();
          ArrayList<Point> cyanTurrets = new ArrayList<>();
          ArrayList<Point> purpleTurrets = new ArrayList<>();
          ArrayList<Point> characterCollisionPoints = new ArrayList<>();
          ArrayList<Point> enemyPoints = new ArrayList<>();
          ArrayList<Point> allyPoints = new ArrayList<>();
          ArrayList<InterestPoint> interestPoints = new ArrayList<>();
          
          int chatTimer = 0;
          
          public void processChatCommand()
          {                    
                    if (chatTimer ==0 && MapState.chatCommand != null)
                    {
                    InterestPoint ip = null;
                    if (MapState.chatCommand.equals("go baron"))
                    {
                              Point p = new Point(MapData.baron);
                              ip = new InterestPoint(p, 100);
                    }
                    if (MapState.chatCommand.equals("go dragon"))
                    {
                              Point p = new Point(MapData.dragon);
                              ip = new InterestPoint(p, 100);
                    }
                              
                    chatTimer = 50;
                    
                    if (ip != null)         
                    interestPoints.add(ip);
                    }
                              
          }

          private void paintPoint(Point p, Color color) {
                    for (int i = -1; i <= 1; i++) {
                              for (int j = -1; j <= 1; j++) {
                                        if (Mathematician.valid(p.x + i + (p.y + j) * width, length)) {
                                                  Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb[p.x + i + (p.y + j) * width]);
                                        }
                              }
                    }
          }

          private void paintPoint2(Point p, Color color) {
                    for (int i = -1; i <= 1; i++) {
                              for (int j = -1; j <= 1; j++) {
                                        if (Mathematician.valid(p.x + i + (p.y + j) * width, length)) {
                                                  Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbFinal[p.x + i + (p.y + j) * width]);
                                        }
                              }
                    }
          }

          private void paintLargePoint(Point p, Color color) {
                    for (int i = -2; i <= 2; i++) {
                              for (int j = -2; j <= 2; j++) {
                                        if (Mathematician.valid(p.x + i + (p.y + j) * width, length)) {
                                                  Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb[p.x + i + (p.y + j) * width]);
                                        }
                              }
                    }
          }

          private void clear() {

                    cyanTurrets.clear();
                    purpleTurrets.clear();

                    minionCollisionPoints.clear();
                    characterPushedTurret.clear();
                    characterUnpushedTurret.clear();
                    minionPushedTurret.clear();
                    minionUnpushedTurret.clear();
                    characterCollisionPoints.clear();
                    enemyPoints.clear();
                    allyPoints.clear();
          }

          private void formInterestPoints() {
                    
                    if (chatTimer > 0)
                    chatTimer--;
                    
                    processChatCommand();
                    
                    if (self == null) {
                              self = MapData.interestPoint[3];
                    }

                    InterestPoint ip;
                    Random r = new Random();
                    float distancefromMeModifier = 30+r.nextInt(20);
                    float distancefromMidModifier = 70+r.nextInt(20);

                    for (Point p : minionCollisionPoints) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 10 - cost);
                              interestPoints.add(ip);
                    }

                    for (Point p : enemyPoints) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 15 - cost);
                              interestPoints.add(ip);
                    }

                    for (Point p : characterPushedTurret) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 20 - cost);
                            //  interestPoints.add(ip);
                    }


                    for (Point p : minionPushedTurret) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 6 - cost);
                          //    interestPoints.add(ip);
                    }

                    for (Point p : minionUnpushedTurret) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 9 - cost);
                              interestPoints.add(ip);
                    }

                    for (Point p : characterUnpushedTurret) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 6 - cost);
                              interestPoints.add(ip);
                    }

                    for (Point p : characterCollisionPoints) {
                              float cost = (float) (self.distance(p) / distancefromMeModifier + p.distance(MapData.interestPoint[1]) / (distancefromMidModifier));
                              ip = new InterestPoint(p, 17 - cost);
                              interestPoints.add(ip);
                    }

                    compareInterestPoints();
          }

          private void compareInterestPoints() {
                    
                    // Should only create interest points, not follow them
                    // leave the job of following to the explorer
                    
                    float maxInterest = -1000;
                    InterestPoint maxIp = null;
                    for (InterestPoint ip : interestPoints) {
                              if (ip.getInterest() > maxInterest) {
                                        maxIp = new InterestPoint(ip);
                                        maxInterest = ip.getInterest();
                              }
                    }
                    
                    if (maxIp != null)
                    {
                                                  if (debug)
                    System.out.println("Best place to be at this time seems to be " + maxIp.getP().getX() + " " + maxIp.getP().getY() + " with interest " + maxIp.getInterest());
                    paintPoint2(maxIp.getP(), Color.LIGHT_GRAY);
                    
                    
                    // This shouldn't be done here
                    
                    if (self.distance(maxIp.getP()) < 50/maxIp.getInterest())
                    {
                              if (debug)
                              System.out.println("I am already there");
                              
                    
                    }
                    else
                    {
                              LearningAbstraction.interestPoint = new Point(maxIp.getP());
                              LearningAbstraction.interestValue = (int)maxIp.getInterest();
                            //  if (!Tactician.interest)//
                            //  MotionActor.clickView(maxIp.getP().x, maxIp.getP().y, true); // just give the point to the learning abstraction
                              if (debug)
                              System.out.println("Moving to that place");
                    }
                    }
                    
          }

          public void createInterestPoints() {
                    
                    clear();

                    for (Point p : MapState.CyanMinions) {
                              paintPoint(p, Color.CYAN);
                    }

                    for (Point p : MapState.RedMinions) {
                              paintPoint(p, Color.PINK);
                    }

                    for (Person p : MapState.EnemyMembers) {
                              {
                                        enemyPoints.add(new Point(p.getLocation()));
                                        paintPoint(p.getLocation(), Color.RED);
                                        paintPoint2(p.getLocation(), Color.RED);
                              }
                    }

                    for (Person p : MapState.AllyMembers) {
                              {
                                        allyPoints.add(new Point(p.getLocation()));
                                        paintPoint(p.getLocation(), Color.BLUE);
                                        paintPoint2(p.getLocation(), Color.BLUE);
                              }
                    }

                    if (self != null) {
                              paintPoint(self, Color.YELLOW);
                              paintPoint2(self, Color.YELLOW);
                    }

                    for (int i = 0; i < MapState.cyanTurrets.length; i++) {
                              if (MapState.cyanTurrets[i]) {
                                        Point p = MapData.cyanTowers[i];
                                        paintPoint(p, Color.GREEN);
                                        cyanTurrets.add(p);
                              }
                    }

                    for (int i = 0; i < MapState.cyanTurrets.length; i++) {
                              if (MapState.purpleTurrets[i]) {
                                        Point p = MapData.cyanTowers[i];
                                        Point finalpoint = new Point(230 - p.x, 215 - p.y);
                                        paintPoint(finalpoint, Color.ORANGE);
                                        purpleTurrets.add(finalpoint);
                              }
                    }

                    for (Point pc : MapState.CyanMinions) {
                              for (Point pr : MapState.RedMinions) {
                                        if (pc.distance(pr) < 20) {
                                                  Point p = new Point((pc.x + pr.x) / 2, (pc.y + pr.y) / 2);
                                                  tempPoints.add(p);
                                        }
                              }
                    }



                    for (int i = 0; i < tempPoints.size(); i++) {
                              for (int j = 0; j < tempPoints.size(); j++) {
                                        if (i != j) {
                                                  if (tempPoints.get(i).distance(tempPoints.get(j)) < 70) {
                                                            tempPoints.remove(i);
                                                            i = 0;
                                                            break;
                                                  }
                                        }
                              }
                    }

                    for (Point p : tempPoints) {
                              minionCollisionPoints.add(p);
                              paintLargePoint(p, Color.WHITE);
                                                  if (debug)
                              System.out.println("Minion collision at " + p.x + " " + p.y);
                              paintPoint2(p, Color.WHITE);
                    }

                    for (Point pturret : purpleTurrets) {
                              for (Point pminion : MapState.CyanMinions) {
                                        if (pminion.distance(pturret) < 30) {
                                                  minionPushedTurret.add(pturret);
                                                  paintPoint2(pturret, Color.MAGENTA);
                                                  if (debug)
                                                  System.out.println("Proximity for red turret at " + pturret.x + " " + pturret.y);
                                                  break;
                                        }



                              }

                              for (Point pcharacter : allyPoints) {
                                        if (pcharacter.distance(pturret) < 40) {
                                                  characterPushedTurret.add(pturret);
                                                  paintPoint2(pturret, Color.MAGENTA);
                                                  if (debug)
                                                  System.out.println("Character proximity for red turret at " + pturret.x + " " + pturret.y);
                                                  break;
                                        }



                              }
                    }

                    for (Point pturret : cyanTurrets) {
                              for (Point pminion : MapState.RedMinions) {

                                        if (pminion.distance(pturret) < 30) {
                                                  minionUnpushedTurret.add(pturret);
                                                  paintPoint2(pturret, Color.CYAN);
                                                  if (debug)
                                                  System.out.println("Proximity for cyan turret at " + pturret.x + " " + pturret.y);
                                                  break;
                                        }



                              }

                              for (Point pcharacter : enemyPoints) {
                                        if (pcharacter.distance(pturret) < 40) {
                                                  characterUnpushedTurret.add(pturret);
                                                  paintPoint2(pturret, Color.CYAN);
                                                  if (debug)
                                                  System.out.println("Character proximity for cyan turret at " + pturret.x + " " + pturret.y);
                                                  break;
                                        }



                              }
                    }

                    for (Point pa : allyPoints) {
                              for (Point pe : enemyPoints) {
                                        if (pa.distance(pe) < 20) {
                                                  Point p = new Point((pa.x + pe.x) / 2, (pa.y + pe.y) / 2);
                                                  characterCollisionPoints.add(p);
                                                  paintPoint2(p, Color.GREEN);
                                                  if (debug)
                                                  System.out.println("Character collision at " + p.x + " " + p.y);
                                        }
                              }
                    }

                    formInterestPoints();

                    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int i = 0; i < length; i++) {
                              int c = (Color.HSBtoRGB(hsb[i][0], hsb[i][1], hsb[i][2]));
                              bi.setRGB(i % width, i / width, c);
                    }
                    
                    if (show)
                    ImageManipulator.show(bi, 0);

                    BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int i = 0; i < length; i++) {
                              int c = (Color.HSBtoRGB(hsbFinal[i][0], hsbFinal[i][1], hsbFinal[i][2]));
                              bi2.setRGB(i % width, i / width, c);
                    }

                    
                    if (show)
                    {
                              ImageManipulator.show(bi2, 2);
                    try {
                              Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                              Logger.getLogger(Strategist.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }

          }

          public Strategist(boolean showStrategy, boolean debugStrategy) {

                    debug = debugStrategy;
                    show = showStrategy;

          }

          public void process() {
                    
                    getPosition();
                    createInterestPoints();

                    hsb = new float[Actuator.mapHeight * Actuator.mapWidth][3];
                    hsbFinal = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          }

          public void getSelfPosition() {
                    for (Person p : MapState.AllyMembers) {
                              if (p.getName().equals("Ahri")) {
                                        self = new Point(p.getLocation());
                              }
                    }
          }

          public String getPosition() {
                    getSelfPosition();

                    int distance = 0;
                    String position = null;
                    boolean first = true;

                    if (self != null) {
                              for (int i = 0; i < MapData.interestPoint.length; i++) {
                                        Point p = MapData.interestPoint[i];

                                        if (first || self.distance(p) < distance) {
                                                  first = false;
                                                  distance = (int) self.distance(p);
                                                  position = MapData.interestPointNames[i];
                                        }

                              }
                    }

                    return position;
          }
}
