/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Thinking;

import Concepts.Bar;
import Concepts.BoardCharacter;

/**
 *
 * @author James
 */
public class Chessboard {
          
          int sizex = 20;
          int sizey = 12;
          BoardCharacter[][] board = new BoardCharacter[sizex][sizey];
          
          // type 1 = self
          // type 2 = enemy minion
          // type 3 = ally minion
          // type 4 = enemy character
          // type 5 = ally character
          // type 6 = enemy turret
          // type 7 = ally turret
          
          public void add(BoardCharacter hp)
          {
                              
                    board[hp.getX()>=sizex?sizex-1:hp.getX()][hp.getY()>=sizey?sizey-1:hp.getY()] = hp;
          }
          
          public void print()
          {
                    for (int j=0;j<sizey;j++)
                    {
                              System.out.println();
                              for (int i=0;i<sizex;i++)
                              {
                              
                                        if (board[i][j] != null)
                                                  System.out.print(board[i][j].getType());
                                        else
                                                  System.out.print("-");
                              
                              }
                    }
                    System.out.println();
                    System.out.println();
          }
          
}
