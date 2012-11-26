package viterbi;

import static org.junit.Assert.*;

import java.util.List;

import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;

import org.junit.Test;

import semiring.Derivation;
import semiring.KBestSemiring;
import semiring.KBestSemiringSmart;

import basicViterbi.BaseTest;

public class KBestViterbiTest extends BaseTest{
	
	private Hypergraph h;
	
	private KBestViterbi viterbi;
	
	public KBestViterbiTest() {
		h = createHypergraph();
		
	}

	@Test
	public void testInitialize() {
		viterbi = new KBestViterbi();
		List<List<Derivation>> derSet = viterbi.initialize(h);
		int i = 0;
		for (List<Derivation> der : derSet) {
			System.out.println(h.getVertices(i).getName() + "----------");
			for (Derivation d : der) {
				System.out.println(d.getScore());
			}
			i++;
		}
	}

	@Test
	public void testRun_KBestSemiring() {
		viterbi = new KBestViterbi();
		List<List<Derivation>> derSet = viterbi.run(h, new KBestSemiring(2));
		for (Vertex v :h.getVerticesList()) {
			System.out.println(v.getName() + "--------");
			List<Derivation> kbest = derSet.get(v.getId());
			for (Derivation k : kbest) {
				System.out.println(k.getScore());
			}
			
		}
	}
	
	@Test
	public void testRun_KBestSemiringSmart() {
		viterbi = new KBestViterbi();
		List<List<Derivation>> derSet = viterbi.run(h, new KBestSemiringSmart(2));
		for (Vertex v :h.getVerticesList()) {
			System.out.println(v.getName() + "--------");
			List<Derivation> kbest = derSet.get(v.getId());
			for (Derivation k : kbest) {
				System.out.println(k.getScore());
			}
			
		}
	}

}
