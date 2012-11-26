package viterbi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;

import org.junit.Test;

import basicViterbi.BaseTest;
import semiring.Derivation;

public class OneBestViterbiTest extends BaseTest {

	private Hypergraph h;
	private OneBestViterbi v;

	public OneBestViterbiTest(){ 
		super();
		h = createHypergraph();
		v = new OneBestViterbi();
	}
	
	@Test
	public void testInitialize() {
		List<Double> expectedScores = Arrays.asList(1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		List<Derivation> actual = v.initialize(h);
		
		List<Double> actualScores = new ArrayList<Double>();
		for (int i = 0; i < actual.size(); ++i) {
			actualScores.add(actual.get(i).getScore());
		}
		assertTrue(actualScores.equals(expectedScores));
	}
	
	public void testRun() {
		List<Integer> expectedEdges = Arrays.asList(-1, -1, -1, 2, 1, 0, 3, 5, 7);
		List<Double> expectedScores = Arrays.asList(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
		
		List<Derivation> actual = v.run(h);
		List<Integer> actualEdges = new ArrayList<Integer>();
		List<Double> actualScores = new ArrayList<Double>();
		
		for (int i = 0; i < actual.size(); i++) {
			Hyperedge e = actual.get(i).getE();
			if (e!=null) {
				actualEdges.add(e.getId());
				actualScores.add(actual.get(i).getScore());
			} else {
				actualEdges.add(-1);
				actualScores.add(0.0);
			}
		}
		assertTrue(actualEdges.equals(expectedEdges));
		assertTrue(actualScores.equals(expectedScores));
	}

}
