/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Workspace.Elements;

import java.util.ArrayList;

/**
 *
 * @author James
 */
public class Entity {
          
          String name;
          int place;
          public ArrayList<Descriptor> descriptors = new ArrayList<>();

          public Entity(String name,int place) {
                    this.place = place;
                    this.name = name;
          }

          public String getName() {
                    return name;
          }
          
          public void addDescriptor(Descriptor d)
          {
                    descriptors.add(d);
          }
          
          public int getPlace() {
                    return place;
          }
          
          public void print()
          {
                    System.out.print(name+" ");
                    for (Descriptor d : descriptors)
                              d.print();
          }
          
          public boolean hasDescriptor(String name)
          {
                    for (Descriptor d : descriptors)
                    {
                              if (name.equals(d.name))
                                        return true;
                    }
                    return false;
          }
          
}
