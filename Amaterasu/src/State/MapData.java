/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

/**
 *
 * @author James
 */
public class MapData {

          public final static Point[] interestPoint = {new Point(17, 62), new Point(91, 124), new Point(162, 205), new Point(25, 185), new Point(185, 25),};
          public final static String[] interestPointNames = {"Top", "Mid", "Bot", "Nexus", "eNexus"};
          public final static Point[] cyanTowers = {new Point(17, 62), new Point(91, 124), new Point(162, 205), // the outer turrets
                    new Point(25, 119), new Point(78, 148), new Point(107, 198), // the middle turrets
                    new Point(21, 155), new Point(58, 163), new Point(67, 203), // the inhibitor turrets          
                    new Point(25, 185), new Point(40, 192)}; // the nexus turrets
          // all turrets from left to right
          public final static Point[] cyanInhibitors = {new Point(21, 168), new Point(50, 173), new Point(55, 202)}; // from left to right    
          public final static Point[] ourJungle = {new Point(35, 95), new Point(65, 105), new Point(60, 125),
                    new Point(105, 140), new Point(121, 158), new Point(129, 178)}; // from  top to bottom
          public final static Point dragon = new Point(150, 155);
          public final static Point baron = new Point(80, 65);
          
          public static int[][][] characterViewValues = {
                    //ahri
                    {{4, 21, 8, 2, 1, 0, 9, 36, 13, 0, 0, 0, 1},{5, 0, 0, 55, 0, 0, 11, 0, 0, 23, 0, 0, 3},{0, 0, 0, 0, 0, 0, 71, 0, 0, 0, 0, 0, 28}},
                    // annie
                    {{3, 4, 4, 3, 0, 0, 6, 15, 18, 25, 12, 2, 0},{7, 0, 0, 21, 0, 0, 37, 0, 0, 24, 0, 0, 9},{0, 0, 0, 0, 0, 0, 56, 0, 0, 0, 0, 0, 43}},
                    // annie near our minionsii
                    {{1, 4, 5, 9, 5, 7, 10, 35, 0, 3, 4, 10, 0},{1, 0, 0, 26, 0, 0, 25, 0, 0, 21, 0, 0, 25},{0, 0, 0, 0, 0, 0, 91, 0, 0, 0, 0, 0, 8}},
                    // ryze
                    {{1, 25, 38, 1, 0, 0, 0, 5, 22, 3, 1, 1, 0},{0, 0, 0, 1, 0, 0, 39, 0, 0, 54, 0, 0, 3},{0, 0, 0, 0, 0, 0, 87, 0, 0, 0, 0, 0, 12}},
                    // lux
                    {{26, 229, 291, 121, 27, 39, 681, 233, 14, 5, 14, 2, 3}, {190, 0, 0, 286, 0, 0, 264, 0, 0, 538, 0, 0, 407}, {0, 0, 0, 0, 0, 0, 1452, 0, 0, 0, 0, 0, 233}},
                    // renekton
                    {{105, 886, 491, 144, 218, 592, 608, 125, 52, 40, 155, 19, 9}, {1383, 0, 0, 1201, 0, 0, 753, 0, 0, 107, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 2922, 0, 0, 0, 0, 0, 522}},
                    // wukong
          };
          public final static int[][] valueMap = new int[220][220];
          public static int[][][] characterMapValues = {
                    //ahri
                    {{37, 100, 39, 0, 0, 1, 1, 33, 6, 5, 4, 7, 16}, {1, 7, 58, 82, 39, 30, 13, 12, 6, 1, 0, 0, 0}, {0, 2, 32, 18, 11, 7, 13, 14, 11, 14, 25, 34, 68}} //annie
                    , {{83, 94, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67}, {0, 0, 0, 0, 3, 11, 26, 36, 45, 73, 40, 12, 3}, {0, 8, 32, 35, 28, 25, 18, 15, 17, 21, 33, 17, 0}} // ryze
                    , {{0, 0, 0, 0, 0, 0, 0, 45, 139, 44, 11, 10, 0}, {1, 0, 2, 10, 20, 35, 82, 74, 19, 5, 1, 0, 0}, {1, 29, 30, 18, 13, 18, 23, 24, 20, 18, 34, 17, 4}} // lux
                    , {{51, 161, 19, 2, 0, 0, 0, 3, 1, 1, 4, 1, 6}, {2, 16, 34, 39, 41, 35, 24, 29, 28, 1, 0, 0, 0}, {3, 8, 2, 2, 3, 9, 9, 15, 20, 26, 48, 104, 0}} // renekton
                    , {{15, 91, 141, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 2, 10, 28, 94, 77, 22, 11, 5}, {0, 7, 12, 57, 73, 29, 22, 15, 12, 9, 5, 5, 3}} // wukong
                    , {{151, 41, 3, 0, 0, 0, 0, 0, 0, 4, 4, 18, 28}, {0, 0, 0, 1, 13, 26, 33, 45, 47, 33, 29, 17, 5}, {0, 16, 44, 42, 26, 21, 27, 18, 10, 15, 15, 9, 6}}
          };
          static int n[] = {1, 1, 1, 1, 1, 1}; // updates on values
          
          
          public static int[][] deadScene = {{2809, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},{2809, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 44, 2336, 426, 3, 0, 0, 0, 0, 0, 0, 0, 0}};
          
          public static int[][] actionUpdateValues = {{0, 216, 89, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 4, 10, 8, 27, 114, 17, 18, 43, 64},{305, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
          
          public static void updateCharacterViewValues(int position, int[][] values) throws InterruptedException, IOException {
                    
                    
                    for (int j = 0; j < characterMapValues[0].length; j++) {
                              for (int k = 0; k < characterMapValues[0][0].length; k++) {
                                        characterViewValues[position][j][k] = characterViewValues[position][j][k] * (n[position] - 1) + values[j][k] / n[position];
                              }
                    }


                    n[position]++;
                    
                    //saveFile();
          }

          public static void loadFile() throws InterruptedException, IOException, ClassNotFoundException {
                    
                     try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream("characterViewValues.dat"))) 
                    {
                                        characterViewValues = (int[][][])objIn.readObject();   
                    }
                    
          }

          public static void saveFile() throws InterruptedException, IOException {

                    try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("characterViewValues.dat"))) 
                    {
                                        objOut.writeObject(characterViewValues);      
                    }
                             
                    

          }
          
          public final static int[][][] jungleMapValues = {
                    // jungle creeps & dragon
                    {{0, 69, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 1, 2, 8, 13, 26, 19, 0, 0}, {0, 5, 14, 8, 6, 7, 6, 6, 5, 4, 7, 1, 0}},
                    // baron                    
                    {{69, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 2, 4, 1, 2, 2, 3, 14, 20, 21}, {0, 3, 4, 6, 6, 4, 1, 9, 6, 9, 5, 7, 9}}
          };
          public final static int[][][] buildingMapValues = {
                    // cyan turret
                    {{0, 0, 0, 0, 0, 2, 42, 1, 0, 0, 0, 0, 0}, {0, 0, 1, 8, 6, 5, 9, 7, 2, 1, 1, 0, 5}, {0, 0, 1, 4, 3, 13, 11, 3, 2, 2, 1, 0, 5}},
                    // cyan inhibitor                    
                    {{0, 2, 0, 0, 0, 0, 0, 39, 2, 2, 0, 0, 0}, {0, 1, 0, 2, 0, 1, 1, 0, 3, 3, 6, 12, 16}, {0, 0, 2, 4, 2, 2, 3, 7, 7, 9, 7, 1, 1}},
                    // purple turret
                    {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 34, 0}, {0, 3, 1, 4, 4, 2, 6, 3, 7, 12, 3, 0, 0}, {0, 0, 0, 11, 12, 6, 5, 2, 3, 1, 1, 3, 1}},
                    // purple inhibitor                    
                    {{2, 8, 0, 0, 0, 1, 2, 0, 1, 0, 28, 3, 0}, {0, 0, 0, 0, 0, 6, 7, 3, 0, 2, 4, 7, 16}, {0, 0, 8, 8, 2, 2, 2, 2, 7, 5, 7, 2, 0}}
          };
          public final static String[] characterNames = {"Ahri", "Annie", "Annie", "Ryze", "Lux", "Renekton", "Wukong"};
}