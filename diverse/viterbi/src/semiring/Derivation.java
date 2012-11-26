package semiring;

import hypergraph.HypergraphProto.Hyperedge;

/**
 * 
 * @author swabha
 *
 */
public class Derivation implements Comparable<Derivation> {

	private Hyperedge e;
	
	private Double score;
	
	public Derivation(Hyperedge e, Double score) {
		this.e = e;
		this.score = score;
	}

	public Hyperedge getE() {
		return e;
	}

	public void setE(Hyperedge e) {
		this.e = e;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public int compareTo(Derivation another) {
		if (score > another.getScore()) {
			return 1;
		} else if (score == another.getScore()) {
			return 0;
		} else {
			return -1;
		}		
	}

}
