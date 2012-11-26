package semiring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KBestSemiring implements Semiring<List<Derivation>> {
	
	private int k;
	
	public KBestSemiring(int k) {
		this.k = k;
	}

	/**
	 * Given a list of lists, each of size k, merge them and return a single list of size k,
	 * containing the k largest elements */
	@Override
	public List<Derivation> multiply(List<List<Derivation>> derivationsSet) {		
		List<Derivation> fullSet = getAllCandidateDerivations(derivationsSet);
		Collections.sort(fullSet);
		Collections.reverse(fullSet);
		if (k <= fullSet.size()) {
			return fullSet.subList(0, k);
		} else {
			return fullSet;
		}		
	}
	
	/**
	 * TODO(swabha): Fix error in finding out all permutations
	 * @param dSet
	 * @return
	 */
	private List<Derivation> getAllCandidateDerivations(List<List<Derivation>> dSet) {
		Map<List<Integer>, Derivation> map = new HashMap<List<Integer>, Derivation>();
		int outer = 0;
		for (List<Derivation> childDerivationSet : dSet) {
			for (Derivation kthDerivationInChild : childDerivationSet) {
				double prod = kthDerivationInChild.getScore();
				List<Integer> tracker = new ArrayList<Integer>();
				tracker.add(outer);
				int inner = -1;
				for (List<Derivation> otherChildDerivationSet : dSet) {
					++inner;
					if (otherChildDerivationSet.equals(childDerivationSet)) {
						continue;
					} 
					for (Derivation kthDerivationInOtherChild : otherChildDerivationSet) {
						prod *= kthDerivationInOtherChild.getScore();
						tracker.add(inner);
					}
				}
				Collections.sort(tracker);
				//System.out.println(tracker);
				map.put(tracker, new Derivation(null, prod));
			}
			++outer;
		}
		
		return new ArrayList<Derivation>(map.values());
	}

	/**
	 * Given two lists of size k each, combines them to return a single list containing k largest
	 * elements
	 */
	@Override
	public List<Derivation> add(List<Derivation> derivations1,	List<Derivation> derivations2) {
		
		int it1 = 0;
		int it2 = 0;
		
		List<Derivation> kbest = new ArrayList<Derivation>();
		while (kbest.size() < k && it1 < derivations1.size() && it2 < derivations2.size()) {
			if (derivations1.get(it1).getScore() > derivations2.get(it2).getScore()) {
				kbest.add(derivations1.get(it1));
				++it1;
			} else if ((it2 < derivations2.size()) && 
					(derivations2.get(it2).getScore() > derivations1.get(it1).getScore())) {
				kbest.add(derivations2.get(it2));
				++it2; 
			} else {
				kbest.add(derivations1.get(it1));
				++it1; 
				if (kbest.size() < k) {
					kbest.add(derivations2.get(it2));
					++it2;
				}			
			}
		}
		if (kbest.size() < k) {
			if (it1 == derivations1.size()) {
				kbest.addAll(derivations2.subList(it2, derivations2.size()));
			} else {
				kbest.addAll(derivations1.subList(it1, derivations1.size()));
			}
		}
		if (kbest.size() > k) {
			kbest.removeAll(kbest.subList(k, kbest.size()));
		}
		return kbest;
	}

}
