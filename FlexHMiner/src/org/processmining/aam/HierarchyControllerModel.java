package org.processmining.aam;

import org.deckfour.xes.model.XLog;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMiner.InductiveVisualMinerLauncher;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryIM;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

public class HierarchyControllerModel {

	public ActTreeNode model;
	public MarkedPetrinet net;
	public XLog newlog;


	public HierarchyControllerModel(ActTreeNode model) {
			this.model = model;
			this.newlog = model.getSubLog();
		}


	public Object computeNewProcessModel(PluginContext context) {
		Object net = null;
		
		if(getDiscoveryAlg() != null){
			this.net = getDiscoveryAlg().minePetrinet(context, newlog);
			net = this.net;
			
		} else if(isUsingIvm()){ // is using Inductive Visual Miner
			net = InductiveVisualMinerLauncher.launcher(newlog);
			
		} else {
			ExpDiscoveryAlgorithm alg = new ExpDiscoveryIM();
			net = alg.minePetrinet(context, newlog);
			
		}
		return net;
	}


	private boolean isUsingIvm() {
		return true;
	}


	private ExpDiscoveryAlgorithm getDiscoveryAlg() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setLog(XLog subLog) {
		newlog = subLog;
	}


	public void update() {
		// TODO Auto-generated method stub
		
	}





}
