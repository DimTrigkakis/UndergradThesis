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
public class Skeleton { // for real view
          
          String name;
          boolean isEnemy = false;
          int hp;
          int mp;
          Point location;
          Point velocityDirection;

          public Point getVelocityDirection() {
                    return velocityDirection;
          }
          
         

          public void setVelocityDirection(Point velocityDirection) {
                    this.velocityDirection = velocityDirection;
          }

          public Skeleton(String name, int hp, int mp, Point location, boolean isEnemy) {
                    this.name = name;
                    this.hp = hp;
                    this.mp = mp;
                    this.location = location;
                    this.isEnemy = isEnemy;
          }
          
          public void removeHp(int hpReduction)
          {
                    this.hp -= hpReduction;
          }

          public Point getLocation() {
                    return location;
          }
          public void setLocation(Point location) {
                    this.location = location;
          }

          public String getName() {
                    return name;
          }

          public void setName(String name) {
                    this.name = name;
          }
          
          public int getHp() {
                    return hp;
          }

          public int getMp() {
                    return mp;
          }
          
          public boolean isEnemy()
          {
                    return isEnemy;
          }
          
          public void setEnemy(boolean isEnemy)
          {
                    this.isEnemy = isEnemy;
          }
          
          public void print()
          {
                    System.out.println("I'm "+name+" , enemy "+isEnemy+" , "+hp+" hp at position "+ location);
          }
          
}
