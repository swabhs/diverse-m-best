package basicViterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Viterbi style algorithm to find the path with maximum weight in a hypergraph.
 * The hypergraph is defined in hypergraph.proto
 * 
 * @author swabha
 */
class SimpleViterbi {
	
	private Hypergraph h;
	
	/** Dynamic programming state saving variables */
	List<Double> pi;
	List<Hyperedge> backPointers;
	
	SimpleViterbi(Hypergraph h){
		this.h = h;
		pi = new ArrayList<Double>(h.getVerticesCount());
		backPointers = new ArrayList<Hyperedge>();
	}
	
	/** 
	 * Initializes the weight of terminal nodes to 1.0 and the rest of the nodes to 0.0
	 * For every node, initializes the best possible hyperedge to reach it(backPointers) to null
	 */
	List<Double> initialize() {
		for (Vertex v : h.getVerticesList()) {
			pi.add(v.getId(), 0.0);
		}
				
		List<Integer> terminalIds = HypergraphUtils.getTerminals(h);
		for(Integer id: terminalIds) {
			pi.set(id, 1.0);
		}
		
		return pi;
	}
	
	/**
	 * Run CKY and get a list of vertex ids which result in the highest probability structure
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
				double childProduct = 1.0;
				
				for (Integer i : e.getChildrenIdsList()) {
					childProduct *= pi.get(i);
				}
				if (pi.get(v) < childProduct * e.getWeight()) {
					pi.set(v, childProduct * e.getWeight());
					backPointers.remove(backPointers.size() - 1);
					backPointers.add(e);
				}
			}
		}
		return backPointers;
	}
	
}