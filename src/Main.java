/*
 * Main.java
 *
 * Created on 12 June 2014, 17:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Agus
 */
import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;

public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    public static void main(String args[]) {
        Vertex v6 = new Vertex(null, null, 4, 0, 6, false);
        Vertex v7 = new Vertex(null, null, 4, 1, 7, false);
        Vertex v8 = new Vertex(null, null, 4, 0, 8, false);
        Vertex v9 = new Vertex(null, null, 4, 1, 9, false);
        Vertex v4 = new Vertex(v8, v7, 3, -1, 4, false);
        Vertex v5 = new Vertex(v8, v9, 3, -1, 5, false);
        Vertex v2 = new Vertex(v6, v4, 2, -1, 2, false);
        Vertex v3 = new Vertex(v4, v5, 2, -1, 3, false);
        Vertex v1 = new Vertex(v2, v3, 1, -1, 1, false); 
        
        
        System.out.print("Traverse : ");
        BDDOperations.traverse(v2);
        System.out.println();
        System.out.println("Satisfy count = " + BDDOperations.satisfyCount(v2));  
        //System.out.println();
        //Vertex v = BDDOperations.reduce(v1); 
        /*Vertex v4 = new Vertex(null, null, 4, 0, 4, false);
        Vertex v5 = new Vertex(null, null, 4, 1, 5, false);
        Vertex v3 = new Vertex(v4, v5, 3, -1, 3, false);
        Vertex v2 = new Vertex(v4, v3, 2, -1, 2, false);
        Vertex v1 = new Vertex(v2, v3, 1, -1, 1, false); */
    }
    
}
