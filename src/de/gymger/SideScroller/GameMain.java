package de.gymger.SideScroller;

import de.gymger.SideScroller.Graphics.OpenGLTest;

public class GameMain {
	
	public static void main(String [] args){
		OpenGLTest.getInstance().init();
		Controller.getInstance().init();
		
		Controller.getInstance().gameLoop();
	}
	
}
