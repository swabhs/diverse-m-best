package viterbi;

import static org.junit.Assert.*;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import java.util.List;

import org.junit.Test;

import semiring.Derivation;
import basicViterbi.BaseTest;

public class Viterbi3Test extends BaseTest{
	
	private Hypergraph h;
	
	private Viterbi3 viterbi;
	
	public Viterbi3Test() {
		h = createHypergraph();
	}

	@Test
	public void testInitialize() {
		viterbi = new Viterbi3(2);
		List<List<Derivation>> derSet = viterbi.initialize(h);
		int i = 0;
		for (List<Derivation> der : derSet) {
			System.out.println(h.getVertices(i).getName() + "----------");
			for (Derivation d : der) {
				System.out.println(d.getScore());
			}
			i++;
		}
		System.out.println("\n\n\n");
	}

	@Test
	public void testRun() {
		viterbi = new Viterbi3(2);
		List<List<Derivation>> derSet = viterbi.run(h);
		for (Vertex v :h.getVerticesList()) {
			System.out.println(v.getName() + "--------");
			List<Derivation> kbest = derSet.get(v.getId());
			for (Derivation k : kbest) {
				System.out.println(k.getScore());
			}
			
		}
	}

}
