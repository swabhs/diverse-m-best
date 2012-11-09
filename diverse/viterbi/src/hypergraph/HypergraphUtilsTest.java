package hypergraph;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import basicViterbi.BaseTest;

public class HypergraphUtilsTest extends BaseTest {
	
	private Hypergraph h;

	public HypergraphUtilsTest(){
		super();
		h = createHypergraph();
	}
	
	@Test
	public void testGetTerminals() {
		// Elements need to be in order
		List<Integer> expected = Arrays.asList(0, 1, 2);
		List<Integer> actual = HypergraphUtils.getTerminals(h);
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void testGenerateIncomingMap() {
		Map<Integer, List<Hyperedge>> actualMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> actual = new ArrayList<Integer>();
		for (Hyperedge e :actualMap.get(7)) {
			actual.add(e.getId());
		}
		List<Integer> expected = Arrays.asList(5, 6);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testGenerateIncomingMap_SourceVertices() {
		Map<Integer, List<Hyperedge>> actualMap = HypergraphUtils.generateIncomingMap(h);
		List<Integer> actual = new ArrayList<Integer>();
		for (Hyperedge e :actualMap.get(2)) {
			actual.add(e.getId());
		}
		List<Integer> expected = new ArrayList<Integer>();
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void testGenerateOutgoingMap() {
		Map<Integer, List<Hyperedge>> actualMap = HypergraphUtils.generateOutgoingMap(h);
		List<Integer> actual = new ArrayList<Integer>();
		for (Hyperedge e :actualMap.get(6)) {
			actual.add(e.getId());
		}
		List<Integer> expected = Arrays.asList(6, 7);
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void testGenerateOutgoingMap_TargetVertex() {
		Map<Integer, List<Hyperedge>> outgoingMap = HypergraphUtils.generateOutgoingMap(h);
		List<Integer> actual = new ArrayList<Integer>();
		for (Hyperedge e :outgoingMap.get(8)) {
			actual.add(e.getId());
		}
		List<Integer> expected = new ArrayList<Integer>();
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void testToposort() {
		List<Integer> actual = HypergraphUtils.toposort(h);
		List<Integer> expected = Arrays.asList(0, 5, 1, 4, 6, 2, 3, 7, 8);
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void testRenderResult() {	
		String expected = "S VP V NP N D ";
		List<Hyperedge> allEdges = createEdges();
		
		List<Hyperedge> actualEdges = Arrays.asList(
				allEdges.get(0), allEdges.get(1), allEdges.get(3), 
				allEdges.get(2), allEdges.get(5), allEdges.get(7));
		String actual = HypergraphUtils.renderResult(actualEdges, h);
		assertEquals(expected, actual);
	}
}
