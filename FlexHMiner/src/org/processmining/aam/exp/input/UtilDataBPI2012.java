package org.processmining.aam.exp.input;

import java.io.File;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.in.XesXmlGZIPParser;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.model.ActTreeNode;

public class UtilDataBPI2012 {
	
	
	public XLog getBPI2012or2017() {
		String readlogfilename = "C:\\Users\\Lu000017\\Dropbox\\_Data\\BPIC\\BPI_Challenge_2012.xes.gz";
//	    String readlogfilename = "C:\\Users\\Lu000017\\Dropbox\\_Data\\BPIC\\2017_fi\\BPI Challenge 2017.xes.gz";
//	    String readlogfilename = ".\\logs\\BPIC15_2f.xes.gz";
		XesXmlGZIPParser parser = new XesXmlGZIPParser();
		XLog log = null;
		try {
			log = parser.parse(new File(readlogfilename)).get(0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return log;
	}
	
	
	public static ActTreeNode getBPI2012Labels(XLog log) {
		//		Set<String> subproc = new THashSet<>();

		ActTreeNode root = new ActTreeNode("root");

		ActTreeNode subO = new ActTreeNode("O");
		ActTreeNode subA = new ActTreeNode("A");
		ActTreeNode subW = new ActTreeNode("W");

		root.add(subO);
		root.add(subA);
		root.add(subW);

		for (XTrace t : log) {
			for (XEvent e : t) {
				String label = XConceptExtension.instance().extractName(e);
				if (label.startsWith("O_")) {

					subO.add(new ActTreeNode(label));
					//					subproc.add(label);
				} else if (label.startsWith("A_")) {

					subA.add(new ActTreeNode(label));
					//					subproc.add(label);
				} else if (label.startsWith("W_")) {

					subW.add(new ActTreeNode(label));
					//					subproc.add(label);
				} else {
					root.add(new ActTreeNode(label));
				}
			}
		}

		//		root.updateTreeHeight();
		System.out.print(root.getDepth());
		return root;
	}

}
