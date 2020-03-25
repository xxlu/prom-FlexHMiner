package org.processmining.aam.alg;

import org.deckfour.xes.model.XLog;
import org.processmining.aam.model.ActTreeNode;

public interface ActivityClusteringAlg {

	public ActTreeNode calcActivityHierarchy(XLog log);

	
	
}
