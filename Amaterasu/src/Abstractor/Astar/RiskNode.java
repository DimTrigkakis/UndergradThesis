package Abstractor.Astar;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author James
 */
public class RiskNode {
          
          int x;
          int y;
          float flow;
          int type; // 1 risk 2 self 3 target
          
          float gscore = 0;
          float fscore = 0;
          
          RiskNode cameFrom = null;
          RiskNode wentTo = null;

          public RiskNode getCameFrom() {
                    return cameFrom;
          }
          public void setWentTo(RiskNode wentTo) {
                    this.wentTo = wentTo;
          }

          public void setCameFrom(RiskNode cameFrom) {
                    this.cameFrom = cameFrom;
          }

          public RiskNode getWentTo() {
                    return wentTo;
          }
          
          public void findFscore(RiskNode target)
          {
                    fscore = (float)(Math.abs(target.x-x)+Math.abs(target.y-y))+gscore;
          }

          public void setGscore(float score)
          {
                    gscore = score;
          }

          public float getGscore() {
                    return gscore;
          }
          
          public int getX() {
                    return x;
          }

          public int getY() {
                    return y;
          }

          public float getFlow() {
                    return flow;
          }

          public int getType() {
                    return type;
          }

          public void setX(int x) {
                    this.x = x;
          }

          public void setY(int y) {
                    this.y = y;
          }

          public void setFlow(float flow) {
                    this.flow = flow;
          }

          public void setType(int type) {
                    this.type = type;
          }

          
}
