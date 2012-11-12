package viterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import semiring.Derivation;
import utility.MaxPriorityQ;

public class Viterbi3 {
	/** Dynamic programming state saving variables */
	List<List<Derivation>> derivationsSet;
		
	/** 
	 * Initializes the weight of terminal nodes to 1.0 and the rest of the nodes to 0.0
	 * For every node, initializes the best possible hyperedge to reach it(backPointers) to null
	 */
	public List<List<Derivation>> initialize(Hypergraph h) {
		derivationsSet = new ArrayList<List<Derivation>>();
		List<Integer> terminalIds = HypergraphUtils.getTerminals(h);
		
		for (Vertex v : h.getVerticesList()) {
			if (terminalIds.contains(v.getId())) {
				Derivation d = new Derivation(null, 1.0);
				List<Derivation> dList = new ArrayList<Derivation>();
				dList.add(d);
				derivationsSet.add(dList);
			}			
		}
		return derivationsSet;
	}
	
	/**
	 * Run Viterbi on a semiring and get a list of vertex ids which result in the highest probability structure
	 */
	public List<Derivation> run(Hypergraph h, int k) {
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		initialize(h);
		
		for (Integer v: vertices) {	
			derivationsSet.add(v, findKBestForVertex(k, inMap.get(v)));
			
		}
		int root = derivationsSet.size() - 1;
		return derivationsSet.get(root);
	}
	
	/** merge + max */
	List<Derivation> findKBestForVertex(int k, List<Hyperedge> edges) {
			
		Map<Hyperedge, List<List<Derivation>>> fullSet = 
				new HashMap<Hyperedge, List<List<Derivation>>>();
		Map<Hyperedge, List<Integer>> counter = new HashMap<Hyperedge, List<Integer>>();
		
		// Initialize data structures
		for (Hyperedge e: edges) {
			counter.put(e, getZeroVector(e.getChildrenIdsCount()));
			List<List<Derivation>> derivationsUnderEdge = new ArrayList<List<Derivation>>();
			for (Integer u : e.getChildrenIdsList()) {
				derivationsUnderEdge.add(derivationsSet.get(u));
			}
			fullSet.put(e, derivationsUnderEdge);
		}
		
		List<Derivation> kbest = new ArrayList<Derivation>();
		MaxPriorityQ candidates = new MaxPriorityQ();
		while (candidates.getSize() < k) {			
			generateNextCandidates(fullSet, counter, candidates, edges);
			kbest.add(candidates.extractMax());			
		}
		return kbest;
	}
	
	/** generates next |e| candidates to look at */
	void generateNextCandidates(
			Map<Hyperedge, List<List<Derivation>>> fullSet,
			Map<Hyperedge, List<Integer>> counter,
			MaxPriorityQ candidates,
			List<Hyperedge> edges) {
		
		for (Hyperedge e: edges) {
			List<List<Derivation>> allD = fullSet.get(e); 
			List<Integer> vector = counter.get(e);
			double scoreProd = 1.0;
			double max = 0.0;
			int maxPos = 0;
			for (int i = 0; i < e.getChildrenIdsCount(); i++) {
				if (allD.get(i).get(vector.get(i)).getScore() > max) {
					maxPos = i;
				}
				scoreProd *= allD.get(i).get(vector.get(i)).getScore();
			}
			vector.set(maxPos, vector.get(maxPos) + 1);
			counter.put(e, vector);
			candidates.insert(new Derivation (e, scoreProd * e.getWeight()));
		}
	}
	
	private List<Integer> getZeroVector(int size) {
		List<Integer> zeroVector = new ArrayList<Integer>(size);
		for (Integer i : zeroVector) {
			zeroVector.add(new Integer(0));
		}
		return zeroVector;
	}
}
