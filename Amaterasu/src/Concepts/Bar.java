package Concepts;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author James
 */
public class Bar {

          int x;
          int y;
          int length;

          public Bar(int x, int y, int length) {
                    this.x = x;
                    this.y = y;
                    this.length = length;
          }

          public int getX() {
                    return x;
          }

          public int getY() {
                    return y;
          }

          public int getLength() {
                    return length;
          }

          public void print() {
                    System.out.println(x + " " + y + " " + length);
          }
}
