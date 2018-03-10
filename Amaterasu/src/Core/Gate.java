/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

//import Thesis.ImageAnalyzers.MapAbstraction;
import Abstractor.LearningAbstraction;
import ImageAnalyzers.ActionAbstraction;
import ImageAnalyzers.ChatAbstraction;
import ImageAnalyzers.ImageManipulator;
import ImageAnalyzers.MapAbstraction;
import ImageAnalyzers.Validity;
import ImageAnalyzers.ViewAbstraction;
import MotorAnalyzers.KeyActor;
import MotorAnalyzers.MouseActor;
import Thinking.Strategist;
import Thinking.Tactician;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author James
 */
public class Gate {

          /**
           * @param args the command line arguments
           */
          // static MapAbstraction map = new MapAbstraction();
          final private static boolean showMap = false;
          final private static boolean showView = false;
          final private static boolean showChat = false;
          final private static boolean showStrategy = false;
          final private static boolean showTactics = false;
          final private static boolean debugMap = false;
          final private static boolean debugView = false;
          final private static boolean debugChat = true;
                  
          final private static boolean debugStrategy = false;
          final private static boolean debugTactics = false;
          final private static boolean debugActions = false;
          final private static boolean showActions = false;
          final private static boolean debugLearning = false;
          final private static boolean showLearning = false;
          public static boolean onlyVisionEnabled = false; // used for disabling the map
          static MapAbstraction map = new MapAbstraction(null, showMap, debugMap);
          static ViewAbstraction view = new ViewAbstraction(null, showView, debugView);
          static ChatAbstraction chat = new ChatAbstraction(showChat, debugChat);
          static Strategist strat = new Strategist(showStrategy, debugStrategy);
          static Tactician tact = new Tactician(showTactics, debugTactics);
          static ActionAbstraction act = new ActionAbstraction(showActions, debugActions);
          static LearningAbstraction learn = new LearningAbstraction(showLearning, debugLearning);
          static Validity validity = new Validity();
          public static BufferedImage mapImage = null;
          public static BufferedImage viewImage = null;
          public static BufferedImage actionImage = null;
          public static BufferedImage barImage = null;
          public static BufferedImage moneyImage = null;
          public static BufferedImage validImage = null;
          public static BufferedImage chatImage = null;
          public static boolean continueValidity = false;
          
          // Ahri
          // Recommended item page
          // Ignite Flash
          // 1600x900
          // quickcast with indicators
          

          public static void main(String[] args) throws AWTException, InterruptedException, IOException, ClassNotFoundException, TesseractException {
                    // TODO code application logic here
                    // Thread.sleep(2000); // so that we have time to set up the program, with alt-tab

                    ImageManipulator.init(); // because the arrays won't load in time otherwise
                     
                    /*System.out.println("Run the game in lower resolution?");
                    Scanner scan = new Scanner(System.in);
                    String a = scan.nextLine();
                    if (a.equals("yes"))
                    {
                              System.out.println("ok lowering resolution");
                              Actuator.lowerResolution = true;
                    }     */
                    
                    Actuator.init();   // because it throws exceptions on robot     
                         
                    
                    new MouseActor().start(); // the mouse is a different thread from the rest, so that it can be asynchronous  
                    new KeyActor().start();

                    //Ay4iiyctuator.lowerResolution(); // only if you want to play on a small monitor

                    //new Actor().start();


                    /*/
                     MotionActor.raiseLevel("Q");
                     Thread.sleep(1000);
                     MotionActor.useSkill("Q");
                     //*/

                    /*/
                     KeyActor.addEvent(100,1,500,KeyEvent.VK_Z);
                     KeyActor.addEvent(100,1,500,KeyEvent.VK_K);                    
                     KeyActor.addEvent(100,1,500,KeyEvent.VK_Z);
                     //*/

                    // you simply add events to the actor, and he will execute them

                    /*/                     
                     MouseActor.addEvent(new MouseAction(new Point(500,500),100,1,true,0)); // order matters, but priority matters more
                     MouseActor.addEvent(new MouseAction(new Point(50,0),100,5,true,2));
                     //*/

                    //*/      
                    //MapData.loadFile();
                                
                    Thread.sleep(3000);
                    boolean firstTime = true;
                    while (true) {
                              
                              continueValidity = false;// we don't think we have pressed Y or I accidentally, we run the validity process
                              // to fix it. if it fixes it, we need to run a new loop
                                        
                              if (ActionAbstraction.boughtItems > 0)
                              ActionAbstraction.boughtItems--;
                              
                              //firstTime = false;// !!!!!!!!!!!!!!!!!
                                                                      
                              if (new Random().nextInt(300) == 65) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!                                      
                              {                                        
                                        ExecutorService imageGrab = Executors.newFixedThreadPool(1);
                                        imageGrab.execute(new Actuator("chat"));
                                        imageGrab.shutdown();
                                        imageGrab.awaitTermination(1, TimeUnit.MINUTES);
                                        chat.process(chatImage); // check how chat responds to input, can we use it?
                              }
                              else if (new Random().nextInt(70) == 2 || firstTime) // once per minute or when we have recalled and have to buy stuff
                              {
                                        System.out.println("here");
                                        firstTime = false;
                                        ExecutorService imageGrab = Executors.newFixedThreadPool(2);
                                        imageGrab.execute(new Actuator("map"));
                                        imageGrab.shutdown();
                                         imageGrab = Executors.newFixedThreadPool(2);
                                       // imageGrab.execute(new Actuator("bar"));
                                        //imageGrab.execute(new Actuator("chat"));
                                        imageGrab.execute(new Actuator("action"));
                                        imageGrab.execute(new Actuator("money"));
                                        imageGrab.shutdown();
                                        imageGrab.awaitTermination(1, TimeUnit.MINUTES);
                                        // chatImage = Actuator.getImage(5);
                                                                    
                                        if (mapImage == null)
                                                  continue;
                                        
                                        
                                        System.out.println("here");
                                        map.process(mapImage);       
                                        
                                        if (actionImage != null && moneyImage != null) {
                                                 act.process(actionImage, barImage, moneyImage); // is this possibly sometimes huge? maybe the network
                                        }

                                        //chat.process(ImageManipulator.loadFile("chat"));
                              }
                              else 
                              {
                                        ExecutorService imageGrab = Executors.newFixedThreadPool(2);
                                        imageGrab.execute(new Actuator("validity"));
                                        imageGrab.execute(new Actuator("map"));
                                        imageGrab.shutdown();
                                        imageGrab.awaitTermination(1, TimeUnit.MINUTES);
                                        imageGrab = Executors.newFixedThreadPool(1);
                                        imageGrab.execute(new Actuator("view"));
                                        imageGrab.shutdown();
                                        imageGrab.awaitTermination(1, TimeUnit.MINUTES);
                                        // chatImage = Actuator.getImage(5);
                                        
                                        if (mapImage == null || viewImage == null || validImage == null)
                                                    continue;
                                        validity.process(validImage);
                                        if (continueValidity)
                                                  continue;

                                        map.process(mapImage);

                                        view.process(viewImage); // it's quicker to load the file that to produce it 
                                        strat.process();
                                        learn.process();
                                        //chat.process(ImageManipulator.loadFile("chat"));
                              }
                              

                    }

                    //*/
          }
}
