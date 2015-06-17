package de.gymger.sidescroller.gameplay;

import org.lwjgl.util.vector.Vector2f;

import de.gymger.sidescroller.graphics.GraphicsManager;
import de.gymger.sidescroller.graphics.IDrawable;
import de.gymger.sidescroller.physics.PhysicsObject;

public abstract class Actor {

	protected IDrawable graphicsObject;
	protected PhysicsObject physicsObject;
	
	public Vector2f getPosition(){
		return physicsObject.getPosition();
	}
	
	public Vector2f getPositionOnScreen() {
		Vector2f ret = new Vector2f();
		
		Vector2f.sub(getPosition(), GraphicsManager.getViewPosition(), ret);
		
		return ret;
	}
	
}
