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
    
    /** Create a new instance of Main */
    public Main() {
    }
    
    public static void main(String args[]) {
       
        BDDManager mng1 = new BDDManager("Manager1");
        String[] vars = {"x","y","z"};
        mng1.setOrderedVariables(vars);
        
        
        XVertex x = mng1.getBDD("x");
        XVertex negx = mng1.getBDD("-x");
        XVertex y = mng1.getBDD("y");
        XVertex negy = mng1.getBDD("-y");
        XVertex z = mng1.getBDD("z");
        XVertex negz = mng1.getBDD("-z");
        
        XVertex u = mng1.apply(negx, negz, "OR", "-xOR-z");
        XVertex v = mng1.apply(x,y, "AND", "xANDy");
        XVertex w = mng1.apply(u,v, "OR", "(negxORnegz)OR(xANDy)");
        System.out.println("DOT format:");
        mng1.printDOT(w);
        System.out.println("Satisfy count: " + mng1.satisfyCount(w));
    }
    
}
