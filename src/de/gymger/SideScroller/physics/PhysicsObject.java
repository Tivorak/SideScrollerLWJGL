package de.gymger.sidescroller.physics;

import org.lwjgl.util.vector.Vector2f;

import de.gymger.sidescroller.ITickable;

public abstract class PhysicsObject implements ITickable{

	protected Vector2f position, lastPosition, velocity;
	
	protected boolean dynamic, affectedByGravity, detectsCollision, affectedByCollision;
	
	public float getPosX(){
		return position.x;
	}
	
	public float getPosY(){
		return position.y;
	}
	
	public float getVelX(){
		return velocity.x;
	}
	
	public float getVelY(){
		return velocity.y;
	}
	
	public void setPosX(float x){
		lastPosition.set(position);
		position.setX(x);
	}
	
	public void setPosY(float y){
		lastPosition.set(position);
		position.setY(y);
	}
	
	@Override
	public void tick(int i){
		if(dynamic){
			if(affectedByGravity)
				velocity.y += PhysicsManager.GRAVITY;
			
			Vector2f.add(position, velocity, position);
		}
	}

	public Vector2f getPosition() {
		return position;
	}
	
	
}
