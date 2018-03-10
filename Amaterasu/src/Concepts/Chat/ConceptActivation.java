/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts.Chat;

/**
 *
 * @author James
 */
public class ConceptActivation {
          
          float activation;
          String concept;

          public ConceptActivation(float activation, String concept) {
                    this.activation = activation;
                    this.concept = concept;
          }

          public ConceptActivation(String concept) {
                    this.concept = concept;
                    this.activation = 0;
          }

          public float getActivation() {
                    return activation;
          }

          public String getConcept() {
                    return concept;
          }

          public void setActivation(float activation) {
                    this.activation = activation;
          }
          
          public void addActivation(float activation) {
                    this.activation += activation;
          }

          public void setConcept(String concept) {
                    this.concept = concept;
          }
          
          public void print()
          {
                    System.out.println("Concept "+concept+" has activation "+activation);
          }
}
