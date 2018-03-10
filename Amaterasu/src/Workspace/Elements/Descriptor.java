/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Workspace.Elements;

/**
 *
 * @author James
 */
public class Descriptor {
          
          String name = null;
          int quantity = 0;

          public Descriptor(String name, int quantity) {
                    
                    this.name = name;
                    this.quantity = quantity;
          }

          public Descriptor(String name) {
                    this.name = name;
          }

          public void print()
          {
                    System.out.print(" "+name+" ");
          }
          
          
          
}
