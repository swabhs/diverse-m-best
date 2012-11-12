package utility;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {

	private List<T> elements;
	
	public Stack() {
		elements = new ArrayList<T>();
	}
	
	public void push(T newElement) {
		elements.add(newElement);
	}
	
	public T pop() {
		T element = elements.get(elements.size() - 1);
		elements.remove(elements.size() - 1);
		return element;
	}
	
	public int getSize() {
		return elements.size();
	}
}
