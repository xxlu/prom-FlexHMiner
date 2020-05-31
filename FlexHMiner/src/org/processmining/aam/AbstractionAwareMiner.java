package org.processmining.aam;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.discovery.HierModelDiscoveryAndFlatteningAlg;
import org.processmining.aam.discovery.HierModelDiscoveryFlatteningAlgImpl;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryIMf;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class AbstractionAwareMiner {

	
	public MarkedPetrinet runAlgorithm2FlatModel(ActTreeNode subproc, XLog log) {
		run2createLogHierarchy(subproc, log);
		
		HierModelDiscoveryAndFlatteningAlg flatteningAlg = new HierModelDiscoveryFlatteningAlgImpl();
		ExpDiscoveryAlgorithm dalg = new ExpDiscoveryIMf();
		MarkedPetrinet model = flatteningAlg.discoverAndFlattening(subproc, dalg);
		return model;
	}
	
	// Param 1 : keep empty traces?
	public void run2createLogHierarchy(ActTreeNode subproc, XLog log) {

		int maxHeight = subproc.getDepth();
		Enumeration enode = subproc.breadthFirstEnumeration();
		Map<Integer, Set<ActTreeNode>> mapHeight2Nodes = new THashMap<>();
		while (enode.hasMoreElements()) {
			ActTreeNode node = (ActTreeNode) enode.nextElement();
			int height = node.getDepth();
			if (height > 0 && height < maxHeight) {
				// Node is a sub process
				Set<ActTreeNode> sub = mapHeight2Nodes.get(height);
				sub = sub == null ? new THashSet<>() : sub;
				sub.add(node);
				mapHeight2Nodes.put(height, sub);
			}
		}

		//	List<XLog> sublogs = new ArrayList<XLog>();
		for (int h = 1; h < maxHeight; h++) {
			for (ActTreeNode subprocess : mapHeight2Nodes.get(h)) {
				Set<String> childLabels = new THashSet<>();
				Enumeration children = subprocess.children();
				while (children.hasMoreElements()) {
					ActTreeNode actTreeNode = (ActTreeNode) children.nextElement();
					String childLabel = actTreeNode.getProcessLabel();
					if(actTreeNode.isLeaf()) {
						childLabels.add(childLabel);
					} else {
						childLabels.add(createDummyStart(childLabel));
						childLabels.add(createDummyEnd(childLabel));

					}
				}

	

				XLog[] logs = createSublog(log, subprocess.getProcessLabel(), childLabels);
				XLog newHighLog = logs[0];
				XLog newLowLog = logs[1];
				log = newHighLog;

				subprocess.setSubLog(newLowLog);
			}
		}

		subproc.setSubLog(log);

	}
	
	


	/**
	 *  This function create one low level log for one subprocess. 
	 * @param log
	 * @param subProcLabel : the label of the subprocess
	 * @param childLabels : the activities in the subprocess
	 * @return Two logs: res[0] is the abstracted high level log, and res[1] is the projected low level log. 
	 */
	protected XLog[] createSublog(XLog log, String subProcLabel, Set<String> childLabels) {
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XConceptExtension ce = XConceptExtension.instance();

		XLog newHighLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		XLog newLowLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		for (XTrace t : log) {
			XTrace newHighTrc = factory.createTrace((XAttributeMap) t.getAttributes().clone());
			XTrace newLowTrc = factory.createTrace((XAttributeMap) t.getAttributes().clone());
			List<Integer> indices = new ArrayList<>();

			// Get all indices of low level events in the trace
			for (int i = 0; i < t.size(); i++) {
				XEvent e = t.get(i);
				String label = ce.extractName(e);
				if (childLabels.contains(label)) {
					indices.add(i);
					newLowTrc.add((XEvent) e.clone());
				}
			}
			
			// Substitutes the start and end events of subprocess
			createSublogStartEnd(subProcLabel, factory, ce, t, newHighTrc, indices);
			
			newHighLog.add(newHighTrc);
			if (newLowTrc.size() > 0) {
				newLowLog.add(newLowTrc);
			}
		}
		return new XLog[] { newHighLog, newLowLog };
	}

	
	private void createSublogStart(String subProcLabel, XFactory factory, XConceptExtension ce, XTrace t,
			XTrace newHighTrc, List<Integer> indices) {
		XEvent estart = factory.createEvent();
		for (int i = 0; i < t.size(); i++) {
			XEvent e = t.get(i);
			
			
			if (indices.size() > 0 && i == indices.get(0)) { // if there are two events
				
				estart.setAttributes((XAttributeMap) e.getAttributes().clone());
				ce.assignName(estart, subProcLabel);
				newHighTrc.add(estart);

			} else if (indices.size() > 1 && i == indices.get(indices.size() - 1)) { 
				Date date = XTimeExtension.instance().extractTimestamp(e);
				XAttributeTimestamp time = factory.createAttributeTimestamp("completeTime", date, null);
				estart.getAttributes().put("completeTime", time);
				

			} else if (!indices.contains(i)) {
				
				// Clone the non low level events
				newHighTrc.add(factory.createEvent((XAttributeMap) e.getAttributes().clone()));
			}
		}
	}

	
	public void createSublogStartEnd(String subProcLabel, XFactory factory, XConceptExtension ce, XTrace t,
			XTrace newHighTrc, List<Integer> indices) {
		for (int i = 0; i < t.size(); i++) {
			XEvent e = t.get(i);
			if (indices.size() > 1 && i == indices.get(0)) { // if there are two events
				XEvent estart = (XEvent) e.clone();
				ce.assignName(estart, createDummyStart(subProcLabel));
				newHighTrc.add(estart);

			} else if ((indices.size() > 0 || indices.size() == 1) && i == indices.get(indices.size() - 1)) { 
				XEvent estart = (XEvent) e.clone();
				ce.assignName(estart, createDummyEnd(subProcLabel));
				newHighTrc.add(estart);

			} else if (!indices.contains(i)) {
				
				// Clone the non low level events
				newHighTrc.add((XEvent) e.clone());
			}
		}
	}


	private static String createDummyStart(String s) {
		return s + DUMMY_START;
	}

	private static String createDummyEnd(String s) {
		return s +  DUMMY_END;
	}
	
	public static String DUMMY_START = "__start";
	
	public static String DUMMY_END = "__end";
		
	

}
