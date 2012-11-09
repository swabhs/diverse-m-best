package semiring;

import java.util.List;

interface Semiring<T> {

	T add(List<T> elements);
	
	T multiply(List<T> elements);
}
