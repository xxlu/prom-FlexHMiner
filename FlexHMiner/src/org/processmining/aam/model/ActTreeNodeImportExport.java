package org.processmining.aam.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.aam.exp.util.UtilIO;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ActTreeNodeImportExport {

	public static void exportToFile(ActTreeNode tree, String filePath) {
		File file = new File(filePath);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		Set<String> paths = new THashSet<>();
		try {

			Enumeration enode = tree.breadthFirstEnumeration();
			while (enode.hasMoreElements()) {
				ActTreeNode node = (ActTreeNode) enode.nextElement();
				if (node.isLeaf()) {
					List<String> path = new ArrayList<>();
					path.add(node.getProcessLabel());

					ActTreeNode newNode = (ActTreeNode) node.getParent();
					while (newNode.getParent() != null) { // else, newNode is root

						path.add(0, newNode.getProcessLabel());
						newNode = (ActTreeNode) newNode.getParent();

					}
					paths.add(String.join(";", path));
					
				}
				
			
			}
			for(String path : paths) {
				bw.write(path);
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ActTreeNode ImportFromFile(String filePath) {
		File file = new File(filePath);
		FileInputStream out = null;
		try {
			out = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(out));

		ActTreeNode root = new ActTreeNode("root");
		Map<String, ActTreeNode> map = new THashMap<>();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				String[] path = line.split(";");
				
				addPathToHierachy(root, map, path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return root;
	}
	
	public static void addPathToHierachy(ActTreeNode root, Map<String, ActTreeNode> map, String[] path) {
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
}
