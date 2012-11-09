package basicViterbi;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SimpleViterbiTest extends BaseTest {

	private Hypergraph h;
	private SimpleViterbi v;

	public SimpleViterbiTest(){ 
		super();
		h = createHypergraph();
		v = new SimpleViterbi(h);
	}
	
	@Test
	public void testInitialize() {
		v.initialize();
		List<Double> actual = new ArrayList<Double>();
		for (int i = 0; i < v.pi.size(); i++)
			actual.add(v.pi.get(i));
		List<Double> expected = Arrays.asList(1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testRun() {
		List<Integer> expected = Arrays.asList(0, 1, 3, 2, 5, 7);
		List<Hyperedge> results = v.run();
		List<Integer> actual = new ArrayList<Integer>();
		for (Hyperedge result : results) {
			actual.add(result.getId()); 
		}
		assertTrue(actual.equals(expected));
		System.out.println(HypergraphUtils.renderResult(results, h));
	}

}