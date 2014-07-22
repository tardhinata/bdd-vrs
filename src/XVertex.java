/*
 * XXVertex.java 
 * Created on 17 July 2014, 08:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Agus
 */
public class XVertex {
    private String key;
    private String low, high;
    private int index;
    private int value;
    private int id;
    private boolean mark;
    
    /** Creates a new instance of XXVertex */
    public XVertex() {
    }
    
    public XVertex(String ke, String lo, String hi, int ind, int val, int ID, boolean mk) {
        this.key = ke;
        this.low = lo;
        this.high = hi;
        this.index = ind;
        this.value = val;
        this.id = ID;
        this.mark = mk;
    }
    
    public void setKey(String ke) {
        this.key = ke;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setLow(String lo) {
        this.low = lo;
    }
    
    public String getLow() {
        return low;
    }
    
    public void setHigh(String hi) {
        this.high = hi;
    }
    
    public String getHigh() {
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
