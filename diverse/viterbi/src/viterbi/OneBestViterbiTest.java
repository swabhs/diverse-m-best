package viterbi;

import static org.junit.Assert.*;

import java.util.List;

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
		List<Derivation> actual = v.initialize(h);
		
		for (Derivation d : actual) {
			System.out.println(d.getScore());
		}
	}
	
	public void testRun() {
		Derivation d = v.run(h);
		System.out.println(d.getE());
	}

}
