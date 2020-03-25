package org.processmining.aam.exp;

import java.io.File;

import org.deckfour.xes.model.XLog;
import org.processmining.aam.AbstractionAwareMiner;
import org.processmining.aam.exp.input.UtilDataBPI2012;
import org.processmining.aam.exp.util.UtilIOXES;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;
import org.processmining.xlu.relabel.experiment.ExpRelabelingPlugin;

public class HAIMexperimentFlatmodel {
	
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//
//				HAIMexperimentFlatmodel exp = new HAIMexperimentFlatmodel();
//				ActTreeNode node = exp.run(null);
////				ActTreeNode node = exp.run(new ExpDummyPluginContext());
//
//				// Visualize the activity hierarchy
//				JTree tree = new JTree(node);
//				JFrame frame = new JFrame();
//				frame.add(tree);
//
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				frame.setTitle("JTree Example");
//				frame.pack();
//				frame.setVisible(true);
//			}
//		});
//	}
	
	@Plugin(name = "HAIM experiment flat", returnLabels = {"Flat Net"}, returnTypes = {Petrinet.class}, parameterLabels = {}, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = INFO.MY_NAME, email = INFO.MY_MAIL)
	@PluginVariant(variantLabel = "HAIM experiment flat", requiredParameterLabels = {})
	public Petrinet runExperiment(PluginContext context) {
		HAIMexperimentFlatmodel exp = new HAIMexperimentFlatmodel();
		MarkedPetrinet node = exp.run(context);
		
		return node.getPetrinet();
	}
	

	protected MarkedPetrinet run(Object object) {
		String logname = "BPIC12";
		XLog log = UtilIOXES.importXesGZLog(logname);
		ActTreeNode subproc = UtilDataBPI2012.getBPI2012Labels(log);
		
		AbstractionAwareMiner miner = new AbstractionAwareMiner();
		MarkedPetrinet model = miner.runAlgorithm2FlatModel(subproc, log);
		
		
		ExpRelabelingPlugin.export2Image(model, new File("Flat_"+logname+ "_Model.svg"));
		
		return model;
	}

}
