import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;

/*
 * StorageOperations.java
 *
 * Created on 08 July 2014, 20:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Agus
 */
public class StorageOperations {
    private static Jedis jedis;
    private static Vector helper;
    
    /** Creates a new instance of StorageOperations */
    public StorageOperations() {
    }
    
    private static void storeVertexInStorage(String bddId, Vertex v, boolean isHead) {
        Map mVertex = new HashMap();
        if (v.getLow() != null) mVertex.put("low", "" + v.getLow().getId());
        if (v.getHigh() != null) mVertex.put("high", "" + v.getHigh().getId());
        mVertex.put("index", "" + v.getIndex());
        mVertex.put("value", "" + v.getValue());
        mVertex.put("id", "" + v.getId());
        if (v.getMark()) mVertex.put("mark", "true"); 
        else mVertex.put("mark", "false");
        
        if (isHead) jedis.hmset(bddId + ":head", mVertex);
        else jedis.hmset(bddId + ":" + v.getId(), mVertex);
    }
    
    public static void storeBDDInStorageRecursive(String bddId, Vertex v, boolean isHead) {
        
        v.setMark(!v.getMark());
        storeVertexInStorage(bddId, v, isHead);
        if (v.getIndex() <= 3) {
            if (v.getMark() != v.getLow().getMark()) storeBDDInStorageRecursive(bddId, v.getLow(), false);
            if (v.getMark() != v.getHigh().getMark()) storeBDDInStorageRecursive(bddId, v.getHigh(), false);
        }
    }
    
    public static void storeBDDInStorage(String bddId, Vertex v) {
        jedis = new Jedis("ckrck.com", 6597);
        jedis.flushAll();
        storeBDDInStorageRecursive(bddId, v, true);
        jedis.close();
    }
    
    private static Vertex alreadyExist(int id) {
        int i = 0;
        boolean found = false;
        
        Vertex temp = null;
        
        while ((!found) && (i<helper.size())) {
            temp = (Vertex) helper.elementAt(i);
            if (temp.getId() == id) found = true;
            else i++;
        }
        if (found) return temp;
        else return null;
    }
    
    public static Vertex loadBDDFromStorageRecursive(String bddId, String vertexId) {
        Map result = jedis.hgetAll(bddId + ":" + vertexId);
        int id = Integer.parseInt((String)result.get("id"));
        Vertex check = alreadyExist(id);
        if (check != null) return check;
        else {
            Vertex v = new Vertex();
            if (result.get("low") != null) v.setLow(loadBDDFromStorageRecursive(bddId, (String) result.get("low")));
            if (result.get("high") != null) v.setHigh(loadBDDFromStorageRecursive(bddId, (String) result.get("high")));
            v.setIndex(Integer.parseInt((String)result.get("index")));
            v.setValue(Integer.parseInt((String)result.get("value")));
            v.setId(id);
            
            if (((String)(result.get("mark"))).equalsIgnoreCase("true")) v.setMark(true);
            else v.setMark(false);
            helper.add(v);
            return v;
        }
    }
    
    public static Vertex loadBDDFromStorage(String bddId) {
        jedis = new Jedis("ckrck.com", 6597);
        helper = new Vector();
        Vertex v = loadBDDFromStorageRecursive(bddId, "head");
        helper = null;
        jedis.close();
        return v;
    }
    
}
