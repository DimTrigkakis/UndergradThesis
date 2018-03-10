/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Thinking.Concepts.Command;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class Commander {
          
          private static ArrayList<Command> commands = new ArrayList<>();
          
          public static void reset()
          {
                    commands.clear();
          }
          
          public static void addCommand(Command c)
          {
                    commands.add(c);
          }
          
          public static void executeCommands()
          {
                    while (commands.size() > 0)
                    {
                              int maxPriority = -1;
                              String commandName = null;
                    
                              for (Command c : commands)
                              {
                              if (c.getPriority() > maxPriority)
                              {
                                        commandName = c.getName();
                                        c.execute();
                                        break;
                                        
                                        
                                } 
                                 }
                              
                              for (int i=0;i<commands.size();i++)
                                        if (commands.get(i).getName().equals(commandName))
                                                  commands.remove(i--);
                              try {
                                        Thread.sleep(5);
                              } catch (InterruptedException ex) {
                                        Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
                              }
                    }
                    
          }
          
}
