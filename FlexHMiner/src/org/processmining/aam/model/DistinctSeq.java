package org.processmining.aam.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gnu.trove.set.hash.THashSet;

public class DistinctSeq {
	
	private List<String> trace = new ArrayList<>();
	
	
	public DistinctSeq(List<String> trace) {
		this.trace = new ArrayList<>(trace);
	}
	
	
	public List<String> getSequence(){
		return trace;
	}
	
	@Override
	public boolean equals(Object anObject) {  
	      if (this == anObject) {  
	          return true;  
	      }  
	      if (anObject instanceof DistinctSeq) {  
	    	  DistinctSeq anotherSeq = (DistinctSeq) anObject;  
	          int n =  anotherSeq.trace.size();  
	          if (n == this.trace.size()) {  
	              int i = 0;  
	              while (i < n) {  
	                  if (!this.trace.get(i).equals(anotherSeq.trace.get(i))) {  
	                          return false;  
	                  }
	                  i++;  
	              }  
	              return true;  
	          }  
	      }  
	      return false;  
	  }  
	
	@Override
	public int hashCode() {
		String s = "";
		for(int i = 0; i < this.trace.size(); i++) {
			s += this.trace.get(i);
		}
	    return s.hashCode();
	}


	public Set<String> getSet() {
		return new THashSet<>(this.trace);
	}

}
