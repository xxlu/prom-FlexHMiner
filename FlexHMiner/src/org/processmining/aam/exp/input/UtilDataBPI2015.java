package org.processmining.aam.exp.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.exp.HAIMexperiment;
import org.processmining.aam.exp.util.UtilIO;
import org.processmining.aam.exp.util.UtilIOXES;
import org.processmining.aam.model.ActTreeNode;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class UtilDataBPI2015 {
	
	public XLog getBPI2015() {
		String readlogfilename = "C:\\Users\\xlu330\\Dropbox\\_Data\\BPIC\\2015 Municipalities\\BPIC15_2.xes";
		
		return UtilIOXES.importXesLog(readlogfilename);
	}

	
	public static ActTreeNode getBPI2015Labels(XLog log) {
		Set<String> labels = new THashSet<>();
		for (XTrace t : log) {
			for (XEvent e : t) {
				String label = XConceptExtension.instance().extractName(e);
				labels.add(label);
			}
		}
		ActTreeNode root = new ActTreeNode();
		Map<String, ActTreeNode> map = new THashMap<>();

		for (String label : labels) {
			List<String> path = new ArrayList<String>();
			
			int sub = label.indexOf("_", 3);
			if(sub >= 0) {
				String parentProc = label.substring(0, sub);
				path.add(parentProc);
				
				// *** The commented code are used for DK2
//				if (label.startsWith("01_HOOFD") || label.startsWith("08_AWB45")) {
				if (label.startsWith("01_HOOFD")) {
					String subParentProc = label.substring(0, sub + 2);
					path.add(subParentProc);
				}
				
//				if (label.startsWith("01_HOOFD_4")) {
//					String subsubParentProc = label.substring(0, sub + 3);
//					if(subsubParentProc.startsWith("01_HOOFD_49")){
//						path.add(subsubParentProc);
//					} else {
//						path.add("01_HOOFD_4X");
//					}				
//				}
			}
			path.add(label);
			
			HAIMexperiment.addPathToHierachy(root, map, path);		
			System.out.println(UtilIO.concatenate(path, ","));

//			if (label.startsWith("01_HOOFD") || label.startsWith("08_AWB45")) {
//				String subParentProc = label.substring(0, sub + 2);
//				ActTreeNode subParentNode = map.get(subParentProc);
//				if (subParentNode == null) {
//					subParentNode = new ActTreeNode(subParentProc);
//					map.put(subParentProc, subParentNode);
//					parentNode.add(subParentNode);
//				}
//				subParentNode.add(new ActTreeNode(label));
//			} else {
//				parentNode.add(new ActTreeNode(label));
//			}
		}
		return root;
	}


}
