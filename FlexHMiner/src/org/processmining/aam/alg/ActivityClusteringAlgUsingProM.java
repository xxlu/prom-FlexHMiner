package org.processmining.aam.alg;

import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XLog;
import org.processmining.aam.exp.util.ExpDummyPluginContext;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.activityclusterarray.models.ActivityClusterArray;
import org.processmining.activityclusterarraycreator.plugins.DiscoverClustersFromEventLogPlugin;

public class ActivityClusteringAlgUsingProM implements ActivityClusteringAlg {

	public ActTreeNode calcActivityHierarchy(XLog log) {
		/* xxl: the ProM activity clustering do NOT work because the method duplicates 
		 * the activities into multiple clusters (i.e., clusters are overlapping).
		 */
		
		DiscoverClustersFromEventLogPlugin plugin = new DiscoverClustersFromEventLogPlugin();
		
//		DiscoverClustersFromEventLogParameters parameters = new DiscoverClustersFromEventLogParameters(log);
//		ActivityClusterArray clusters = plugin.run(new ExpDummyPluginContext(), log,parameters);		
		ActivityClusterArray clusters = plugin.runDefault(new ExpDummyPluginContext(), log);
		
		ActTreeNode root = new ActTreeNode("root");
		int i = 0;
		for( Set<XEventClass> cluster : clusters.getClusters()) {
			ActTreeNode sub = new ActTreeNode("C" + String.valueOf(i++));
			root.add(sub);
			
			for(XEventClass c : cluster) {
//				String l = mapP2E.get(p).getId();
				ActTreeNode child = new ActTreeNode(c.toString());
				sub.add(child);
			}		
			
		}	
		
		return root;
	}

}
