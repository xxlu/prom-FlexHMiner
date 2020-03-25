package org.processmining.aam.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XLog;
import org.processmining.aam.model.ActTreeNode;

import gnu.trove.map.hash.THashMap;
//import smile.mds.MDS;

public class ActivityClusteringAlgImpl implements ActivityClusteringAlg {

	public ActTreeNode calcActivityHierarchy(XLog log) {
		
		// Compute DFG
		DFGraph dfg = (new GraphCreationAlg()).calculateGraph(log);
		
		// Convert to Points and Distances		
		int[][] dist = dfg.getDFMatrix();
		int n=dist[0].length;    
		double[][] distDouble = new double[n][n];
		int maxFreq = 0;
		for(int i = 0; i < n; i++) {
			maxFreq = Math.max(dist[i][i], maxFreq);
			for(int j = i+1; j < n ; j++) {
				maxFreq = Math.max(dist[i][j] + dist[j][i], maxFreq);
				System.out.println(dfg.getEventClass(i).toString() + " -> " + dfg.getEventClass(j).toString() 
						+ " : " + dist[i][j]);
			}
		}
		for(int i = 0; i < n; i++) {
			distDouble[i][i] = 0.0d;
			for(int j = i+1; j < n ; j++) {
				distDouble[i][j] = 1.0d - (((double) dist[i][j] + (double) dist[j][i] ) / (double) maxFreq);
				distDouble[j][i] = distDouble[i][j];
				System.out.println(dfg.getEventClass(i).toString() + " -> " + dfg.getEventClass(j).toString() 
						+ " : " + distDouble[i][j]);
			}
		}
//		double[][] output= MDSJ.classicalScaling(distDouble); // apply MDS
//		MDS mds = new MDS(distDouble);
		double[][] coords = null;//mds.getCoordinates();
		
		
		List<DoublePoint> points = new ArrayList<DoublePoint>(n);
		Map<DoublePoint, XEventClass> mapP2E = new THashMap<>();
		for(int i=0; i<n; i++) {  
//		    double x = output[0][i];
//			double y = output[1][i];
			double x = coords[i][0];
			double y = coords[i][1];
		    DoublePoint doublePoint = new DoublePoint(new double[] {x, y});
			
		    points.add(doublePoint);
		    mapP2E.put(doublePoint, dfg.getEventClass(i));
		    System.out.println(dfg.getEventClass(i).toString() + ":" + x + ", " + y);
		    
		}
		// 
		
		int clusterMaxSize = n < 200 ?   10 :  20 ;
		int numclusters = n / clusterMaxSize  + 1;
		
//		Clusterer<DoublePoint> clusterer = new DBSCANClusterer<DoublePoint>(0.95, 4);
//		Clusterer<DoublePoint> clusterer = new FuzzyKMeansClusterer<>(6, 2);
		Clusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(numclusters);
		List<? extends Cluster<DoublePoint>> res = clusterer.cluster(points);
		
		
		
		ActTreeNode root = new ActTreeNode("root");
		int i = 0;
		for( Cluster<DoublePoint> cluster : res) {
			ActTreeNode sub = new ActTreeNode(String.valueOf(i++));
			root.add(sub);
			
			for(DoublePoint p : cluster.getPoints()) {
				String l = mapP2E.get(p).getId();
				ActTreeNode child = new ActTreeNode(l);
				sub.add(child);
			}					
		}	
		
		return root;
	}
	

	
	
//	public static void main(String[] args) {
//		double[][] input={        // input dissimilarity matrix
//		    {0.00,2.04,1.92,2.35,2.06,2.12,2.27,2.34,2.57,2.43,1.90,2.41},
//		    {2.04,0.00,2.10,2.00,2.23,2.04,2.38,2.36,2.23,2.36,2.57,2.34},
//		    {1.92,2.10,0.00,1.95,2.21,2.23,2.32,2.46,1.87,1.88,2.41,1.97},
//		    {2.35,2.00,1.95,0.00,2.05,1.78,2.08,2.27,2.14,2.14,2.38,2.17},
//		    {2.06,2.23,2.21,2.05,0.00,2.35,2.23,2.18,2.30,1.98,1.74,2.06},
//		    {2.12,2.04,2.23,1.78,2.35,0.00,2.21,2.12,2.21,2.12,2.17,2.23},
//		    {2.27,2.38,2.32,2.08,2.23,2.21,0.00,2.04,2.44,2.19,1.74,2.13},
//		    {2.34,2.36,2.46,2.27,2.18,2.12,2.04,0.00,2.19,2.09,1.71,2.17},
//		    {2.57,2.23,1.87,2.14,2.30,2.21,2.44,2.19,0.00,1.81,2.53,1.98},
//		    {2.43,2.36,1.88,2.14,1.98,2.12,2.19,2.09,1.81,0.00,2.00,1.52},
//		    {1.90,2.57,2.41,2.38,1.74,2.17,1.74,1.71,2.53,2.00,0.00,2.33},
//		    {2.41,2.34,1.97,2.17,2.06,2.23,2.13,2.17,1.98,1.52,2.33,0.00}
//	        };
//		int n=input[0].length;    // number of data objects
//		double[][] output=MDSJ.classicalScaling(input); // apply MDS
//		for(int i=0; i<n; i++) {  // output all coordinates
//		    System.out.println(output[0][i]+" "+output[1][i]);
//		}
//	}

}
