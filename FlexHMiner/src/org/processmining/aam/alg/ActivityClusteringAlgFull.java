package org.processmining.aam.alg;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.aam.model.ActTreeNode;

public class ActivityClusteringAlgFull implements ActivityClusteringAlg {

	public ActTreeNode calcActivityHierarchy(XLog log) {
	
		
		XEventClassifier classifier = new XEventNameClassifier();
		XEventClasses eclasses = XEventClasses.deriveEventClasses(classifier, log);
		
		
		ActTreeNode root = new ActTreeNode("root");
		for(  XEventClass eclass : eclasses.getClasses()) {
			ActTreeNode sub = new ActTreeNode(eclass.getId());
			root.add(sub);
			
		}
		
		return root;
	}
	
	public String toString() {
		return "F";
	}

}
