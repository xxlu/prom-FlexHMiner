package org.processmining.aam.exp;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.AbstractionAwareMiner;
import org.processmining.aam.HierarchyMainPanel;
import org.processmining.aam.alg.ActivityClusteringAlg;
import org.processmining.aam.alg.ActivityClusteringAlgFreq;
import org.processmining.aam.alg.ActivityClusteringAlgFull;
import org.processmining.aam.exp.input.UtilDataVUMC;
import org.processmining.aam.exp.util.ExpDummyPluginContext;
import org.processmining.aam.exp.util.ExpModelSimplicityUtil;
import org.processmining.aam.exp.util.ExprReplayUtils;
import org.processmining.aam.exp.util.ModelQuality;
import org.processmining.aam.exp.util.UtilIO;
import org.processmining.aam.exp.util.UtilIOXES;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryIMf;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

import com.google.common.base.Stopwatch;

public class HAIMexperiment {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				HAIMexperiment exp = new HAIMexperiment();
//				ActTreeNode node = exp.run(null);
				ActTreeNode node = exp.run(new ExpDummyPluginContext());

				// Visualize the activity hierarchy
				JTree tree = new JTree(node);
				JFrame frame = new JFrame();
				frame.add(tree);

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("JTree Example");
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	@Plugin(name = "HAIM experiment", returnLabels = {"Hierarchy"}, 
			returnTypes = {ActTreeNode.class}, parameterLabels = {}, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = INFO.MY_NAME, email = INFO.MY_MAIL)
	@PluginVariant(variantLabel = "HAIM experiment", requiredParameterLabels = {})
	public ActTreeNode runExperiment(final UIPluginContext context) {
//		HAIMexperiment exp = new HAIMexperiment();
		ActTreeNode node = run(context);
		HierarchyMainPanel panel = new HierarchyMainPanel(context, node);
		return node;
	}

	@Plugin(name = "HAIM experiment vis", returnLabels = { "AHierarchy" }, returnTypes = {
			JComponent.class }, parameterLabels = { "Event log" }, userAccessible = true)
	@Visualizer
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "x.lu", email = "x.lu@tue.nl")
	@PluginVariant(variantLabel = "HAIM experiment vis", requiredParameterLabels = { 0 })
	public JComponent visualize(final UIPluginContext context, XLog log) {
//		HAIMexperiment exp = new HAIMexperiment();
		ActTreeNode node = run(context);
		HierarchyMainPanel panel = new HierarchyMainPanel(context, node);

		return panel;
	}
	

	String[] logsBPIC15f = new String[] {"BPIC15_1f", "BPIC15_2f", "BPIC15_3f", "BPIC15_4f", "BPIC15_5f"};
	String[] logsBPIC12f = new String[] {"BPIC12", "BPIC17f"};
	String[] logsBPIC15 = new String[] {"BPIC15_1"};//, "BPIC15_2", "BPIC15_3", "BPIC15_4", "BPIC15_5"};

	public ActTreeNode run(PluginContext context) {

		String outputf = "./outICPM/";
		PrintWriter resWriter = UtilIO.createWriter(outputf + "res.csv", this.columnNames);

		List<String> lognames = new ArrayList<>();
		lognames.add("BPIC12"); //gz
//		lognames.add("BPIC17"); //gz
//		lognames.add("BPIC17f"); //gz
		
//		lognames.add("BPIC15_1f"); //gz
//		lognames.add("BPIC15_2f");
//		lognames.add("BPIC15_3f");
//		lognames.add("BPIC15_4f");
//		lognames.add("BPIC15_5f");
		
//		lognames.add("BPIC15_1"); //xes
//		lognames.add("BPIC15_2");
//		lognames.add("BPIC15_3");
//		lognames.add("BPIC15_4");
//		lognames.add("BPIC15_5");
		
//		lognames.add("Kidney17");
//		lognames.add("Diabetes17");
//		lognames.add("HNTumor17");
		
		ActTreeNode subproc = null;
		
		for(String logname : lognames) {
//			XLog log = UtilIOXES.importXesLog(logname);
			XLog log = UtilIOXES.importXesGZLog(logname);

			
//		context.getProvidedObjectManager().createProvidedObject(logname, log, XLog.class, context);

		Stopwatch stopwatchHierAct = Stopwatch.createStarted();
		
//		Object[] temp = UtilDataVUMC.getVUMCLabels(logname);
//		XLog log = (XLog) temp[0];
////		subproc = (ActTreeNode) temp[1];
		
		
		ActivityClusteringAlg calg = null;
//		calg = new ActivityClusteringAlgFull();
		calg = new ActivityClusteringAlgFreq();
//		calg = new ActivityClusteringAlgRandom();
		subproc = calg.calcActivityHierarchy(log);
		
		// Get Hierarchy using DK
//		subproc = UtilDataBPI2012.getBPI2012Labels(log);
//		subproc = UtilDataBPI2015.getBPI2015Labels(log);
		stopwatchHierAct.stop();

//		String logname = XConceptExtension.instance().extractName(log);
		subproc.setProcessLabel("root");

		AbstractionAwareMiner miner = new AbstractionAwareMiner();
		Stopwatch stopwatchHierlog = Stopwatch.createStarted();
		miner.run2createLogHierarchy(subproc, log);
		stopwatchHierlog.stop();

		outputf += logname + "\\";
		(new File(outputf)).mkdirs();

		List<ExpDiscoveryAlgorithm> algs = new ArrayList<>();
//		algs.add(new ExpDiscoveryETM());
//		algs.add(new ExpDiscoverySM());
		algs.add(new ExpDiscoveryIMf());
//		algs.add(new ExpDiscoveryIM());

		Enumeration<?> enode = subproc.breadthFirstEnumeration();
		while (enode.hasMoreElements()) {
			ActTreeNode sub = (ActTreeNode) enode.nextElement();
			if (sub.isLeaf() ) {//|| alreadyDone(sub)) {
				continue;
			}
			XLog newLowLog = sub.getSubLog();

//			UtilExp.serializeLogAsGZ(newLowLog, outputf + UtilIO.simplify(sub.getProcessLabel()) + "_Log.xes.gz");
//			UtilIOXES.exportXesAsCsv(newLowLog, outputf + UtilIO.simplify(sub.getProcessLabel()) + "_Log.csv");
			//context.getProvidedObjectManager().createProvidedObject(sub.getProcessLabel(), newLowLog, XLog.class, context);

			for (ExpDiscoveryAlgorithm alg : algs) {
				//*****TIME
				Stopwatch stopwatchAlg = Stopwatch.createStarted();
				MarkedPetrinet markedNet = alg.minePetrinet(context, newLowLog);
				stopwatchAlg.stop();

//				ExpRelabelingPlugin.export2Image(markedNet,
//						new File(outputf + UtilIO.simplify(sub.getProcessLabel()) + "_" + alg.toString() + "_Model.png"));
				if (context != null) {
					//					boolean useILP = alg instanceof ExpDiscoveryIM ? false : true;
					boolean useILP = true;

					//*****TIME
					Stopwatch stopwatchQuality = Stopwatch.createStarted();
					
					// Run with executor
//					ExecutorService executor = Executors.newCachedThreadPool();
//					Callable<Object> task = new Callable<Object>() {
//					   public Object call() {
//						   ModelQuality threadquality =  ExprReplayUtils.computeMeasures(context, newLowLog, markedNet.getPetrinet(),
//									markedNet.getInitialMarking(), markedNet.getFinalMarking(), useILP);
//						   ExprReplayUtils.computeGeneralizationMeasures(context, newLowLog, alg, threadquality, useILP);
//					      return threadquality;
//					   }
//					};
//					Future<Object> future = executor.submit(task);
//					ModelQuality quality = null;
//					try {
//						quality = (ModelQuality) future.get(1*60, TimeUnit.SECONDS); 
//					} catch (TimeoutException ex) {
//					   // handle the timeout
////						context.getFutureResult(0).cancel(true);
//						System.out.println("timeout");
//					} catch (InterruptedException e) {
//					   // handle the interrupts
//					} catch (ExecutionException e) {
//					   // handle other exceptions
//					} finally {
//					   future.cancel(true); // may or may not desire this
//					}
					ModelQuality quality = ExprReplayUtils.computeMeasures(context, newLowLog, markedNet.getPetrinet(),
							markedNet.getInitialMarking(), markedNet.getFinalMarking(), useILP);
					if(!quality.isTimeout()) {
//					ExprReplayUtils.computeGeneralizationMeasures(context, newLowLog, alg, quality, useILP);
					}
					stopwatchQuality.stop();

					List<String> res = new ArrayList<>();
					res.add(logname);
					res.add(calg == null ? "DK" : calg.toString());
//					res.add("DK");
					res.add(sub.getProcessLabel());
					res.add(String.valueOf(newLowLog.size()));
					res.add(String.valueOf(getNumberOfEvt(newLowLog)));
					res.add(String.valueOf(getNumberOfAct(newLowLog)));
					res.add(alg.toString());

					// Print Quality
					res.add(String.valueOf(quality.fitness));
					res.add(String.valueOf(quality.precision));
					res.add(String.valueOf(quality.generalisation));
					// Print Complexity
					for (String simplicityKey : ExpModelSimplicityUtil.propertykeys) {
						res.add(quality.getProperties().get(simplicityKey));
					}
					// Print Time					
					res.add(String.valueOf(stopwatchHierAct.elapsed(TimeUnit.MILLISECONDS)));
					res.add(String.valueOf(stopwatchHierlog.elapsed(TimeUnit.MILLISECONDS)));
					res.add(String.valueOf(stopwatchAlg.elapsed(TimeUnit.MILLISECONDS)));
					res.add(String.valueOf(stopwatchQuality.elapsed(TimeUnit.MILLISECONDS)));
					// Print t/o
					res.add(String.valueOf(quality.isTimeout()));
					res.add(String.valueOf(quality.isGEtimeout));
					
					System.out.println(UtilIO.concatenate(res, UtilIO.SEP));
					resWriter.println(UtilIO.concatenate(res, UtilIO.SEP));
					resWriter.flush();
				}
			}
		}

				}
		resWriter.close();
		
		
		return subproc;
	}

	private int getNumberOfAct(XLog newLowLog) {
		int numEvt = 0;
		for (XTrace t : newLowLog) {
			numEvt += t.size();
		}
		return numEvt;
	}

	private int getNumberOfEvt(XLog log) {
		return XEventClasses.deriveEventClasses(new XEventNameClassifier(), log).size();
	}

	String[] columnNames = new String[] { "Data", "CAlg", "Subp", "Num.Trcs", "DAlg", "Fitn", "Prec", "Gen", "Size",
			"NumTrans", "ACD", "CFC", "CNC", "Density" };

	private boolean alreadyDone(ActTreeNode sub) {
		String[] done = new String[] { 
				"16_LGSD",
				"15_NGV",
				"03_GBH",
				"11_OLO",
				"08_OLO",
				"03_VD",
				"99_NOCODE",
				"16_LGSV",
				"09_AWB45",
				"01_HOOFD_0",
				"01_HOOFD_1",
				"01_HOOFD_4",
				"01_HOOFD_5",
				"01_HOOFD_2",
				"01_HOOFD_3",
				"01_HOOFD_7",
				"01_HOOFD_8"
			
		};
		List<String> doneList = Arrays.asList(done);

		return doneList.contains(sub.getProcessLabel());
	}

	public static void addPathToHierachy(ActTreeNode root, Map<String, ActTreeNode> map, List<String> path) {
		ActTreeNode parent = root;
		for (String current : path) {
			ActTreeNode currentNode = map.get(current);
			if (currentNode == null) {
				currentNode = new ActTreeNode(current);
				map.put(current, currentNode);
				parent.add(currentNode);
			}
			parent = currentNode;
		}
	}

	//	public Set<String> getSubProcLabels() {
	//		Set<String> subproc = new THashSet<>();
	//		subproc.add("N");
	//		subproc.add("O");
	//		subproc.add("P");
	//		//		subproc.add("A");
	//		//		subproc.add("B");
	//		//		subproc.add("C");
	//		//		subproc.add("D");
	//		subproc.add("G");
	//		subproc.add("H");
	//		subproc.add("I");
	//
	//		subproc.add("E");
	//		subproc.add("F");
	//		subproc.add("J");
	//		return subproc;
	//	}
	//
	//	//	private Set<String> getSubProcess(ProcessTree tree) {
	//	//		List<Node> selectedNodes = new ArrayList<Node>();
	//	//		List<Node> selectedNodes = new ArrayList<Node>();
	//	//		for (Node n : tree.getNodes()) {
	//	////			if (n.isLeaf() && n instanceof org.processmining.processtree.Task.Manual) {
	//	////				selectedNodes.add(n);
	//	////			}
	//	//			if 
	//	//		}
	//	//		
	//	//		for(Node node : tree.getNodes()) {
	//	//			if(node instanceof Block) {
	//	//				Block b = (Block) node;
	//	//				b.getChildren();
	//	//			} else if () {
	//	//				
	//	//			}
	//	//		}
	//	//		return null;
	//	//	}
}
