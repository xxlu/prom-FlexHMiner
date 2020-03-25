package org.processmining.aam.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.aam.model.ActTreeNode;

import gnu.trove.set.hash.THashSet;

public class ActivityClusteringAlgRandom implements ActivityClusteringAlg {

	public ActTreeNode calcActivityHierarchy(XLog log) {
		
		// Compute DFG
		DFGraph dfg = (new GraphCreationAlg()).calculateGraph(log);
		
		XEventClassifier classifier = new XEventNameClassifier();
		XEventClasses eclasses = XEventClasses.deriveEventClasses(classifier, log);
		
		int clusterMaxSize = eclasses.size() < 200 ?   10 :  20 ;
		int numclusters = eclasses.size()/ clusterMaxSize  + 1;
		List<Set<XEventClass>> clusters = new ArrayList<>(numclusters);
		for(int i = 0; i < numclusters; i++) {
			clusters.add(new THashSet<>());
		}
		Random rn = new Random();
		for (XEventClass eclass : eclasses.getClasses()) {
			int index = rn.nextInt(numclusters);
			Set<XEventClass> cluster = clusters.get(index);
			while(cluster.size() > clusterMaxSize) {
				index++;
				cluster = clusters.get(index % numclusters); 
				// TODO buggy: possibly infinite loop
			}
			cluster.add(eclass);		
		}
	
		
		ActTreeNode root = new ActTreeNode("root");
		int i = 0;
		for( Set<XEventClass> cluster : clusters) {
			if(cluster.isEmpty()) {
				continue;
			}
			if(cluster.size() == 1) {
				for(XEventClass p : cluster) {
					ActTreeNode child = new ActTreeNode(p.getId());
					root.add(child);
				}
				continue;
			}
			// else Cluster size >= 2
			ActTreeNode sub = new ActTreeNode(String.valueOf(i++));
			root.add(sub);
			
			for(XEventClass p : cluster) {
				ActTreeNode child = new ActTreeNode(p.getId());
				sub.add(child);
			}					
		}	
		
		return root;
	}

	public String toString() {
		return "AC-Random";
	}
}
