package ch.fhnw.algd2.collections.list.linkedlist;

import ch.fhnw.algd2.collections.list.MyAbstractList;


/**
 * Tests adding numbers to the list. Doesn't matter if head or tail add.
 * 
 * @author Michael
 */
public class SLL_A_Test_Add extends Abstract_A_ListTest_Add {

	@Override
	protected MyAbstractList<Integer> getListInstance() {
		return new SinglyLinkedList<Integer>();
	}

}
