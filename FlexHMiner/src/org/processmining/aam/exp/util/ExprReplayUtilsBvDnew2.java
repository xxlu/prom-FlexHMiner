package org.processmining.aam.exp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.exp.INFO;
import org.processmining.alignment.plugin.AStarPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.connections.petrinets.behavioral.FinalMarkingConnection;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.multietc.plugins.MultiETCPlugin;
import org.processmining.plugins.multietc.res.MultiETCResult;
import org.processmining.plugins.multietc.sett.MultiETCSettings;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayAlgorithm;
import org.processmining.plugins.petrinet.replayer.algorithms.costbasedcomplete.CostBasedCompleteParam;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;
import org.processmining.pnetreplayer.utils.TransEvClassMappingUtils;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryAlgorithm;
import org.processmining.xlu.experiment.discovery.ExpDiscoveryIM;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

import nl.tue.alignment.Progress;
import nl.tue.alignment.Replayer;
import nl.tue.alignment.ReplayerParameters;
import nl.tue.alignment.algorithms.ReplayAlgorithm.Debug;
import nl.tue.astar.AStarException;

/**
 * @author xlu
 * 
 */
public class ExprReplayUtilsBvDnew2 {

	@Plugin(name = "Model Quality Compute", returnLabels = {}, returnTypes = {}, parameterLabels = { "Log", "Net",
			"init marking", "final marking", }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = INFO.MY_NAME, email = INFO.MY_MAIL)
	@PluginVariant(variantLabel = "Model Quality Compute", requiredParameterLabels = { 0, 1, 2, 3 })
	public void runExperiment(PluginContext context, XLog log, Petrinet petrinet, Marking initialMarking,
			Marking finalMarking) {
		computeMeasures(context, log, petrinet, initialMarking, finalMarking, false);
	}

	@Plugin(name = "Model Quality Compute2", returnLabels = {}, returnTypes = {}, parameterLabels = {
			"Log" }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = INFO.MY_NAME, email = INFO.MY_MAIL)
	@PluginVariant(variantLabel = "Model Quality Compute2", requiredParameterLabels = { 0 })
	public void runExperiment2(PluginContext context, XLog log) {
		ExpDiscoveryAlgorithm alg = new ExpDiscoveryIM();
		MarkedPetrinet markedNet = alg.minePetrinet(context, log);
		computeMeasures(context, log, markedNet.getPetrinet(), markedNet.getInitialMarking(),
				markedNet.getFinalMarking());
	}

	public static ModelQuality computeMeasures(PluginContext context, XLog log, Petrinet petrinet,
			Marking initialMarking, Marking finalMarking) {
		return computeMeasures(context, log, petrinet, initialMarking, finalMarking, true);
	}

	public static ModelQuality computeMeasures(PluginContext context, XLog log, Petrinet petrinet,
			Marking initialMarking, Marking finalMarking, boolean useILP) {
		//		static {
		try {
			System.loadLibrary("lpsolve55");
			System.loadLibrary("lpsolve55j");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		}
		// Init variables
		XEventClassifier classifier = new XEventNameClassifier();

		XEventClasses newClasses = XEventClasses.deriveEventClasses(classifier, log);
		//		Petrinet petrinet = markedNet.petrinet;
		//		Marking initialMarking = markedNet.initialMarking;
		//		Marking finalMarking = markedNet.finalMarking;
		
		
		XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
//		TransEvClassMapping mapping = constructMappingBasedOnLabelEquality(petrinet, log, dummyEvClass, classifier);
		XLogInfo summary = XLogInfoFactory.createLogInfo(log, classifier);
		XEventClasses classes = summary.getEventClasses();
		Map<Transition, Integer> costModelMove = new HashMap<>();
		Map<Transition, Integer> costSyncMove = new HashMap<>();
		Map<XEventClass, Integer> costLogMove = new HashMap<>();
		for (Transition t : petrinet.getTransitions()) {
			costSyncMove.put(t, 0);
			costModelMove.put(t, t.isInvisible() ? 0 : 2);
		}
		for (XEventClass c : summary.getEventClasses().getClasses()) {
			costLogMove.put(c, 5);
		}
		costLogMove.put(dummyEvClass, 5);

		TransEvClassMapping etMapping = TransEvClassMappingUtils.getInstance().getMapping(petrinet,
				new HashSet<>(newClasses.getClasses()), newClasses.getClassifier());

		context.addConnection(new InitialMarkingConnection(petrinet, initialMarking));
		context.addConnection(new FinalMarkingConnection(petrinet, finalMarking));

		ModelQuality quality = new ModelQuality();

		// use cost-based prefix algorithm
//		CostBasedCompleteParam replayParam = ExprReplayUtils.getCostBasedCompleteParam(initialMarking, finalMarking,
//				newClasses, etMapping, 1, 1);
		

		int numThreads = 1;
		int maximumNumberOfStates = 100000;
		boolean usePartialOrder = false;
		
//		ReplayerParameters replayParam = new ReplayerParameters.AStar(true, true, true, numThreads, false,
//				Debug.NONE, Integer.MAX_VALUE, maximumNumberOfStates,Integer.MAX_VALUE, usePartialOrder);

		int costUpperBound = Integer.MAX_VALUE;
		// timeout per trace in milliseconds
		int timeoutMilliseconds = 10 * 1000;

		ReplayerParameters replayParam = new ReplayerParameters.IncrementalAStar(true, numThreads, false, Debug.NONE,
				timeoutMilliseconds, maximumNumberOfStates, costUpperBound, false, false);
		
		
//		IPNReplayAlgorithm replayAlgorithm = useILP ? new PetrinetReplayerWithILP() : new PetrinetReplayerWithoutILP();
		IPNReplayAlgorithm replayAlgorithm = new AStarPlugin();
		System.out.println("Call " + replayAlgorithm.toString());

		Replayer replayer = new Replayer(replayParam, petrinet, initialMarking, finalMarking,
				newClasses, costModelMove, costLogMove, costSyncMove, etMapping, true);
//		if (replayAlgorithm.isAllReqSatisfied(context, petrinet, log, etMapping, replayParam)) {
//			PNLogReplayer replayer = new PNLogReplayer();
//			PNRepResult replayResult = null;
			try {
//				replayResult = replayer.computePNRepResult(context.getProgress(), log);
				PNRepResult replayResult;
				try {
					replayResult = replayer.computePNRepResult(new Progress() {

						public void setMaximum(int maximum) {
							context.getProgress().setMinimum(0);
							context.getProgress().setMaximum(maximum);
						}

						public void inc() {
							context.getProgress().inc();
						}

						public boolean isCanceled() {
							return context.getProgress().isCancelled();
						}

						public void log(String message) {
							context.log(message);
						}
					}, log);
				} catch (InterruptedException | ExecutionException e) {
					throw new AStarException(e);
				}


						//.replayLog(context, petrinet, log, etMapping, replayAlgorithm, replayParam);

				/*
				 * -------------------------------------------------------------
				 * -- Compute my recall and precision
				 * -------------------------------------------------------------
				 * -
				 */
				//				ExprReplayUtils.computeMyRecallAndPrecision(replayResult, quality);

				/*
				 * -------------------------------------------------------------
				 * -- Compute fitness & Precision
				 * -------------------------------------------------------------
				 * -
				 */
				quality.fitness = ExprReplayUtilsBvDnew2.getAlignmentValue(replayResult);

				MultiETCSettings precisionSettings = new MultiETCSettings();
				precisionSettings.put(MultiETCSettings.ALGORITHM, MultiETCSettings.Algorithm.ALIGN_1);
				precisionSettings.put(MultiETCSettings.REPRESENTATION, MultiETCSettings.Representation.ORDERED);
				//				precisionSettings.put(MultiETCSettings.WINDOW,
				//						MultiETCSettings.Window.BACKWARDS);

				MultiETCPlugin precisionPlugin = new MultiETCPlugin();
				Object[] precisions = precisionPlugin.checkMultiETCAlign1(context, log, petrinet, precisionSettings,
						replayResult);
				quality.precision = (double) ((MultiETCResult) precisions[0])
						.getAttribute(MultiETCResult.BALANCED_PRECISION);

				// Only for experiment
				//				quality.precisionDistinctlabels = (double) ((MultiETCResult) precisions[0])
				//						.getAttribute(MultiETCResult.BALANCED_PRECISION_DISTINCT_LABEL);
				//				quality.precisionDistinctlabelsTau = (double) ((MultiETCResult) precisions[0])
				//						.getAttribute(MultiETCResult.BALANCED_PRECISION_DISTINCT_LABELANDTAU);

				/*
				 * -------------------------------------------------------------
				 * --- Compute Wil's recall and precision w.r.t. log
				 * -------------------------------------------------------------
				 * ---
				 */
				//				 MapTrcEnabled enabledSet = EventToEnabledSets.getEventToEnabledSets(replayResult, net, initM);
				//				 quality.setEnabledSet(enabledSet);

			} catch (AStarException e) {
				e.printStackTrace();
			} catch (ConnectionCannotBeObtained e) {
				e.printStackTrace();
			}

//		}
		ExpModelSimplicityUtil.computeAllSimplicities(petrinet, quality);
		System.out.println(quality);

		context.getConnectionManager().clear();
		return quality;
	}

	/**
	 * 
	 * @param initM
	 * @param finalM
	 * @param newClasses
	 * @param etMapping
	 * @param logMoveCost
	 * @param modelMoveCost
	 * @return
	 */
	public static CostBasedCompleteParam getCostBasedCompleteParam(Marking initM, Marking finalM,
			XEventClasses newClasses, TransEvClassMapping etMapping, int logMoveCost, int modelMoveCost) {
		CostBasedCompleteParam replayParam = new CostBasedCompleteParam(etMapping.values(),
				etMapping.getDummyEventClass(), etMapping.keySet());
		

		Map<XEventClass, Integer> map = replayParam.getMapEvClass2Cost();
		for (XEventClass c : newClasses.getClasses()) {
			//			Integer cost = map.get(c);
			map.put(c, logMoveCost);
		}
		for (Entry<Transition, Integer> t : replayParam.getMapTrans2Cost().entrySet()) {
			replayParam.getMapTrans2Cost().put(t.getKey(), t.getValue() > 0 ? modelMoveCost : 0);
		}
		replayParam.setInitialMarking(initM);
		replayParam.setFinalMarkings(finalM);
		//		replayParam.setCanceller();
		return replayParam;
	}

	/**
	 * Return the trace fitness if more than 50% of traces have a reliable
	 * alignment, otherwise return -1.
	 * 
	 * @param pnRepResult
	 * @return
	 */
	public static double getAlignmentValue(PNRepResult pnRepResult) {
		int unreliable = 0;
		int total = 0;
		for (SyncReplayResult srp : pnRepResult) {
			if (!srp.isReliable()) {
				unreliable += srp.getTraceIndex().size();
			}
			total += srp.getTraceIndex().size();
		}
		if (unreliable > total / 2) {
			System.err.println("Fitness unreliable");
			return -1.0;
		} else {
			return (Double) pnRepResult.getInfo().get(PNRepResult.TRACEFITNESS);
		}
	}

	public static void computeMyRecallAndPrecision(PNRepResult replayResult, ModelQuality quality) {
		int numSM = 0, numMM = 0, numLM = 0, numMMinv = 0;
		for (SyncReplayResult alignment : replayResult) {
			int numTraces = alignment.getTraceIndex().size();
			if (alignment.isReliable()) {
				for (StepTypes type : alignment.getStepTypes()) {
					if (type.equals(StepTypes.L)) {
						numLM += numTraces;
					} else if (type.equals(StepTypes.MREAL)) {
						numMM += numTraces;
					} else if (type.equals(StepTypes.MINVI)) {
						numMMinv += numTraces;
					} else if (type.equals(StepTypes.LMGOOD)) {
						numSM += numTraces;
					}
				}
			}
		}
		quality.set("numSM", numSM);
		quality.set("numMM", numMM);
		quality.set("numLM", numLM);
		quality.set("numMMinv", numMMinv);
	}

	public static void computeGeneralizationMeasures(PluginContext context, XLog newLowLog, ExpDiscoveryAlgorithm alg, ModelQuality quality, boolean useILP) {
		int kValidation = 3;
		Object[] res = splitLogs(newLowLog, kValidation);
		List<XLog> trainLogs = (List<XLog>) res[0];
		List<XLog> testLogs = (List<XLog>) res[1];
		
		double[] fitnesslist = new double[kValidation];
		double[] precisionlist = new double[kValidation];
		double generalization = 0.0;
		
		for(int i = 0 ; i < trainLogs.size() ; i++ ) {
			XLog trainLog = trainLogs.get(i);
			XLog testLog = testLogs.get(i);
			
			MarkedPetrinet markedNet = alg.minePetrinet(null, trainLog);
			// Compute fitness using test log
			fitnesslist[i] = ExprReplayUtilsBvDnew2.computeMeasures(context, testLog, markedNet.getPetrinet(),
					markedNet.getInitialMarking(), markedNet.getFinalMarking()).fitness;
			
			// Compute precision using full log
			precisionlist[i] = ExprReplayUtilsBvDnew2.computeMeasures(context, newLowLog, markedNet.getPetrinet(),
					markedNet.getInitialMarking(), markedNet.getFinalMarking()).precision;
			
			generalization += 2d * fitnesslist[i] * precisionlist[i]  / (fitnesslist[i] + precisionlist[i]);
			
		}
		

		quality.generalisation = generalization / kValidation;
		System.out.println("Gen : " + quality.generalisation);
	}

	private static Object[] splitLogs(XLog newLowLog, int kvalidation) {
		List<XLog> trainLogs = new ArrayList<>();
		List<XLog> testLogs = new ArrayList<>();
		XFactory factory = new XFactoryNaiveImpl();
		for(int i = 0; i < kvalidation; i++) {
			trainLogs.add(factory.createLog());
			testLogs.add(factory.createLog());
		}
		Random ran = new Random();
		for(int i = 0; i < newLowLog.size(); i++) {
			XTrace t = newLowLog.get(i);
			int x = ran.nextInt(3);
			
			testLogs.get(x).add(t);
			trainLogs.get((x+1)%3).add(t);
			trainLogs.get((x+2)%3).add(t);
		}
		
		return new Object[] { trainLogs, testLogs };
	}

}
