/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageAnalyzers;

import Core.Actuator;
import State.MapState;
import Thinking.Cloud;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author James
 */
public class ChatAbstraction {

          private static boolean debug;
          private static boolean show;
          private static float[][] hsb = new float[Actuator.chatHeight * Actuator.chatWidth][3];
          private static boolean[] hsbTouched = new boolean[Actuator.chatHeight * Actuator.chatWidth];
          static int width = Actuator.chatWidth;
          static int height = Actuator.chatHeight;
          private static int length = Actuator.chatWidth * Actuator.chatHeight;

          public ChatAbstraction(boolean show, boolean debug) //used for calling different threads of this class
          {
                    ChatAbstraction.show = show;
                    ChatAbstraction.debug = debug;
          }

          public void process(BufferedImage bi) throws InterruptedException, IOException {

                    int[] pixel = new int[3];

                    BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int i = 0; i < length; i++) {
                              bi.getRaster().getPixel(i % width, i / width, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsb[i]);
                              int c = (Color.HSBtoRGB(hsb[i][0], hsb[i][1], hsb[i][2]));

                             // /*  
                               if (hsb[i][2] > 0.5)
                               c = Color.WHITE.getRGB();
                               else
                               c = Color.BLACK.getRGB();//*/
                              bi2.setRGB(i % width, i / width, c);
                    }

                    
        Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
        // Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping

        String items = "";
        try {
            items = instance.doOCR(bi2);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        /*
                      ImageManipulator.show(bi2,2);
                      OCRScanner scanner = new OCRScanner();
                      Image moneyImageFinal = (Image) bi2;
                      String items = scanner.scan(moneyImageFinal, 0, 0, width,height, null);*/

                             processText(items);
                    
          }

          private void processText(String s) {
                    
                  //  processWords("hey. wait! don't go there! go to baron sometime soon. ");

                    
                     
                     
                    // /*
                     s = s.toLowerCase();
                  
                     try {
                     String text = s.substring(s.lastIndexOf(")")+2,s.length()).trim();
                     String firstPart = s.substring(0,s.lastIndexOf(')'));                  
                  
                     String champion = firstPart.substring(firstPart.lastIndexOf('(')+1,firstPart.length()).trim();
                     String name = firstPart.substring(0,firstPart.lastIndexOf('(')).trim();
                  
                     
                     if (debug)
                     System.out.println("i am "+champion+", played by "+name+", and i said \""+ text+"\"");
                  
                     processWords(text,name);       
                     }
                     catch (StringIndexOutOfBoundsException e)
                     {
                     if (debug)
                     System.out.println("Chat view couldn't be properly detected");
                     }//*/
                     

          }
          
          
          ArrayList<ArrayList<String>> sentences = new ArrayList<>();

          private void processWords(String text,String name) {
                    text = text.replace("!", ".");
                    text = text.replaceAll("[^A-Za-z .?]+", "");
                    text = text.replace("...", ".");
                    text = text.replace(". . .", ".");
                    text = text.replace("?", " ? .");

                    System.out.println(text);
                    String[] mySentences = text.trim().split("\\.");

                    for (int i = 0; i < mySentences.length; i++) {

                              ArrayList<String> words = new ArrayList<>();
                              words.addAll(Arrays.asList(mySentences[i].trim().split("\\s+")));
                              sentences.add(words);

                    }

                    // Here we have sentences containing lists of words per sentence
                    // Use those to detect meaning



                    if (debug) {
                              for (ArrayList<String> sentence : sentences) {
                                        System.out.print("Sentence -> ");
                                        for (String word : sentence) {
                                                  System.out.print(word + " ");
                                        }
                                        System.out.println();
                              }
                    }

                    String myAnswer = "If I could type I'd say : ";
                    Cloud cloud = new Cloud();
                    for (ArrayList<String> sentence : sentences) {

                              cloud.clear();
                              for (String word : sentence) {
                                        cloud.add(word);
                              }

                              myAnswer = myAnswer.concat(cloud.dream());

                    }

                    System.out.println(myAnswer);
                    System.out.print("Commands -> ");
                    if (cloud != null) {
                              for (String s : cloud.getCommands()) {
                                        System.out.println(s);
                                        
                                          if (!name.equals("sompro")) 
                                          {
                                                  MapState.chatCommand = s;
                                          }
                                        
                              }
                    }
                    
                    sentences.clear();
          }
}
