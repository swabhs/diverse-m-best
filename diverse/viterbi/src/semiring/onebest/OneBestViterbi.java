package semiring.onebest;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Viterbi style algorithm to find the path with maximum weight in a hypergraph.
 * The hypergraph is defined in hypergraph.proto
 * 
 * @author swabha
 */
class OneBestViterbi {
	
	private Hypergraph h;
	
	/** Dynamic programming state saving variables */
	OneBestSemiring pi;
	List<Hyperedge> backPointers;
	
	OneBestViterbi(Hypergraph h){
		this.h = h;
		backPointers = new ArrayList<Hyperedge>();
	}
	
	/** 
	 */
	OneBestSemiring initialize() {
		List<Double> initialValues = new ArrayList<Double>(h.getVerticesCount());
		for (Vertex v : h.getVerticesList()) {
			initialValues.add(v.getId(), 0.0);
		}
				
		List<Integer> terminalIds = HypergraphUtils.getTerminals(h);
		for(Integer id: terminalIds) {
			initialValues.set(id, 1.0);
		}
		
		pi = new OneBestSemiring(initialValues);
		return pi;
	}
	
	/**
	 */
	List<Hyperedge> run() {
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		initialize();
		for (Integer v: vertices) {	
			List<Hyperedge> incomingEdges = inMap.get(v);
			// If non-terminal, randomly initialize the backPointer
			if (incomingEdges.size() != 0) {
				backPointers.add(incomingEdges.get(0));
			}
			for (Hyperedge e : incomingEdges) {
				
				List<Double> valuesToBeMultiplied = new ArrayList<Double>();
				for (Integer i : e.getChildrenIdsList()) {
					valuesToBeMultiplied.add(pi.getElements().get(i));
				}
				
				Double childProd = pi.multiply(valuesToBeMultiplied);
				List<Double> elements = pi.getElements();
				List<Double> valuesToBeAdded = Arrays.asList(elements.get(v), childProd);
				elements.set(v, pi.add(valuesToBeAdded));
				if (pi.argmax(valuesToBeAdded) == 1) {
					
					backPointers.remove(backPointers.size() - 1);
					backPointers.add(e);
				}
			}
		}
		return backPointers;
	}
	
}