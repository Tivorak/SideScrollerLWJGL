package de.gymger.sidescroller;

import de.gymger.sidescroller.graphics.Background;
import de.gymger.sidescroller.graphics.GraphicsManager;
import de.gymger.sidescroller.graphics.IDrawable;
import de.gymger.sidescroller.graphics.SampleGraphicsObject;
import de.gymger.sidescroller.graphics.TestObject;

public class GameMain {
	
	public static void main(String [] args) throws InterruptedException{
		GraphicsManager.init();
		Controller.init();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				Background b = new Background();
				
				b.init();
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				IDrawable[] tests = new IDrawable[]{
						new TestObject(1, 1),
						new TestObject(0.5, 1),
						new TestObject(5, 2),
						new TestObject(3, 0.5),
						new SampleGraphicsObject(null)
				};
				
				for(IDrawable t : tests){
					t.init();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}).start();
		

		Controller.gameLoop();
	}
	
}
