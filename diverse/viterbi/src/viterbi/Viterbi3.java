package viterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import semiring.Derivation;
import utility.MaxPriorityQ;
import utility.PositionVector;

/**
 * Implementation of Algorithm 2 from the Chiang-Huang paper.
 * @author swabha
 *
 */
public class Viterbi3 {
	
	private int k;
	
	/** Saves a k sized list of derivations for each vertex */
	private List<List<Derivation>> derivationsSet;
	
	Viterbi3(int k) {
		super();
		this.k = k;
	}
		
	/** 
	 * Initializes the weight of terminal nodes to 1.0 and the rest of the nodes to 0.0
	 * For every node, initializes the best possible hyperedge to reach it(backPointers) to null
	 */
	List<List<Derivation>> initialize(Hypergraph h) {
		derivationsSet = new ArrayList<List<Derivation>>();
		List<Integer> terminalIds = HypergraphUtils.getTerminals(h);
		
		for (int i = 0; i < terminalIds.size(); i++) {
			Derivation d = new Derivation(null, 1.0);
			List<Derivation> dList = new ArrayList<Derivation>();
			dList.add(d);
			derivationsSet.add(dList);			
		}
		return derivationsSet;
	}
	
	/**
	 * Run Viterbi to get a list of k best derivations for each vertex in hypergraph
	 */
	List<List<Derivation>> run(Hypergraph h) {
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		List<Integer> terminals = HypergraphUtils.getTerminals(h);
		System.out.println(terminals);
		System.out.println(vertices);
		initialize(h);
		
		for (Integer v: vertices) {	
			List<Hyperedge> edges = inMap.get(v);
			System.out.println(v + ": " + edges.size());
			if (!terminals.contains(v)) {
				derivationsSet.add(v, findKBestForVertex(edges));
			}
		}
		return derivationsSet;
	}
	
	/** merge + max */
	private List<Derivation> findKBestForVertex(List<Hyperedge> edges) {
		List<Derivation> kbest = new ArrayList<Derivation>();	
		MaxPriorityQ q = new MaxPriorityQ();
		
		Map<Hyperedge, List<List<Derivation>>> derivationMap = 
				new HashMap<Hyperedge, List<List<Derivation>>>();
		Map<Hyperedge, PositionVector> positionMap = 
				new HashMap<Hyperedge, PositionVector>();
		Map<Hyperedge, Map<Derivation, PositionVector>> fullMap = 
				new HashMap<Hyperedge, Map<Derivation, PositionVector>>();
		
		// Fill in data structures
		for (Hyperedge e: edges) {
			// Set derivationMap
			List<List<Derivation>> derivationsUnderEdge = new ArrayList<List<Derivation>>();
			for (Integer u : e.getChildrenIdsList()) {
				derivationsUnderEdge.add(derivationsSet.get(u));
			}
			
			// Set positionMap
			PositionVector pVector = new PositionVector(-1, e.getChildrenIdsCount());
			positionMap.put(e, pVector);
			
			// Set fullMap
			Derivation candidate = 
					ViterbiUtils.getCandidateDerivation(derivationsUnderEdge, pVector);
			Map<Derivation, PositionVector> posOfDerivation = 
					new HashMap<Derivation, PositionVector>();
			posOfDerivation.put(candidate, pVector);
			fullMap.put(e, posOfDerivation);
			
			q.insert(candidate);
		}
		
		while (kbest.size() < (k - 1) && q.size() > 0) {
			Derivation best = q.extractMax();
			kbest.add(best);			
			
			// Find out which derivation from which hyperedge was the best and 
			// get new candidates from that hyperedge
			for (Hyperedge e: edges) {
				boolean bestDerivationInEdge = false;
				Map<Derivation, PositionVector> posOfDerivation = fullMap.get(e);
				for (Derivation d : posOfDerivation.keySet()) {
					if (best.getScore().equals(d.getScore())) {
						positionMap.put(e, posOfDerivation.get(d));
						posOfDerivation.remove(d);
						queueNextBestCandidates(
								derivationMap.get(e), positionMap.get(e), posOfDerivation, q, e);
						bestDerivationInEdge = true;
						break;
					}
				}
				if (bestDerivationInEdge) {
					fullMap.put(e, posOfDerivation);
					break;
				}
			}
			
		}
		
		if (q.size() > 0) {
			kbest.add(q.extractMax());
		}
		return kbest;
	}
	
	/**
	 * Adds to the priority queue all neighboring candidates to the extracted best candidate
	 * @param fullSet
	 * @param pVector
	 * @param q
	 * @param edges
	 */
	void queueNextBestCandidates(
			List<List<Derivation>> fullSet, 
			PositionVector counters, 
			Map<Derivation, PositionVector> posOfDerivation,
			MaxPriorityQ q, 
			Hyperedge e) {
		for (int i = 0; i < counters.size(); i++) {
			PositionVector candidatePosition = 
					counters.add(new PositionVector(i, counters.size()));
			Derivation candidateDerivation = 
					ViterbiUtils.getCandidateDerivation(derivationsSet, candidatePosition);
			candidateDerivation.setE(e);
			candidateDerivation.setScore(candidateDerivation.getScore() * e.getWeight());
			
			if (candidateDerivation != null && !q.contains(candidateDerivation)) {
				posOfDerivation.put(candidateDerivation, candidatePosition);
				q.insert(candidateDerivation);
			}				
		}
	}
	
}
