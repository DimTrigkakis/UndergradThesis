/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageAnalyzers;

import Concepts.Bar;
import Concepts.Skeleton;
import State.MapData;
import State.MapState;
import State.ViewState;
import Core.Actuator;
import Utilities.Mathematician;
import Utilities.MyComparator;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author James
 */
public class ViewAbstraction {

          private static boolean debug; // used for the big image
          private static float[][] hsb = new float[Actuator.viewHeight * Actuator.viewWidth][3];
          public static float[][] hsbOriginal = new float[Actuator.viewHeight * Actuator.viewWidth][3];
          static int width = Actuator.viewWidth;
          static int height = Actuator.viewHeight;
          private static int length = Actuator.viewWidth * Actuator.viewHeight;
          private String name;
          static boolean show;

          public ViewAbstraction(String name, boolean show, boolean debug) //used for calling different threads of this class
          {
                    this.name = name;
                    ViewAbstraction.show = show;
                    ViewAbstraction.debug = debug;
          }

          public ViewAbstraction(String name) //used for calling different threads of this class
          {
                    this.name = name;
          }
          ArrayList<Integer> EntityPoints = new ArrayList<>();
          ArrayList<Integer> CharacterPoints = new ArrayList<>();

          public void process(BufferedImage bi) throws InterruptedException, IOException {
                    int[] pixel = new int[3];

                    if (Actuator.viewHeight != height) {
                              hsb = new float[Actuator.viewHeight * Actuator.viewWidth][3];
                              hsbOriginal = new float[Actuator.viewHeight * Actuator.viewWidth][3];
                              width = Actuator.viewWidth;
                              height = Actuator.viewHeight;
                              length = Actuator.viewWidth * Actuator.viewHeight;
                    }

                    CharacterPoints.clear();
                    EntityPoints.clear();

                    boolean deathView = false;
                    // detect if view is gray
                    Random r = new Random(); // throw some random points, if all are gray, then we are dead :(
                    for (int i = 0; i < 5; i++) {
                              int x = r.nextInt(width - 200) + 100;
                              int y = r.nextInt(height - 200) + 100;
                              int[][] Vector = allRoundHistogram(x, y, 30, false, 1);
                              int distance = Mathematician.vectorDistance2(Vector, MapData.deadScene);
                              if (distance < 5000) {
                                        deathView = true;
                              }
                    }

                    if (!deathView) {
                              for (int i = 0; i < length; i++) {
                                        bi.getRaster().getPixel(i % width, i / width, pixel);
                                        Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal[i]);

                                        if (ImageManipulator.colourMatch(hsbOriginal[i], -0.15f, 0.2f, 0.6f, 0.3f)) {
                                                    hsb[i][0] = 0.2f;
                                                     hsb[i][1] = 1f;
                                                     hsb[i][2] = 1f;
                                                  CharacterPoints.add(i);
                                        } else if (ImageManipulator.colourMatch(hsbOriginal[i], 0.0f, 0.1f, 0.1f, 0.1f)) {
                                                   hsb[i][0] = 0.8f;
                                                    hsb[i][1] = 1f;
                                                     hsb[i][2] = 1f;
                                                  EntityPoints.add(i);
                                        } else {
                                                    hsb[i][0] = 0f;
                                                     hsb[i][1] = 0f;
                                                     hsb[i][2] = 0f;
                                        }

                              }
                              // if not

                              lineDetection();
                    } else {
                              ViewState.dead = true;
                    }

                    if (show) {
                              BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                              for (int i = 0; i < length; i++) {
                                        int c = (Color.HSBtoRGB(hsbOriginal[i][0], hsbOriginal[i][1], hsbOriginal[i][2]));
                                        bi2.setRGB(i % width, i / width, c);
                              }

                              ImageManipulator.show(bi2, 0);
                    }

                    if (show) {
                              ViewState.draw();
                    }

          }
          ArrayList<Integer> RealEntityPoints = new ArrayList<>();
          ArrayList<Integer> RealCharacterPoints = new ArrayList<>();

          private void lineDetection() throws InterruptedException, IOException {
                    RealEntityPoints.clear();
                    RealCharacterPoints.clear();

                    for (int i = 0; i < EntityPoints.size(); i++) {
                              int a = EntityPoints.get(i);
                              if (a - width > 0 && a + width < length) {
                                        if (hsb[a - width][0] != 0.8f || hsb[a + width][0] != 0.8f) {
                                                 //   hsbOriginal[a][0] = 0.5f;
                                                 //     hsbOriginal[a][1] = 1f;
                                                  //    hsbOriginal[a][2] = 1f;
                                                  RealEntityPoints.add(a);
                                        }
                              }
                    }

                    for (Integer i : CharacterPoints) {
                              if (i - width > 0 && i + width < length) {
                                        if (hsb[i - width][0] != 0.2f || hsb[i + width][0] != 0.2f) {
                                                  //   hsbOriginal[i][0] = 1f;
                                                  //    hsbOriginal[i][1] = 1f;
                                                  //     hsbOriginal[i][2] = 1f;
                                                  RealCharacterPoints.add(i);
                                        }
                              }
                    }

                    FinalLineDetection();
          }
          ArrayList<Point> EntityPoint = new ArrayList<>();
          ArrayList<Point> CharacterPoint = new ArrayList<>();

          private void FinalLineDetection() throws InterruptedException, InterruptedException, IOException {
                    EntityPoint.clear();
                    CharacterPoint.clear();
                    int previous = 0;
                    boolean breakpoint;
                    int startingPosition = -1;
                    int sum = 0;

                    for (int i = 0; i < RealCharacterPoints.size(); i++) {
                              int place = RealCharacterPoints.get(i);
                              breakpoint = false;

                              sum++;

                              if (startingPosition == -1) {
                                        startingPosition = place;
                              }

                              if (place > previous + 1 && previous != 0) {
                                        breakpoint = true;
                              }

                              if (breakpoint || place % width == 0) {
                                        if (sum >= 100) {
                                                  CharacterPoint.add(new Point(startingPosition, startingPosition + sum));
                                        }
                                        startingPosition = -1;
                                        sum = 0;
                              }

                              previous = place;

                    }

                    for (int i = 0; i < RealEntityPoints.size(); i++) {
                              int place = RealEntityPoints.get(i);
                              breakpoint = false;

                              sum++;

                              if (startingPosition == -1) {
                                        startingPosition = place;
                              }

                              if (place > previous + 1 && previous != 0) {
                                        breakpoint = true;
                              }

                              if (breakpoint || place % width == 0) {
                                        if (sum >= 30) {
                                                  if (Math.abs(sum - 100) > 10) {
                                                            EntityPoint.add(new Point(startingPosition - 1 - width, startingPosition + sum - 1 - width));
                                                  }
                                        }
                                        startingPosition = -1;
                                        sum = 0;
                              }

                              previous = place;

                    }


                    detectCharacters();
                    detectEntities();

                    ViewState.updateState();
                    ViewState.updated = true;
                    if (show) {
                              ViewState.draw();
                    }

          }
          ArrayList<Point> FinalEntities = new ArrayList<>();
          ArrayList<Point> SemiFinalEntities = new ArrayList<>();

          private void detectEntities() {

                    for (Point p : EntityPoint) {
                              for (int i = 0; i < p.getY() - p.getX(); i++) {
                                           hsbOriginal[(int)(p.getX()+i+width)][0] = 0.2f;
                                            hsbOriginal[(int)(p.getX()+i+width)][1] = 1f;
                                          hsbOriginal[(int)(p.getX()+i+width)][2] = 1f;
                              }
                    }

                    String entityName;

                    FinalEntities.clear();
                    SemiFinalEntities.clear();
                    int displacement = 2 + 2 * width;


                    Collections.sort(EntityPoint, new MyComparator());

                    while (!EntityPoint.isEmpty()) {
                              boolean duplicate = false;

                              Point p1 = EntityPoint.get(0);

                              for (int j = 0; j < EntityPoint.size(); j++) {
                                        Point p2 = EntityPoint.get(j);

                                        if (Math.abs(p2.getX() - p1.getX() - 5 * width) <= 5) {
                                                  EntityPoint.remove(p1);
                                                  EntityPoint.remove(p2);
                                                  FinalEntities.add(p1);
                                                  duplicate = true;
                                                  break;
                                        }


                              }

                              if (!duplicate) {
                                        EntityPoint.remove(p1);
                                        SemiFinalEntities.add(p1);
                              }
                    }

                    // we know that double lines are removed
                    // but some lines are solo, detect the solo lines

                    
                     for (Point p : SemiFinalEntities)
                     {
                              
                     for (int i=0;i<p.getY()-p.getX();i++)
                     {
                //     hsbOriginal[(int)(p.getX()+i+3*width)][0] = 60f/360;
                //     hsbOriginal[(int)(p.getX()+i+3*width)][1] = 1f;
                 //    hsbOriginal[(int)(p.getX()+i+3*width)][2] = 1f;
                     }
                              
                     }
                     for (Point p : FinalEntities)
                     {
                              
                     for (int i=0;i<p.getY()-p.getX();i++)
                     {
                 //    hsbOriginal[(int)(p.getX()+i+3*width)][0] = 0.5f;
                 //    hsbOriginal[(int)(p.getX()+i+3*width)][1] = 1f;
                //     hsbOriginal[(int)(p.getX()+i+3*width)][2] = 1f;
                     }
                              
                     }


                    for (Point p : FinalEntities) {
                              if (p.getY() - p.getX() > 65) {
                                        entityName = "Building";
                              } else {
                                        entityName = "Entity";
                              }

                              if (entityName.equals("Building") && (Mathematician.vectorDistance2(MapState.view, MapData.dragon) < 30
                                      || Mathematician.vectorDistance2(MapState.view, MapData.baron) < 30)) {
                                        entityName = "Boss";
                              }

                              int hpred = 0;
                              int hpblue = 0;


                              for (int j = 0; j < p.getY() - p.getX() - 5; j++) {
                                         hsbOriginal[(int)(p.getX()+j+displacement)][1] = 1f;
                                         hsbOriginal[(int)(p.getX()+j+displacement)][2] = 1f;

                                        if (Math.abs(hsbOriginal[(int) (p.getX() + j + 2 + 2 * width)][0] - 0.57) < 0.1f) {
                                                   hsbOriginal[(int)(p.getX()+j+displacement)][0] = 1f;          
                                                  hpblue++;
                                        }

                                        if (Math.abs(hsbOriginal[(int) (p.getX() + j + 2 + 2 * width)][0] - 1) < 0.1f || Math.abs(hsbOriginal[(int) (p.getX() + j + 2 + 2 * width)][0]) < 0.1f) {
                                                  hsbOriginal[(int)(p.getX()+j+displacement)][0] = 1f;               
                                                  hpred++;
                                        }
                              }

                              Skeleton s;
                              int hp = Math.max(hpblue, hpred);
                              if (entityName.equals("Entity")) {
                                        s = new Skeleton(name, hp, 0, new Point(p.x % width + 20, p.x / width + 30), false);
                              } else {
                                        s = new Skeleton(name, hp, 0, new Point(p.x % width + 60, p.x / width + 110), false);
                              }

                              if (entityName.equals("Entity") || entityName.equals("Boss")) {
                                        if (hpred > hpblue) {
                                                  s.setEnemy(true);
                                                  ViewState.EnemyMinions.add(s); // baron goes here too
                                        } else {
                                                  s.setEnemy(false);
                                                  ViewState.AllyMinions.add(s);
                                        }
                              } else {

                                        // only for summoner's rift
                                        if (MapState.view.x > MapState.view.y) {
                                                  s.setEnemy(true);
                                                  ViewState.EnemyBuildings.add(s);
                                        } else {
                                                  s.setEnemy(false);
                                                  ViewState.AllyBuildings.add(s);
                                        }
                              }
                    }

                    if (show && debug) {
                              System.out.println("In this beautiful view");
                              System.out.println("Minions -> " + ViewState.EnemyMinions.size() + " + " + ViewState.AllyMinions.size() + " and Buildings ->"
                                      + ViewState.EnemyBuildings.size() + " + " + ViewState.AllyBuildings.size() + " ,as for Characters -> " + ViewState.Characters.size() + " \n\n\n\n");
                    }
          }

          private void detectCharacters() throws InterruptedException, IOException {
                    String myName = "X";

                    for (Point p : CharacterPoint) {
                              boolean isEnemy = false;

                              int displacementGreen = width * 10 + 23;// - 7*width;
                              int displacementBlue = width * 14 + 23;// - 7*width;                              

                              /*
                               if (Mathematician.valid((int)p.getX()+50+displacementGreen,length))
                               if (hsbOriginal[(int)p.getX()+50+displacementGreen][0] != 0.5376603)    
                               {
                               // ally
                               }*/

                              float meanHue = 0;
                              int numbers = 0;

                              for (int i = -5; i < 5; i++) {
                                        int placement = 15 + 5 * width;
                                        if (Mathematician.valid((int) p.getX() - placement + i + displacementGreen, length)) {
                                                  meanHue += hsbOriginal[(int) p.getX() - placement + i + displacementGreen][0];
                                                  numbers++;
                                                  hsbOriginal[(int) p.getX() - placement + i + displacementGreen][0] = 0.3f;
                                                 hsbOriginal[(int) p.getX() - placement + i + displacementGreen][1] = 0f;
                                                  hsbOriginal[(int) p.getX() - placement + i + displacementGreen][2] = 0f;
                                        }
                              }

                              float value = meanHue / numbers;

                              if (Math.abs(value - 0.35171565f) < 0.001f) // do these need fixing?
                              {
                                        myName = "Ahri";
                                        isEnemy = false;
                                        displacementGreen += -5 * width;
                              } else {

                                        numbers = 0;
                                        meanHue = 0;
                                        for (int i = -5; i < 5; i++) {
                                                  int placement = -97 + 5 * width;
                                                  if (Mathematician.valid((int) p.getX() - placement + i + displacementGreen, length)) {
                                                            meanHue += hsbOriginal[(int) p.getX() - placement + i + displacementGreen][0];
                                                            numbers++;
                                                            hsbOriginal[(int) p.getX() - placement + i + displacementGreen][0] = 0.3f;
                                                            hsbOriginal[(int) p.getX() - placement + i + displacementGreen][1] = 0f;
                                                            hsbOriginal[(int) p.getX() - placement + i + displacementGreen][2] = 0f;
                                                  }
                                        }

                                        float value2 = meanHue / numbers;

                                        displacementGreen += -21 - 5 * width;
                                        displacementBlue += -21;

                                        if (Math.abs(value2 - 0.49012345f) < 0.001f) 
                                                  isEnemy = false;
                                        else
                                                  isEnemy = true;
                              }


                              int hp = 103;
                              int mp = 103;
                              int hpSwitch = 0;
                              int countHp100 = 100;

                              for (int j = 0; j < 103; j++) {
                                        if (Mathematician.valid((int) p.getX() + j + displacementGreen, length)) {
                                                  if (Math.abs(hsbOriginal[(int) (p.getX() + j + displacementGreen)][2]) < 0.1f) {
                                                            hpSwitch--;
                                                            hp--;
                                                  } else {
                                                            if (hpSwitch >= -3) {
                                                                      countHp100++;
                                                                      hpSwitch = 0;
                                                            }
                                                  }

                                                  hsbOriginal[(int) (p.getX() + j + displacementGreen)][0] = 0.2f;
                                                  hsbOriginal[(int) (p.getX() + j + displacementGreen)][1] = 1f;
                                                  hsbOriginal[(int) (p.getX() + j + displacementGreen)][2] = 1f;
                                        }
                              }

                              for (int j = 0; j < 103; j++) {
                                        if (Mathematician.valid((int) p.getX() + j + displacementBlue, length)) {
                                                  if (Math.abs(hsbOriginal[(int) (p.getX() + j + displacementBlue)][2]) < 0.1f) {
                                                            mp--;
                                                  }

                                                  hsbOriginal[(int) (p.getX() + j + displacementBlue)][0] = 0.8f;
                                                  hsbOriginal[(int) (p.getX() + j + displacementBlue)][1] = 1f;
                                                  hsbOriginal[(int) (p.getX() + j + displacementBlue)][2] = 1f;
                                        }
                              }


                              // I think this displacement should be removed, or implemented for blue too
                              int[][] Vector = allFilteringHistogram(((int) p.getX() + 40 + 80 * width + displacementGreen) % width, (int) (p.getX() + 50 + 80 * width + displacementGreen) / width, false, 1);


                              if (Vector != null) {

                                        int distance = -1;

                                        if (!myName.equals("Ahri")) {
                                                  for (int i = 0; i < MapData.characterViewValues.length; i++) {
                                                            if (distance == -1) {
                                                                      distance = Mathematician.vectorDistance2(Vector, MapData.characterViewValues[0]);
                                                                      myName = MapData.characterNames[0];
                                                            } else {
                                                                      if (Mathematician.vectorDistance2(Vector, MapData.characterViewValues[i]) < distance) {
                                                                                distance = Mathematician.vectorDistance2(Vector, MapData.characterViewValues[i]);
                                                                                myName = MapData.characterNames[i];
                                                                      }
                                                            }

                                                  }
                                        } else {
                                                  for (int i = 0; i < MapData.characterNames.length; i++) {
                                                            if (MapData.characterNames[i].equals(myName)) {

                                                                      MapData.updateCharacterViewValues(i, Vector);
                                                            }
                                                  }
                                        }

                                        if (distance > 200) {
                                                  if (show && debug) {
                                                            System.out.println("Fuzzy character, don't know who he is (" + distance + ")" + " guessing " + myName);
                                                  }
                                                  myName = "X";
                                        } else {
                                                  for (int i = 0; i < MapData.characterNames.length; i++) {
                                                            if (MapData.characterNames[i].equals(myName)) {
                                                                      if (debug) {
                                                                                System.out.println("Updating object " + myName);
                                                                      }

                                                                      //MapData.updateCharacterViewValues(i,Vector);    // maybe you want to update based on objects                                                        
                                                            }
                                                  }


                                        }

                                        hp = (int) (600f * hp / 130f);

                                        if (show && debug) {
                                                  System.out.println("I see " + myName + " at " + p.getX() % width + " " + p.getX() / width + " with health " + hp + " " + isEnemy);
                                        }

                                        ViewState.Characters.add(new Skeleton(myName, hp, mp, new Point((int) p.getX() % width + 60, (int) ((p.getX() / width) + 80)), isEnemy));
                              }


                    }

          }

          private void drawLines() {
                    for (Point p : EntityPoint) {

                              for (int i = 0; i < p.getY() - p.getX(); i++) {
                                        hsbOriginal[(int) (p.getX() + i + width)][0] = 0.2f;
                                        hsbOriginal[(int) (p.getX() + i + width)][1] = 1f;
                                        hsbOriginal[(int) (p.getX() + i + width)][2] = 1f;
                              }




                    }

                    for (Point p : CharacterPoint) {

                              for (int i = 0; i < p.getY() - p.getX(); i++) {
                                        hsbOriginal[(int) (p.getX() + i + width)][0] = 0.7f;
                                        hsbOriginal[(int) (p.getX() + i + width)][1] = 1f;
                                        hsbOriginal[(int) (p.getX() + i + width)][2] = 1f;
                              }




                    }
          }
          /*
           int a = 3;
           int newHeight = height/a;
           int newWidth = width/a;
           int newLength = newWidth*newHeight;*/
          // subsampling at "a" and quantizing at qs (qunatum steps)
          // float qsh = 8; // quantum step     
          //float qss = 1; // quantum step  
          //float qsb = 1; // quantum step    
          /*
                     
           int[] pixel = new int[3];
           float[] finalPixel = new float[3];
           for (int x = 0;x<width;x++)
           for (int y =0;y<height;y++)
           {                        
           }    
                              
           for (int j1 = 0;j1<width;j1+=a)
           for (int j2 =0; j2<height;j2+=a)
           {
           bi.getRaster().getPixel(j1, j2, pixel);
           Color.RGBtoHSB(pixel[0],pixel[1],pixel[2], finalPixel);
                              
           int halfJump =j1/a+(j2*newWidth)/a;
                              
           hsb[halfJump][0] = Math.round(finalPixel[0] * qsh) / qsh;
           hsb[halfJump][1] = Math.round(finalPixel[1] * qss) / qss;
           hsb[halfJump][2] = Math.round(finalPixel[2] * qsb) / qsb;
           }  */
          /*
           new ViewAbstraction("Lines").start();
           new ViewAbstraction("Characters").start();
                   
           BufferedImage bi2 = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
           for (int i = 0; i < newLength; i++) {
           int c = (Color.HSBtoRGB(hsb[i][0], hsb[i][1], hsb[i][2]));
           bi2.setRGB(i % newWidth, i / newWidth, c);
           } 
                   
           ImageManipulator.show(bi2,0);
           */
          /*
           edgeDetection();
                    
                   
           bi2 = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
           for (int i = 0; i < newLength; i++) {
           int c = (Color.HSBtoRGB(hsb[i][0], hsb[i][1], hsb[i][2]));
           bi2.setRGB(i % newWidth, i / newWidth, c);
           } 
                   
           ImageManipulator.show(bi2,0);*/
          //  new ViewAbstraction("Lines").start();
          //   new ViewAbstraction("Characters").start();
          //    detectRedLines();
          //  detectCharacters();         
          /*
           bi2 = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
           for (int i = 0; i < newLength; i++) {
           int c = (Color.HSBtoRGB(hsb[i][0], hsb[i][1], hsb[i][2]));
           bi2.setRGB(i % newWidth, i / newWidth, c);
           } 
                   
           ImageManipulator.show(bi2);            */
          final int hueValues = 12;

          // make a hue histogram based on hsbOriginal, around a point (circle),
          // this will make distinctions between characters
          // type = h/s/v 0/1/2
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


                                                            int hue = (int) (Math.round(hsbOriginal[position][type] * hueValues));
                                                            //hsbOriginal[position][2] = 0;                                                                           
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

          private int[] filteringHistogram(int type, boolean show, int channel, float[][] hsbType) {

                    int[] histogram = new int[hueValues + 1];

                    for (int i = 0; i < hsbType.length; i++) {

                              if (hsbType[i][2] > 0.1f) {
                                        int hue = (int) (Math.round(hsbType[i][type] * hueValues));
                                        histogram[hue]++;

                              }
                    }

                    long sum = 0;

                    for (int i = 0; i <= hueValues; i++) {
                              sum += histogram[i];
                    }

                    for (int i = 0; i <= hueValues; i++) {
                              histogram[i] = (int) (histogram[i] / (sum / 100f));
                    }

                    if (show) {
                              paintHistogram(histogram, type, 60 * 10, channel);
                    }

                    return histogram;
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

          private int[][] allFilteringHistogram(int positionx, int positiony, boolean show, int channel) {

                    int radiusx = 80;
                    int radiusy = 60;
                    float window[][] = new float[2 * radiusx * 2 * radiusy][3];
                    float tempWindow[][] = new float[2 * radiusx * 2 * radiusy][3];
                    float finalWindow[][] = new float[2 * radiusx * 2 * radiusy][3];

                    for (int i = positionx - radiusx; i < positionx + radiusx; i++) {

                              for (int j = positiony - radiusy; j < positiony + radiusy; j++) {
                                        int a = (i - positionx + radiusx) + (j - positiony + radiusy) * radiusx * 2;
                                        if (Mathematician.valid(a, 2 * radiusx * 2 * radiusy)) {
                                                  if (Mathematician.valid(i + j * width, width * height)) {


                                                            for (int color = 0; color <= 2; color++) {

                                                                      window[a][color] = hsbOriginal[i + j * width][color];
                                                            }


                                                            //hsbOriginal[i+j*width][color] = 0;
                                                  }
                                        }
                              }
                    }

                    float qsh = 12; // quantum step     
                    float qss = 1; // quantum step  
                    float qsb = 1; // quantum step      

                    for (int x = 0; x < window.length; x++) {
                              tempWindow[x][0] = Math.round(window[x][0] * qsh) / qsh;
                              tempWindow[x][1] = Math.round(window[x][1] * qss) / qss;
                              tempWindow[x][2] = Math.round(window[x][2] * qsb) / qsb;
                    }

                    qsh = 12; // quantum step     
                    qss = 4; // quantum step  
                    qsb = 2; // quantum step      

                    for (int x = 0; x < window.length; x++) {
                              if (lightNearby(x, tempWindow)) {
                                        finalWindow[x][0] = Math.round(window[x][0] * qsh) / qsh;
                                        finalWindow[x][1] = Math.round(window[x][1] * qss) / qss;
                                        finalWindow[x][2] = Math.round(window[x][2] * qsb) / qsb;
                              }
                    }


                    for (int i = positionx - radiusx; i < positionx + radiusx; i++) {
                              for (int j = positiony - radiusy; j < positiony + radiusy; j++) {
                                        for (int color = 0; color <= 2; color++) {
                                                  if (Mathematician.valid((i - positionx + radiusx) + (j - positiony + radiusy) * radiusx * 2, 2 * radiusx * 2 * radiusy)) {
                                                            if (Mathematician.valid(i + j * width, width * height)) {
                                                                      hsbOriginal[i + j * width][color] = finalWindow[(i - positionx + radiusx) + (j - positiony + radiusy) * radiusx * 2][color];
                                                            }
                                                  }
                                        }
                              }
                    }


                    int[] sampleVector1 = filteringHistogram(0, show, channel, finalWindow);
                    int[] sampleVector2 = filteringHistogram(1, show, channel + 1, finalWindow);
                    int[] sampleVector3 = filteringHistogram(2, show, channel + 2, finalWindow);

                    int[][] sampleVector = {sampleVector1, sampleVector2, sampleVector3};

                    return sampleVector;
          }

          private boolean lightNearby(int x, float[][] window) {
                    int radius = 9;

                    for (int y = x - radius; y < x + radius; y++) {
                              if (Mathematician.valid(y, window.length)) {
                                        if (window[y][2] > 0.1f) {
                                                  window[y][2] = 1f;

                                                  return true;
                                        }
                              }
                    }

                    return false;


          }

          private int[][] allRoundHistogram(int positionx, int positiony, int radius, boolean show, int channel) {
                    int[] sampleVector1 = roundHistogram(positionx, positiony, radius, 0, false, channel);
                    int[] sampleVector2 = roundHistogram(positionx, positiony, radius, 1, false, channel + 1);
                    int[] sampleVector3 = roundHistogram(positionx, positiony, radius, 2, false, channel + 2);

                    int[][] sampleVector = {sampleVector1, sampleVector2, sampleVector3};

                    return sampleVector;
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
                    if (vector != null) {
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
