/*
 * Vertex.java
 *
 * Created on 12 June 2014, 14:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Agus
 */
public class Vertex {
    private Vertex low, high;
    private int index;
    private int value;
    private int id;
    private boolean mark;
    
    /** Creates a new instance of Vertex */
    public Vertex() {
    }
    
    public Vertex(Vertex lo, Vertex hi, int ind, int val, int ID, boolean mk) {
        this.low = lo;
        this.high = hi;
        this.index = ind;
        this.value = val;
        this.id = ID;
        this.mark = mk;
    }
     
    public void setLow(Vertex lo) {
        this.low = lo;
    }
    
    public Vertex getLow() {
        return low;
    }
    
    public void setHigh(Vertex hi) {
        this.high = hi;
    }
    
    public Vertex getHigh() {
        return high;
    }
    
    public void setIndex(int ind) {
        this.index = ind;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setValue(int val) {
        this.value = val;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setId(int ID) {
        this.id = ID;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setMark(boolean mk) {
        this.mark = mk;
    }
    
    public boolean getMark() {
        return mark;
    }
}
