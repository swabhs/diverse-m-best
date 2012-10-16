package shortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shortestPath.HypergraphProto.Hyperedge;
import shortestPath.HypergraphProto.Hypergraph;
import shortestPath.HypergraphProto.Vertex;

/**
 * Viterbi style algorithm to find the shortest path in a hypergraph.
 * The hypergraph is defined in hypergraph.proto
 * 
 * @author swabha
 */
class Viterbi {
	
	private Hypergraph h;
	
	/** Dynamic programming state saving variable */
	private Map<Integer, Double> pi = new HashMap<Integer, Double>();
	private Map<Integer, List<Integer>> backPointers = new HashMap<Integer, List<Integer>>();

	Viterbi(Hypergraph h){
		this.h = h;
	}
	
	/** Get the list of ids for the vertices which are not parents for any hyperedge */
	List<Integer> getTerminals(Hypergraph h) {
		List<Integer> terminals = new ArrayList<Integer>();
		for (Vertex v : h.getVerticesList()) {
			terminals.add(v.getId());
		}
		for (Hyperedge e : h.getEdgesList()) {
			if (terminals.contains(e.getParentId())) {
				terminals.remove(e.getParentId());
			}
		}
		return terminals;
	}
	
	/** 1*/
	void initialize() {
		List<Integer> terminalIds = getTerminals(h);
		for(Integer id: terminalIds) {
			pi.put(id, 1.0);		
		}
		for (Vertex v : h.getVerticesList()) {
			if (!pi.containsKey(v.getId())) {
				pi.put(v.getId(), 0.0);
			}
		}
	}
	
	/** Construct a map that stores the list of incoming hyperedges for a parent vertex */
	Map<Integer, List<Hyperedge>> generateIncomingMap() {
		Map<Integer, List<Hyperedge>> inMap = new HashMap<Integer, List<Hyperedge>>();
		for(Hyperedge e: h.getEdgesList()) {
			List<Hyperedge> incoming;
			if (inMap.containsKey(e.getParentId())) {
				incoming = inMap.get(e.getParentId());
			} else {
				incoming = new ArrayList<Hyperedge>();
			}
			incoming.add(e);
			inMap.put(e.getParentId(), incoming);
		}
		return inMap;
	}
	
	/**
	 * Run CKY and get a list of vertex ids which result in the highest probability structure
	 * @param 
	 */
	List<Integer> run() {
		Map<Integer, List<Hyperedge>> inMap = generateIncomingMap();
		List<Integer> vertices = toposort(getTerminals(h), h);
		initialize();
		for (Integer v: vertices) {
			for (Hyperedge e : inMap.get(v)) {
				double childProduct = 1.0;
				for (Integer i : e.getChildrenIdsList()) {
					childProduct *= pi.get(i);
				}
				
				if (pi.get(v) < childProduct * e.getWeight()) {
					pi.put(v, childProduct * e.getWeight());
					backPointers.put(v, e.getChildrenIdsList());
				}
			}
		}
		return backPointers.get(vertices.get(vertices.size() - 1));
	}
	
	/**
	 * Topologically sorts all vertices given a list of terminal vertices
	 * @param vertices
	 * @return
	 */
	List<Integer> toposort(List<Integer> terminals, Hypergraph h) {
		List<Integer> sorted = new ArrayList<Integer>();
		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		Map<Integer, List<Hyperedge>> outMap = generateOutgoingMap();
		for(Vertex v : h.getVerticesList()) {
			visited.put(v.getId(), false);
		}
		
		for (Integer v : terminals) {
			visit(v, sorted, visited, outMap);
		}
		return sorted;
	}
	
	/** Hypergraph topological sort recursion */
	void visit(
			Integer vertexId,
			List<Integer> sorted,
			Map<Integer, Boolean> visited, 
			Map<Integer, List<Hyperedge>> outMap) {
		if (!visited.get(vertexId)) {
			visited.put(vertexId, true);
			sorted.add(vertexId);
			// get children of hyperedge that has vertexId as parent
			for (Hyperedge e : outMap.get(vertexId)) {
				boolean shouldVisitParent = true;
				for (Integer v : e.getChildrenIdsList()) {
					if (!visited.get(v)){
						shouldVisitParent = false;
						break;
					}
					if (shouldVisitParent) {
						visit(e.getParentId(), sorted, visited, outMap);
					}
				}
			}
		}
	}
	
	Map<Integer, List<Hyperedge>> generateOutgoingMap() {
		Map<Integer, List<Hyperedge>> outMap = new HashMap<Integer, List<Hyperedge>>();
		for(Hyperedge e: h.getEdgesList()) {
			List<Hyperedge> outgoing;
			for (Integer childId : e.getChildrenIdsList()) {
				if (outMap.containsKey(childId)) {
					outgoing = outMap.get(e.getParentId());
				} else {
					outgoing = new ArrayList<Hyperedge>();
				}
				outgoing.add(e);
				outMap.put(childId, outgoing);
			}			
		}
		return outMap;
	}
}
