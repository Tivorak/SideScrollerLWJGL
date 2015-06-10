package de.gymger.SideScroller.util;

import de.gymger.SideScroller.util.ListOperation.OperationType;

public class MapOperation {

	OperationType type;
	
	Object o1, o2;
	
	public MapOperation(Object obj1, Object obj2, OperationType t){
		o1 = obj1;
		o2 = obj2;
		type = t;
	}
	
	public Object getObject1(){
		return o1;
	}
	
	public Object getObject2(){
		return o2;
	}

	public OperationType getType() {
		return type;
	}
}
