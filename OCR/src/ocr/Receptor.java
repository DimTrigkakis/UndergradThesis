/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author jim
 */
public class Receptor implements Comparable<Receptor>,Serializable {
    
    @Override
    public int compareTo(Receptor info) {
        if (this.entropy < info.entropy) {
            return -1;
        } else if (this.entropy > info.entropy) {
            return 1;
        } else {
            return 0;
        }
    }

    int position;
    int angle;
    int entropy;

    public void setEntropy(int entropy) {
        this.entropy = entropy;
    }

    public Receptor(int position, int angle) {
        this.position = position;
        this.angle = angle;
        entropy = 100;
    }
    
    public boolean cross(boolean[] hsbBoolean)
    {
        boolean cross = false;
        
        if (angle == 0)
        {
            for (int i=-2;i<=2;i++)
            {
                if (hsbBoolean[position+i*(int)Math.sqrt(hsbBoolean.length)])
                    cross  = true;
            }
        }
        
        if (angle == 2)
        {
            for (int i=-2;i<=2;i++)
            {
                if (hsbBoolean[position+i])
                    cross = true;
            }
        }
        
        if (angle == 1)
        {
            for (int i=-2;i<=2;i++)
            {
                if (hsbBoolean[position+i*(int)Math.sqrt(hsbBoolean.length)+i])
                    cross = true;
            }
        }
        
        if (angle == 3)
        {
            for (int i=-2;i<=2;i++)
            {
                if (hsbBoolean[position+i*(int)Math.sqrt(hsbBoolean.length)-i])
                    cross = true;
            }
        }
        
        
        
        return cross;
    }
    
    
    public void paint(float[][] hsbOriginal,int n,float max)
    {
        float value = 0.8f*(n/max);
        if (angle == 0)
        {
            for (int i=-2;i<=2;i++)
            {
                hsbOriginal[position+i*(int)Math.sqrt(hsbOriginal.length)]= new float[]{value,1f,1f};
            }
        }
        
        if (angle == 2)
        {
            for (int i=-2;i<=2;i++)
            {
                hsbOriginal[position+i]= new float[]{value,1f,1f};
            }
        }
        
        if (angle == 1)
        {
            for (int i=-2;i<=2;i++)
            {
                hsbOriginal[position+i*(int)Math.sqrt(hsbOriginal.length)+i]= new float[]{value,1f,1f};
            }
        }
        
        if (angle == 3)
        {
            for (int i=-2;i<=2;i++)
            {
                hsbOriginal[position+i*(int)Math.sqrt(hsbOriginal.length)-i]= new float[]{value,1f,1f};
            }
        }
        
        
        
    }
        
}


