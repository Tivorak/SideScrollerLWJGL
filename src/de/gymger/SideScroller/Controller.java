package de.gymger.SideScroller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

import de.gymger.SideScroller.Graphics.OpenGLTest;
import de.gymger.SideScroller.util.ListOperation;
import de.gymger.SideScroller.util.ListOperation.OperationType;

public class Controller {

	public static boolean running = false;
	
	private static List<Tickable> tickables = new LinkedList<>();
	private static List<ListOperation<Tickable>> operationBuffer = new LinkedList<>();
	
	private static float currentTick;
	
	private static float timeDilation = 1;
	
	private static long baseTickLength = 33554432, tickLength = baseTickLength;
	
	private static Controller instance = new Controller();
	
	private Controller(){
		
	}
	
	public static Controller getInstance(){
		return instance;
	}
	
	public void init(){
		GLFW.glfwSetKeyCallback(OpenGLTest.getInstance().getWindowHandle(), new GLFWKeyCallback(){

			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
					GLFW.glfwSetWindowShouldClose(OpenGLTest.getInstance().getWindowHandle(), GL11.GL_TRUE);
			}
			
		});
		
		
		GLFW.glfwSetWindowSizeCallback(OpenGLTest.getInstance().getWindowHandle(),  new GLFWWindowSizeCallback(){

			@Override
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
			}
			
		});
	}
	
	public void gameLoop(){
		if(running)
			return;
		
		running = true;
		
		long startOfTick;
		
		long d;
		ListOperation<Tickable> o = null;
		
		while(running){
			
			startOfTick = System.nanoTime();
			
			if(operationBuffer.size() > 0){
				Iterator<ListOperation<Tickable>> i = operationBuffer.iterator();
				while(i.hasNext()){
					o = i.next();
					
					if(o.getType() == OperationType.ADD)
						tickables.add(o.getObject());
					else
						tickables.remove(o.getObject());
					
					i.remove();
				}
				
				
				
			}
			
			for(Tickable t : tickables)
				t.tick(Math.round(currentTick));
			
			currentTick += timeDilation;
			
			
			d = (startOfTick + tickLength) - System.nanoTime();
			
			try {
				//System.out.println("Sleeping " + d  + " ns in " + (int)(d / 1000000) + " ms and " + (int)(d % 1000000) + " ns.");
				Thread.sleep((int)(d / 1000000), (int)(d % 1000000));
			} catch (InterruptedException | IllegalArgumentException e) {}
		}
	}
	
	public void addTickable(Tickable t){
		operationBuffer.add(new ListOperation<>(t, OperationType.ADD));
	}
	
	public void removeTickable(Tickable t){
		operationBuffer.add(new ListOperation<>(t, OperationType.REMOVE));
	}
	
	public void setTimeDilation(float f){
		timeDilation = f;
		
		tickLength = Math.round(baseTickLength * timeDilation);
	}

	public void setRunning(boolean b) {
		running = false;
	}
}
