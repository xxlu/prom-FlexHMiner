package org.processmining.aam;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import org.deckfour.xes.in.XUniversalParser;
import org.deckfour.xes.model.XLog;
import org.processmining.aam.alg.ActivityClusteringAlg;
import org.processmining.aam.alg.ActivityClusteringAlgFreq;
import org.processmining.aam.alg.ActivityClusteringAlgRandom;
import org.processmining.aam.exp.HAIMexperiment;
import org.processmining.aam.exp.util.ExpDummyPluginContext;
import org.processmining.aam.exp.util.ExpModelSimplicityUtil;
import org.processmining.aam.exp.util.ExprReplayUtils;
import org.processmining.aam.exp.util.ModelQuality;

import org.processmining.aam.model.ActTreeNode;
import org.processmining.aam.model.ActTreeNodeImportExport;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.Dot2Image;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryIMf;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;
import org.processmining.xlu.relabel.experiment.ExpRelabelingPlugin;

import com.google.common.base.Stopwatch;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// 0. Import Log
				String folder = "./example/";
				String file = folder + "BPIC12.xes.gz";
				XLog log = importLog(file);

				// 1.a Import Activity Hierarchy
//				ActTreeNode root = ActTreeNodeImportExport.ImportFromFile(folder + "BPI12_hier.txt");

				// 1.b create Activity Hierarchy parser
//				ActTreeNode root = UtilDataBPI2012.getBPI2012Labels(log);
			
				// 1.c use automated clustering
//				ActivityClusteringAlg calg = new ActivityClusteringAlgRandom();
				ActivityClusteringAlg calg = new ActivityClusteringAlgFreq();
				
				ActTreeNode root = calg.calcActivityHierarchy(log);
				ActTreeNodeImportExport.exportToFile(root, folder + "BPI12_hierRandom.txt");
				

				// 2. Create log hierarchy
				AbstractionAwareMiner miner = new AbstractionAwareMiner();
				miner.run2createLogHierarchy(root, log);

				// 3. Discover submodels.
				ExpDiscoveryIMf alg = new ExpDiscoveryIMf();
				Enumeration<?> enode = root.breadthFirstEnumeration();
				while (enode.hasMoreElements()) {
					ActTreeNode sub = (ActTreeNode) enode.nextElement();
					if (sub.isLeaf()) {// || alreadyDone(sub)) {
						continue;
					}
					XLog newLowLog = sub.getSubLog();
					MarkedPetrinet markedNet = alg.minePetrinet(null, newLowLog);

					export2Image(markedNet, new File(folder + sub.getProcessLabel() + "_" + alg.toString() + "_M.svg"));

				}
				
				// Visualize the activity hierarchy
				JTree tree = new JTree(root);
				JFrame frame = new JFrame();
				frame.add(tree);

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("JTree Example");
				frame.pack();
				frame.setVisible(true);
				

			}

			private void export2Image(MarkedPetrinet net, File file) {

				Dot dot = org.processmining.xlu.relabel.visualization.GraphvizPetriNet.convert(net.getPetrinet(),
						net.getInitialMarking(), net.getFinalMarking());
				DotPanel panel = new DotPanel(dot);
				dot = panel.getDot();

				Dot2Image.dot2image(dot, file, org.processmining.plugins.graphviz.dot.Dot2Image.Type.svg);

			}

			private XLog importLog(String file) {
				XUniversalParser parser = new XUniversalParser();
				XLog log = null;
				try {
					Iterator<XLog> it = parser.parse(new File(file)).iterator();
					if (it.hasNext()) {
						log = it.next();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return log;
			}

		});

	}

}
