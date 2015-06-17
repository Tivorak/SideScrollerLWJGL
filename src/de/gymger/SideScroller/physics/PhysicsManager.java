package de.gymger.sidescroller.physics;

import java.util.LinkedList;
import java.util.List;

import de.gymger.sidescroller.Controller;
import de.gymger.sidescroller.ITickable;
import de.gymger.sidescroller.util.ListOperation;
import de.gymger.sidescroller.util.ListOperation.OperationType;

public class PhysicsManager implements ITickable{

	private PhysicsManager(){}

	private static List<PhysicsObject> physicsObjects = new LinkedList<>();
	private static List<ListOperation<PhysicsObject>> listOps = new LinkedList<>();
	
	private static boolean listChanged = false;
	
	static final int GRAVITY = 4;
	
	public static void init(){
		Controller.addTickable(new PhysicsManager());
	}
	
	@Override
	public void tick(int i) {
		if(listChanged)
			updateLists();
		
		for(PhysicsObject o : physicsObjects)
			o.tick(i);
	};
	
	public static void addPhysicsObject(PhysicsObject o){
		if(physicsObjects.contains(o))
			return;
		
		listOps.add(new ListOperation<PhysicsObject>(o, OperationType.ADD));
		listChanged = true;
	}
	
	public static void removePhysicsObject(PhysicsObject o){
		if(!physicsObjects.contains(o))
			return;
		
		listOps.add(new ListOperation<PhysicsObject>(o, OperationType.REMOVE));
		listChanged = true;
	}
	
	private static void updateLists(){
		for(ListOperation<PhysicsObject> l : listOps)
			if(l.getType() == OperationType.ADD)
				physicsObjects.add(l.getObject());
			else
				physicsObjects.remove(l.getObject());
		
		listOps.clear();
		listChanged = false;
	}
}
