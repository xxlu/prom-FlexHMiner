package org.processmining.aam.exp.input;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.LineIterator;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.exp.HAIMexperiment;
import org.processmining.aam.exp.util.UtilIO;
import org.processmining.aam.model.ActTreeNode;

import gnu.trove.map.hash.THashMap;

public class UtilDataVUMC {

	static XFactory fac = new XFactoryNaiveImpl();

	public static Object[] getVUMCLabels(String dataname) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
//		String dataname = "Nierschade";
//		String dataname = "Diabetes";
//		String dataname = "HNTumor";
//		String laptop = "Lu000017";
		String laptop = "xlu330";
		String filePath = "C:\\Users\\" + laptop +"\\Dropbox\\_Data\\2018 VUmc\\"
				+"20190524 VUmc - hierachical activities\\vumc_DBCVerrichtingen"
				+ "2017" + "actFiltered_GT"+dataname+ "_withKluza.csv";
		
		LineIterator it = UtilIO.readFile(filePath);
		String SEP = ";";
		int idIndex = 0;
//		int[] labelIndices = new int[] {  18, 16, 14, 12 , 4};
		int[] labelIndices = new int[] {  19, 17, 15, 13, 11, 4}; // Use Kluza. 
//		int[] labelIndices = new int[] {  7, 4}; // Use DBC as hierarchy
		 
		
		XLog log = fac.createLog();
		Map<String, XTrace> mapId2trace = new THashMap<>();
		
		ActTreeNode root = new ActTreeNode();
		Map<String, ActTreeNode> map = new THashMap<>();
		
		// skip column names
		String line = it.nextLine();
		while (it.hasNext()) {
			line = it.nextLine();

			String[] values = line.split(SEP);
			
			// Create event
			XEvent e = fac.createEvent(fac.createAttributeMap());
			for (int i = 3; i < values.length; i++) {
				XAttributeLiteral attr = fac.createAttributeLiteral(String.valueOf(i), values[i], null);
				e.getAttributes().put(String.valueOf(i), attr);
			}
			
			Date d = null;
			try {
				d = format.parse(values[2]);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			XTimeExtension.instance().assignTimestamp(e, d);
			
			
			// Create hierarchy path
			List<String> path = new ArrayList<String>();
			String label = "";
			for(int i : labelIndices) {
				label = label + "_" + values[i];
				path.add(label);				
			}
			
			HAIMexperiment.addPathToHierachy(root, map, path);
			XConceptExtension.instance().assignName(e, label);
			
//			List<String> path = new ArrayList<String>();
//			for(int i : labelIndices) {				
//				path.add(values[i]);				
//			}
//			String label = UtilIO.concatenate(path, "_");
			
//			HAIMexperiment.addPathToHierachy(root, map, path);
//			XConceptExtension.instance().assignName(e, values[4]);
			
			updateTrace(values[idIndex], mapId2trace, e);
			
		}		
		for(XTrace t : mapId2trace.values()) {
			log.add(t);
		}
		return new Object[] {log, root};
	}
	
	

	public static void updateTrace(String stringId, Map<String, XTrace> mapId2trace, XEvent e) {
		XTrace t = mapId2trace.get(stringId);
		if(t == null) {
			t = XFactoryRegistry.instance().currentDefault().createTrace();
			XConceptExtension.instance().assignName(t, stringId);
		}
		t.add(e);
		mapId2trace.put(stringId, t);
	}
}
