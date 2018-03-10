/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Thinking.Concepts.Concept;

/**
 *
 * @author James
 */
public class Slipnet {
          
          static final Concept[] concepts = {new Concept("Enemy Proximity",1), new Concept("Character Low Hp",1), 
                    new Concept("Danger",2),new Concept("Enemy Minion Low Hp",1)};
          static float[][] connections = new float[concepts.length][concepts.length];
          
          static final boolean[][] conceptAgentConnections = new boolean[concepts.length][Coderack.agents];
          
          public static void initSlipnet()
          {
                    for (int i=0;i<concepts.length;i++)
                              for (int j=0;j<concepts.length;j++)
                                        connections[i][j] = 0f;
                    
                    connections[0][2] = 1f;
                    connections[1][2] = 1f;          
                    
                    conceptAgentConnections[3][0] = true; // enemy minion low hp activates agent that tries to cs
                    conceptAgentConnections[0][4] = true; // enemy character is close, hit him
                    
          }    
          
          public static void createActivation(int position, int activation)
          {
                    concepts[position].setActivation(activation);
          }
          
          public static void updateSlipnet()
          {                    
                    for (int i=0;i<concepts.length;i++)
                    {
                              for (int j=0;j<concepts.length;j++)
                              {                        
                                        if (connections[i][j] != 0 && concepts[i].getActivation() != 0)
                                        {
                                        float m1 = concepts[i].getActivation();
                                        float m2 = concepts[j].getActivation();
                                        float m3 = connections[i][j];
                                        
                                        
                                        concepts[j].setActivation(Math.max((m2+m1*m3)/2,m2));
                                        }
                                        
                              }
                                        
                              concepts[i].lowerActivation();
                    }
                    
                    for (int i=0;i<concepts.length;i++)
                    {
                              for (int j=0;j<Coderack.agents;j++)
                              {
                                        if (conceptAgentConnections[i][j])
                                                  Coderack.setProbability(j,(int)concepts[i].getActivation());
                              }
                                        
                    }
                    
          }
          
          public static void printSlipnet()
          {
                    for (int i=0;i<concepts.length;i++)
                              concepts[i].print();
          }
          
}
