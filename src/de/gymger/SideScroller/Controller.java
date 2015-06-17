package de.gymger.sidescroller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

import de.gymger.sidescroller.graphics.GraphicsManager;
import de.gymger.sidescroller.util.ListOperation;
import de.gymger.sidescroller.util.ListOperation.OperationType;

public class Controller {

	public static boolean running = false;
	
	private static List<ITickable> tickables = new LinkedList<>();
	private static List<ListOperation<ITickable>> operationBuffer = new LinkedList<>();
	
	private static float currentTick;
	
	private static float timeDilation = 1;
	
	private static long baseTickLength = 33554432, tickLength = baseTickLength;
	
	static GLFWErrorCallback errorCallback;
	static GLFWKeyCallback keyCallback;
	static GLFWWindowSizeCallback windowSizeCallback;
	
	private Controller(){
		
	}
	
	public static void init(){
		GLFW.glfwSetKeyCallback(GraphicsManager.getWindowHandle(), keyCallback = new GLFWKeyCallback(){

			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
					GLFW.glfwSetWindowShouldClose(GraphicsManager.getWindowHandle(), GL11.GL_TRUE);
				
			}
			
		});
		
		
		GLFW.glfwSetWindowSizeCallback(GraphicsManager.getWindowHandle(), windowSizeCallback = new GLFWWindowSizeCallback(){

			@Override
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
			}
			
		});
		
		GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
	}
	
	public static void gameLoop(){
		if(running)
			return;
		
		running = true;
		
		long startOfTick;
		
		long d;
		ListOperation<ITickable> o = null;
		
		while(running){
			GLFW.glfwPollEvents();
			if(GLFW.glfwWindowShouldClose(GraphicsManager.getWindowHandle()) == GL11.GL_TRUE){
				running = false;
				break;
			}
			startOfTick = System.nanoTime();
			
			if(operationBuffer.size() > 0){
				Iterator<ListOperation<ITickable>> i = operationBuffer.iterator();
				while(i.hasNext()){
					o = i.next();
					
					if(o.getType() == OperationType.ADD)
						tickables.add(o.getObject());
					else
						tickables.remove(o.getObject());
					
					i.remove();
				}
				
				
				
			}
			
			for(ITickable t : tickables)
				t.tick(Math.round(currentTick));
			
			currentTick += timeDilation;
			
			
			d = (startOfTick + tickLength) - System.nanoTime();
			
			try {
				//System.out.println("Sleeping " + d  + " ns in " + (int)(d / 1000000) + " ms and " + (int)(d % 1000000) + " ns.");
				Thread.sleep((int)(d / 1000000), (int)(d % 1000000));
			} catch (InterruptedException | IllegalArgumentException e) {}
		}
		
		System.exit(0);
	}
	
	public static void addTickable(ITickable t){
		operationBuffer.add(new ListOperation<>(t, OperationType.ADD));
	}
	
	public static void removeTickable(ITickable t){
		operationBuffer.add(new ListOperation<>(t, OperationType.REMOVE));
	}
	
	public static void setTimeDilation(float f){
		timeDilation = f;
		
		tickLength = Math.round(baseTickLength * timeDilation);
	}

	public static void setRunning(boolean b) {
		running = false;
	}
}
