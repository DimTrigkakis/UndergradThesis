/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Concepts;

import java.awt.Point;

/**
 *
 * @author James
 */
public class Person { // for minimap
          
          private Point p;
          private String name;
          private boolean isEnemy;

          public Person(Point p, String name, boolean isEnemy) {
                    this.p = p;
                    this.name = name;
                    this.isEnemy = isEnemy;
          }
          
          public Person(Person p) {
                    this.p = new Point((int)p.getLocation().getX(),(int)p.getLocation().getY());
                    this.name = p.getName();
                    this.isEnemy = p.isEnemy();
          }
          
          public boolean isEnemy()
          {
                    return isEnemy;
          }
          
          public void print()
          {
                    System.out.println("This is "+ name+" at "+p.x+" , "+p.y);
          }

          public Point getLocation() {
                    return p;
          }
          public void setP(Point p) {
                    this.p = new Point(p);
          }

          public String getName() {
                    return name;
          }
          
          
}
