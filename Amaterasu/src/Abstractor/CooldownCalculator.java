/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

/**
 *
 * @author James
 */
public class CooldownCalculator {
          
          static float OrbCooldown = 7;
          static float timerQ = 0;
          static float FoxfireCooldown = 9;
          static float timerW = 0;
          static float CharmCooldown = 12;
          static float timerE = 0;
          static float SpiritRushCooldown = 110;
          static float timerR = 0;
          static int countR = 0;
          static float IgniteCooldown = 210;
          static float timerD = 0;
          static float FlashCooldown = 300;
          static float timerF = 0;
          
          float cdr = 0;
          
          public static boolean actionUsable(int action)
          {
                    switch (action)
                    {
                              case 1:
                                        if (System.currentTimeMillis() - timerQ > OrbCooldown)
                                                  return true; break;
                              case 2:
                                        if (System.currentTimeMillis() - timerW > FoxfireCooldown)
                                                  return true; break;
                              case 3:
                                        if (System.currentTimeMillis() - timerE > CharmCooldown)
                                                  return true; break;
                              case 4:
                                        if (System.currentTimeMillis() - timerR > SpiritRushCooldown || countR > 0)
                                                  return true; break;
                              case 5:
                                        if (System.currentTimeMillis() - timerD > IgniteCooldown)
                                                  return true; break;
                              case 6:
                                        if (System.currentTimeMillis() - timerF > FlashCooldown)
                                                  return true; break;
                    }
                    
                    return false;
          }
          
          public static void useAction(int action)
          { switch (action)
                    {
                              case 1:
                                                  timerQ = System.currentTimeMillis()/1000f;
                                                  break;
                              case 2:
                                                  timerW = System.currentTimeMillis()/1000f;
                                                  break;
                              case 3:
                                                  timerE = System.currentTimeMillis()/1000f;
                                                  break;
                              case 4:
                                        if (countR == 0)
                                                  timerR = System.currentTimeMillis()/1000f;
                                        else
                                                  countR--;
                                                  break;
                              case 5:
                                                  timerD = System.currentTimeMillis()/1000f;
                                                  break;
                              case 6:
                                                  timerF = System.currentTimeMillis()/1000f;
                                                  break;
                    }
                    
                    
                    
          }
          
}
