/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageAnalyzers;

import Concepts.Person;
import Utilities.Mathematician;
import State.MapData;
import State.MapState;
import State.ViewState;
import Core.Actuator;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author James
 */
public class MapAbstraction extends Thread {

          private static boolean debug;
          private static float[][] hsb = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          private static float[][] hsb2 = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          private static float[][] hsbOriginal = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          private static float[][] hsbPerception = new float[Actuator.mapHeight * Actuator.mapWidth][3];
          private static int width = Actuator.mapWidth;
          private static int height = Actuator.mapHeight;
          private static int length = Actuator.mapWidth * Actuator.mapHeight;
          private String name = null;
          private static boolean show;
          private static float LRF = 185f / 220; // this is the lower resolution factor

          @Override
          public void run() {
                    switch (name) {                              
                              case "view":
                                        detectWhiteLines();
                                        break;
                              case "cyanCharacters":
                                        detectCharacters("cyan");
                                        break;
                              case "redCharacters":
                                        detectCharacters("red");
                                        break;
                              case "cyanMinions":
                                        detectMinions("cyan");
                                        break;
                              case "redMinions":
                                        detectMinions("red");
                                        break;
                              case "cyanBuildings":
                                        detectBuildings("cyan");
                                        break;
                              case "purpleBuildings":
                                        detectBuildings("purple");
                                        break;
                              case "jungle":
                                        detectJungle();
                                        break;
                              case "idCharacters":
                                        idCharacters();
                                        break;
                    }
          } // every kind of processing is a different thread over the immutable data

          public MapAbstraction(String name, boolean show,boolean debug) {
                    this.name = name;
                    MapAbstraction.show = show;
                    MapAbstraction.debug = debug;
          } // used to know which thread processes what

          public MapAbstraction(String name) {
                    this.name = name;
          } // used to know which thread processes what

          // this is where we enter the class
          public void process(BufferedImage bi) throws InterruptedException {

                    ViewState.resetState(); // The object isn't updated after the reset, until this method finishes execution
                    // has to be done , because the map affects the viewstate
                    
                    MapState.resetState(); // The object isn't updated after the reset, until this method finishes execution
                    int[] pixel = new int[3];
                    
                    if (Actuator.mapHeight != height) {
                              hsb = new float[Actuator.mapHeight * Actuator.mapWidth][3];
                              hsbOriginal = new float[Actuator.mapHeight * Actuator.mapWidth][3];
                              width = Actuator.mapWidth;
                              height = Actuator.mapHeight;
                              length = Actuator.mapWidth * Actuator.mapHeight;
                    }

                    for (int i = 0; i < length; i++) {

                              bi.getRaster().getPixel(i % width, i / width, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal[i]);

                              float qsh = 8; // quantum step        
                              float qss = 10; // quantum step    
                              float qsb = 1; // quantum step     

                              hsb[i][0] = Math.round(hsbOriginal[i][0] * qsh) / (1.0f * qsh);
                              hsb[i][1] = Math.round(hsbOriginal[i][1] * qss) / (1.0f * qss);
                              hsb[i][2] = Math.round(hsbOriginal[i][2] * qsb) / (1.0f * qsb);

                    }

                    if (show) {
                              for (int i=0;i<length;i++)
                              {
                                                  hsbPerception[i][0] = hsbOriginal[i][0];
                                                  hsbPerception[i][1] = hsbOriginal[i][1];
                                                  hsbPerception[i][2] = hsbOriginal[i][2];
                              }
                    }

                    // hsbOriginal contains the original image
                    // hsb now contains a quantized version of the image

                    for (int i = 0; i < length; i++) {
                              if (ImageManipulator.colourMatch(hsbOriginal[i], 0.1f, 0.54f, 0.71f, 0.3f)) {

                                        hsb[i][0] = 0.5f;
                                        hsb[i][1] = 0;
                                        hsb[i][2] = 0;

                              }

                              if (ImageManipulator.colourMatch(hsbOriginal[i], 0.5f, 0f, 1f, 0.3f)) {

                                        hsb[i][0] = 0.5f;
                                        hsb[i][1] = 0;
                                        hsb[i][2] = 0;

                              }
                    } // this removes most colours from the picture, and only leaves the players, minions, camps and buildings

                    // we need to synchronize the threads
                    ExecutorService miscExecutor;
                    ExecutorService characterExecutor = null;
                    miscExecutor = Executors.newFixedThreadPool(7);
                    miscExecutor.execute(new MapAbstraction("view"));
                    miscExecutor.execute(new MapAbstraction("cyanMinions"));
                    miscExecutor.execute(new MapAbstraction("redMinions"));
                    miscExecutor.execute(new MapAbstraction("cyanBuildings"));
                    miscExecutor.execute(new MapAbstraction("purpleBuildings"));
                    miscExecutor.execute(new MapAbstraction("jungle"));   
                    
                    characterExecutor = Executors.newFixedThreadPool(2);
                    characterExecutor.execute(new MapAbstraction("cyanCharacters"));
                    characterExecutor.execute(new MapAbstraction("redCharacters"));

                    characterExecutor.shutdown();
                    characterExecutor.awaitTermination(1, TimeUnit.MINUTES);
                    
                    miscExecutor.execute(new MapAbstraction("idCharacters"));
                    miscExecutor.shutdown();
                    miscExecutor.awaitTermination(1, TimeUnit.MINUTES);

                    MapState.updated = true;

                    if (show) {
                              BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                              for (int i = 0; i < length; i++) {
                                        int c = (Color.HSBtoRGB(hsbPerception[i][0], hsbPerception[i][1], hsbPerception[i][2]));
                                        bi2.setRGB(i % width, i / width, c);
                              }

                              ImageManipulator.show(bi2, 0);
                    }

          }

          // we use the manhattan distance to calculate the difference
          // between a character's icon to a vector which contains the expected values         
          // the vector is hueValue(12)-dimensional
          // and the minimum distance decides which character it is
          private void idCharacters() {

                    int radius = 9; // the approximate radius of the disk of a character
                    if (Actuator.hasLowerResolution()) {
                              radius = (int) (radius * LRF);
                    }



                    for (Point p : MapState.AllyMemberPositions) {

                              int[][] Vector = allRoundHistogram(p.x, p.y, radius, show, 0);

                              String characterName = "noone";
                              int minDistance = 0;

                              for (int i = 0; i < MapData.characterMapValues.length; i++) {
                                        if (characterName.equals("noone")) {
                                                  minDistance = Mathematician.vectorDistance3(Vector, MapData.characterMapValues[i]);
                                                  characterName = MapData.characterNames[i];
                                        } else if (Mathematician.vectorDistance3(Vector, MapData.characterMapValues[i]) < minDistance) {
                                                  minDistance = Mathematician.vectorDistance3(Vector, MapData.characterMapValues[i]);
                                                  characterName = MapData.characterNames[i];
                                        }
                              }

                              if (minDistance < 250)
                              {
                                        // perfect detection
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.BLUE, false);
                                        MapState.AllyMembers.add(new Person(p, characterName,false));
                              }
                              else if (minDistance < 600)
                              {
                                        // close characters, messy detection
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.MAGENTA, false);
                                        MapState.AllyMembers.add(new Person(p, "X",false));
                              }
                              else
                              {      
                                        // minion gathering, false detection
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.GREEN, false);
                              }
                              


                    }

                    for (Point p : MapState.EnemyMemberPositions) {

                              int[][] Vector = allRoundHistogram(p.x, p.y, radius, show, 0);

                              String characterName = "noone";
                              int minDistance = 0;

                              for (int i = 0; i < MapData.characterMapValues.length; i++) {
                                        if (characterName.equals("noone")) {
                                                  minDistance = Mathematician.vectorDistance3(Vector, MapData.characterMapValues[i]);
                                                  characterName = MapData.characterNames[i];
                                        } else if (Mathematician.vectorDistance3(Vector, MapData.characterMapValues[i]) < minDistance) {
                                                  minDistance = Mathematician.vectorDistance3(Vector, MapData.characterMapValues[i]);
                                                  characterName = MapData.characterNames[i];
                                        }
                              }
                              
                              
                              if (minDistance < 250)
                              {
                                        // perfect detection
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.RED, false);
                                        MapState.EnemyMembers.add(new Person(p, characterName,true));
                              }
                              else if (minDistance < 400)
                              {
                                        // close characters, messy detection
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.MAGENTA, false);
                                        MapState.EnemyMembers.add(new Person(p, "X",true));
                              }
                              else
                              {      
                                        // minion gathering, false detection
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.ORANGE, false);
                              }

                    }


                    if (show && debug) {

                              System.out.println("I found (" + (MapState.AllyMemberPositions.size() + MapState.EnemyMemberPositions.size()) + ") people.");
                              for (Person p : MapState.AllyMembers) {
                                        System.out.print("ally -> ");
                                        p.print();
                              }

                              for (Person p : MapState.EnemyMembers) {
                                        System.out.print("enemy -> ");
                                        p.print();
                              }
                    }
                    
                    MapState.compareViewState();
          }

          // we use the same technique as with buildings,
          // we check the histogram of known locations to see if they match with expected values
          private void detectJungle() {

                    int radius = 5;

                    int ourNum = 0;
                    for (Point p : MapData.ourJungle) {


                              int[][] Vector = allRoundHistogram(p.x, p.y, radius, show, 0);
                              int distance = Mathematician.vectorDistance3(Vector, MapData.jungleMapValues[0]);
                              if (distance < 150) {
                                        MapState.ourJungle[ourNum] = true;
                                        paintPoint((int) (p.getX() + p.getY() * width), Color.orange, false);
                              }
                              ourNum++;
                    }

                    int theirNum = 0;
                    for (Point p : MapData.ourJungle) {

                              int x;
                              int y;

                              int distance;
                              if (theirNum == 5) {

                                        x = 225 - p.x;
                                        y = 215 - p.y;
                                        int[][] Vector = allRoundHistogram(x, y, radius, show, 0);
                                        distance = Mathematician.vectorDistance3(Vector, MapData.jungleMapValues[0]);

                                        //     count = hueHistogram(225 - p.x, 215 - p.y, 3, 3, false, 0)[1];
                                        //       MapState.theirJungle[theirNum] = (count > 5) && meanSaturation(225 - p.x, 215 - p.y, 3, 3) > 0.6f && meanSaturation(225 - p.x, 215 - p.y, 3, 3) < 0.9f;
                              } else {

                                        x = 230 - p.x;
                                        y = 220 - p.y;
                                        int[][] Vector = allRoundHistogram(x, y, radius, show, 0);
                                        distance = Mathematician.vectorDistance3(Vector, MapData.jungleMapValues[0]);
                              }

                              if (distance < 150) {
                                        MapState.theirJungle[theirNum] = true;
                                        paintPoint(x + y * width, Color.orange, false);
                              }

                              theirNum++;
                    }

                    int[][] Vector = allRoundHistogram(MapData.dragon.x, MapData.dragon.y, radius, show, 0);
                    int distance = Mathematician.vectorDistance3(Vector, MapData.jungleMapValues[0]);


                    if (distance < 150) {
                              MapState.dragon = true;
                              paintPoint(MapData.dragon.x + width * MapData.dragon.y, Color.orange, false);
                    }

                    Vector = allRoundHistogram(MapData.baron.x, MapData.baron.y, radius, show, 0);
                    distance = Mathematician.vectorDistance3(Vector, MapData.jungleMapValues[1]);

                    if (distance < 150) {
                              MapState.baron = true;
                              paintPoint(MapData.baron.x + width * MapData.baron.y, Color.GREEN, false);
                    }

          }

          // the idea of a hough transform accumulator is used, but with disks instead of circles
          private void detectMinions(String colour) {
                    // we will detect minions by using proximity accumulators
                    float c;
                    if (colour.equals("cyan")) {
                              c = 0.5f;
                    } else {
                              c = 0f;
                    }

                    int[] pa = new int[hsb.length];
                    int radius = 3;


                    if (Actuator.hasLowerResolution()) {
                              radius = (int) (radius * LRF);
                    }

                    for (int i = 0; i < hsb.length; i++) {
                              if (ImageManipulator.colourMatch(hsb[i], c, 1f, 1f, 0.1f)) {
                                        for (int x = -radius; x <= radius; x++) {
                                                  for (int y = -radius; y <= radius; y++) {
                                                            if (x != y) {
                                                                      int xg = x + i % width;
                                                                      int yg = y + i / width;

                                                                      if (Mathematician.valid(xg, yg, width, height)) {
                                                                                if (ImageManipulator.colourMatch(hsb[xg + yg * width], c, 1f, 1f, 0.1f)) {
                                                                                          pa[xg + yg * width]++;
                                                                                }
                                                                      }
                                                            }
                                                  }
                                        }
                              }
                    }

                    for (int i = 0; i < hsb.length; i++) {
                              if (pa[i] >= 25) {
                                        for (int x = -radius; x <= radius; x++) {
                                                  for (int y = -radius; y <= radius; y++) {
                                                            int xg = x + i % width;
                                                            int yg = y + i / width;

                                                            if (Mathematician.valid(xg, yg, width, height)) {
                                                                      pa[xg + yg * width] = 0;
                                                            }
                                                  }
                                        }

                                        if (colour.equals("cyan")) {
                                                  paintPoint(i, Color.CYAN, false);
                                                  MapState.CyanMinions.add(new Point(i % width, i / width));
                                        } else {
                                                  paintPoint(i, Color.PINK, false);
                                                  MapState.RedMinions.add(new Point(i % width, i / width));
                                        }
                              }
                    }
          }

          // make a histogram of known locations
          // to see if some of the values match the expected building values
          private void detectBuildings(String colour) // since characters can conceal buildings, don't trust this a lot, only when there are no characters nearby
          {
                    int radius = 4;
                    switch (colour) {
                              case "cyan": {
                                        int TurNum = 0;


                                        for (Point p : MapData.cyanTowers) {

                                                  int[][] Vector = allRoundHistogram(p.x, p.y, radius, show, 0);
                                                  int distance = Mathematician.vectorDistance3(Vector, MapData.buildingMapValues[0]);
                                                  if (distance < 150) {
                                                            MapState.cyanTurrets[TurNum] = true;
                                                            paintPoint((int) (p.getX() + p.getY() * width), Color.BLACK, false);
                                                  }

                                                  TurNum++;
                                        }

                                        int InhNum = 0;
                                        for (Point p : MapData.cyanInhibitors) {

                                                  int[][] Vector = allRoundHistogram(p.x, p.y, radius, show, 0);
                                                  int distance = Mathematician.vectorDistance3(Vector, MapData.buildingMapValues[1]);
                                                  if (distance < 150) {
                                                            MapState.cyanInhibitors[InhNum] = true;
                                                            paintPoint((int) (p.getX() + p.getY() * width), Color.DARK_GRAY, false);
                                                  }

                                                  InhNum++;
                                        }


                                        break;
                              }
                              case "purple": {
                                        int TurNum = 0;


                                        for (Point p : MapData.cyanTowers) {

                                                  int[][] Vector = allRoundHistogram(230 - p.x, 215 - p.y, radius, show, 0);
                                                  int distance = Mathematician.vectorDistance3(Vector, MapData.buildingMapValues[2]);
                                                  if (distance < 150) {
                                                            MapState.purpleTurrets[TurNum] = true;
                                                            paintPoint((int) (230 - p.getX() + (215 - p.getY()) * width), Color.WHITE, false);
                                                  }

                                                  TurNum++;
                                        }

                                        int InhNum = 0;
                                        for (Point p : MapData.cyanInhibitors) {

                                                  int[][] Vector = allRoundHistogram(230 - p.x, 215 - p.y, radius, show, 0);
                                                  int distance = Mathematician.vectorDistance3(Vector, MapData.buildingMapValues[3]);
                                                  if (distance < 150) {
                                                            MapState.purpleInhibitors[InhNum] = true;
                                                            paintPoint((int) (230 - p.getX() + (215 - p.getY()) * width), Color.LIGHT_GRAY, false);
                                                  }

                                                  InhNum++;
                                        }


                                        break;
                              }
                    }

          }
          
          private void detectCharacters(String colour)
          {                     
                    hsb2 = new float[Actuator.mapHeight * Actuator.mapWidth][3];
                        
                    float c = 0.5f;
                    if (colour.equals("red"))
                              c = 0;
                    
                    float radius = 10.5f;

                    int[][] circlePoint = new int[width][height];
                    
                     for (int i = 0; i < length-1-width; i++) 
                     {
                               
                              if (ImageManipulator.colourMatch(hsbOriginal[i], c,1f,1f, 0.2f)) 
                              
                              if (!ImageManipulator.colourMatch(hsbOriginal[i], hsbOriginal[i+1], 0.2f) || !ImageManipulator.colourMatch(hsbOriginal[i], hsbOriginal[i+width], 0.2f))
                                {                              
                                          hsb[i][1] = 0.73f;
                                          for (float j1 = i % width - radius; j1 < i % width + radius; j1++) // hough transform
                                          for (float j2 = i / width - radius; j2 < i / width + radius; j2++) 
                                                            if (j1 > 0 && j1 < width && j2 > 0 && j2 < height) {
                                                                      float d1 = Math.abs(j1 - i % width);
                                                                      float d2 = Math.abs(j2 - i / width);

                                                                      float r = (float) Math.sqrt(d1 * d1 + d2 * d2);

                                                                      if (Math.abs(radius - r) <= 1) {
                                                                                circlePoint[(int) j1][(int) j2] += 1;
                                                                      }
                                                            }
                                }
                              
                     }
                    
                    int max = 0;
                    for (int i = 0; i < length; i++) {
                              if (circlePoint[i / width][i % width] > max) {
                                        max = circlePoint[i / width][i % width];
                              }
                    }

                    if (show)
                    {
                    BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                              if (c == 0.5f) {
                                        for (int i = 0; i < length; i++) {
                                                  int cc;
                                                  if (ImageManipulator.colourMatch(hsbOriginal[i], c, 1f,1f, 0.2f))  
                                                  {
                                                            if (hsb[i][1] == 0.73f)                                                                      
                                                            cc = Color.HSBtoRGB(0f, 1f, 1f);
                                                            else
                                                            cc = Color.HSBtoRGB(0.5f, 1f, 0.5f);
                                                  }
                                                  else                                                     
                                                  cc = Color.HSBtoRGB(0.5f, 1f, 0f);
                                                  bi2.setRGB(i % width, i / width, cc);
                                        }
                                        ImageManipulator.show(bi2, 4);
                              }
                    
                              bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                              if (c == 0) {
                                        for (int i = 0; i < length; i++) {
                                                  int cc = Color.HSBtoRGB(circlePoint[i / width][i % width] / (max * 1f), 1f, 1f);
                                                  bi2.setRGB(i / width, i % width, cc);
                                        }
                                        ImageManipulator.show(bi2, 5);
                              } else {
                                        for (int i = 0; i < length; i++) {
                                                  int cc = Color.HSBtoRGB(1-circlePoint[i / width][i % width] / (max * 1f), 1f, 1f);
                                                  bi2.setRGB(i / width, i % width, cc);
                                        }
                                        ImageManipulator.show(bi2, 7);
                              }
                    }
                    
                    // deletion radius, since we need to locate maxima
                    // instead of locating them, we erase the information that a character is there
                    int erasureRadius = 6;
                    for (int i = 0;i < hsb.length;i++) {
                              if (circlePoint[i % width][i / width] >= 20) {         
                                        
                                        for (int k=-erasureRadius;k<=erasureRadius;k++)
                                                  for (int j=-erasureRadius;j<=erasureRadius;j++)
                                        {
                                                  if (Mathematician.valid(i%width+k, i/width+j,width,height))
                                                  circlePoint[i%width+k][i/width+j] = 0;
                                        }

                                        if (colour.equals("cyan")) {
                                                  MapState.AllyMemberPositions.add(new Point(1 + i % width, 1 + i / width));
                                        } else {
                                                  MapState.EnemyMemberPositions.add(new Point(1 + i % width, 1 + i / width));
                                        }
                              }

                    }
                    
          }
          
/*
          // we use the idea of hough transform, using accumulators for a certain radius (which we know predicts character location)
          private void detectCharacters2(String colour) {
                    float c = 0.5f;
                    if (colour.equals("red")) {
                              c = 0f;
                    }
                    


                    for (int i = 0; i < hsb.length; i++) {
                              if (ImageManipulator.colourMatch(hsb[i], c, 1f, 1f, 0.1f)) {
                                        boolean sameNeighbours = false;

                                                  if (Mathematician.valid((i+1)%width, width) && Mathematician.valid((i-1)%width, width)
                                                          && Mathematician.valid((i+1)/width, height) && Mathematician.valid((i-1)/width, height))
                                                  {
                                                  if (ImageManipulator.colourMatch(hsb[i + 1], c, 1f, 1f, 0.1f)) 
                                                            sameNeighbours = true;
                                                  if (ImageManipulator.colourMatch(hsb[i - 1], c, 1f, 1f, 0.1f)) 
                                                            sameNeighbours = true;
                                                  if (ImageManipulator.colourMatch(hsb[i + width], c, 1f, 1f, 0.1f)) 
                                                            sameNeighbours = true;
                                                  if (ImageManipulator.colourMatch(hsb[i - width], c, 1f, 1f, 0.1f)) 
                                                            sameNeighbours = true;
                                                  
                                                  }
                                        

                                        if (sameNeighbours) 
                                        {
                                                  circlePoint[i%width][i/width] = 0;
                                                  continue;
                                        }
                                        // same neighbours technique is used for removal of thick lines (as in minion waves)

                                        for (float j1 = i % width - radius; j1 < i % width + radius; j1++) // hough transform
                                        {
                                                  for (float j2 = i / width - radius; j2 < i / width + radius; j2++) {
                                                            if (j1 > 0 && j1 < width && j2 > 0 && j2 < height) {
                                                                      float d1 = Math.abs(j1 - i % width);
                                                                      float d2 = Math.abs(j2 - i / width);

                                                                      float r = (float) Math.sqrt(d1 * d1 + d2 * d2);

                                                                      if (Math.abs(radius - r) <= 1) {
                                                                                circlePoint[(int) j1][(int) j2] += 1;
                                                                      }
                                                            }
                                                  }
                                        }


                              }
                    }

          }*/
          
// we detect the horizontal white lines
// if one of the two thick lines is obscured, we use the other to guess the central view point
          private void detectWhiteLines() {

                    int sumyfinal = 0;
                    int n = 0;
                    int[] sumy = new int[height];
                    int sumxStart = -1;
                    int sumxStartLine = -1;
                    int sumxEnd = -1;
                    int sumxEndLine = -1;

                    float factor = 1f;

                    if (Actuator.hasLowerResolution()) {
                              factor = LRF;
                    }

                    for (int i = 0; i < hsb.length; i++) {
                              if (ImageManipulator.colourMatch(hsb[i], 0.0f, 0f, 1f, 0.1f)) {
                                        if (sumxStart == -1) {
                                                  sumxStart = i % width;
                                        }
                                        sumxEnd = i % width;

                                        sumy[i / width]++;
                                        //hsb[i][0] = 0.5f;
                                        //hsb[i][1] = 1f;
                                        //hsb[i][2] = 1;
                              }

                              if (i % width == width - 1) {
                                        if (sumy[i / width] > 10) {
                                                  sumxStartLine = sumxStart;
                                                  sumxStart = -1;
                                                  sumxEndLine = sumxEnd;
                                                  sumxEnd = -1;
                                                  sumyfinal += i / width;
                                                  n++;
                                        }
                              }
                    }


                    if (n != 0) // just a safe check, this should never happen
                    {
                              sumyfinal /= n;
                    }

                    if (Actuator.hasLowerResolution()) {
                              if (sumxStartLine < 43) {
                                        sumxStartLine = sumxEndLine - (int) (43);
                              } else {
                                        sumxEndLine = sumxStartLine + (int) (43);
                              }
                    } else {
                              if (sumxStartLine < 68) {
                                        sumxEndLine += 1;
                                        sumxStartLine = sumxEndLine - (int) (68);
                              } else {
                                        sumxStartLine -= 1;
                                        sumxEndLine = sumxStartLine + (int) (68);
                              }
                    }

                    if (n == 2) // two white lines, cause it was outside the window
                    {
                              if (sumyfinal > 150) {
                                        sumyfinal += (int) (17);
                              } else {
                                        sumyfinal -= (int) (17);
                              }
                    }

                    if (n == 3) // two white lines, cause it was outside the window
                    {
                              if (sumyfinal > 150) {
                                        sumyfinal += (int) (7);
                              } else {
                                        sumyfinal -= (int) (7);
                              }
                    }

                    sumyfinal += 6;
                    if (show) {
                              paintPoint(sumyfinal * width + (sumxStartLine), Color.YELLOW, false);
                              paintPoint(sumyfinal * width + (sumxEndLine), Color.YELLOW, false);
                              paintPoint(sumyfinal * width + (sumxStartLine + sumxEndLine) / 2, Color.YELLOW, false);
                    }

                    MapState.view.x = (sumxStartLine + sumxEndLine) / 2;
                    MapState.view.y = sumyfinal;
          }
          // the histograms for hues depend on these values
          // in general, you want enough to be able to make distinctions between things
          // like characters, so 12 is enough
          final int hueValues = 12;

          // make a circle around a point, and calculate the histogram of hue/saturation or value
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
                                                            //   hsbOriginal[position][2] = 0;                                                                           
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
                    int[] sampleVector1 = roundHistogram(positionx, positiony, radius, 0, false, channel);
                    int[] sampleVector2 = roundHistogram(positionx, positiony, radius, 1, false, channel + 1);
                    int[] sampleVector3 = roundHistogram(positionx, positiony, radius, 2, false, channel + 2);

                    int[][] sampleVector = {sampleVector1, sampleVector2, sampleVector3};

                    return sampleVector;
          }

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

          // used for debugging, and shows where a point is by creating a coloured square in either the original or the simplified picture
          private void paintPoint(int location, Color colour, boolean original) {

                    int x = location % width;
                    int y = location / width;


                    int r = colour.getRed();
                    int b = colour.getBlue();
                    int g = colour.getGreen();
                    float[] hsv = new float[3];
                    Color.RGBtoHSB(r, g, b, hsv);

                    if (original) {
                              for (int i = -5 + x; i < x + 5; i++) {
                                        for (int j = -5 + y; j < y + 5; j++) {
                                                  if (i + j * width > 0 && i + j * width < length) {
                                                            hsbOriginal[i + j * width][0] = hsv[0];
                                                            hsbOriginal[i + j * width][1] = hsv[1];
                                                            hsbOriginal[i + j * width][2] = hsv[2];
                                                  }
                                        }
                              }


                    } else {
                              for (int i = -5 + x; i < x + 5; i++) {
                                        for (int j = -5 + y; j < y + 5; j++) {
                                                  if (i + j * width > 0 && i + j * width < length) {
                                                            hsbPerception[i + j * width][0] = hsv[0];
                                                            hsbPerception[i + j * width][1] = hsv[1];
                                                            hsbPerception[i + j * width][2] = hsv[2];
                                                  }
                                        }
                              }
                    }

          }

          private void print(int[] vector) {
                    System.out.print("{");
                    for (int i = 0; i < vector.length - 1; i++) {
                              System.out.print(vector[i] + ", ");
                    }
                    System.out.print(vector[vector.length - 1]);
                    System.out.print("}");
                    System.out.println();
          }

          private void print(int[][] vector) {
                    System.out.println("{");
                    for (int i = 0; i < vector.length; i++) {
                              print(vector[i]);
                              if (i < vector.length - 1) {
                                        System.out.println(",");
                              }
                    }
                    System.out.println("}");
          }
}
