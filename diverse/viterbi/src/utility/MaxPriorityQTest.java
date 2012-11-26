package utility;

import static org.junit.Assert.*;

import org.junit.Test;

import semiring.Derivation;

public class MaxPriorityQTest {
	
	private MaxPriorityQ q;
	
	public MaxPriorityQTest() {
		q = new MaxPriorityQ();
		q.insert(new Derivation(null, 3.0));
		q.insert(new Derivation(null, 5.9));
		q.insert(new Derivation(null, 4.1));
		q.insert(new Derivation(null, 0.4));
		q.insert(new Derivation(null, 7.8));
	}

	@Test
	public void testExtractMax() {
		assertEquals(q.extractMax().getScore(), new Double(7.8));
		assertEquals(q.extractMax().getScore(), new Double(5.9));
		assertEquals(q.extractMax().getScore(), new Double(4.1));
		assertEquals(q.extractMax().getScore(), new Double(3.0));
		assertEquals(q.extractMax().getScore(), new Double(0.4));
		assertNull(q.extractMax());
	}
	
	@Test
	public void testcontains() {
		q = new MaxPriorityQ();
		q.insert(new Derivation(null, 3.0));
		assertTrue(q.contains(new Derivation(null, 3.0)));
	}

}
