package viterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import semiring.Derivation;
import semiring.Semiring;

/**
 * Generic viterbi to run on algo0 and algo1
 * @author swabha
 *
 */
public class KBestViterbi {
	
	/** Dynamic programming state saving variables */
	List<List<Derivation>> derivationsSet;
	
	/** 
	 * Initializes the weight of terminal nodes to 1.0 and the rest of the nodes to 0.0
	 * For every node, initializes the best possible hyperedge to reach it(backPointers) to null
	 */
	public List<List<Derivation>> initialize(Hypergraph h) {
		derivationsSet = new ArrayList<List<Derivation>>();
		List<Integer> terminalIds = HypergraphUtils.getTerminals(h);
		
		for (int i = 0; i < terminalIds.size(); i++) {
			List<Derivation> dList = new ArrayList<Derivation>();
			dList.add(new Derivation(null, 1.0));
			derivationsSet.add(dList);
		}
		return derivationsSet;
	}
	
	/**
	 * Run Viterbi on a KBestSemiring and get a list of at most K derivation lists, the 1 best for each vertex
	 */
	public List<List<Derivation>> run(Hypergraph h, Semiring<List<Derivation>> semiring) {
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		initialize(h);
				
		for (Integer v: vertices) {	
			List<Hyperedge> incomingEdges = inMap.get(v);
			List<Derivation> dList = null;
			if (v >= derivationsSet.size()) {
				dList = new ArrayList<Derivation>();
			} else {
				dList = derivationsSet.get(v);
			}
			
			for (Hyperedge e : incomingEdges) {
				List<List<Derivation>> subDerivations = new ArrayList<List<Derivation>>();
				for (Integer child : e.getChildrenIdsList()) {
					subDerivations.add(derivationsSet.get(child));
				}
				List<Derivation> product = semiring.multiply(subDerivations);
				for (Derivation d : product) {
					d.setE(e);
					d.setScore(d.getScore() * e.getWeight());
				}
				dList = semiring.add(dList, product);				
			}
			derivationsSet.add(dList);
		}
		return derivationsSet;
	}
}
