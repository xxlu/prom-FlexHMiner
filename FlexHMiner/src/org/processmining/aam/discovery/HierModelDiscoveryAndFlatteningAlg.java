package org.processmining.aam.discovery;

import org.processmining.aam.model.ActTreeNode;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

public interface HierModelDiscoveryAndFlatteningAlg {
	
	public MarkedPetrinet discoverAndFlattening(ActTreeNode subproc, ExpDiscoveryAlgorithm dalg );

}
