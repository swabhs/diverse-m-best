package viterbi;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import semiring.Derivation;
import utility.PositionVector;

public class ViterbiUtils {

	/** Get backpointers from a derivation - one best*/
	public static List<Hyperedge> getBackPointers(List<Derivation> dList) {
		List<Hyperedge> backPointers = new ArrayList<Hyperedge>();
		Hyperedge e = dList.get(dList.size() - 1).getE();
		backPointers.add(e);
		Stack<Integer> stack= new Stack<Integer>(); 
		stack.addAll(e.getChildrenIdsList());
		do {
			Integer v = stack.pop();
			e = dList.get(v).getE();
			backPointers.add(e);
			stack.addAll(e.getChildrenIdsList());
			
		} while (!stack.empty());
		return backPointers;
	}
	
	/** Get backpointers from a derivation - k best*/
	//TODO(swabha) : Implement!
	public static List<Hyperedge> getBackPointersK(List<List<Derivation>> dList) {
		List<Hyperedge> backPointers = new ArrayList<Hyperedge>();
		
		return backPointers;
	}
	
	/**
	 * 
	 * @param dSet
	 * @param pv
	 * @return
	 */
	public static Derivation getCandidateDerivation(
			List<List<Derivation>> dSet, PositionVector pv) {
		double prod = 1.0;
		for (int i = 0; i < dSet.size(); i++) {
			if (dSet.get(i).size() <= pv.get(i)) {
				return null;
			}
			prod *= dSet.get(i).get(pv.get(i)).getScore();
		}
		return new Derivation(null, prod);
	}
}
