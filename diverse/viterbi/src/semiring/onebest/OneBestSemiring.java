package semiring.onebest;

import java.util.ArrayList;
import java.util.List;

import semiring.Semiring;


class OneBestSemiring implements Semiring<Double> {
	
	List<Double> elements;
	
	OneBestSemiring(int size) {
		this.elements = new ArrayList<Double>(size);
	}
	
	OneBestSemiring(List<Double> elements) {
		this.elements = elements;
	}
	
	List<Double> getElements() {
		return elements;
	}
	
	void setElements(List<Double> elements) {
		this.elements = elements;
	}

	int argmax(List<Double> elements) {
		int argmax = 0;
		for (int i = 1; i < elements.size(); ++i) {
			if (elements.get(i) > elements.get(argmax)) {
				argmax = i;
			}
		}
		return argmax;
	}
	
	@Override
	public Double add(List<Double> elements) {
		Double max = elements.get(0);
		for (double x : elements) {
			if (max > x) {
				max = x;
			}
		}
		return max;
	}

	@Override
	public Double multiply(List<Double> elements) {
		Double product = 1.0;
		for (double x : elements) {
			product *= x;
		}
		return product;
	}

}
