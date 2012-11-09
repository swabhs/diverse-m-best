package basicViterbi;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class BaseTest extends TestCase {
	
	public BaseTest() {
		super();
	}
	
	protected static Hypergraph createHypergraph() {
		List<Vertex> vertices = createVertices();
		List<Hyperedge> edges = createEdges();
		Hypergraph h = Hypergraph.newBuilder().addAllEdges(edges).addAllVertices(vertices).build();
		return h;
	}
	
	static List<Vertex> createVertices() {
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(Vertex.newBuilder().setId(0).setName("the").build());
		vertices.add(Vertex.newBuilder().setId(1).setName("man").build());
		vertices.add(Vertex.newBuilder().setId(2).setName("laughed").build());
		vertices.add(Vertex.newBuilder().setId(3).setName("V").build());
		vertices.add(Vertex.newBuilder().setId(4).setName("N").build());
		vertices.add(Vertex.newBuilder().setId(5).setName("D").build());
		vertices.add(Vertex.newBuilder().setId(6).setName("NP").build());
		vertices.add(Vertex.newBuilder().setId(7).setName("VP").build());
		vertices.add(Vertex.newBuilder().setId(8).setName("S").build());
		return vertices;
	}
	
	protected static List<Hyperedge> createEdges() {
		List<Hyperedge> edges = new ArrayList<Hyperedge>();
		edges.add(Hyperedge.newBuilder()
				.setId(0).setParentId(5).addAllChildrenIds(Arrays.asList(0)).setWeight(1.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(1).setParentId(4).addAllChildrenIds(Arrays.asList(1)).setWeight(1.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(2).setParentId(3).addAllChildrenIds(Arrays.asList(2)).setWeight(1.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(3).setParentId(6).addAllChildrenIds(Arrays.asList(4, 5)).setWeight(1.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(4).setParentId(6).addAllChildrenIds(Arrays.asList(4)).setWeight(0.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(5).setParentId(7).addAllChildrenIds(Arrays.asList(3)).setWeight(1.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(6).setParentId(7).addAllChildrenIds(Arrays.asList(3, 6)).setWeight(0.0)
				.build());
		edges.add(Hyperedge.newBuilder()
				.setId(7).setParentId(8).addAllChildrenIds(Arrays.asList(6, 7)).setWeight(1.0)
				.build());
		return edges;
	}
	
	static Hyperedge createHyperedge(int id, int parent, List<Integer> children, double weight) {
		Hyperedge.Builder builder = Hyperedge.newBuilder();
		builder.setId(id).setParentId(parent).setWeight(weight).addAllChildrenIds(children);
		return builder.build();
	}

}
