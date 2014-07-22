
/**
 *
 * @author Taufan Ardhinata
 */

import java.io.File;

public class DOTVisualizationExample
{
	public static void main(String[] args)
	{
		DOTVisualizationExample p = new DOTVisualizationExample();
		p.start();
		//p.start2();
	}

	// Construct a DOT graph programmatically and store the image in the file system. 
	private void start()
	{
		DOTManager gv = new DOTManager();
		gv.addln(gv.start_graph());
		gv.addln("A -> B;");
		gv.addln("A -> C;");
		gv.addln("B -> C;");
		gv.addln("B -> D;");
		gv.addln("C -> D;");
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());

		gv.increaseDpi();   // 106 dpi
		
		String dir =  System.getProperty("user.dir");
		
		String type = "gif";
		//	    String type = "dot, fig, pdf, ps, svg, png, plain"; 
		
		String repesentationType= "dot";
		//		String repesentationType= "neato, fdp, sfdp, twopi, circo"; 
		
		File out = new File(dir + "/graph/output_example." + type); 
		byte[] graph = gv.getGraph(gv.getDotSource(), type, repesentationType); 
		int res = gv.writeGraphToFile(graph , out );
		if(res==1)
			System.out.println("Success export graph to: "+out.getAbsolutePath());
	}

	//Read the DOT source from a file convert to image and store the image in the file system. 
	private void start2()
	{
		String dir =  System.getProperty("user.dir");
		String input = dir + "/graph/simple.gv"; 
		
		DOTManager gv = new DOTManager();
		gv.readSource(input);
		System.out.println(gv.getDotSource());

		String type = "gif";
		//    String type = "dot, fig, pdf, ps, svg, png, plain"; 
		 
		String repesentationType= "dot";
		//		String repesentationType= "neato, fdp, sfdp, twopi, circo"; 
		
		File out = new File(dir + "/graph/simple." + type); 
		byte[] graph = gv.getGraph(gv.getDotSource(), type, repesentationType); 
		int res = gv.writeGraphToFile(graph , out );
		if(res==1)
			System.out.println("Success export graph to: "+out.getAbsolutePath());
	}
}
