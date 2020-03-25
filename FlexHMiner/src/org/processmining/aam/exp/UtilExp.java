package org.processmining.aam.exp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlGZIPSerializer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.ptml.exporting.PtmlExportTree;
import org.processmining.xlu.experiment.generator.GenerateLog;
import org.processmining.xlu.experiment.generator.GenerateLogParameters;
import org.processmining.xlu.experiment.generator.GenerateTree;
import org.processmining.xlu.experiment.generator.GenerateTreeParameters;

public class UtilExp {
	


	public static ProcessTree generateProcessTree(int numActs) {
		PluginContext context = null;
		String modelfilename = "model.ptml";

		PtmlExportTree exporter = new PtmlExportTree();
//		String modelName = UtilWriter.int2Letter(modelNum);

		GenerateTree treeGen = new GenerateTree();
		GenerateTreeParameters treeGenParam = new GenerateTreeParameters();
		int numAct = 20;
		treeGenParam.setNumberOfActivities(numAct);
		ProcessTree tree = treeGen.generateTree(treeGenParam);
		
		try {
			exporter.exportDefault(context, tree, new File(modelfilename));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tree;
	}
	
	public static XLog generateLog(ProcessTree tree) {
		
		GenerateLog logGen = new GenerateLog();
		GenerateLogParameters logGenParam = new GenerateLogParameters();
		logGenParam.setNoiseEventsPerTrace(0);
		logGenParam.setNumberOfTraces(1000);
		logGenParam.setMaxTraceLength(80);

		XLog log = logGen.generateLog(tree, logGenParam);

		String readlogfilename = "Log.xes.gz";
//		XesXmlGZIPParser parser = new XesXmlGZIPParser();
//		log = parser.parse(new File(readlogfilename)).get(0);

		// Output log
		serializeLogAsGZ(log, readlogfilename);
		return log;
	}

	
	
	public static void serializeLogAsGZ(XLog log, String readlogfilename) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(readlogfilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XesXmlGZIPSerializer serializer = new XesXmlGZIPSerializer();
		try {
			serializer.serialize(log, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exportTree() {
//		// Export process tree
//		try {
//			exporter.exportDefault(context, tree, new File(modelfilename));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// Import process tree
//		try {
//			tree = (ProcessTree) (new PtmlImportTree()).importFile(context, modelfilename);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
	}
}
