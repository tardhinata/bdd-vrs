/*
 * QElement.java
 *
 * Created on 12 June 2014, 19:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Agus
 */
public class QElement {
    private int[] key;
    private Vertex vertex;
    
    /** Creates a new instance of QElement */
    public QElement(int[] key, Vertex vertex) { 
        this.key = key;
        this.vertex = vertex;
    }
    
    public int[] getKey() {
        return key;
    }
    
    public Vertex getVertex() {
        return vertex;
    } 
}
