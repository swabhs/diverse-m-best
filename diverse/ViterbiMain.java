/**
 * Class that executes Viterbi for hypergraphs.
 * @author swabha
 */
package shortestPath;

import java.util.List;
import java.util.ArrayList;

import shortestPath.HypergraphProto.Hyperedge;
import shortestPath.HypergraphProto.Hypergraph;
import shortestPath.HypergraphProto.Vertex;

public class ViterbiMain {

	public static void main(String[] args) {
		Viterbi v = new Viterbi(null);
		v.run();
	}
	
	static Hypergraph createHypergraph() {
		List<Vertex> vertices = createVertices();
		List<Hyperedge> edges = createEdges(vertices);
		Hypergraph h = Hypergraph.newBuilder().addAllEdges(edges).addAllVertices(vertices).build();
		return h;
	}
	
	static List<Vertex> createVertices() {
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(Vertex.newBuilder().setId(1).setName("the").build());
		vertices.add(Vertex.newBuilder().setId(2).setName("man").build());
		vertices.add(Vertex.newBuilder().setId(3).setName("laughed").build());
		vertices.add(Vertex.newBuilder().setId(4).setName("D").build());
		vertices.add(Vertex.newBuilder().setId(5).setName("N").build());
		vertices.add(Vertex.newBuilder().setId(6).setName("V").build());
		vertices.add(Vertex.newBuilder().setId(7).setName("NP").build());
		vertices.add(Vertex.newBuilder().setId(8).setName("VP").build());
		vertices.add(Vertex.newBuilder().setId(9).setName("S").build());
		return vertices;
	}
	
	static List<Hyperedge> createEdges(List<Vertex> vertices) {
		List<Hyperedge> edges = new ArrayList<Hyperedge>();
		edges.add(Hyperedge.newBuilder().setId(1).setParentId(6).setChildrenIds(0, 1).setWeight(1.0).build());
		edges.add(Hyperedge.newBuilder().setId(2).setParentId(5).setChildrenIds(0, 2).setWeight(1.0).build());
		edges.add(Hyperedge.newBuilder().setId(3).setParentId(4).setChildrenIds(0, 3).setWeight(1.0).build());
		//edges.add(Hyperedge.newBuilder().setId(2).setParentId(5).setChildrenIds(0, 2).setWeight(1.0).build());
		edges.add(Hyperedge.newBuilder().setId(5).setParentId(7).setChildrenIds(0, 5).setWeight(0.0).build());
		edges.add(Hyperedge.newBuilder().setId(6).setParentId(8).setChildrenIds(0, 4).setWeight(1.0).build());
		//edges.add(Hyperedge.newBuilder().setId(2).setParentId(5).setChildrenIds(0, 2).setWeight(1.0).build());
		//edges.add(Hyperedge.newBuilder().setId(2).setParentId(5).setChildrenIds(0, 2).setWeight(1.0).build());
		return edges;
	}
	
	
}
