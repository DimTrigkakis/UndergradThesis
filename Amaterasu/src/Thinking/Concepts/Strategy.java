/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking.Concepts;

/**
 *
 * @author James
 */
public class Strategy {
          
          public enum Command {EMPTY,GOTOLANE};
          private String target = null;
          private Command command = null;

          public Strategy(Command command,String target) {
                    this.command = command;
                    this.target = target;
          }
          
}
