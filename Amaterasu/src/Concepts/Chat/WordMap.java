/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts.Chat;

/**
 *
 * @author James
 */
public class WordMap {
          
          String a;
          String b;

          public WordMap(String a, String b) {
                    this.a = a;
                    this.b = b;
          }
          
          public String getMapping(String a)
          {
                    if (a.equals(this.a))
                              return b;
                    
                    return null;
          }
          
          public String getMapping()
          {
                    return b;
          }
          
          public String getSource()
          {
                    return a;
          }
}
