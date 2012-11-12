package viterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import semiring.Derivation;
import semiring.OneBestSemiring;

public class OneBestViterbi {

	/** Dynamic programming state saving variables */
	List<Derivation> derivations;
		
	/** 
	 * Initializes the weight of terminal nodes to 1.0 and the rest of the nodes to 0.0
	 * For every node, initializes the best possible hyperedge to reach it(backPointers) to null
	 */
	public List<Derivation> initialize(Hypergraph h) {
		derivations = new ArrayList<Derivation>();
		List<Integer> terminalIds = HypergraphUtils.getTerminals(h);
		
		for (Vertex v : h.getVerticesList()) {
			Derivation d = null;
			if (terminalIds.contains(v.getId())) {
				d = new Derivation(null, 1.0);
			} else {
				d = new Derivation(null, 0.0);
			}			
			derivations.add(v.getId(), d);
		}
		
		return derivations;
	}
	
	/**
	 * Run Viterbi on a semiring and get a list of vertex ids which result in the highest probability structure
	 */
	public List<Derivation> run(Hypergraph h) { //, Semiring semiring) {
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		initialize(h);
		OneBestSemiring semiring = new OneBestSemiring();
		for (Integer v: vertices) {	
			List<Hyperedge> incomingEdges = inMap.get(v);
			Derivation dv = derivations.get(v);
			
			for (Hyperedge e : incomingEdges) {
				
				List<Derivation> subDerivations = new ArrayList<Derivation>();
				for (Integer child : e.getChildrenIdsList()) {
					subDerivations.add(derivations.get(child));
				}
				
				Derivation product = semiring.multiply(subDerivations);
				product.setE(e);
				product.setScore(e.getWeight() * product.getScore());
				
				dv = semiring.add(dv, product);							
			}
			derivations.set(v, dv);
		}
		int terminalVertex = vertices.get(vertices.size() - 1);
		return derivations;
	}
	
	
}
