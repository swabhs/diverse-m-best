package semiring;

import java.util.ArrayList;
import java.util.List;

public class KBestSemiring implements Semiring<List<Double>>{

	/**
	 * Given two lists of size k each, combines them to return a single list of size k
	 */
	@Override
	public List<Double> add(List<List<Double>> elements) {
		if (elements.size() > 2) {
			System.out.println("Something wrong");
			return null;
		}
		List<Double> list1 = elements.get(0);
		List<Double> list2 = elements.get(1);
		int k = list1.size();
		int it1 = 0;
		int it2 = 0;
		List<Double> kbest = new ArrayList<Double>();
		while (it1 < k && it2 < k) {
			if (list1.get(it1) > list2.get(it2)) {
				kbest.add(list1.get(it1));
				it1++;
			} else if (list2.get(it2) > list1.get(it1)) {
				
			}
		}
		return kbest;
	}

	/**
	 * Given a list of lists, each of size k, merge them and return a single list of size k,
	 * containing the k largest elements */
	@Override
	public List<Double> multiply(List<List<Double>> elements) {
		// TODO Auto-generated method stub
		return null;
	}

}
