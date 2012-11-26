package utility;

import java.util.ArrayList;
import java.util.List;

import semiring.Derivation;

/**
 * Max priority queue for Derivation valued data points
 * @author swabha
 *
 */
public class MaxPriorityQ {

	private List<Derivation> elements;
	
	public MaxPriorityQ() {
		elements = new ArrayList<Derivation>();
	}
	
	public int size() {
		return elements.size();
	}
	
	public void insert(Derivation newElement) {
		// because the score of a derivation needs to be between 0 and 1.
		elements.add(new Derivation(null, -0.9)); 
		heapIncKey(elements, elements.size() - 1, newElement);
	}
	
	public boolean contains(Derivation testD) {
		for (Derivation d : elements) {
			if (testD.getScore().equals(d.getScore())) {
					return true;
			}
		}
		return false;
	}
	
	public Derivation extractMax() {
		if (elements.size() == 0) {
			System.err.println("Heap underflow!");
			return null;
		}
		Derivation max = (elements.get(0));
		elements.set(0, elements.get(elements.size() - 1));
		elements.remove(elements.size() - 1);
		maxHeapify(elements, 0);
		return max;
	}
	
	private void heapIncKey(List<Derivation> elements, int i, Derivation value) {
		if (value.getScore() < elements.get(i).getScore()) {
			System.err.println(value.getScore() + " New value is less than current");
			return;
		} 
		elements.set(i, value);
		while (i > 0 && elements.get(i/2).getScore() < value.getScore()) {
			Derivation temp = elements.get(i/2);
			elements.set(i/2, elements.get(i));
			elements.set(i, temp);
			i = i/2;
		}
	}
	
	private void maxHeapify(List<Derivation> elements, int pos){
		int left = pos*2 + 1;
		int right = pos*2 + 2;
		int largest;
		if (left < elements.size() && 
				elements.get(left).getScore() > elements.get(pos).getScore()) {
			largest = left;
		} else {
			largest = pos;
		}
		if (right < elements.size() && 
				elements.get(right).getScore() > elements.get(largest).getScore()) {
			largest = right;
		}
		if (largest != pos) {
			Derivation temp = elements.get(largest);
			elements.set(largest, elements.get(pos));
			elements.set(pos, temp);
			maxHeapify(elements, largest);
		}
	}
	
}