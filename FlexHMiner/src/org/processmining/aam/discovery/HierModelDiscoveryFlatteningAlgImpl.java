package org.processmining.aam.discovery;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.deckfour.xes.model.XLog;
import org.processmining.aam.AbstractionAwareMiner;
import org.processmining.aam.exp.util.ExpDummyPluginContext;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

public class HierModelDiscoveryFlatteningAlgImpl implements HierModelDiscoveryAndFlatteningAlg {

	public MarkedPetrinet discoverAndFlattening(ActTreeNode subproc, ExpDiscoveryAlgorithm dalg) {
		ExpDummyPluginContext context = new ExpDummyPluginContext();
		Enumeration<?> enode = subproc.breadthFirstEnumeration();
		while (enode.hasMoreElements()) {
			ActTreeNode sub = (ActTreeNode) enode.nextElement();
			if (sub.isLeaf() ) {//|| alreadyDone(sub)) {
				continue;
			}
			XLog newLowLog = sub.getSubLog();
			MarkedPetrinet markedNet = dalg.minePetrinet(context, newLowLog);
			
			sub.setModel(markedNet);
			
		}
		
		ActTreeNode root = (ActTreeNode)subproc.getRoot();
		MarkedPetrinet rootModel = root.getModel();
		
		Queue<ActTreeNode> queue = new LinkedList<ActTreeNode>();
		updateQueue(queue, root);
		while(!queue.isEmpty()) {
			ActTreeNode node = queue.poll();
			MarkedPetrinet submodel = node.getModel();
			flattenModelWithSubmodel(rootModel, node, submodel);
		}
		
		return rootModel;
	}

	private void flattenModelWithSubmodel(MarkedPetrinet rootModel, ActTreeNode node, MarkedPetrinet submodel) {
		String subpLable = node.getProcessLabel();
		
		// 1. Find start and end transitions of the subprocess
		Transition startSp = null;
		Transition endSp = null;
		Petrinet rootpnet = rootModel.getPetrinet();
		for (Transition t : rootpnet.getTransitions()) {
			if(isSpStartTransition(t, node)) {
				startSp = t;
			} else if (isSpEndTransition(t, node)) {
				endSp = t;
			}
		}

		
		// 2. Make them silent and create a branch 
//		startSp.setInvisible(true);
//		endSp.setInvisible(true);
		
		Petrinet subpnet = submodel.getPetrinet();
		

		HashMap<Place, Place> mapPlaceOrigToClone = new HashMap<Place, Place>();
//		HashMap<Transition, Transition> mapCloneToOrig = new HashMap<Transition, Transition>();
		
		for (Place p : subpnet.getPlaces()) {
			Place clonePlace = rootpnet.addPlace(subpLable + p.getLabel());
			mapPlaceOrigToClone.put(p, clonePlace);			
		}
		for (Transition origTrans : subpnet.getTransitions()) {
			// create transition
			Transition cloneTrans = rootpnet.addTransition(origTrans.getLabel());

			// add input places
			for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : subpnet.getInEdges(origTrans)) {
				Place clonePlace = mapPlaceOrigToClone.get(edge.getSource());
				rootpnet.addArc(clonePlace, cloneTrans);
			}

			// add output places
			for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : subpnet.getOutEdges(origTrans)) {
				Place clonePlace = mapPlaceOrigToClone.get(edge.getTarget());
				rootpnet.addArc(cloneTrans, clonePlace);
			}

		}
		for (Place p : submodel.getInitialMarking()) {
			rootpnet.addArc(startSp, mapPlaceOrigToClone.get(p));
		}
		for(Place p : submodel.getFinalMarking()) {
			rootpnet.addArc(mapPlaceOrigToClone.get(p), endSp);
		}

		// [alternative] if only single start and single end, then change the label.
		
		
		
	}
	
	private void updateQueue(Queue<ActTreeNode> queue, ActTreeNode root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			ActTreeNode node = (ActTreeNode)root.getChildAt(i);
			if(!node.isLeaf()) {
				queue.add(node);
			}
		}
	}

	private boolean isSpEndTransition(Transition t, ActTreeNode node) {
		return t.getLabel().equals(node.getProcessLabel() + AbstractionAwareMiner.DUMMY_END);
	}

	private boolean isSpStartTransition(Transition t, ActTreeNode node) {
		return t.getLabel().equals(node.getProcessLabel() + AbstractionAwareMiner.DUMMY_START);
	}



}
