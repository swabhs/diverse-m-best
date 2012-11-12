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
	
	//private List<Derivation> subDerivations;

	public Derivation(Hyperedge e, Double score) {
	// public Derivation(Hyperedge e, Double score, List<Derivation> subDerivations) {
		super();
		this.e = e;
		this.score = score;
		//this.subDerivations = subDerivations;
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
/*
	List<Derivation> getSubDerivations() {
		return subDerivations;
	}

	void setSubDerivations(List<Derivation> subDerivations) {
		this.subDerivations = subDerivations;
	}*/

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
