package semiring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.MaxPriorityQ;
import utility.PositionVector;
import viterbi.ViterbiUtils;

public class KBestSemiringSmart extends KBestSemiring {

	private int k;
	
	public KBestSemiringSmart(int k) {
		super(k);
		this.k = k;
	}
	
	/**
	 * Given a list of lists, each of size at most k, merge them and return a single list of size 
	 * at most k, containing the largest elements
	 */
	public List<Derivation> multiply(List<List<Derivation>> derivationsSet) {
		List<Derivation> kbest = new ArrayList<Derivation>();
		
		PositionVector pVector = new PositionVector(-1, derivationsSet.size());
		kbest.add(ViterbiUtils.getCandidateDerivation(derivationsSet, pVector));
		
		Map<Derivation, PositionVector> argmax = new HashMap<Derivation, PositionVector>();
		MaxPriorityQ q = new MaxPriorityQ();
		
		while (kbest.size() < k ) {
			for (int i = 0; i < pVector.size(); i++) {
				PositionVector candidatePosition = 
						pVector.add(new PositionVector(i, pVector.size()));
				Derivation candidateDerivation = 
						ViterbiUtils.getCandidateDerivation(derivationsSet, candidatePosition);
				
				if (candidateDerivation != null && !q.contains(candidateDerivation)) {
					argmax.put(candidateDerivation, candidatePosition);
					q.insert(candidateDerivation);
				}				
			}
			
			if (q.size() == 0) {
				break;
			}
			Derivation best = q.extractMax();
			
			for (Derivation d : argmax.keySet()) {
				if (d.getScore().equals(best.getScore())) {
					pVector = argmax.get(d);
					break;
				}
			}
			kbest.add(best);
		}
		
		/* Old Implementation
		 * for (List<Derivation> derivations : derivationsSet) {
			Derivation topD = derivations.get(0);
			q.insert(topD);
		}
		
		while (kbest.size() < k) {
			Derivation max = q.extractMax();
			kbest.add(max);
			
			for (List<Derivation> derivations : derivationsSet) {
				
				if (derivations.size() > 0 && max.equals(derivations.get(0))) {
					
					derivations.remove(0);
					if (derivations.size() != 0) {
						q.insert(derivations.get(0));
					}
					break;
				}
			}
		}*/
		return kbest;
	}

}
