package de.gymger.sidescroller.graphics;

public interface IDrawable {

	public default void preLoad(){}
	
	public default void init(){
		GraphicsManager.addDrawable(this);
	}
	
	public void draw();
	public void destroy();
	
}
