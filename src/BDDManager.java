import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Agus
 */
public class BDDManager {
    private String managerName;
    private String[] orderedVariables = new String[3];
    private Vector bdds = new Vector();
    private XVertex[][] T;
    private String currentBDDName;
    private int count;
    private String DOTResult="";
    private String common_dir =  System.getProperty("user.dir"); 
    
    /** Creates a new instance of BDDManager */
    public BDDManager(String name) {
        this.managerName = name;
        StorageOperations.clearStorage();
    }
    
    private void createVarBDDPos(int varCount) {
        XVertex vertex;
        String key;
        
        key = managerName + ":" + orderedVariables[varCount] + ":0";
        vertex = new XVertex(key, null, null, 3, 0, 0, false); 
        StorageOperations.storeVertexInStorage(vertex, key);
        
        key = managerName + ":" + orderedVariables[varCount] + ":1";
        vertex = new XVertex(key, null, null, 3, 1, 1, false);
        StorageOperations.storeVertexInStorage(vertex, key);
        
        key = managerName + ":" + orderedVariables[varCount] + ":root";
        vertex = new XVertex(key, managerName + ":" + orderedVariables[varCount] + ":0", managerName + ":" + orderedVariables[varCount] + ":1", varCount, -1, 2, false);
        StorageOperations.storeVertexInStorage(vertex, key);
        
        bdds.add(key);
    }
    
    
    private void createVarBDDNeg(int varCount) {
        XVertex vertex;
        String key;
        
        key = managerName + ":-" + orderedVariables[varCount] + ":0";
        vertex = new XVertex(key, null, null, 3, 1, 0, false); 
        StorageOperations.storeVertexInStorage(vertex, key);
        
        key = managerName + ":-" + orderedVariables[varCount] + ":1";
        vertex = new XVertex(key, null, null, 3, 0, 1, false);
        StorageOperations.storeVertexInStorage(vertex, key);
        
        key = managerName + ":-" + orderedVariables[varCount] + ":root";
        vertex = new XVertex(key, managerName + ":-" + orderedVariables[varCount] + ":0", managerName + ":-" + orderedVariables[varCount] + ":1", varCount, -1, 2, false);
        StorageOperations.storeVertexInStorage(vertex, key);
        
        bdds.add(key);
    }
    
    private void createVarBDDs() {
        for (int i=0; i<3; i++) createVarBDDPos(i);
        for (int i=0; i<3; i++) createVarBDDNeg(i);
    }
    
    public void setOrderedVariables(String[] vars) {
        this.orderedVariables = vars;
        createVarBDDs();
    }
    
    public XVertex getBDD(String name) {
        boolean found = false;
        int numOfBdds = bdds.size();
        int i = 0;
        String key = null;
        while ((!found) && (i<numOfBdds)) {
            key = (String) bdds.elementAt(i);
            if (key.replace(managerName + ":","").replace(":root","").equalsIgnoreCase(name)) found = true;
            else i++;
        }
        return StorageOperations.loadVertexFromStorage(key);
    }
    
    private void printNodeInfoRecursive(XVertex v) {
        XVertex low = null;
        XVertex high = null;
        
        if (v.getIndex() < 3) {
            low = StorageOperations.loadVertexFromStorage(v.getLow());
            high = StorageOperations.loadVertexFromStorage(v.getHigh());
        }
        
        v.setMark(!v.getMark());
       
        String label = null;
        
        if (v.getValue() < 0) label = orderedVariables[v.getIndex()];
        else label = "" + v.getValue();
        
        //DOT file builder
        DOTResult += v.getId() + " [label=\"" + label + "\"];\n";
        
        System.out.println(v.getId() + " [label=\"" + label + "\"];");
        
        if (v.getIndex() < 3) {
            if (v.getMark() != low.getMark()) printNodeInfoRecursive(low);
            if (v.getMark() != high.getMark()) printNodeInfoRecursive(high);
        }
        StorageOperations.storeVertexInStorage(v, v.getKey());
    }
    
    
    private void printEdgeInfoRecursive(XVertex v) {
        XVertex low = null;
        XVertex high = null;
        
        if (v.getIndex() < 3) {
            low = StorageOperations.loadVertexFromStorage(v.getLow());
            high = StorageOperations.loadVertexFromStorage(v.getHigh());
        }
        
        v.setMark(!v.getMark());
        
        if (v.getValue() < 0) {
            //DOT file builder
            DOTResult += v.getId() + " -> " + low.getId() + " [label=\"0\"];\n";
            DOTResult += v.getId() + " -> " + high.getId() + " [label=\"1\"];\n";
            
            System.out.println(v.getId() + " -> " + low.getId() + " [label=\"0\"];");
            System.out.println(v.getId() + " -> " + high.getId() + " [label=\"1\"];");
        }
        
        if (v.getIndex() < 3) {
            if (v.getMark() != low.getMark()) printEdgeInfoRecursive(low);
            if (v.getMark() != high.getMark()) printEdgeInfoRecursive(high);
        }
        StorageOperations.storeVertexInStorage(v, v.getKey());
    }
    
    public void printDOT(XVertex v) {
    	//DOT file builder
    	DOTResult += "digraph {"; 
        System.out.println("digraph {");
        
        printNodeInfoRecursive(v);
        printEdgeInfoRecursive(v);
        
        //DOT file builder
        DOTResult += "}"; 
        System.out.println("}");
        
        //save DOT to file  
        exportDOT();
    }
    
    public void printDOT(String name) {
    	//DOT file builder
    	DOTResult += "digraph {\n"; 
        System.out.println("digraph {");
        
        XVertex v = getBDD(name);
        printNodeInfoRecursive(v);
        printEdgeInfoRecursive(v);
        
        //DOT file builder
        DOTResult += "}\n"; 
        System.out.println("}"); 
    }
    
    private void exportDOT(){
		try{ 
	        //File name generated by date
	        DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
	        Date date = new Date();
	        //Directory setting 
			String filename = common_dir + "/graph/BDD_"+dateFormat.format(date)+".gv"; 
			PrintWriter out = new PrintWriter(filename);
			out.println(DOTResult);
			out.close();
			System.out.println("Success export DOT file to: "+ filename);
			
			exportDOTImage(filename);
			
		}catch(Exception e){
			System.out.println(e);
		} 
    }
    
    private void exportDOTImage(String gvFIleName)
	{ 
		DOTManager gv = new DOTManager();
		gv.readSource(gvFIleName); 

		String type = "gif";
		//  String type = "dot, fig, pdf, ps, svg, png, plain"; 
		 
		String repesentationType= "dot";
		//	String repesentationType= "neato, fdp, sfdp, twopi, circo"; 
		
		File out = new File(gvFIleName+ "."+ type); 
		byte[] graph = gv.getGraph(gv.getDotSource(), type, repesentationType); 
		int res = gv.writeGraphToFile(graph , out );
		if(res==1){
			System.out.println("Success export graph Image to: "+out.getAbsolutePath());
			showDOTImage(out.getAbsolutePath());
		}else{
			System.out.println("Failed export graph Image to: "+out.getAbsolutePath());
		}
	}
    
    private void showDOTImage(String imgFIleName){
    	//Show Generated file in swing form
    	try {
    		BufferedImage img = null;
    	    img = ImageIO.read(new File(imgFIleName));
    	    
            //Create and set up the window.
            JFrame frame = new JFrame("BDD Result");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
            JLabel imageLabel = new JLabel(new ImageIcon(img)); 
            frame.getContentPane().add(imageLabel); 
            //Display the window.
            frame.pack();
            frame.setLocation(200,200); 
            frame.setAlwaysOnTop(true);
            frame.requestFocus();
            frame.setVisible(true); 
            
    	} catch (IOException e) {
    		System.out.println(e);
    	}  
    }
    
    private Vector[] loadList(XVertex v, Vector[] vlist) {
        v.setMark(!v.getMark());
        vlist[v.getIndex()].add(v);
        if (v.getIndex() < 3) {
            XVertex low = StorageOperations.loadVertexFromStorage(v.getLow());
            XVertex high = StorageOperations.loadVertexFromStorage(v.getHigh());
            if (v.getMark() != low.getMark()) loadList(low, vlist);
            if (v.getMark() != high.getMark()) loadList(high, vlist);
        }
        return vlist;
    }
    
    private boolean isGreater(int[] el1, int[] el2) {
        if (el1[0] > el2[0]) return true;
        else {
            if (el1[0] < el2[0]) return false;
            else {
                if (el1[1] > el2[1]) return true;
                else return false;
            }
        }
    }
    
    private Vector sortQ(Vector Q) {
        int n = Q.size();
        for (int pass=0; pass < n-1; pass++) {  
            for (int i=0; i < n-pass-1; i++) {
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
    
    public XVertex reduce(XVertex v) {
        XVertex[] subgraph = new XVertex [999];
        Vector[] vlist = new Vector [4];
        
        for (int i=0; i<4; i++) {
            vlist[i] = new Vector();
        }
        
        vlist = loadList(v, vlist);
        int nextId = -1;
        for (int i = 3; i >=0; i--) {
            Vector Q = new Vector();
            for (int j=0; j<vlist[i].size(); j++) {
                XVertex u = (XVertex) vlist[i].elementAt(j);
                int[] key = new int[2];
                if (u.getIndex() == 3) {
                    key[0] = u.getValue();
                    key[1] = -1;
                    Q.add(new QElement(key, u));
                } else {
                    XVertex low = StorageOperations.loadVertexFromStorage(u.getLow());
                    XVertex high = StorageOperations.loadVertexFromStorage(u.getHigh());
                    if (low.getId() == high.getId()) u.setId(low.getId());
                    else {
                        key[0] = low.getId();
                        key[1] = high.getId();
                        Q.add(new QElement(key, u));
                    }
                }
                StorageOperations.storeVertexInStorage(u, u.getKey());
            }
            Vector P = sortQ(Q);
            int[] oldKey = new int[2];
            oldKey[0] = -9;
            oldKey[1] = -9;
            
            for (int j=0; j<P.size(); j++) {
                QElement currentEl = (QElement) P.elementAt(j);
                int[] currentKey = currentEl.getKey();
                XVertex u = currentEl.getVertex();
               
                if ((currentKey[0] == oldKey[0]) && (currentKey[1] == oldKey[1])) u.setId(nextId);
                else {
                    nextId++;
                    u.setId(nextId);
                    
                    subgraph[nextId] = u;
                    
                    XVertex low = null;
                    XVertex high = null;
        
                    if (u.getIndex() < 3) {
                        low = StorageOperations.loadVertexFromStorage(u.getLow());
                        high = StorageOperations.loadVertexFromStorage(u.getHigh());
                    }
                    
                    if (low != null) u.setLow(subgraph[low.getId()].getKey());
                    if (high != null) u.setHigh(subgraph[high.getId()].getKey());
                    
                    oldKey[0] = currentKey[0];
                    oldKey[1] = currentKey[1];
                }
                StorageOperations.storeVertexInStorage(u, u.getKey());
            }
        }
        return subgraph[v.getId()];
    }
    
    private boolean intToBool(int a) {
        if (a == 0) return false;
        else return true;
    }
    
    private int evaluate(int a, int b, String op) {
        if ((a > -1) && (b > -1)) {
            boolean A = intToBool(a);
            boolean B = intToBool(b);
            if (op.equalsIgnoreCase("or")) {
                if (A || B) return 1;
                else return 0;
            } else {
                if (A && B) return 1;
                else return 0;
            }
        } else return -1;
    }
    
    private int min(int a, int b) {
        if (a < b) return a;
        else return b;
    }
    
    public XVertex applyRecursive(XVertex v1, XVertex v2, String op) {
        XVertex u = T[v1.getId()][v2.getId()];
        if (u != null) return u;
        u = new XVertex();
        String key = null;
        if (count == 0) key = managerName + ":" + currentBDDName + ":root";
        else key = managerName + ":" + currentBDDName + ":" + count;
        u.setKey(key);
        u.setId(count);
        count++;
        u.setMark(false);
        T[v1.getId()][v2.getId()] = u;
        
        u.setValue(evaluate(v1.getValue(), v2.getValue(), op));
        if (u.getValue() > -1) {
            u.setIndex(3);
            u.setLow(null);
            u.setHigh(null);
        } else {
            XVertex vlow1, vhigh1, vlow2, vhigh2;
            u.setIndex(min(v1.getIndex(), v2.getIndex()));
    
            if (v1.getIndex() == u.getIndex()) {
                vlow1 = StorageOperations.loadVertexFromStorage(v1.getLow());
                vhigh1 = StorageOperations.loadVertexFromStorage(v1.getHigh());
            } else {
                vlow1 = v1;
                vhigh1 = v1;
            }
            if (v2.getIndex() == u.getIndex()) {
                vlow2 = StorageOperations.loadVertexFromStorage(v2.getLow());
                vhigh2 = StorageOperations.loadVertexFromStorage(v2.getHigh());
            } else {
                vlow2 = v2;
                vhigh2 = v2;
            }
            u.setLow(applyRecursive(vlow1, vlow2, op).getKey());
            u.setHigh(applyRecursive(vhigh1, vhigh2, op).getKey());
        }
        
        StorageOperations.storeVertexInStorage(u, key);
        return u;
    }
    
    public XVertex apply(XVertex v1, XVertex v2, String op, String name) {
        currentBDDName = name;
        count =0;
        T = new XVertex[9][9];
        XVertex u = applyRecursive(v1, v2, op);
        return reduce(u);
    }
    
    public int satisfyCount(XVertex v) {
        if (v.getIndex() == 3) return v.getValue();
        else {
            XVertex low = StorageOperations.loadVertexFromStorage(v.getLow());
            XVertex high = StorageOperations.loadVertexFromStorage(v.getHigh());
            return (int) (satisfyCount(low) * Math.pow(2, low.getIndex()- v.getIndex()-1) + satisfyCount(high) * Math.pow(2, high.getIndex()- v.getIndex()-1));
        }
    }
    
}
