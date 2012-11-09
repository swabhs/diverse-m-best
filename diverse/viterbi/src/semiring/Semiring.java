package semiring;

import java.util.List;

public interface Semiring<T> {

	T add(List<T> elements);
	
	T multiply(List<T> elements);
}
