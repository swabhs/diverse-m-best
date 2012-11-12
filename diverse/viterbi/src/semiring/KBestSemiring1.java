package semiring;

import java.util.ArrayList;
import java.util.List;

import utility.MaxPriorityQ;

public class KBestSemiring1 implements Semiring<List<Derivation>> {

private int k;
	
	public KBestSemiring1(int k) {
		this.k = k;
	}
	
	@Override
	public List<Derivation> add(List<Derivation> derivations1,
			List<Derivation> derivations2) {
		int it1 = 0;
		int it2 = 0;
		
		List<Derivation> kbest = new ArrayList<Derivation>();
		while (kbest.size() < k && it1 < derivations1.size() && it2 < derivations2.size()) {
			if (derivations1.get(it1).getScore() > derivations2.get(it2).getScore()) {
				kbest.add(derivations1.get(it1));
				++it1;
			} else if ((it2 < derivations2.size()) && (derivations2.get(it2).getScore() > derivations1.get(it1).getScore())) {
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

	@Override
	public List<Derivation> multiply(List<List<Derivation>> derivationsSet) {
		List<Derivation> kbest = new ArrayList<Derivation>();
		MaxPriorityQ q = new MaxPriorityQ();
		
		for (List<Derivation> derivations : derivationsSet) {
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
		}
		return kbest;
	}

}
