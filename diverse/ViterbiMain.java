/**
 * Class that executes Viterbi for hypergraphs.
 * @author swabha
 */
package shortestPath;

import java.util.List;
import java.util.ArrayList;

import shortestPath.HypergraphProto.Hyperedge;
import shortestPath.HypergraphProto.Vertex;

public class ViterbiMain {

	public static void main(String[] args) {
		Viterbi v = new Viterbi(null);
		v.run();
	}
	
	static void createHypergraph() {
		
	}
	
	static List<Vertex> createVertices() {
		List<Vertex> vertices = new ArrayList<Vertex>();
		for (int i = 0; i < 9; i++) {
			Vertex v = Vertex.newBuilder().setId(i).build();
			vertices.add(v);
		}
		return vertices;
	}
	
	static void createEdges(List<Vertex> vertices) {
		List<Hyperedge> edges = new ArrayList<Hyperedge>();
		
	}
}
