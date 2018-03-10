/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.Chat.ConceptActivation;
import Concepts.Chat.ConceptMap;
import Concepts.Chat.WordMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author James
 */
public class Cloud {

          public static ArrayList<String> Commands = new ArrayList<>(); // These are strings that contain commands for the behavioural part of our character,
          // like going somewhere          
          
          ArrayList<String> words = new ArrayList<>();
          ArrayList<String> meanings = new ArrayList<>();
          ArrayList<WordMap> mappings = new ArrayList<>();
          ArrayList<WordMap> correlations = new ArrayList<>();
          ConceptActivation[] activations = {
                    new ConceptActivation("baron"),
                    new ConceptActivation("dragon"),
                    new ConceptActivation("blue"),
                    new ConceptActivation("no"),
                    new ConceptActivation("go"),
                    new ConceptActivation("wait"),
                    new ConceptActivation("what"),
                    new ConceptActivation("question"),
                    new ConceptActivation("imperative"),
                    new ConceptActivation("hello"),
                    new ConceptActivation("happy"),
                    new ConceptActivation("you"),
                    new ConceptActivation("or"),
                    new ConceptActivation("affirmation"),
                    new ConceptActivation("angry"),
                    new ConceptActivation("fortunate"),
                    new ConceptActivation("name"),};
          WordMap[] initialMappingData = {
                    new WordMap("dont", "no"),
                    new WordMap("who", "what"),
                    new WordMap("your", "you"),
                    new WordMap("?", "question"),
                    new WordMap("hi", "hello"),
                    new WordMap("yes", "affirmation"),
                    new WordMap("hey", "hello"),};
          WordMap[] mappingData;
          ConceptMap[] correlationData = {
                    new ConceptMap(new ConceptActivation(2, "hello"), new ConceptActivation(0, "happy")),
                    new ConceptMap(new ConceptActivation(-2, "hello"), new ConceptActivation(0, "angry")),
                    new ConceptMap(new ConceptActivation(2f, "happy"), new ConceptActivation(0, "fortunate")),
                    new ConceptMap(new ConceptActivation(-2f, "question"), new ConceptActivation(0, "imperative")),
                    new ConceptMap(new ConceptActivation(0.4f, "or"), new ConceptActivation(0, "question"))
          };

          public Cloud() {
          }

          public void add(String s) {
                    words.add(s);
          }

          public void clear() {

                    words.clear();
                    meanings.clear();
                    mappings.clear();
                    correlations.clear();
                    
                    for (ConceptActivation ca  : responseActivations)
                              ca.setActivation(0);
                    
                    for (ConceptActivation ca  : activations)
                              ca.setActivation(0);
                                        
                    // Create the mapping data table
                    int size = initialMappingData.length;
                    size += activations.length;

                    mappingData = new WordMap[size];

                    int i = 0;
                    for (ConceptActivation c : activations) {
                              mappingData[i++] = new WordMap(c.getConcept(), c.getConcept());
                    }
                    for (WordMap w : initialMappingData) {
                              mappingData[i++] = new WordMap(w.getSource(), w.getMapping());
                    }
                    //                    
          }

          public String dream() {

                    mapWords();

                    String answer = formulateAnswer();
                    return answer;
          }

          private void mapWords() {
                    
                     for (String s : words) {
                     for (WordMap w : mappingData) {
                     if (w.getMapping(s) != null) {
                     activate(w.getMapping(s), true);
                     }
                     }
                     }

                     for (ConceptActivation c : activations) {
                              activate(c.getConcept(), false);
                     }
                     
                     /*
                     for (int i=0;i<responseActivations.length;i++)
                     if (responseActivations[i].getActivation() < 0)
                     responseActivations[i].addActivation(1);*/
                    
                     
          }

          private void activate(String s, boolean primitive) {
                    if (primitive) {
                              for (ConceptActivation c : activations) {
                                        if (c.getConcept().equals(s)) {
                                                  c.addActivation(3); // u can later add activation based on present activation                                        
                                        }
                              }
                    } else {
                              for (ConceptMap c : correlationData) {
                                        if (c.getSource().getConcept().equals(s)) {
                                                  ConceptActivation a = getConceptActivation(c.getSource().getConcept());
                                                  ConceptActivation b = getConceptActivation(c.getMapping().getConcept());

                                                  if (b != null) {
                                                            if (a.getActivation() != 0)
                                                            b.addActivation((a.getActivation()) * c.getSource().getActivation()); // the correlationData contain the multipliers
                                                            else if (c.getSource().getActivation() < 0)
                                                            b.addActivation(-c.getSource().getActivation()); 
                                                                      
                                                            if (b.getActivation() > 10)
                                                                      b.setActivation(10);
                                                  }
                                        }
                              }
                    }
          }

          private ConceptActivation getConceptActivation(String concept) {
                    for (ConceptActivation c : activations) {
                              if (c.getConcept().equals(concept)) {
                                        return c;
                              }
                    }

                    return null;
          }
                    
          
          private static final ConceptActivation[] responseActivations = {
                    new ConceptActivation(0,"Say Hi"),          
                    new ConceptActivation(0,"Introduce yourself"),       
                    new ConceptActivation(0,"Travelling agent"),            
                    new ConceptActivation(0,"Stahp!"),                 
          };
          
          private static int[] answerTypeExhausted = new int[responseActivations.length];
          
          private static int exhaustion = -6; // how many points are subtracted from answerTypeExhausted
                    
          private static WordMap[] responseMappingData = { // produce rewards
                    new WordMap("hello", "Say Hi"),
                    new WordMap("name", "Introduce yourself"),
                    new WordMap("you", "Introduce yourself"),          
                    new WordMap("what", "Introduce yourself"),         
                    new WordMap("question", "Introduce yourself"),       
                    new WordMap("imperative", "Travelling agent"),         
                    new WordMap("go", "Travelling agent"),                     
                    new WordMap("wait", "Stahp!"),             
          };
          
          private static WordMap[] responseIllegalData = { // produce punishments
                    
                    new WordMap("no", "Travelling agent"),    
          };
                  
          private String formulateAnswer() {
                    
                    String destination = null;
                    
                    for (int i=0;i<responseActivations.length;i++) {
                              if (answerTypeExhausted[i] > 0)
                              {
                                        responseActivations[i].setActivation(answerTypeExhausted[i]*exhaustion);
                                        if (new Random().nextInt(4) == 0)
                                        answerTypeExhausted[i]--;
                              }
                    }
                                        
                    for (ConceptActivation ca : activations) {                
                              
                                                  for (WordMap w : responseMappingData)
                                                  {
                                                            if (ca.getConcept().equals(w.getSource()))
                                                            {
                                                                      for (ConceptActivation response : responseActivations)
                                                                      {
                                                                                if (response.getConcept().equals(w.getMapping()))
                                                                                {
                                                                                          
                                                                                          if (response.getActivation() >= 0)
                                                                                          if (ca.getActivation() > 0)
                                                                                          response.addActivation(ca.getActivation());
                                                                                          else
                                                                                          response.addActivation(ca.getActivation()-5);
                                                                                          
                                                                                          break;
                                                                                }
                                                                      }
                                                                      
                                                                      break;
                                                            }
                                                  }
                                                  
                                                  for (WordMap w : responseIllegalData)
                                                  {
                                                            if (ca.getConcept().equals(w.getSource()) && ca.getActivation() != 0)
                                                            {
                                                                      for (ConceptActivation response : responseActivations)
                                                                      {
                                                                                if (response.getConcept().equals(w.getMapping()))
                                                                                {
                                                                                          
                                                                                          if (ca.getActivation() > 0)    
                                                                                          if (response.getActivation() >= 0)                                     
                                                                                          response.addActivation(-3*ca.getActivation());
                                                                                          
                                                                                          break;
                                                                                }
                                                                      }
                                                                      
                                                                      break;
                                                            }
                                                  }
                                                  
                                                  if (ca.getActivation() > 0)
                                                  {
                                                            switch(ca.getConcept())
                                                            {
                                                                      case ("baron"):
                                                                      case ("dragon"):
                                                                      case ("blue"):
                                                                      case ("red"):
                                                                      case ("mid"):
                                                                      
                                                                      destination = ca.getConcept();
                                                            }
                                                  }
                                                            
                    }
                    
                    

                    String answer = "";

                    int max = 0;
                    int index = -1;
                    boolean chosen = false;
                    
                    for (int i = 0; i < responseActivations.length; i++) {
                                                            
                              if (!chosen && responseActivations[0].getActivation() > 0) {
                                        index = 0;
                                        max = (int)Math.round(responseActivations[0].getActivation());
                              } else if ( responseActivations[i].getActivation()  > max && responseActivations[i].getActivation() >= 0) {
                                        max = (int)Math.round(responseActivations[i].getActivation());
                                        index = i;
                              }
                    }

                    if (index == 0) {
                              answerTypeExhausted[0]++;
                              int choice = new Random().nextInt(2);
                              if (choice == 0) {
                                        answer = answer.concat("Hi to you too. ");
                              } else {
                                        answer = answer.concat("Hello. ");
                              }
                    }

                    if (index == 1) {
                              answerTypeExhausted[1]++;
                              int choice = new Random().nextInt(2);
                              if (choice == 0) {
                                        answer = answer.concat("My name is Ahri. ");
                              } else {
                                        answer = answer.concat("I am Ahri. ");
                              }
                    }
                    
                     if (index == 2 && destination != null) {
                              answerTypeExhausted[2]++;
                                                         
                              Commands.add("go "+destination);
                              
                              int choice = new Random().nextInt(2);
                              if (choice == 0) {
                                        answer = answer.concat("Ok, I will go there.");
                              } else {
                                        answer = answer.concat("I'm going.");
                              }
                              
                              destination = Character.toUpperCase(destination.charAt(0)) + destination.substring(1);
                              answer = answer.concat(" "+destination+" , right? ");
                    }
                     
                     if (index == 3) {
                              answerTypeExhausted[3]++;
                              
                              Commands.add("wait");
                              
                              int choice = new Random().nextInt(2);
                              if (choice == 0) {
                                        answer = answer.concat("I will wait. ");
                              } else {
                                        answer = answer.concat("Ok I will wait. ");
                              }
                    }
                     
                    return answer;
          }
          
          public ArrayList<String> getCommands()
          {
                    return Commands;
          }
}
