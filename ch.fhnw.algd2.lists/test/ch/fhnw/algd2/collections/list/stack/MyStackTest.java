package ch.fhnw.algd2.collections.list.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class MyStackTest {
	private IStack<Integer> stack;
	@Rule
	public Timeout globalTimeout = new Timeout(1000);

	@Before
	public void initialize() {
		stack = new MyStack<Integer>();
	}

	@Test
	public void empty_EmptyStack_True() {
		assertTrue(stack.empty());
	}

	@Test
	public void testPushAndPop_OnEmptyStack_AllElementsPushedAndPopped() {
		int[] numbers = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		for (int i = 0; i < numbers.length; i++) {
			stack.push(numbers[i]);
		}
		checkPopOrder(numbers);
		assertTrue(stack.empty());
	}

	@Test
	public void testPushAndPop_OnEmptyStack_OnlyTopLevelElementIsRemoved() {
		int[] numbers = new int[] { 0, 1, 0, 1, 0 };
		for (int i = 0; i < numbers.length; i++) {
			stack.push(numbers[i]);
		}
		checkPopOrder(numbers);
		assertTrue(stack.empty());
	}

	private void checkPopOrder(int[] numbers) {
		for (int i = numbers.length - 1; i >= 0; i--) {
			assertFalse(
					"Stack should not be empty as long as not all elements have been poped",
					stack.empty());
			assertEquals(numbers[i], stack.pop().intValue());
		}
		assertTrue("Stack should be empty again after removing all elements",
				stack.empty());
	}

	@Test
	public void peek_OneElementAdded_CanPeekMultipleTimesWithoutPop() {
		Integer i = new Integer(2);
		assertSame(i, stack.push(i));
		assertFalse(stack.empty());
		assertSame(i, stack.peek());
		assertSame(i, stack.peek());
		assertFalse(stack.empty());
	}

	@Test(expected = EmptyStackException.class)
	public void peek_EmptyStack_EmptyStackException() {
		stack.peek();
	}

	@Test(expected = EmptyStackException.class)
	public void pop_EmptyStack_EmptyStackException() {
		stack.pop();
	}
}
