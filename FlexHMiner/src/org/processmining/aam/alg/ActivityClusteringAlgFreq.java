package org.processmining.aam.alg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XLogImpl;
import org.processmining.aam.model.ActTreeNode;
import org.processmining.aam.model.DistinctSeq;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultiset;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ActivityClusteringAlgFreq implements ActivityClusteringAlg {

	public ActTreeNode calcActivityHierarchy(XLog log) {
		

		XEventClassifier classifier = new XEventNameClassifier();
		XEventClasses eclasses = XEventClasses.deriveEventClasses(classifier, log);
		

		int spNum = 0;
		
		Queue<String> queueActivities = new LinkedList<>();
		for(XEventClass c : eclasses.getClasses()) {
			queueActivities.add(c.toString());
		}
		ActTreeNode root = new ActTreeNode("root");
		ActTreeNode sp = root;
		
		XLog curLog = (XLog) log.clone();

		// Create variants
		Multiset<DistinctSeq> set = HashMultiset.create();
		for (XTrace t : curLog) {
			List<String> ts = getTrace(t, classifier);
			set.add(new DistinctSeq(ts));
		}
		Comparator<Multiset.Entry<DistinctSeq>> byCount = new Comparator<Multiset.Entry<DistinctSeq>>() {
		    public int compare(Multiset.Entry<DistinctSeq> e1, Multiset.Entry<DistinctSeq> e2) {
		        return e2.getCount() - e1.getCount();
		    }
		};
		Set<Multiset.Entry<DistinctSeq>> entriesSortedByCount = createVariants(set, byCount);
		
		
		int spMaxSize = 15;
		int spMinSize = 5;
		while(!queueActivities.isEmpty()) { // TODO
			Set<String> selectedActs = new THashSet<>();
			Iterator<Entry<DistinctSeq>> iter = entriesSortedByCount.iterator();
			DistinctSeq seq = iter.next().getElement();
			Set<String> seqActs = seq.getSet();
			selectedActs.addAll(seqActs);
			
			while(selectedActs.size() < spMinSize && iter.hasNext()) {
				
				DistinctSeq seqNext = iter.next().getElement();
				Set<String> seqNextActs = seqNext.getSet();
				
				seqNextActs.removeAll(selectedActs);
				
				if(seqNextActs.size() > 0 && dist(seq, seqNext)) { 
					selectedActs.addAll(seqNextActs);					
				}
				
				
			} // either sp.size > spMinSize, or no trace
			
			queueActivities.removeAll(selectedActs);
			
			set = updateLog(set, selectedActs);
			entriesSortedByCount = createVariants(set, byCount);
			
			
			for (String s : selectedActs) {
				ActTreeNode nodeS = new ActTreeNode(s);
				sp.add(nodeS);
			}
			
			if(!queueActivities.isEmpty() && queueActivities.size() > 2) {
				ActTreeNode nodeSp = new ActTreeNode("Sp" + spNum);
				sp.add(nodeSp);
				sp = nodeSp;
				spNum++;
			}
			
		}
		return root;
		
	}



	private Multiset<DistinctSeq> updateLog(Multiset<DistinctSeq>  curLog, Set<String> selectedActs) {
		Multiset<DistinctSeq> set = HashMultiset.create();
		for ( Entry<DistinctSeq> entry: curLog.entrySet()) {
//			List<String> newList = new ArrayList<>();
			List<String> org = entry.getElement().getSequence();
			org.removeAll(selectedActs);
			if(org.size() > 0) {
				DistinctSeq seq = new DistinctSeq(org);
				set.add(seq, entry.getCount());
			}
		}
		return set;
	}

	private boolean dist(DistinctSeq seq, DistinctSeq seqNext) {
		// TODO Auto-generated method stub
		return true;
	}

	private Set<Multiset.Entry<DistinctSeq>> createVariants(Multiset<DistinctSeq> set, Comparator<Entry<DistinctSeq>> byCount) {
		
		Set<Entry<DistinctSeq>> entries = set.entrySet();
		Set<Multiset.Entry<DistinctSeq>> entriesSortedByCount = Sets.newTreeSet(byCount);
		entriesSortedByCount.addAll(entries);
		return entriesSortedByCount;
	}

	private List<String> getTrace(XTrace t, XEventClassifier classifier) {
		List<String> list = new ArrayList<String>();
		for(XEvent e : t) {
			list.add(classifier.getClassIdentity(e));
		}
		return list;
	}

}
