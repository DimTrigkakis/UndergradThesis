/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageAnalyzers;

import Core.Actuator;
import MotorAnalyzers.MotionActor;
import State.MapData;
import State.MapState;
import State.ViewState;
import Thinking.Commander;
import Thinking.Concepts.Command;
import Utilities.Mathematician;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.javaocr.gui.GUIController;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.CharacterRange;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.OCRScanner;
import net.sourceforge.javaocr.scanner.DocumentScanner;
import net.sourceforge.javaocr.scanner.PixelImage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import ocr.OCR;

/**
 *
 * @author James
 */
public class ActionAbstraction {
          
          private static boolean debug;
          private static boolean show = false;        
          
          private static float[][] hsb = new float[Actuator.actionWidth * Actuator.actionWidth][3];
       //   private static float[][] hsb2 = new float[Actuator.action2Width * Actuator.action2Width][3];
          private static float[][] hsb3 = new float[Actuator.moneyWidth * Actuator.moneyWidth][3];
          private static float[][] hsbOriginal = new float[Actuator.actionWidth * Actuator.actionWidth][3];
       //   private static float[][] hsbOriginal2 = new float[Actuator.action2Width * Actuator.action2Width][3];
          private static float[][] hsbOriginal3 = new float[Actuator.moneyWidth * Actuator.moneyWidth][3];
          
          private static int width = Actuator.actionWidth;
          private static int height = Actuator.actionWidth;
          private static int length = Actuator.actionWidth * Actuator.actionWidth;
        //  private static int width2 = Actuator.action2Width;
       //   private static int height2 = Actuator.action2Width;
       //   private static int length2 = Actuator.action2Width * Actuator.action2Width;
          private static int width3 = Actuator.moneyWidth;
          private static int height3 = Actuator.moneyWidth;
          private static int length3 = Actuator.moneyWidth * Actuator.moneyWidth;
          private String name = null;  
          
          // values for items

          public ActionAbstraction(boolean show, boolean debug) {
                    ActionAbstraction.show = show;
                    ActionAbstraction.debug = debug;
          }
          
          public void process(BufferedImage bi,BufferedImage bi2,BufferedImage bi3) throws InterruptedException, IOException, TesseractException, FileNotFoundException, ClassNotFoundException {
                    
                    System.out.println("In action abstraction");
                    
                       if (Actuator.actionHeight != height) {
                              hsb = new float[Actuator.actionHeight * Actuator.actionWidth][3];
                              hsbOriginal = new float[Actuator.actionHeight * Actuator.actionWidth][3];
                              width = Actuator.actionWidth;
                              height = Actuator.actionHeight;
                              length = Actuator.actionWidth * Actuator.actionHeight;
                    }
                       
               /*        if (Actuator.action2Height != height2) {
                              hsb2 = new float[Actuator.action2Height * Actuator.action2Width][3];
                              hsbOriginal2 = new float[Actuator.action2Height * Actuator.action2Width][3];
                              width2 = Actuator.action2Width;
                              height2 = Actuator.action2Height;
                              length2 = Actuator.action2Width * Actuator.action2Height;
                    }*/
                    
                       if (Actuator.moneyHeight != height3) {
                              hsb3 = new float[Actuator.moneyHeight * Actuator.moneyWidth][3];
                              hsbOriginal3 = new float[Actuator.moneyHeight * Actuator.moneyWidth][3];
                              width3 = Actuator.moneyWidth;
                              height3 = Actuator.moneyHeight;
                              length3 = Actuator.moneyWidth * Actuator.moneyHeight;
                    }
                       
                    int[] pixel = new int[3];
                      for (int i = 0; i < length3; i++) {

                              bi3.getRaster().getPixel(i % width3, i / width3, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal3[i]);

                              float qsh = 1; // quantum step        
                              float qss = 1; // quantum step    
                              float qsb = 1; // quantum step     

                              hsb3[i][0] = 0;
                              hsb3[i][1] = 0;
                              hsb3[i][2] = 1-Math.round(hsbOriginal3[i][2] * qsb) / (1.0f * qsb);

                              if (i / width3 < 5)
                                        hsb3[i][2] = 1;
                              if (i /width3 > height3 - 3)
                                        hsb3[i][2] = 1;
                    }
                        
                    pixel = new int[3];
                      for (int i = 0; i < length; i++) {

                              bi.getRaster().getPixel(i % width, i / width, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal[i]);

                              float qsh = 8; // quantum step        
                              float qss = 8; // quantum step    
                              float qsb = 8; // quantum step     

                              hsb[i][0] = Math.round(hsbOriginal[i][0] * qsh) / (1.0f * qsh);
                              hsb[i][1] = Math.round(hsbOriginal[i][1] * qss) / (1.0f * qss);
                              hsb[i][2] = Math.round(hsbOriginal[i][2] * qsb) / (1.0f * qsb);

                    }
                      
                      
                      BufferedImage moneyImage = new BufferedImage(width3, height3, BufferedImage.TYPE_INT_ARGB);
                              
                      for (int i = 0; i < length3; i++)                               
                      {
                                        int c = (Color.HSBtoRGB(hsb3[i][0], hsb3[i][1], hsb3[i][2]));
                                        moneyImage.setRGB(i % width3, i / width3, c);
                              
                      }
                      
                      ImageManipulator.saveFileTif(moneyImage,"moneyImage");
                   //   ImageManipulator.show(moneyImage,3);
                      
                            
                      // money image                      
                      
                      for (int i=0;i<10;i++)
                      {
                                try{
                                        File f = new File(new File("").getAbsolutePath().concat("\\numbers\\char_"+i+".png"));
                                        if (f.exists())
                                        f.delete();
                                }
                                catch(Exception e)
                                {
                                          
                                }
      
              
              }
                      
                      OCRScanner scanner = new OCRScanner();
                      GUIController gui = new GUIController();
                     String s = new File("").getAbsolutePath();
                     
                     
                      gui.extractChars(new File(s.concat("\\moneyImage.tif")),new File(s.concat("\\numbers\\")), 20, 20);
                                   
                      boolean exists = true;
                      int count = -1;
                      do{                         
                                //System.out.println("trying for number "+(count+1));
                                File f = new File(s.concat("\\numbers\\char_"+(count+1))+".png");
                                if (!f.exists())
                                          exists = false;
                                count++;                                
                      }
                      while(exists);
                      
                      long l = System.currentTimeMillis();
                      int sum = 0;
                      for(int j=0;j<count;j++)
                      {
                                int temp = OCR.recognize(ImageManipulator.loadFilePng(s.concat("\\numbers\\char_"+j)));
                                
                                if (temp != -1)
                                        sum = 10*sum+temp;
                                else
                                          sum=10*sum;
                      }
                      
                      System.out.println("sum is"+sum);
                      
                  //    if (!MapState.mapAhri) // needs to become more complicated
                   //   buyItems(sum);
                       
                    /*
                      pixel = new int[3];
                      for (int i = 0; i < length2; i++) {

                              bi2.getRaster().getPixel(i % width2, i / width2, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal2[i]);

                              float qsh = 3; // quantum step        
                              float qss = 2; // quantum step    
                              float qsb = 2; // quantum step     

                              hsb2[i][0] = Math.round(hsbOriginal2[i][0] * qsh) / (1.0f * qsh);
                              hsb2[i][1] = Math.round(hsbOriginal2[i][1] * qss) / (1.0f * qss);
                              hsb2[i][2] = Math.round(hsbOriginal2[i][2] * qsb) / (1.0f * qsb);

                    }*/
                      
                      /*
                      int sumg = 0;
                      int sumb = 0;
                      
                    for (int i=0;i<=410;i++)
                    {
                              float h = hsb2[5*width2+i][0];
                              
                              hsb2[5*width2+i][0] = 0.2f;
                              hsb2[5*width2+i][1] = 0.7f;
                              hsb2[5*width2+i][2] = 0.7f;
                              if (h > 0.1f)
                                        sumg++;
                    }
                    for (int i=0;i<=410;i++)
                    {
                              float h = hsb2[24*width2+i][0];
                              hsb2[24*width2+i][0] = 0.2f;
                              hsb2[24*width2+i][1] = 0.7f;
                              hsb2[24*width2+i][2] = 0.7f;
                              if (h > 0.1f)
                                        sumb++;
                    }
                    ViewState.hp = sumg;
                    ViewState.mp = sumb;*/
                    
                      
                    int[][] Vector;
                    boolean updateAvailable = false;
                    
                    for (int i=0;i<=3;i++)
                    {
                      Vector = allRoundHistogram(30+54*i,20,10,false,2);                    
                      int a = Mathematician.vectorDistance2(Vector, MapData.actionUpdateValues);
                      if (a <= 20)    
                      {
                                updateAvailable = true;
                                break;
                      }
                    }
                    
                    // 
                    int[] upgradeLevelVector = {1,3,2,1,1,4,1,2,1,2,4,2,2,3,3,4,3,3};
                    if (updateAvailable) // change
                    {
                              System.out.println(ViewState.level+" my level");
                                        
                              if (ViewState.level <= 18)
                              {
                              if (upgradeLevelVector[ViewState.level-1] == 1)
                              Commander.addCommand(new Command("Update action Q",4,8,null,0));
                              else if (upgradeLevelVector[ViewState.level-1] == 2)
                              Commander.addCommand(new Command("Update action W",4,7,null,1));
                              else if (upgradeLevelVector[ViewState.level-1] == 3)
                              Commander.addCommand(new Command("Update action E",4,6,null,2));
                              else if (upgradeLevelVector[ViewState.level-1] == 4)
                              Commander.addCommand(new Command("Update action R",4,9,null,3));
                                                            
                              ViewState.level++;
                              Commander.executeCommands();
                              }
                    }
                    
                    {
                              BufferedImage bi4 = new BufferedImage(width3, height3, BufferedImage.TYPE_INT_RGB);
                              for (int i = 0; i < length3; i++) {
                                        int c = (Color.HSBtoRGB(hsb3[i][0], hsb3[i][1], hsb3[i][2]));
                                        bi4.setRGB(i % width3, i / width3, c);
                              }

                           //   ImageManipulator.show(bi4, 0);
                    }
                    
                    System.out.println("Mapstate ahri "+MapState.mapAhri);
                    if (!MapState.mapAhri && boughtItems == 0)
                    {
                              boughtItems = 100;
                              buyItems(sum);
                    }
          
          }
          
          public static int boughtItems = 0;
          
          int itemSelected = 0;
          int[] prices = {325,35,35,35,35,0,400,400,1100,3100,3300,2295,3260};
          int[] position = {0,1,1,1,1,2,3,3,4,8,6,7,10};
          // The actual item page is the recommended one for ahri
          // boots, hp pots, warding totem
          // dorans sorcerers (X)
          // rabadons (X) dfg
          // rylais
          private void buyItems(int money) throws InterruptedException 
          {                    
                    MotionActor.useAction("S");
                    Thread.sleep(400);
                    MotionActor.useAction("P");
                 /*   Thread.sleep(200);
                    MotionActor.moveMouseLeft(500,90,true);
                    Thread.sleep(200);
                    MotionActor.moveMouseLeft(500,170,true);
                    Thread.sleep(200);*/
                    
                    Thread.sleep(400);
                    MotionActor.moveMouse(400,(int)(250-180+20),true);
                    
                    
                    while (money > prices[itemSelected])
                    {
                              int positiong = position[itemSelected];
                              int positionx = positiong %3;
                              int positiony = positiong / 3;
                              
                              
                              Thread.sleep(400);
                              MotionActor.moveMouse(400+positionx*200,250+positiony*120,true);
                              
                              
                              
                              money -= prices[itemSelected];
                              itemSelected++;
                              if (itemSelected > 12)                         
                                        break;
                    }
                    
                    Thread.sleep(400);
                    MotionActor.useAction("P");
                    Thread.sleep(1000);
          }
          
           private int[] roundHistogram(int positionx, int positiony, int radius, int type, boolean show, int channel) {
                    int[] histogram = new int[hueValues + 1];

                    for (int j1 = positionx - radius; j1 < positionx + radius; j1++) {
                              for (int j2 = positiony - radius; j2 < positiony + radius; j2++) {
                                        if (j1 > 0 && j1 < width && j2 > 0 && j2 < height) {

                                                  float d1 = Math.abs(j1 - positionx);
                                                  float d2 = Math.abs(j2 - positiony);

                                                  float r = (float) Math.sqrt(d1 * d1 + d2 * d2);

                                                  if (r < radius) {
                                                            int position = j1 + j2 * width;

                                                            //System.out.println(hsbOriginal[position][type] * hueValues);                                                                               
                                                            int hue = (int) (Math.round(hsbOriginal[position][type] * hueValues));
                                                               hsbOriginal[position][2] = 0;                                                                           
                                                            histogram[hue]++;

                                                  }
                                        }
                              }
                    }

                    if (show) {
                              paintHistogram(histogram, type, radius * radius, channel);
                    }



                    return histogram;
          }

          private int[][] allRoundHistogram(int positionx, int positiony, int radius, boolean show, int channel) {
                    int[] sampleVector1 = roundHistogram(positionx, positiony, radius, 0, show, channel);
                    int[] sampleVector2 = roundHistogram(positionx, positiony, radius, 1, show, channel + 1);
                    int[] sampleVector3 = roundHistogram(positionx, positiony, radius, 2, show, channel + 2);

                    int[][] sampleVector = {sampleVector1, sampleVector2, sampleVector3};

                    return sampleVector;
          }

          private static int hueValues  = 12;
          // paints a hue histogram
          private void paintHistogram(int[] histogram, int type, int max, int channel) {

                    BufferedImage bi = new BufferedImage(hueValues * hueValues + hueValues, max, BufferedImage.TYPE_INT_RGB);

                    for (int i = 0; i <= hueValues; i++) {

                              int c;
                              if (type == 0) {
                                        c = (Color.HSBtoRGB(i / (hueValues * 1f), 1f, 1f));
                              } else if (type == 1) {
                                        c = (Color.HSBtoRGB(1f, i / (hueValues * 1f), 1f));
                              } else {
                                        c = (Color.HSBtoRGB(1f, 1f, i / (hueValues * 1f)));
                              }

                              for (int j = 0; j < max; j++) {
                                        if (j < histogram[i]) {
                                                  for (int reps = 0; reps < hueValues; reps++) {
                                                            bi.setRGB(i * hueValues + reps, j, c);
                                                  }
                                        }
                              }
                    }

                    ImageManipulator.show(bi, channel);
          } 
          
             
          private void print(int[] vector) {
                    System.out.print("{");
                    for (int i = 0; i < vector.length - 1; i++) {
                              System.out.print(vector[i] + ", ");
                    }
                    System.out.print(vector[vector.length - 1]);
                    System.out.print("}");
          }
          
          private void print(int[][] vector) {
                    if (vector != null)
                    {
                    System.out.print("{");
                    for (int i = 0; i < vector.length; i++) {
                              print(vector[i]);
                              if (i < vector.length - 1) {
                                        System.out.print(",");
                              }
                    }
                    System.out.print("}");
                    }
          }

       
          
}
