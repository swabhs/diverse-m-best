package basicViterbi;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Base class for all tests, generates a sample hypergraph to be used for test.
 * @author swabha
 */
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
		vertices.add(Vertex.newBuilder().setId(0).setName("the")
				.addAllOutEdge(Arrays.asList(0)).build());
		vertices.add(Vertex.newBuilder().setId(1).setName("man")
				.addAllOutEdge(Arrays.asList(1)).build());
		vertices.add(Vertex.newBuilder().setId(2).setName("laughed")
				.addAllOutEdge(Arrays.asList(2)).build());
		vertices.add(Vertex.newBuilder().setId(3).setName("V")
				.addAllInEdge(Arrays.asList(2)).addAllOutEdge(Arrays.asList(5, 6)).build());
		vertices.add(Vertex.newBuilder().setId(4).setName("N")
				.addAllInEdge(Arrays.asList(1)).addAllOutEdge(Arrays.asList(3, 4)).build());
		vertices.add(Vertex.newBuilder().setId(5).setName("D")
				.addAllInEdge(Arrays.asList(0)).addAllOutEdge(Arrays.asList(3)).build());
		vertices.add(Vertex.newBuilder().setId(6).setName("NP")
				.addAllInEdge(Arrays.asList(3, 4)).addAllOutEdge(Arrays.asList(6, 7)).build());
		vertices.add(Vertex.newBuilder().setId(7).setName("VP")
				.addAllInEdge(Arrays.asList(5, 6)).addAllOutEdge(Arrays.asList(7)).build());
		vertices.add(Vertex.newBuilder().setId(8).setName("S")
				.addAllInEdge(Arrays.asList(7)).build());
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

}
