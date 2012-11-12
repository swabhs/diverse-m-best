package semiring;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * TODO(swabha): Add tests for smaller lists, when sum of lists might not add to k and all that
 * @author swabha
 *
 */
public class KBestSemiringTest {

	private List<Derivation> d1;
	private List<Derivation> d2;
	private List<Derivation> d3;
	
	private KBestSemiring1 kbest;
	
	public KBestSemiringTest() {
		super();
		d1 = new ArrayList<Derivation>();
		d1.add(new Derivation(null, 0.68));
		d1.add(new Derivation(null, 0.6));
		d1.add(new Derivation(null, 0.1));
		
		d2 = new ArrayList<Derivation>();
		d2.add(new Derivation(null, 0.91));
		d2.add(new Derivation(null, 0.48));
		d2.add(new Derivation(null, 0.02));
		
		d3 = new ArrayList<Derivation>();
		d3.add(new Derivation(null, 0.74));
		d3.add(new Derivation(null, 0.732));
		d3.add(new Derivation(null, 0.0));
		
		kbest = new KBestSemiring1(3);
	}
	
	@Test
	public void testAdd() {
		List<Derivation> expected = Arrays.asList(new Derivation(null, 0.91),
				new Derivation(null, 0.68), new Derivation(null, 0.6));
		System.out.println("adder");
		List<Derivation> actual = kbest.add(d1, d2);
		
		for (Derivation d : actual) {
			System.out.println(d.getScore());
		}
	}
	
	@Test
	public void testMultiply() {
		System.out.println("multiplier");
		List<Derivation> actual = (kbest.multiply(Arrays.asList(d1, d2, d3)));
		for (Derivation d : actual) {
			System.out.println(d.getScore());
		}
	}

}
