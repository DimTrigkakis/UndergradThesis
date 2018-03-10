/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts.Chat;

/**
 *
 * @author James
 */
public class ConceptMap {
          
          ConceptActivation a;
          ConceptActivation b;

          public ConceptMap(ConceptActivation a, ConceptActivation b) {
                    this.a = a;
                    this.b = b;
          }

          public ConceptActivation getSource() {
                    return a;
          }

          public ConceptActivation getMapping() {
                    return b;
          }
          
          
}
