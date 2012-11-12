package hypergraph;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HypergraphUtils {
	
	/**
	 * Runs in O(|V|+|E|).
	 * @return
	 */
	static Map<Integer, List<Hyperedge>> generateOutgoingMap(Hypergraph h) {		
		Map<Integer, List<Hyperedge>> outMap = new HashMap<Integer, List<Hyperedge>>();
		for (Vertex v : h.getVerticesList()) {
			outMap.put(v.getId(), new ArrayList<Hyperedge>());
		}
		
		for(Hyperedge e: h.getEdgesList()) {			
			for (Integer childId : e.getChildrenIdsList()) {
				List<Hyperedge> outgoing = outMap.get(childId);
				outgoing.add(e);
				outMap.put(childId, outgoing);
			}			
		}
		return outMap;
	}
	
	/** 
	 * Constructs a map between each vertex and its incoming hyperedges.
	 * Source vertices map to an empty list.
	 */
	public static Map<Integer, List<Hyperedge>> generateIncomingMap(Hypergraph h) {
		Map<Integer, List<Hyperedge>> inMap = new HashMap<Integer, List<Hyperedge>>();
		for (Vertex v : h.getVerticesList()) {
			inMap.put(v.getId(), new ArrayList<Hyperedge>());
		}
		
		for(Hyperedge e: h.getEdgesList()) {
			List<Hyperedge> incoming = inMap.get(e.getParentId());
			incoming.add(e);
			inMap.put(e.getParentId(), incoming);
		}
		return inMap;
	}

	/** Get the list of ids for the vertices which are not parents for any hyperedge */
	public static List<Integer> getTerminals(Hypergraph h) {
		List<Integer> terminals = new ArrayList<Integer>();
		// Assuming all the vertices are terminals
		for (Vertex v : h.getVerticesList()) {
			terminals.add(v.getId());
		}
		
		for (Hyperedge e : h.getEdgesList()) {
			int index = terminals.indexOf(e.getParentId());
			if (index != -1) {
				terminals.remove(index);
			}
		}
		return terminals;
	}
	
	/**
	 * Topologically sorts all vertices in a hypergraph given a list of terminal vertices
	 * Runs in O(|V|+|E|).
	 */
	public static List<Integer> toposort(Hypergraph h) {
		List<Integer> sorted = new ArrayList<Integer>();
		List<Integer> terminals = getTerminals(h);
		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		Map<Integer, List<Hyperedge>> outMap = generateOutgoingMap(h);
		for(Vertex v : h.getVerticesList()) {
			visited.put(v.getId(), false);
		}
		
		for (Integer v : terminals) {
			visit(v, sorted, visited, outMap);
		}
		return sorted;
	}
	
	/** Hypergraph topological sort recursion. Runs in O(|V|+|E|). */
	static private void visit(Integer n, List<Integer> sorted,	Map<Integer, Boolean> visited, 
			Map<Integer, List<Hyperedge>> outMap) {
		if (!visited.get(n)) {
			visited.put(n, true);
			sorted.add(n);
			// get children of hyperedge that has vertexId as parent
			for (Hyperedge e : outMap.get(n)) {
				boolean shouldVisitParent = true;
				for (Integer m : e.getChildrenIdsList()) {
					if (!visited.get(m)){
						shouldVisitParent = false;
						break;
					}
				}
				if (shouldVisitParent) {
					visit(e.getParentId(), sorted, visited, outMap);
				}
			}
		}
	}
	
	/** Displays the 1-best parse */
	public static String renderResult(List<Hyperedge> edges, Hypergraph h) {
		StringBuilder builder = new StringBuilder();
		Map<Integer, String> names = new HashMap<Integer, String>();
		for (Vertex v : h.getVerticesList()) {
			names.put(v.getId(), v.getName());
		}
		Collections.reverse(edges);
		for (Hyperedge e : edges) {
			if (e != null)
				builder.append(names.get(e.getParentId()));
				builder.append(" ");
		}
		return builder.toString();
	}
	
	/** Displays the hypergraph, listing each hyperedge in a line */
	public static void renderHypergraph(Hypergraph h) {
		Map<Integer, String> names = new HashMap<Integer, String>();
		for (Vertex v : h.getVerticesList()) {
			names.put(v.getId(), v.getName());
		}
		
		for (Hyperedge edges : h.getEdgesList()) {
			System.out.print("Edge: " + edges.getId() + "\t");
			System.out.print("Parent: " + names.get(edges.getParentId()) + "\t");
			System.out.print("Children: " );
			for (Integer v : edges.getChildrenIdsList()) {
				System.out.print(names.get(v) + " ");
			}
			System.out.println();
		}
	}
}
