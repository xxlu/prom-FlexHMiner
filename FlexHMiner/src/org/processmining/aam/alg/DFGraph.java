package org.processmining.aam.alg;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;

public class DFGraph {

	
	private int[][] dfgMatrix;
	private XEventClasses nodes;

	public void setDFMatrix(int[][] dfgMatrix) {
		this.dfgMatrix = dfgMatrix;
	}

	public void setNodes(XEventClasses eclasses) {
		this.nodes = eclasses;
	}

	public int[][] getDFMatrix() {
		return this.dfgMatrix;
	}

	public XEventClass getEventClass(int i) {
		// TODO Auto-generated method stub
		return nodes.getByIndex(i);
	}

}
