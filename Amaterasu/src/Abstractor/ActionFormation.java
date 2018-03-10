/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

import Core.Actuator;
import MotorAnalyzers.MotionActor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author James
 */
public class ActionFormation {
          
          public enum ActionType {attack,approach,reposition,explore,haven};
          public enum ActionForm {skillshot,approach,quickapproach,aoe,stun,ignite,item,auto,any};
          static ArrayList<Action> actions = new ArrayList<>();
          
          public static void addAction(Action a)
          {
                    actions.add(a);
          }
          
          public static void coordinateActions(Point self)
          {                    
                    // create some kind of delay between actions
                    
                    /*
                    Point avoidVector = new Point(0,0);
                    Point desireVector = new Point(0,0);
                    
                    
                    for (Action a : actions)
                    {                       
                              if (a.getActionType().equals(ActionType.avoid))
                              {
                                        int xx = (self.x - a.target.x)/Math.abs(self.x - a.target.x);                                        
                                        int yy = (self.y - a.target.y)/Math.abs(self.y - a.target.y);
                                        System.out.println("I have action for "+a.getTarget().getX()+" "+a.getTarget().getY()+" , type :"+a.getActionType()+" , urgency "+a.getUrgency());
                                        
                                        avoidVector.translate(xx*a.urgency, yy*a.urgency);
                                        
                              }
                                                  
                    }
                    
                    for (Action a : actions)
                    {
                              
                              if (a.getActionType().equals(ActionType.approach) || 
                               (a.getActionType().equals(ActionType.attack)))
                              {
                              System.out.println("I have action for "+a.getTarget().getX()+" "+a.getTarget().getY()+" , type :"+a.getActionType()+" , urgency "+a.getUrgency());
                              
                              int xx = (-self.x + a.target.x)/Math.abs(self.x - a.target.x);     
                              int yy = (-self.y + a.target.y)/Math.abs(self.y - a.target.y);
                                        
                              desireVector.translate(xx*a.urgency, yy*a.urgency);        
                              }
                    }
                    
                    
                    for (Action a : actions)
                    {                          
                              {
                                        if (avoidVector.distance(0,0) < desireVector.distance(0,0))
                                        {
                                                  
                                                  
                                                  if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.auto))
                                                            MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), true);                              
                                                  if (a.getActionType().equals(ActionType.approach))
                                                            MotionActor.moveMouse((int)a.target.getX()+50,(int) a.target.getY(), true);        
                                                  
                                                  
                                        }
                                        else
                                        {
                                                  
                                                  MotionActor.moveMouse(self.x+avoidVector.x*30,self.x+avoidVector.y*30, true);
                                        }
                              }
                    }*/
                     
                    for (Action a : actions)
                    {                          
                              if (a.getActionType().equals(ActionType.approach))
                              {
                                        Random r = new Random();
                                        int i = r.nextInt(360);
                                        
                                        int xpos = (int) (50*Math.cos(i*Math.toRadians(i)));
                                        int ypos = (int) (50*Math.sin(i*Math.toRadians(i)));
                                        
                                        int fposx =(int) a.target.getX()+xpos;
                                        int fposy =(int) a.target.getY()+ypos;
                                        
                                        if (fposx > Actuator.viewWidth - 20)
                                                  fposx = Actuator.viewWidth - 20;
                                        if (fposx < 20)
                                                  fposx =20;
                                        if (fposy > Actuator.viewHeight - 20)
                                                  fposy = Actuator.viewHeight - 20;
                                        if (fposy < 20)
                                                  fposy = 20;
                                        
                                        // you wanna approach a place near the target
                                        MotionActor.moveMouse(fposx,fposy, true); 
                              }
                                            
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.auto))
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), true);        
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.skillshot))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), false);       
                                        MotionActor.useAction("Q");      
                              } 
                              if (a.getActionType().equals(ActionType.reposition))
                              {
                                        MotionActor.useAction("Y");      
                              }
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.stun))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), false);       
                                        MotionActor.useAction("E");      
                              } 
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.aoe))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), false);       
                                        MotionActor.useAction("W");      
                              } 
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.approach))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), false);       
                                        MotionActor.useAction("R");      
                              } 
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.quickapproach))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), false);       
                                        MotionActor.useAction("F");      
                              } 
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.ignite))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), false);       
                                        MotionActor.useAction("D");      
                              } 
                              if (a.getActionType().equals(ActionType.attack) && a.getActionForm().equals(ActionForm.auto))
                              {
                                        MotionActor.moveMouse((int)a.target.getX(),(int) a.target.getY(), true);       
                              } 
                              
                          /*    if (a.getActionType().equals(ActionType.avoid))
                              {
                                        // you wanna avoid a place near the target
                                        MotionActor.moveMouse((int)(self.x-a.target.getX()),(int)(self.y- a.target.getY()), true); 
                              } */
                              
                              if (a.getActionType().equals(ActionType.explore))
                              {
                                        MotionActor.clickView(a.target.x, a.target.y, true);  
                              }
                              
                                                    
                              // if we are lost recall!
                                                  
                                            
                    }
                    
                    
                    actions.clear();                    
          }
          
}
