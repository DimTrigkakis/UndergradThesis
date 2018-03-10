/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking.Concepts;

/**
 *
 * @author James
 */
public class MonteCarloAction {
          String name;
          int targetObject;

          public MonteCarloAction(String name, int targetObject)
          {
                    this.targetObject = targetObject;
          }

          public String getName() {
                    return name;
          }

          public int getTargetObject() {
                    return targetObject;
          }

          public void setName(String name) {
                    this.name = name;
          }

          public void setTargetObject(int targetObject) {
                    this.targetObject = targetObject;
          }
          
          
          
}
