package basicViterbi;

import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	/** Dynamic programming state saving variable that saves the best score to reach a node */
	List<Double> pi;
	
	/** Dynamic programming state saving variable that saves the best hyperedge to reach a node */
	Map<Integer, Hyperedge> backPointers;
	
	SimpleViterbi(Hypergraph h){
		this.h = h;
		pi = new ArrayList<Double>(h.getVerticesCount());
		backPointers = new HashMap<Integer, Hyperedge>();
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
	Map<Integer, Hyperedge> run() {
		Map<Integer, List<Hyperedge>> inMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> vertices = HypergraphUtils.toposort(h);
		
		initialize();
		for (Integer v: vertices) {	
			List<Hyperedge> incomingEdges = inMap.get(v);
			// If non-terminal, randomly initialize the backPointer
			if (incomingEdges.size() != 0) {
				backPointers.put(v, incomingEdges.get(0));
			}
			for (Hyperedge edge : incomingEdges) {
				double childProduct = 1.0;
				
				for (Integer child : edge.getChildrenIdsList()) {
					childProduct *= pi.get(child);
				}
				if (pi.get(v) < childProduct * edge.getWeight()) {
					pi.set(v, childProduct * edge.getWeight());
					backPointers.put(v, edge);
				}
			}
		}
		int j = 0;
		for (double p : pi) {
			System.out.println("Pi for " + j + ": " + p);
			++j;
		}
		return backPointers;
	}
	
}