package org.processmining.aam;

import java.io.File;
import java.util.Enumeration;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.aam.alg.ActivityClusteringAlg;
import org.processmining.aam.alg.ActivityClusteringAlgFreq;
import org.processmining.aam.exp.INFO;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.aam.model.ActTreeNodeImportExport;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryIMf;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

public class FlexHMinerPlugin {
	
	@Plugin(name = "FlexH Miner", returnLabels = { "AHierarchy" }, returnTypes = {
			ActTreeNode.class }, parameterLabels = { "Event log" }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "x.lu", email = "x.lu@tue.nl")
	@PluginVariant(variantLabel = "FlexH Miner", requiredParameterLabels = { 0 })
	public ActTreeNode run(final UIPluginContext context, XLog log) {
		// 1.c use automated clustering
//		ActivityClusteringAlg calg = new ActivityClusteringAlgRandom();
		ActivityClusteringAlg calg = new ActivityClusteringAlgFreq();
		
		ActTreeNode root = calg.calcActivityHierarchy(log);

		// 2. Create log hierarchy
		AbstractionAwareMiner miner = new AbstractionAwareMiner();
		miner.run2createLogHierarchy(root, log);

		
		HierarchyMainPanel panel = new HierarchyMainPanel(context, root);
		return root;
	}
	
	

}
