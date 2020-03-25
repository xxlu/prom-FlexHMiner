
package org.processmining.aam.exp.util;
import java.util.Map;

import org.processmining.framework.util.collection.MultiSet;

import gnu.trove.map.hash.THashMap;

/**
 * @author xlu
 * 
 */
public class ModelQuality {

	public static String LOG_PREC = "NormPr(ABS)";
	public static String LOG_PREC_TO_UB = "NormToImpr";
	public static String SYS_PREC_TO_LB = "SysPr(ABS)";
	public static String SYS_PREC_TO_UB = "SysPrecToImpr";

	public static final String SYS_PREC_LB = "SysPrecBase";
	public static String LOG_PREC_LB = "LogPrecBase";

	public double fitness = 0.0;
	public double precision = 0.0;
	public double generalisation = 0.0;

//	public MarkedPetrinet net;

	public Map<String, Integer> mapTypeMove2Num = new THashMap<>();
	public Map<String, String> properties = new THashMap<>();

	public double precisionDistinctlabels;
	public double precisionDistinctlabelsTau;

	/**
	 * 
	 */
//	public MapTrcEnabled mapTrcEnable;
	public double precWrtMGen = 0.0;
	public double recallWrtMGen = 0.0;

	public Map<String, MultiSet<String>> mapEvtClass2Set;
	private boolean isImproved = true;

	public boolean isImproved() {
		return isImproved;
	}

	public Map<String, MultiSet<String>> getMapEvtClass2Set() {
		return mapEvtClass2Set;
	}

	public void setMapEvtClass2Set(Map<String, MultiSet<String>> mapEvtClass2Set) {
		this.mapEvtClass2Set = mapEvtClass2Set;
	}

	public String toString() {
		return "f: " + this.fitness + " | p: " + this.precision;				 
	}
//	public MapTrcEnabled getMapTrcEnable() {
//		return mapTrcEnable;
//	}

//	public void setMarkedNet(MarkedPetrinet markedPetrinet) {
//		net = markedPetrinet;
//
//	}

	public void set(String string, int num) {
		// TODO Auto-generated method stub
		this.mapTypeMove2Num.put(string, num);
//		quality.set("numSM", numSM);
//		quality.set("numMM", numMM);
//		quality.set("numLM", numLM);
//		quality.set("numMMinv", numMMinv);
	}

	public int getNumSM() {
		return this.mapTypeMove2Num.get("numSM");
	}

	public int getNumLM() {
		return this.mapTypeMove2Num.get("numLM");
	}

	public int getNumMM() {
		return this.mapTypeMove2Num.get("numMM");
	}

	public int getNumMMinv() {
		return this.mapTypeMove2Num.get("numMMinv");
	}

	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

//	public void setEnabledSet(MapTrcEnabled enabledSet) {
//		this.mapTrcEnable = enabledSet;		
//	}

	public void setImproved(boolean b) {
		this.isImproved = b;

	}

	boolean istimeout = false;
	public boolean isGEtimeout = false;
	
	public void setTimeout(boolean b) {
		// TODO Auto-generated method stub
		istimeout = b;
	}
	public boolean isTimeout() {
		return istimeout;
	}
}
