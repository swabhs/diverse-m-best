package viterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import semiring.Derivation;
import semiring.OneBestSemiring;
import semiring.Semiring;

public class OneBestViterbi {

	/** Derivation(v) = <edge, score> */
	List<Derivation> derivations;
	
	Semiring<Derivation> semiring;
	
	OneBestViterbi() {
		this.semiring = new OneBestSemiring();
	}

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
				// Exact definition for terminal vertices - no edge associated, so null
				d = new Derivation(null, 1.0);
			} else {
				// For each non terminal vertex, this needs to be set to the 1 best edge to reach it
				d = new Derivation(null, 0.0);
			}			
			derivations.add(v.getId(), d);
		}
		
		return derivations;
	}
	
	/**
	 * Run Viterbi on a OneBestSemiring and get a list of derivations, the 1 best for each vertex
	 */
	public List<Derivation> run(Hypergraph h) { 
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		initialize(h);
		
		for (Integer v: vertices) {	
			List<Hyperedge> incomingEdges = inMap.get(v);
			Derivation dv = derivations.get(v);
			
			for (Hyperedge e : incomingEdges) {
				// Construct a list of all sub derivations of a derivation
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
		return derivations;
	}
	
}
