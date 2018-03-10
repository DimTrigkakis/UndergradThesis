/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking.Concepts;

import java.util.ArrayList;

/**
 *
 * @author James
 */
public class Concept {
          
          String name;
          float activation;
          float abstraction;

          public Concept(String name, float abstraction) {
                    this.name = name;
                    this.abstraction = abstraction;
          }

          public void setActivation(float activation) {
                    this.activation = activation;
          }         

          public float getActivation() {
                    return activation;
          }
          
          public void lowerActivation()
          {
                    activation -= 1/abstraction;
                    if (activation < 0)
                              activation = 0;
          }
          
          public void print()
          {
                    System.out.println("Concept "+name+ " has activation "+activation);
          }
}
