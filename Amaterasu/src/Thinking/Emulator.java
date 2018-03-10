/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.Skeleton;
import State.ViewState;
import Thinking.Concepts.MonteCarloAction;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author James
 */
public class Emulator {      
          
          public ArrayList<Skeleton> EnemyMinions = new ArrayList<>();
          public ArrayList<Skeleton> AllyMinions = new ArrayList<>();
          public ArrayList<Skeleton> EnemyBuildings = new ArrayList<>();
          public ArrayList<Skeleton> AllyBuildings = new ArrayList<>();          
          public ArrayList<Skeleton> FinalCharacters = new ArrayList<>();      
          public ArrayList<Emulator> childEmulators = new ArrayList<>(); 
          
          public ArrayList<MonteCarloAction> actions = new ArrayList<>();
          
          ArrayList<Skeleton> EnemyMinionsTemp ;
                    ArrayList<Skeleton> AllyMinionsTemp;
                    ArrayList<Skeleton> EnemyBuildingsTemp ;
                    ArrayList<Skeleton> AllyBuildingsTemp ;
                    ArrayList<Skeleton> FinalCharactersTemp;
          
          static int depth = 3;
          Emulator motherEmulator = null;
          float viability = 0;
          
          int localSimulations = 0;
          int localWins = 0;
          static int globalSimulations = 0;
          
          public Emulator()
          {
                    EnemyMinions = (ArrayList<Skeleton>) ViewState.EnemyMinions.clone();
                    AllyMinions = (ArrayList<Skeleton>) ViewState.AllyMinions.clone();
                    EnemyBuildings = (ArrayList<Skeleton>) ViewState.EnemyBuildings.clone();
                    AllyBuildings = (ArrayList<Skeleton>) ViewState.AllyBuildings.clone();
                    FinalCharacters = (ArrayList<Skeleton>) ViewState.FinalCharacters.clone();
                    
                    generateMoves();
                    //print();                    
          }
          
          public Emulator(Emulator clone)
          {
                    EnemyMinions = (ArrayList<Skeleton>) clone.EnemyMinions.clone();
                    AllyMinions = (ArrayList<Skeleton>) clone.AllyMinions.clone();
                    EnemyBuildings = (ArrayList<Skeleton>) clone.EnemyBuildings.clone();
                    AllyBuildings = (ArrayList<Skeleton>) clone.AllyBuildings.clone();
                    FinalCharacters = (ArrayList<Skeleton>) clone.FinalCharacters.clone();
                    motherEmulator  = clone;
                    
                    generateMoves();
                    //print();                    
          }
          
          public void addWins()
          {                    
                              localWins++;
          }
          public void addSimulations()
          {
                    
                              localSimulations++;
          }
          
          public void performAction(MonteCarloAction action)//(boolean self,int actionType, int targetList, int targetObject)
          {
                    
          }
          
          private float emulateActions(boolean enemy)
          {
                                        
                    Random r = new Random();
                    
                    
                    
                    // perform all the actions with a perform action
                    if (enemy)
                    {
                              for (Skeleton s : FinalCharactersTemp)
                              {
                                        int x = r.nextInt(10);
                                        int y = r.nextInt(10);
                                        
                                                 
                                        if(r.nextBoolean()) // either damage opponent or move randomly
                                        {
                                                  
                                                  for (Skeleton s2 : FinalCharactersTemp)
                                                  {
                                                            if (!s2.isEnemy() && s2.getLocation().distance(s.getLocation()) < 500)
                                                                      s2.removeHp(30);
                                                  }
                                        }
                                        else
                                        {                                                  
                                                  s.getLocation().move(x, y);   
                                        }
                                        
                              }
                              
                              
                    }
                    else
                    {
                              // actions implemented no 2 -> autoattack minion action
                              if (r.nextBoolean())
                              {
                                       for (Skeleton s : EnemyMinionsTemp)
                                        {
                                                  if (s.getHp() < 50)
                                                  {
                                                            if (myself!=null && s.getLocation().distance(myself.getLocation()) < 600)
                                                            {
                                                                      s.removeHp(50);
                                                                      if (s.getHp() < 0)
                                                                                EnemyMinionsTemp.remove(s);
                                                            }                                                            
                                                  }
                                                            
                                        } 
                                        
                                        
                              }
                              else
                              {
                                        int x = r.nextInt(10);
                                        int y = r.nextInt(10);                                        
                                                 
                                        myself.getLocation().move(x, y);    
                                        
                              }
                              
                              
                              
                    }
                    
                    return evaluateViability();
          }
          
          private float evaluateViability()
          {
                    float viabilityOfPosition = 0f;
                    
                    
                    for (Skeleton s : FinalCharactersTemp)
                              {
                                        if (s.getName().equals("Ahri"))
                                                  viabilityOfPosition = s.getHp()/130f;
                              }
                    
                    for (Skeleton s : EnemyMinions)
                    {
                              viabilityOfPosition -= 0.02;
                    }
                                   
                    return viabilityOfPosition;
          }
          
          
          Skeleton myself = null;
          Skeleton myTrueSelf = null;
          
          private float emulateActions()
          {               
                              for (Skeleton s : FinalCharactersTemp)
                                 {
                                              if (s.getName().equals("Ahri"))
                                       myself = s;
                                 }    
                               
                     for (Skeleton s : FinalCharacters)
                                 {
                                              if (s.getName().equals("Ahri"))
                                       myTrueSelf = s;
                                 }
                   
                   boolean enemy = true;
                   for (int i=0;i<depth;i++)
                   { 
                               
                             emulateActions(enemy);
                             enemy = !enemy;
                   }
                   
                   return evaluateViability();
          }
          
          public float emulate()
          {
                   EnemyMinionsTemp = (ArrayList<Skeleton>) EnemyMinions.clone();
                   AllyMinionsTemp = (ArrayList<Skeleton>) AllyMinions.clone();
                   EnemyBuildingsTemp = (ArrayList<Skeleton>) EnemyBuildings.clone();
                   AllyBuildingsTemp = (ArrayList<Skeleton>) AllyBuildings.clone();
                   FinalCharactersTemp = (ArrayList<Skeleton>) FinalCharacters.clone();
                   
                    // first perform an action
                   
                   Random r = new Random();
                   
                   MonteCarloAction myAction = actions.get(r.nextInt(actions.size()));
                    switch (myAction.getName()) {
                              case "Move":                                        
                                        
                                        myTrueSelf.getLocation().move(r.nextInt(10)-5,r.nextInt(10)-5);
                                        
                                        
                                        
                                        
                                        
                                        break;
                              case "Attack Minion":
                                        
                                        EnemyMinions.get(myAction.getTargetObject()).removeHp(50);
                                        
                                        break;
                    }
                             
                   
                   // then emulate
                    float viabilityOfNode = emulateActions();                    
                    
                    return viabilityOfNode;
          }
          
          public void backPropagation(boolean win)
          {
                    if (win)    
                              addWins();    
                              
                    addSimulations();                    
                    
                    if (motherEmulator != null)
                    motherEmulator.backPropagation(win);   
          }
          
          public final void generateMoves()
          {
                    for (int i=0;i<EnemyMinions.size();i++)
                    {                              
                              actions.add(new MonteCarloAction("Attack Minion",i));
                    }
                    
                    actions.add(new MonteCarloAction("Move",0));
          }
          
          private void playRandomMove()
          {
                    
          }
          
          public void makeFirstMove()
          {
                    // create a list of player , enemy and minion/ turret actions
                    Emulator em = new Emulator(this);
                    childEmulators.add(em);
                    
                    // make Q move in the clone
                    playRandomMove();
                    
                    // check how viable it is 
                    float emulatedViability = em.emulate();
                    
                    if (emulatedViability > viability) // 1/0 win condition
                    {
                              em.backPropagation(true);                     
                    }
                    
                    globalSimulations++;
                    
                    
          }
          
          public void makeMove()
          {
                    // move is Q towards a place
                    
                    
          }
          
          public final void print()
          {
                    System.out.println("Enemy Minions");
                    for (Skeleton s: EnemyMinions)
                              s.print();
                    System.out.println("Ally Minions");
                    for (Skeleton s: AllyMinions)
                              s.print();
                    System.out.println("Buildings");
                    for (Skeleton s: EnemyBuildings)
                              s.print();
                    for (Skeleton s: AllyBuildings)
                              s.print();
                    System.out.println("Characters");
                    for (Skeleton s: FinalCharacters)
                              s.print();
          }
          
}
