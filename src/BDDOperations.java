import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;
/*
 * BDDOperations.java
 *
 * Created on 12 June 2014, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Agus
 */

public class BDDOperations {
    
    
    /**
     * Creates a new instance of BDDOperations
     */
    public BDDOperations() {
    }
    
    private static void traverseRecursive(Vertex v) {
        v.setMark(!v.getMark());
        System.out.print(v.getId() + " ");
        if (v.getIndex() <= 3) {
            if (v.getMark() != v.getLow().getMark()) traverseRecursive(v.getLow());
            if (v.getMark() != v.getHigh().getMark()) traverseRecursive(v.getHigh());
        }
    }
    
    public static void traverse(Vertex v) {
        StorageOperations.storeBDDInStorage("bdd1", v);
        Vertex w = StorageOperations.loadBDDFromStorage("bdd1");
        traverseRecursive(w);
    }
    
    private static Vector[] loadList(Vertex v, Vector[] vlist) {
        
        v.setMark(!v.getMark());
        if (vlist[v.getIndex()- 1] == null) vlist[v.getIndex()- 1] = new Vector();
        vlist[v.getIndex()- 1].add(v);
        if (v.getIndex() <= 3) {
            if (v.getMark() != v.getLow().getMark()) loadList(v.getLow(), vlist);
            if (v.getMark() != v.getHigh().getMark()) loadList(v.getHigh(), vlist);
        }
        return vlist;
    }
    
    private static boolean isGreater(int[] el1, int[] el2) {
        if (el1[0] > el2[0]) return true;
        else {
            if (el2[0] > el1[0]) return false;
            else {
                if (el1[1] > el2[1]) return true;
                else return false;
            }
        }
    }
    
    private static Vector sortQ(Vector Q) {
        int n = Q.size();
        for (int pass=1; pass < n-1; pass++) {  
            for (int i=0; i < n-pass; i++) {
                QElement currentEl = (QElement) Q.elementAt(i);
                QElement nextEl = (QElement) Q.elementAt(i+1);
                if (isGreater(currentEl.getKey(), nextEl.getKey())) {
                    Q.setElementAt(nextEl,i);
                    Q.setElementAt(currentEl,i+1);
                }
            }
        }
        return Q;
    }
    
    public static Vertex reduce(Vertex v) {
        Vertex[] subgraph = new Vertex [9];
        Vector[] vlist = new Vector [4];
        
        StorageOperations.storeBDDInStorage("bdd1", v);
        Vertex w = StorageOperations.loadBDDFromStorage("bdd1");
        
        vlist = loadList(w, vlist);
        int nextId = 0;
        for (int i = 3; i >=0; i--) {
            Vector Q = new Vector();
            for (int j=0; j<vlist[i].size(); j++) {
                Vertex u = (Vertex) vlist[i].elementAt(j);
                int[] key = new int[2];
                if (u.getIndex() == 4) {
                    key[0] = u.getValue();
                    key[1] = -1;
                    Q.add(new QElement(key, u));
                } else {
                    if (u.getLow().getId() == u.getHigh().getId()) u.setId(u.getLow().getId());
                    else {
                        key[0] = u.getLow().getId();
                        key[1] = u.getHigh().getId();
                        Q.add(new QElement(key, u));
                    }
                }
            }
            Q = sortQ(Q);
            int[] oldKey = new int[2];
            oldKey[0] = -1;
            oldKey[1] = -1;
            
            for (int j=0; j<Q.size(); j++) {
                QElement currentEl = (QElement) Q.elementAt(j);
                int[] currentKey = currentEl.getKey();
                Vertex u = currentEl.getVertex();
                if ((currentKey[0] == oldKey[0]) && (currentKey[1] == oldKey[1])) u.setId(nextId);
                else {
                    nextId++;
                    u.setId(nextId);
                    subgraph[nextId-1] = u;
                    u.setLow(subgraph[u.getLow().getId()-1]);
                    u.setHigh(subgraph[u.getHigh().getId()-1]);
                    oldKey[0] = currentKey[0];
                    oldKey[1] = currentKey[1];
                }
            }
        }
        return subgraph[v.getId()];
    }
    
    
    private static void satisfyAllRecursive(int i, Vertex v, int[] x) {
        if (v.getValue() == 0) {
            return;
        }
        if ((i==4) && (v.getValue() == 1)) {
            for (int j=1; j<4; j++) {
                System.out.print(x[j] + " ");
            }
            System.out.println();
            return;
        }
        if (v.getIndex() > i) {
            x[i] = 0;
            satisfyAllRecursive(i+1, v, x);
            x[i] = 1;
            satisfyAllRecursive(i+1, v, x);
        } else {
            x[i] = 0;
            satisfyAllRecursive(i+1, v.getLow(), x);
            x[i] = 1;
            satisfyAllRecursive(i+1, v.getHigh(), x);
        }
    }
    
    public static void satisfyAll(Vertex v) {
        StorageOperations.storeBDDInStorage("bdd1", v);
        Vertex w = StorageOperations.loadBDDFromStorage("bdd1");
        int[] x = new int[4];
        satisfyAllRecursive(0, w, x);
    }
       
    private static int satisfyCountRecursive(Vertex v) {
        if (v.getIndex() == 4) return v.getValue();
        else {
            return (int) (satisfyCountRecursive(v.getLow()) * Math.pow(2, v.getLow().getIndex()- v.getIndex()-1) + satisfyCountRecursive(v.getHigh()) * Math.pow(2, v.getHigh().getIndex()- v.getIndex()-1));
        }
    }
    
    public static int satisfyCount(Vertex v) {
        StorageOperations.storeBDDInStorage("bdd1", v);
        Vertex w = StorageOperations.loadBDDFromStorage("bdd1");
        return satisfyCountRecursive(w);
    }
    
    
}
