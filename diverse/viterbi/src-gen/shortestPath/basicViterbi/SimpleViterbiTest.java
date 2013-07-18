package basicViterbi;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import hmmHypergraph.BaseTest;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphUtils;

public class SimpleViterbiTest extends BaseTest {

	private SimpleViterbi v;
	
	public SimpleViterbiTest() {
		super();
		v = new SimpleViterbi(h);
	}

	@Test
	public void testInitialize() {
		v.initialize();
		List<Double> actual = new ArrayList<Double>();
		for (int i = 0; i < v.pi.size(); i++) {
			actual.add(v.pi.get(i));
		}
		List<Double> ones = Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0);
		List<Double> zeros = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
		List<Double> expected = new ArrayList<Double>();
		expected.addAll(ones);
		expected.addAll(zeros);
		expected.addAll(zeros);
		expected.addAll(zeros);
		expected.addAll(zeros);
		expected.add(0.0);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testRun() {
		Map<Integer, Hyperedge> results = v.run();
		for (Integer vertex : results.keySet()) {
			System.out.println("Best way to reach vertex " + vertex + " : " + results.get(vertex).getId() + "\t"); 
		}
	}
}
