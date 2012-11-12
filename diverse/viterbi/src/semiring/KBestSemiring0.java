package semiring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KBestSemiring0 implements Semiring<List<Derivation>> {
	
	private int k;
	
	KBestSemiring0(int k) {
		this.k = k;
	}

	/**
	 * Given a list of lists, each of size k, merge them and return a single list of size k,
	 * containing the k largest elements */
	@Override
	public List<Derivation> multiply(List<List<Derivation>> derivationsSet) {		
		List<Derivation> fullSet = new ArrayList<Derivation>();
		for (List<Derivation> derivations : derivationsSet) {
			fullSet.addAll(derivations);
		}
		Collections.sort(fullSet);
		Collections.reverse(fullSet);
		return fullSet.subList(0, k);
	}

	/**
	 * Given two lists of size k each, combines them to return a single list of size k
	 */
	@Override
	public List<Derivation> add(List<Derivation> derivations1,	List<Derivation> derivations2) {
		
		int it1 = 0;
		int it2 = 0;
		
		List<Derivation> kbest = new ArrayList<Derivation>();
		while (kbest.size() < k) {
			if (derivations1.get(it1).getScore() > derivations2.get(it2).getScore()) {
				kbest.add(derivations1.get(it1));
				++it1;
			} else if (derivations2.get(it2).getScore() > derivations1.get(it1).getScore()) {
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
		return kbest;
	}
	
	public List<Derivation> add(List<List<Derivation>> elements) {
		if (elements.size() > 2) {
			System.out.println("Something wrong");	
		}
		return null;
	}

}
