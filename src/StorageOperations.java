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
    
    public static void clearStorage() {
        jedis = new Jedis("ckrck.com", 6597);
        jedis.flushAll();
        jedis.close();
    }
    
    public static void storeVertexInStorage(XVertex v, String key) {
        jedis = new Jedis("ckrck.com", 6597);
        Map mVertex = new HashMap();
        mVertex.put("key", "" + v.getKey());
        mVertex.put("low", "" + v.getLow());
        mVertex.put("high", "" + v.getHigh());
        mVertex.put("index", "" + v.getIndex());
        mVertex.put("value", "" + v.getValue());
        mVertex.put("id", "" + v.getId());
        if (v.getMark()) mVertex.put("mark", "true"); 
        else mVertex.put("mark", "false");
        jedis.hmset(key, mVertex);
        jedis.close();
    }
    
    public static XVertex loadVertexFromStorage(String key) {
        jedis = new Jedis("ckrck.com", 6597);
        Map result = jedis.hgetAll(key);
        XVertex v = new XVertex();
        v.setKey(key);
        v.setLow((String) result.get("low"));
        v.setHigh((String) result.get("high"));
        v.setIndex(Integer.parseInt((String)result.get("index")));
        v.setValue(Integer.parseInt((String)result.get("value")));
        v.setId(Integer.parseInt((String)result.get("id")));
        if (((String)(result.get("mark"))).equalsIgnoreCase("true")) v.setMark(true);
        else v.setMark(false);
        jedis.close();
        return v;
    }
}
