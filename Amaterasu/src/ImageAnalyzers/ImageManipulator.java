/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageAnalyzers;

import State.MapData;
import Core.Actuator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author James
 */
public class ImageManipulator {
          
          /**
          * Colour Matching withing a sensitivity threshold
          * @param  colours the colour vector to be checked
          * @param h the hue colours will be matched against
          * @param s the saturation colours will be matched against
          * @param b the brightness colours will be matched against
          * @param sensitivity controls how far away from the hsv values colours can be
          * @return true if the colours are close, within sensitivity thresholds
          */
          
          private static float[][] hsbOriginal = new float[Actuator.viewHeight * Actuator.viewWidth][3];
          
          public static boolean colourMatch(float[] colours, float h, float s, float b, float sensitivity) {
                    
                    if (Math.abs(colours[2] - b) <= sensitivity && Math.abs(colours[0] - h) <= sensitivity / 2
                            && Math.abs(colours[1] - s) <= sensitivity) {
                              return true;
                    }

                    return false;
                    
          }  
          
          public static boolean colourMatch(float[] colours, float[] colours2, float sensitivity) {
                    
                    if (Math.abs(colours[2] - colours2[2]) <= sensitivity && Math.abs(colours[0] - colours2[0]) <= sensitivity / 2
                            && Math.abs(colours[1] - colours2[1]) <= sensitivity) {
                              return true;
                    }

                    return false;
                    
          } 
          
          
          
          public final static int frames = 30;
          static JFrame frame[] = new JFrame[frames];

          public static void init() throws InterruptedException, IOException {
                    for (int i = 0; i < frames; i++) 
                              frame[i] = new JFrame();                    
                    
                    // load image valueMap and add it to mapdataii
                    BufferedImage valueMap = loadFile("src/Resources/valueMapPrecise");
                    
                    int width = valueMap.getWidth();
                    int height = valueMap.getHeight();
                    int length = height*width;
                    
                    int[] pixel = new int[3];
                                        
                    for (int i = 0; i < length ; i++) {

                              valueMap.getRaster().getPixel(i % width, i / width, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal[i]);
                              
                              if (pixel[2] == 0f)
                                        MapData.valueMap[i%width][i/width] = 1;
                              else
                                        MapData.valueMap[i%width][i/width] = 0;
                    }
                                   
                    
                    /*
                                BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                   for (int i = 0; i < length; i++) {
                             
                              int c = MapData.valueMap[i%width][i/width]*100;
                             bi2.setRGB(i % width, i / width, c);
                    }            
                   
                   show(bi2,3);
                   */
                    
          } 

          public static void show(BufferedImage bi, int channel) {

                    frame[channel].dispose();
                    frame[channel] = new JFrame();
                    JLabel lblimage = new JLabel(new ImageIcon(bi));
                //    frame[channel].setFocusableWindowState(false);
                    frame[channel].getContentPane().add(lblimage, BorderLayout.CENTER);
                    frame[channel].pack();
                    frame[channel].setLocation(channel * 200, 0);
                    if (channel >= 20)
                    {
                    frame[channel].setLocation(0,(channel-20) * 200);
                    frame[channel].setFocusableWindowState(false);
                    }
                    frame[channel].setVisible(true);
                    
          } // make a new framed picture in the appropriate frame channel
          
          public static BufferedImage loadFilePng(String string) throws InterruptedException, IOException
          {
                    BufferedImage img = null;
                    try {
                     img = ImageIO.read(new File(string+".png"));
                    }
                    catch (IIOException e)
                    {
                              
                    }
                    
                    return img;        
          }
          
          public static BufferedImage loadFile(String string) throws InterruptedException, IOException // bmp files are the usual
          {
                    BufferedImage img = null;
                    try {
                     img = ImageIO.read(new File(string+".bmp"));
                    }
                    catch (IIOException e)
                    {
                              
                    }
                    
                    return img;        
          }
          
          
          public static void saveFile(BufferedImage bi,String string) throws InterruptedException, IOException
          {
                    File outputfile = new File(string+".bmp");
                    if (outputfile != null)
                    {
                              try{
                              ImageIO.write(bi, "bmp", outputfile);     
                              }
                              catch (FileNotFoundException | NullPointerException e)                                      
                              {
                                                System.out.println("Something is null"); // bad 
                                                System.out.println("Something is not read right");
                              }
                                      
                    }
          }
           public static void saveFileTif(BufferedImage bi,String string) throws InterruptedException, IOException
          {
                    File outputfile = new File(string+".tif");
                    if (outputfile != null)
                    {
                              try{
                              ImageIO.write(bi, "tif", outputfile);     
                              }
                              catch (FileNotFoundException | NullPointerException e)
                                      
                              {
                                                System.out.println("Something is null");
                                                System.out.println("Something is not read right");
                              }
                                      
                    }
          }
          
}
