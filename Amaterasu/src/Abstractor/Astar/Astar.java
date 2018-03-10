/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor.Astar;

import java.util.ArrayList;

/**
 *
 * @author James
 */
public class Astar {
          
          public static RiskNode findCurrentNode(ArrayList<RiskNode> openSet)
          {
                    RiskNode least = null;
                    float min = -10;
                    for (RiskNode n : openSet)
                    {
                              if (least == null || n.fscore < min)
                              {
                                        min = n.fscore;
                                        least = n;
                              }
                    }
                    
                    return least;
                    
          }   
          
          
}
