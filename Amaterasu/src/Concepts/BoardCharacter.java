/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts;

/**
 *
 * @author James
 */
public class BoardCharacter {
          
          int x;
          int y;
          int hp;
          int mp;
          int type;
          
          // type 1 = self
          // type 2 = enemy minion
          // type 3 = ally minion
          // type 4 = enemy character
          // type 5 = ally character
          // type 6 = enemy turret
          // type 7 = ally turret

          public BoardCharacter(int x, int y, int length) {
                    this.x = x;
                    this.y = y;
                    this.hp = length;
          }
          
          public int getType()
          {
                    return type;
          }

          public int getX() {
                    return x;
          }

          public int getY() {
                    return y;
          }

          public int getHp() {
                    return hp;
          }
          public int getMp() {
                    return mp;
          }

          public BoardCharacter(int x, int y, int mp,int hp, int type) {
                    this.x = x;
                    this.y = y;
                    this.hp = hp;
                    this.mp = mp;
                    this.type = type;
          }

          public void print() {
                    System.out.println(x + " " + y + " " + hp);
          }
          
}
