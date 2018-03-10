/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

import Abstractor.ActionFormation.ActionForm;
import Abstractor.ActionFormation.ActionType;
import Concepts.Skeleton;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author James
 */
public class Oracle {
          
          // Make a simple minimax algorithm that will show whether a fight is victorious or destructive for our player's side
          // Maybe make it learn based on certain things that happen at beginning of fights
          // like a low health concept or a closeness concept or a cooldown advantage concept
          // or a near turret concept
          // train it with a deep network
          
          //enum OtherAction {runAway,runTowards,damage};
          //enum SelfActionAct {orb,charm,foxfires,Ignite};
          //enum SelfActionMove {ultiAway,ultiTowards,runAway,runTowards,flashAway,flashTowards};
          
          public static void inventPlan(Skeleton self,Skeleton target)
          {
                    System.out.println("target "+target);
                    if (target != null)
                    {
                              if (self.getHp() < target.getHp() - 300)
                                        useStrategy("run",self,target);
                              if (self.getHp() >target.getHp() - 100)
                              {
                                        if (self.getLocation().distance(target.getLocation()) < 300)
                                                  useStrategy("attack",self,target);
                                        else
                                                  useStrategy("engage",self,target);
                              }
                              
                              
                              
                              
                    }
                    else                              
                              RiskMap.loadMap(new PointValue(new Point(self.getLocation()),0)); 
                              
                    
                    // if things are getting awry, this is the dodge action
                    // we also have an attack action
                    // and an engage action
                    
                    // perform learning based on "engage/attack/dodge" actions
                    
                    
                    
                    
                    
                    // make a minimax tree of moving/ability using possibilities
                    // simplifying it first ofc
                    
                    // should be predicting turrets hits, cooldowns, and positioning
                    
                    
          }
          
          private static void useStrategy(String name,Skeleton self,Skeleton target)
          {
                    switch (name)
                    {
                              case "run":
                    System.out.println("MY CHILD IS USING RUN");
                                        RiskMap.loadMap(new PointValue(new Point(self.getLocation()),0)); 
                                        break;
                              case "engage":
                                        useEngage(self,target);
                                        break;
                              case "attack":
                                        useAttack(self,target);
                                        break;
                    }
          }
          
          private static void useAttack(Skeleton self,Skeleton target)
          {
                    System.out.println("MY CHILD IS USING ATTACK");
                    if (CooldownCalculator.actionUsable(3) && (new Random().nextBoolean()))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.stun,10));
                              CooldownCalculator.useAction(3);
                    }
                    else
                    {
                    if (CooldownCalculator.actionUsable(1))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.skillshot,5));
                              CooldownCalculator.useAction(1);
                    }
                    if (CooldownCalculator.actionUsable(2))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.aoe,4));
                              CooldownCalculator.useAction(2);
                    }
                    if (CooldownCalculator.actionUsable(4))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.approach,11));
                              CooldownCalculator.useAction(4);
                    }
                    if (target.getHp() < 200)
                    if (CooldownCalculator.actionUsable(5))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.ignite,6));
                              CooldownCalculator.useAction(5);
                    }
                              
                    }
                              
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.auto,16));
          }
          
          private static void useEngage(Skeleton self,Skeleton target)
          {
                    System.out.println("MY CHILD IS USING ENGAGE");
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.approach,ActionForm.approach,11));
                    if (CooldownCalculator.actionUsable(4))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.approach,11));
                              CooldownCalculator.useAction(4);
                    }
                    
                    if (target.getHp() < 200)
                    if (CooldownCalculator.actionUsable(6))
                    {
                              ActionFormation.addAction(new Action(target.getLocation(),ActionType.attack,ActionForm.quickapproach,12));
                              CooldownCalculator.useAction(6);
                    }
                    
          }
          
}
