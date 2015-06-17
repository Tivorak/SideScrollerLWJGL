package de.gymger.sidescroller.util;

public class ListOperation<E> {

	public enum OperationType{
		ADD, 
		REMOVE;
	}
	
	E object;
	private OperationType type;
	
	public ListOperation(E o, OperationType t){
		object = o;
		type = t;
	}

	public OperationType getType() {
		return type;
	}

	public E getObject() {
		return object;
	}
}
