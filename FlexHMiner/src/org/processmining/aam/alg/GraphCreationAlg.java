package org.processmining.aam.alg;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class GraphCreationAlg {

	
	public DFGraph calculateGraph(XLog log) {
		DFGraph graph = new DFGraph();
		XEventClassifier classifier = new XEventNameClassifier();
		XEventClasses eclasses = XEventClasses.deriveEventClasses(classifier, log);
		
		int[][] dfgMatrix = new int[eclasses.size()][eclasses.size()];
		
		
		for(XTrace t : log) {
			if(t.isEmpty()) {
				continue;
			}
			
			XEvent prevEvent = t.get(0);
			for (int i = 1; i < t.size() ; i++ ) {
				XEvent currEvent = t.get(i);
				
				int prevIndex = eclasses.getClassOf(prevEvent).getIndex();
				int currIndex = eclasses.getClassOf(currEvent).getIndex();
				dfgMatrix[prevIndex][currIndex]++;
				
				
				prevEvent = currEvent;
			}
		}		
		graph.setNodes(eclasses);
		graph.setDFMatrix(dfgMatrix);
		return graph;
	}
	
}
