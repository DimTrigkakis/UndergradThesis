/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author jim
 */
public class OCR {

          /**
           * @param args the command line arguments
           */
          static int length = 400;
          static final int width = 20;
          static int n = 5000;
          static final int images = 44;
          private static float[][][] hsbOriginal = new float[images][width * width][3];
          private static boolean[][] hsbBoolean = new boolean[images][width * width];
          // for our loaded image          
          private static float[][] hsbOriginalFinal = new float[width * width][3];
          private static boolean[] hsbBooleanFinal = new boolean[width * width];
          static boolean[] tableFinal = new boolean[n];
          private static double[][] input = new double[images][];
          private static double[][] output = new double[images][];

          public static void main(String[] args) throws InterruptedException, IOException, FileNotFoundException, ClassNotFoundException {
                    // TODO code application logic here
                    String s = new File("").getAbsolutePath();
                    recognize(ImageManipulator.loadFile(s.concat("\\numbers\\char_" + 5 + ".png")));
          }
          static BufferedImage[] characters = new BufferedImage[images];
          static boolean[][] table = new boolean[n][images];
          
          static int[] characterValues = new int[images];

          public static int recognize(BufferedImage bi) throws InterruptedException, IOException, FileNotFoundException, ClassNotFoundException {
                    ImageManipulator.init();
                    String s = new File("").getAbsolutePath();

                    boolean loadImages = false;
                    boolean loadFromMemory = true;
                    boolean trainingLoad = true;
                    
                    int[] pixel = new int[3];

                    
                    if (loadImages) {
                              int k =0;
                              
                              for (int i = 0; i < 10; i++) {
                                        
                                        if (new File(s.concat("\\numbersIdeal\\char_" + i + ".png")).exists())
                                        {
                                        characters[k]= ImageManipulator.loadFile(s.concat("\\numbersIdeal\\char_" + i + ".png"));
                                        characterValues[k++] = i;
                                        System.out.println(s.concat("\\numbersIdeal\\char_" + i + ".png"));
                                        }
                              }
                              
                              for (int i = 0; i < 10; i++) {
                                        
                              for (int j = 1;j< 10; j++) {
                                        
                                        if (new File(s.concat("\\numbers\\broken\\char_" + i + "_broken"+j+".png")).exists())
                                        {
                                        characters[k] = ImageManipulator.loadFile(s.concat("\\numbers\\broken\\char_" + i + "_broken"+j+".png"));
                                        characterValues[k++] = i;
                                        System.out.println(s.concat("\\numbers\\broken\\char_" + i + " broken "+j+".png"));
                                        }
                              }
                              }
                              


                              for (int j = 0; j < images; j++) {
                                        for (int i = 0; i < length; i++) {

                                                  characters[j].getRaster().getPixel(i % width, i / width, pixel);
                                                  Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginal[j][i]);

                                                  if (hsbOriginal[j][i][2] < 0.5f) {
                                                            hsbBoolean[j][i] = true;
                                                  } else {
                                                            hsbBoolean[j][i] = false;
                                                  }
                                                  

                                        }
                              }
                    }

                    for (int i = 0; i < length; i++) {

                              bi.getRaster().getPixel(i % width, i / width, pixel);
                              Color.RGBtoHSB(pixel[0], pixel[1], pixel[2], hsbOriginalFinal[i]);

                              if (hsbOriginalFinal[i][2] < 0.5f) {
                                        hsbBooleanFinal[i] = true;
                              } else {
                                        hsbBooleanFinal[i] = false;
                              }

                    }


                    Random r = new Random();
                    ArrayList<Receptor> receptors = new ArrayList<>();

                    if (!loadFromMemory) {
                              for (int i = 0; i < n; i++) {

                                        Receptor receptor = new Receptor(r.nextInt(16) + 2 + (width) * (r.nextInt(16) + 2), r.nextInt(4));
                                        receptors.add(receptor);

                                        if (loadImages) {
                                                  for (int j = 0; j < images; j++) {
                                                            if (receptor.cross(hsbBoolean[j])) {
                                                                      table[i][j] = true;
                                                            } else {
                                                                      table[i][j] = false;
                                                            }




                                                            receptor.paint(hsbOriginal[j], i, n);
                                                  }
                                        }

                                        if (receptor.cross(hsbBooleanFinal)) {
                                                  tableFinal[i] = true;
                                        } else {
                                                  tableFinal[i] = false;
                                        }
                              }
                    } else {

                              receptors = (ArrayList<Receptor>) (load("receptors"));
                              n = receptors.size();

                              for (int i = 0; i < n; i++) {
                                        Receptor receptor = receptors.get(i);

                                        if (loadImages) {
                                                  for (int j = 0; j < images; j++) {
                                                            if (receptor.cross(hsbBoolean[j])) {
                                                                      table[i][j] = true;
                                                            } else {
                                                                      table[i][j] = false;
                                                            }


                                                  }
                                        }

                                        if (receptor.cross(hsbBooleanFinal)) {
                                                  tableFinal[i] = true;
                                        } else {
                                                  tableFinal[i] = false;
                                        }
                              }
                    }

                    //save(receptors,"receptors");

                    if (loadImages) {
                              for (int i = 0; i < n; i++) {
                                        int sum = 0;
                                        for (int j = 0; j < images; j++) {
                                                  // entropy

                                                  if (table[i][j]) {
                                                            sum++;
                                                  } else {
                                                            sum--;
                                                  }

                                                  //     System.out.print("image " + j + " ");
                                                  //     System.out.print(table[i][j] + " ");




                                        }
                                        sum = Math.abs(sum);
                                        // System.out.println(" receptor " + i + " has entropy " + sum);
                                        receptors.get(i).setEntropy(sum);


                              }
                    }

                    // System.out.println(receptors.size());

                    Collections.sort(receptors);



                    /*
                     for (int i = 0; i < receptors.size(); i++) {
                     Receptor rr = receptors.get(i);
                     if (rr.entropy > images /3 ) {
                     receptors.remove(i--);
                     } else {
                     System.out.println("entropy is " + rr.entropy);
                     }
                     }*/

                    int receptorNumber = 500;

                    for (int i = 0; i < receptors.size(); i++) {
                              if (i == receptorNumber) {
                                        receptors.remove(i--);
                              } else {
                                        for (int j = 0; j < images; j++) {
                                                  receptors.get(i).paint(hsbOriginal[j], i, receptorNumber);
                                        }
                              }
                    }

                    // System.out.println(receptors.size());

                    if (loadImages) {
                              for (int i = 0; i < images; i++) {
                                        input[i] = new double[receptors.size()];
                                        for (int j = 0; j < receptors.size(); j++) {
                                                  if (table[j][i]) {
                                                            input[i][j] = 1f;
                                                  } else {
                                                            input[i][j] = 0f;
                                                  }
                                        }

                                        output[i] = new double[10]; // number of digits we have

                                        for (int k = 0; k < 10; k++) {
                                                
                                                  if (characterValues[i]==k) {
                                                            output[i][k] = 1f;
                                                  } else {
                                                            output[i][k] = 0f;
                                                  }
                                        }
                              }
                    }
                    
                    double[][] outputFinal = new double[1][images];

                    double[][] inputFinal = new double[1][receptors.size()];
                    for (int j = 0; j < receptors.size(); j++) {
                              if (tableFinal[j]) {
                                        inputFinal[0][j] = 1f;
                              } else {
                                        inputFinal[0][j] = 0f;
                              }
                    }


                    for (int i = 0; i < images; i++) {
                              for (int j = 0; j < receptors.size(); j++) {
                                        //System.out.print(input[i][j] + " ");
                              }
                              //System.out.println();
                    }

                    for (int i = 0; i < images; i++) {
                              for (int j = 0; j < images; j++) {
                                        // System.out.print(output[i][j] + " ");
                              }
                              //System.out.println();
                    }
                    NeuralDataSet trainingSet = null;
                    if (loadImages) {
                              trainingSet = new BasicNeuralDataSet(input, output);
                    }

                    BasicNetwork network = new BasicNetwork();

                    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, receptorNumber));
                    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 100));
                    network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 10));

                    network.getStructure().finalizeStructure();

                    network.reset();

                    Train train = new ResilientPropagation(network, trainingSet);


                    if (!trainingLoad) {
                              int epoch = 1;

                              try {
                                        do {

                                                  train.iteration();
                                                  System.out.println("Epoch #" + epoch
                                                          + " Error:" + train.getError());
                                                  epoch++;

                                        } while (train.getError() > 0.01);
                              } catch (Exception e) {
                                        System.out.println(e.getCause());
                              }

                              //EncogDirectoryPersistence.saveObject(new File(s.concat("\\network\\")), network);

                    } else {
                              network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(s.concat("\\network\\")));
                    }

                    /*
                    
                     for (MLDataPair pair : trainingSet) {

                     for (int i = 0; i < pair.getIdeal().getData().length; i++) {
                     System.out.print(pair.getIdeal().getData(i) + " ");
                     }
                     // final MLData output22 = network.compute(pair.getInput());
                     //  System.out.println(
                     // "actual=" + output22.getData(0) + " "+output22.getData(1)+
                     //     ",ideal=" + pair.getIdeal().getData(0));


                              
                     final MLData output22 = network.compute(pair.getInput());
                     for (int i = 0; i < output22.getData().length; i++) {
                     //System.out.print(Math.round(output22.getData(i)) + " ");
                     }
                              
                     //System.out.println();

                     }*/


                    // for (int i=0;i<inputFinal.length;i++)
                    //         System.out.println(inputFinal[i][0]);

                    NeuralDataSet trainingSetIdentity = new BasicNeuralDataSet(inputFinal, outputFinal);

                    MLData output22 = network.compute(trainingSetIdentity.get(0).getInput());
                    for (int i = 0; i < output22.getData().length; i++) {
                              //System.out.print(Math.round(output22.getData(i)) + " ");
                    }

                    //  System.out.println("saving "+receptors.size()+" receptors");
                    //  save(receptors);

                    // train neural network based on receptor truth values


                    /*
                     for (int j = 0; j < images; j++) {
                     BufferedImage bi2 = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
                     for (int i = 0; i < length; i++) {
                     int c = (Color.HSBtoRGB(hsbOriginal[j][i][0], hsbOriginal[j][i][1], hsbOriginal[j][i][2]));
                     bi2.setRGB(i % width, i / width, c);
                     }
                     ImageManipulator.show(bi2, j);
                     }*/



                    //System.out.println("\ndigit is : ");
                    for (int i = 0; i < 10; i++) {
                              if (Math.round(output22.getData(i)) == 1f) {
                                        return i;
                              }

                    }

                    return -1;

                    // XORHelloWorld.main(null);

          }

          private static void save(Object object, String name) throws FileNotFoundException, IOException {
                    FileOutputStream saveFile = new FileOutputStream("receptors.sav");

                    ObjectOutputStream save = new ObjectOutputStream(saveFile);

                    save.writeObject(object);

                    save.close();

          }

          private static Object load(String name) throws FileNotFoundException, IOException, ClassNotFoundException {

                    FileInputStream loadFile = new FileInputStream("receptors.sav");

                    ObjectInputStream load = new ObjectInputStream(loadFile);

                    Object object = load.readObject();

                    load.close();

                    return object;
          }
}
