package org.processmining.aam.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.deckfour.xes.model.XLog;
import org.processmining.aam.exp.util.UtilIOXES;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;

import bsh.StringUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ActTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1896428707404355557L;
	private String label;
	private int height;
	private XLog sublog;
	
	private MarkedPetrinet model;
	
	public ActTreeNode() {
		super();
	}
	
	public ActTreeNode(String userObject) {
		super();
		this.setProcessLabel(userObject);
	}
	
	public String getProcessLabel() {
		return label;
	}
	
	public void setProcessLabel(String label) {
		this.label = label;
		this.setUserObject(label);
	}

	public void setSubLog(XLog newLowLog) {
		this.sublog = newLowLog;
	}
	
	public XLog getSubLog() {
		return sublog;
	}
	
	public String[] getFullLabel() {
		TreeNode[] nodes = this.getPath();
		int n = nodes.length;
		String[] pathlabel = new String[n];
		for (int i = 0; i < n ; i++) {
			pathlabel[i] = ((ActTreeNode)nodes[i]).getProcessLabel();
		}
		return pathlabel;
	}

	public MarkedPetrinet getModel() {
		return model;
	}

	public void setModel(MarkedPetrinet model) {
		this.model = model;
	}

	
	

}
