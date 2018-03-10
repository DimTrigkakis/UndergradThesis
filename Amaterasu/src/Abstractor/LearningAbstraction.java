/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

import Abstractor.ActionFormation.ActionForm;
import Abstractor.ActionFormation.ActionType;
import Concepts.Person;
import Concepts.Skeleton;
import Core.Actuator;
import ImageAnalyzers.ImageManipulator;
import ImageAnalyzers.ViewAbstraction;
import State.ViewState;
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
public class LearningAbstraction {

          private static boolean debug;
          private static boolean show;
          public static Point interestPoint = null; // the strategist will give us this point
          public static int interestValue = 0; // the strategist will give us this value
          public Skeleton self = null;

          public LearningAbstraction(boolean show, boolean debug) {
                    LearningAbstraction.show = show;
                    LearningAbstraction.debug = debug;
          }

          public void process() {
                    developMood();
                    createPlan();
                    //createPlan();
          }

          public void developMood() {

                    Mood.reset();
                    self = null;

                    Mood.interested = interestValue;

                    boolean insideView = false;
                    for (int i = 0; i < ViewState.FinalCharacters.size(); i++) {
                              Skeleton s = ViewState.FinalCharacters.get(i);
                              if (s.getMp() == 86 && s.getLocation().x < 300 && s.getLocation().y > 900) {
                                        ViewState.FinalCharacters.remove(i--);
                              }
                    }



                    for (Skeleton s : ViewState.FinalCharacters) {

                              if (s.getName().equals("Ahri")) {
                                        self = s;
                                        insideView = true;
                              }

                    }

                    if (insideView) {
                              if ((Math.abs(self.getLocation().getX() - Actuator.viewWidth / 2) > Actuator.viewWidth*0.3f)
                                      || (Math.abs(self.getLocation().getY() - Actuator.viewHeight / 2) > Actuator.viewHeight*0.3f)) {
                                          Mood.confused = 15; // almost outside the view

                              }

                              for (Skeleton s : ViewState.EnemyMinions) {
                                        Mood.greedy += 0.5f;
                                        if (s.getHp() <= 40) {
                                                  Mood.greedy += 2;
                                        }
                                        if (s.getHp() <= 20) {
                                                  Mood.greedy += 2;
                                        }
                              }

                              for (Skeleton s : ViewState.FinalCharacters) // check mana cooldowns etc
                              {
                                        if (s.isEnemy()) {
                                                  //     if (s.getHp() < self.getHp()) {
                                                  // that's not how you do it
                                                  // you have to base the decision on a deep learning algorithm that says
                                                  // how good our position is
                                                  Mood.aggressive += 17;
                                                  //     }
                                        }
                              }




                              for (Skeleton s : ViewState.EnemyMinions) {
                                        int distance = (int) s.getLocation().distance(self.getLocation());
                                        boolean dangerous = true;
                                        for (Skeleton minion : ViewState.AllyMinions) {
                                                  if (minion.getLocation().distance(s.getLocation()) < distance) {
                                                            dangerous = false;
                                                            break;
                                                  }
                                        }

                                        if (dangerous) {
                                                  Mood.scared += 1;
                                                  if (self.getHp() < 200) {
                                                            Mood.scared += 10;
                                                  }
                                        }
                              }

                              for (Skeleton s : ViewState.EnemyBuildings) {
                                        int distance = (int) s.getLocation().distance(self.getLocation());
                                        boolean dangerous = true;
                                        for (Skeleton minion : ViewState.AllyMinions) {
                                                  if (minion.getLocation().distance(s.getLocation()) < distance) {
                                                            dangerous = false;
                                                            break;
                                                  }
                                        }

                                        if (dangerous && distance < 500) {
                                                  Mood.scared += 10;
                                        }
                              }
                    } else {
                              Mood.confused = 20;
                    }
                    
                    
          }

          enum ActiveMood {CONFUSED,GREEDY,SCARED, AGGRESSIVE, INTERESTED,RISKY,NOTHING,NULL};
          public void createPlan() {
                    // if there's two targets with low hp - or one, satisfy ur greed

                    ActiveMood am = ActiveMood.NULL;
            //        float[][] hsb = new float[Actuator.viewHeight * Actuator.viewWidth][3];

                    if (Mood.confused > 10) // to move screen towards our place
                    {
                              ActionFormation.addAction(new Action(null, ActionType.reposition, ActionForm.any, 1));
                              
                              am = ActiveMood.CONFUSED;
                              
                    } else if (Mood.aggressive < 5 && Mood.scared < 5 && Mood.greedy > 0) {
                              am = ActiveMood.GREEDY;
                              ArrayList<Point> targets = new ArrayList<>();

                              // Create the middle point between our minions and the collision point
                              Point minionPlaceTheirs = null;
                              Point minionPlaceOurs = null;

                              int meanx = 0;
                              int number = 0;
                              int meany = 0;
                              for (Skeleton s : ViewState.EnemyMinions) {

                                        meanx = (int) (meanx + s.getLocation().getX());
                                        meany = (int) (meany + s.getLocation().getY());
                                        number++;
                              }

                              if (number != 0) {
                                        meanx /= number;
                                        meany /= number;

                                        minionPlaceTheirs = new Point(meanx, meany);
                              }
                              
                              meanx = 0;
                              number = 0;
                              meany = 0;
                              for (Skeleton s : ViewState.AllyMinions) {

                                        meanx = (int) (meanx + s.getLocation().getX());
                                        meany = (int) (meany + s.getLocation().getY());
                                        number++;
                              }

                              if (number != 0) {
                                        meanx /= number;
                                        meany /= number;

                                        minionPlaceOurs = new Point(meanx, meany);
                              }
                              
                              Point finalMiddle = null;
                              if (minionPlaceOurs != null && minionPlaceTheirs != null)
                              {
                              Point middle = new Point((minionPlaceTheirs.x+minionPlaceOurs.x)/2,(minionPlaceTheirs.y+minionPlaceOurs.y)/2);
                              finalMiddle = new Point((middle.x+minionPlaceOurs.x)/2,(middle.y+minionPlaceOurs.y)/2);
                              
                              }


                              // Now we know where we should be
                              // if there are no low hp minions, we should just approach that place

                              boolean foundLow = false;
                              boolean singleBlow = false;
                              int count = 0;
                              for (Skeleton s : ViewState.EnemyMinions) {
                                        if (s.getHp() <= 40) {
                                                  targets.add(s.getLocation());
                                                  foundLow = true;
                                        }
                                        if (s.getHp() <= 20)
                                                  singleBlow = true;
                                        count++;
                              }
                              
                              if (count > 5)
                              {
                                        targets.clear();                                        
                                        for (Skeleton s : ViewState.EnemyMinions) {
                                                  targets.add(s.getLocation());
                                        }
                                        foundLow = true;
                              }
                              
                              // you have some targets now

                              if (!foundLow && finalMiddle != null)                              
                                        ActionFormation.addAction(new Action(finalMiddle, ActionType.approach, ActionForm.any, 2));
                              else
                              {
                              
                              int n = targets.size();
                              System.out.println("target size "+n);
                              
                              if (singleBlow && n == 1)// one target
                              {
                                        ActionFormation.addAction(new Action(targets.get(0).getLocation(), ActionType.attack, ActionForm.auto, 2));
                              }
                              else if (n==1)
                              {
                                        ActionFormation.addAction(new Action(targets.get(0).getLocation(), ActionType.approach, ActionForm.any, 2));                                        
                              }
                              else if (n > 2) {

                                        float X = 0;
                                        float Y = 0;
                                        float XX = 0;
                                        float XY = 0;

                                        for (Point p : targets) {
                                                  float x = (float) p.getX();
                                                  float y = (float) p.getY();

                                                  X += x;
                                                  Y += y;
                                                  XX += x * x;
                                                  XY += x * y;

                                        }

                                        float a = (Y * XX - X * XY) / (n * XX - X * X);
                                        float b = (n * XY - X * Y) / (n * XX - X * X);
                                        System.out.println("Thus equation is y =" + a + " +" + b + " * x");



                                        /*
                                        for (int i = 0; i < Actuator.viewHeight * Actuator.viewWidth; i++) {

                                                  int xxx = i % Actuator.viewWidth;
                                                  int yyy = i / Actuator.viewWidth;

                                                  int distance = (int) (Math.abs(b * xxx - yyy + a) / Math.sqrt(b * b + 1));
                                                  if (distance < 10) {
                                                            hsb[i][0] = 0.3f;
                                                            hsb[i][1] = 0.5f;
                                                            hsb[i][2] = 0.5f;
                                                  } else {
                                                            hsb[i][0] = ViewAbstraction.hsbOriginal[i][0];
                                                            hsb[i][1] = ViewAbstraction.hsbOriginal[i][1];
                                                            hsb[i][2] = ViewAbstraction.hsbOriginal[i][2];
                                                  }


                                                  for (Point p : targets) {
                                                            if (p.distance(new Point(i % Actuator.viewWidth, i / Actuator.viewWidth)) < 10) {
                                                                      hsb[i][0] = 0f;
                                                                      hsb[i][1] = 0.5f;
                                                                      hsb[i][2] = 0.5f;
                                                            }
                                                  }
                                        }*/


                                        if (b != 0) {
                                                  float c = self.getLocation().y + self.getLocation().x / b;
                                                  float bnew = -1 / b;
                                                  System.out.println("Thus equation is y =" + c + " +" + bnew + " * x");

                                                  int xIntersect = (int) ((c - a) / (b - bnew));
                                                  int yIntersect = (int) (a + b * xIntersect);
                                                  System.out.println("Thus point of intersection is " + xIntersect + " " + yIntersect);

                                                  meanx = 0;
                                                  number = 0;
                                                  meany = 0;

                                                  for (Point p : targets) {
                                                            meanx = (int) (meanx + p.getX());
                                                            meany = (int) (meany + p.getY());
                                                            number++;
                                                  }

                                                  meanx /= number;
                                                  meany /= number;



/*
                                                  for (int i = 0; i < Actuator.viewHeight * Actuator.viewWidth; i++) {
                                                            if (new Point(meanx, meany).distance(new Point(i % Actuator.viewWidth, i / Actuator.viewWidth)) < 10) {
                                                                      hsb[i][0] = 0.8f;
                                                                      hsb[i][1] = 0.7f;
                                                                      hsb[i][2] = 0.8f;
                                                            }
                                                  }

                                                  for (int i = 0; i < Actuator.viewHeight * Actuator.viewWidth; i++) {
                                                            if (middle.distance(new Point(i % Actuator.viewWidth, i / Actuator.viewWidth)) < 10) {
                                                                      hsb[i][0] = 0.4f;
                                                                      hsb[i][1] = 1f;
                                                                      hsb[i][2] = 1f;
                                                            }
                                                  }
                                                  
                                                  for (int i = 0; i < Actuator.viewHeight * Actuator.viewWidth; i++) {
                                                            if (Finalmiddle.distance(new Point(i % Actuator.viewWidth, i / Actuator.viewWidth)) < 10) {
                                                                      hsb[i][0] = 0.2f;
                                                                      hsb[i][1] = 1f;
                                                                      hsb[i][2] = 1f;
                                                            }
                                                  }*/
                           /*                     BufferedImage bi2 = new BufferedImage(Actuator.viewWidth, Actuator.viewHeight, BufferedImage.TYPE_INT_RGB);
                                                for (int i = 0; i < Actuator.viewWidth * Actuator.viewHeight; i++) {
                                                         int color = (Color.HSBtoRGB(hsb[i][0], hsb[i][1], hsb[i][2]));
                                                            bi2.setRGB(i % Actuator.viewWidth, i / Actuator.viewWidth, color);
                                                  }

                                             //      ImageManipulator.show(bi2,0);*/


                                                  int distance = (int) self.getLocation().distance(new Point((int) meanx, (int) meany)); // should be distance from cluster
                                                 

                                                  if (distance < 200)//distance from the line but should be distance from the center of the cluster
                                                  {
                                                            // you may want to check the skillshot range before making another approach move towards the cs
                                                            ActionFormation.addAction(new Action(new Point(targets.get(0).x, targets.get(0).y), ActionType.attack, ActionForm.skillshot, 2));
                                                  } else {

                                                            ActionFormation.addAction(new Action(new Point(meanx, meany), ActionType.approach, ActionForm.any, 2));
                                                  }
                                        } else {
                                                  float c = 0;
                                                  System.out.println("x = " + c);

                                                  int xIntersect = (int) c;
                                                  int yIntersect = (int) a;

                                                  int distance = (int) self.getLocation().distance(new Point((int) xIntersect, (int) yIntersect));

                                                  if (distance < 200)// this isn't five it's when u think ur in the line for the skillshot
                                                  {
                                                            // you may want to check the skillshot range before making another approach move towards the cs
                                                            if (CooldownCalculator.actionUsable(1)) {
                                                                      ActionFormation.addAction(new Action(new Point(targets.get(0).x, targets.get(0).y), ActionType.attack, ActionForm.skillshot, 2));
                                                                      CooldownCalculator.useAction(1);
                                                            }
                                                  } else {

                                                            ActionFormation.addAction(new Action(new Point(xIntersect, yIntersect), ActionType.approach, ActionForm.any, 2));
                                                  }

                                        }

                              } 
                              }



                              //
                    } else if (Mood.aggressive < 5 && Mood.scared > 5) // try to find a path in the view using an anadelta
                    {
                              am = ActiveMood.SCARED;
                              if (self != null) { 
                                        RiskMap.loadMap(new PointValue(new Point(self.getLocation()), 0)); // look in pointValue constructor 
                              } else {
                                        System.out.println("self is null cannot use riskmap");
                              }
                    } else if (Mood.aggressive > 5 && Mood.scared < 5) // minimax for the aggressive kill if possible else change mood and reiterate this plan
                    {
                              am = ActiveMood.AGGRESSIVE;
                              Skeleton target = null;
                              for (Skeleton s : ViewState.FinalCharacters) {
                                        if (s.isEnemy()) {
                                                  target = s;
                                                  break;
                                        }
                              }

                              if (self != null) {
                                        Oracle.inventPlan(self, target);
                              }
                    } else if (Mood.aggressive > 5 && Mood.scared > 5) // high risk high reward
                    {         am = ActiveMood.RISKY;
                    
                    } else if (Mood.interested > 0) // to check an interest point outside the screen or just explore if nothing to do and something less interesting nearby
                    {
                              am = ActiveMood.INTERESTED;
                              // this is a point on the map given when the mood was developed
                              ActionFormation.addAction(new Action(interestPoint, ActionType.explore, ActionForm.any, 10));
                              // the strategist should create interesting points near our character
                              // if we are in a peacful state we should follow the lead if it exceeds our interests
                              // an interesting point is one that is close probably and has a high interest rate
                              // chatting creates such a point with huge interest on blue near us so we should go


                    } else // don't just stand there, randomly move somewhere near ur position
                    {
                              am = ActiveMood.NOTHING;
                              /*
                              if (self != null) {
                                        Random r = new Random();
                                        Point location = (Point) self.getLocation().clone();
                                        location.translate(r.nextInt(100) - 55, r.nextInt(100) - 7);
                                        ActionFormation.addAction(new Action(location, ActionType.approach, ActionForm.any, 2));
                              }*/
                    }


                    // the avoid plan creates avoidance points if we are in a scared mood, and the character does hit best to avoid them
                    // every dodge point has an intensity value that creates a mapping of intensities and dangers
                    // the dangers leak in the image of obstacles, and we follow the anadelta

                    // for characters, we use a minimax search of position and skill placements vs dot(damage over time) to see who would win the trade
                    // taking into account turret damage from the minimap

                    // if a character wants to check the screen cause he's interested, he'll have to move the screen, make a creative detection for interest points
                    // and move there if the point is really interesting, this happens in a chase for example


                    if (self != null) {
                              ActionFormation.coordinateActions(self.getLocation());
                    } else {
                              ActionFormation.coordinateActions(null);
                    }


                    interestPoint = null; // this point is reset so that the strategist can alter it after we are done
                    interestValue = 0;
                    // you cannot put this statement before we use it, since it would just nullify the strategist's result



                    Mood.showMood(am); // shows which action was taken (actually what mood cause the action)

          }
}
