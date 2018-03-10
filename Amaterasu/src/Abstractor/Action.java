/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstractor;

import Abstractor.ActionFormation.ActionForm;
import Abstractor.ActionFormation.ActionType;
import java.awt.Point;

/**
 *
 * @author James
 */
public class Action {
          
          Point target;
          Point target2 = null;
          ActionType actionType;
          ActionForm actionForm;

          public ActionForm getActionForm() {
                    return actionForm;
          }
          int urgency = 0;

          public Point getTarget() {
                    return target;
          }

          public ActionType getActionType() {
                    return actionType;
          }

          public int getUrgency() {
                    return urgency;
          }

          public void setUrgency(int urgency) {
                    this.urgency = urgency;
          }

          public void setActionType(ActionType actionType) {
                    this.actionType = actionType;
          }

          public Action(Point target,ActionType actionType,ActionForm actionForm,int urgency) {
                    this.target = target;
                    this.actionType = actionType;
                    this.actionForm = actionForm; 
                    this.urgency = urgency;
          }
          
          public Action(Point target,Point target2,ActionType actionType,ActionForm actionForm,int urgency) {
                    this.target = target;
                    this.target2 = target2;
                    this.actionType = actionType;
                    this.actionForm = actionForm;
                    this.urgency = urgency;
          }
          
          
          
}
