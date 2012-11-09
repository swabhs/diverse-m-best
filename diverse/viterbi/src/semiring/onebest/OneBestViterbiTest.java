package semiring.onebest;

import static org.junit.Assert.*;
import hypergraph.HypergraphUtils;
import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import basicViterbi.BaseTest;

public class OneBestViterbiTest extends BaseTest {

	private Hypergraph h;
	private OneBestViterbi v;

	public OneBestViterbiTest(){ 
		super();
		h = createHypergraph();
		v = new OneBestViterbi(h);
	}
	
	@Test
	public void testInitialize() {
		
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
