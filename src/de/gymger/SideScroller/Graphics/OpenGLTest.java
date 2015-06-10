package de.gymger.SideScroller.Graphics;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import de.gymger.SideScroller.Controller;
import de.gymger.SideScroller.Tickable;

public class OpenGLTest implements Tickable{
	private Logger logger;
	
	private static OpenGLTest instance = new OpenGLTest();
	
	private static int WIDTH = 500, HEIGHT = 500;
	
	private long windowHandle;
	
	private TestObject testObject;
	
	private OpenGLTest(){
		
		logger = Logger.getLogger(OpenGLTest.class.getCanonicalName());
		
	}
	
	public static OpenGLTest getInstance(){
		return instance;
	}
	
	public void init(){
		if(glfwInit() != GL11.GL_TRUE){
			logger.severe("Could not initialize GLFW.");
			System.exit(1);
		} else {
			logger.info("Initialized GLFW Version " + glfwGetVersionString());
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GL11.GL_TRUE);
		
		windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "OpenGLTest", 0, 0);
		glfwMakeContextCurrent(windowHandle);
		GLContext.createFromCurrent();
		
		glfwSwapInterval(1);
		GL11.glClearColor(0, 0, 0, 1);
		
		testObject = new TestObject();
		
		Controller.getInstance().addTickable(this);
	}

	@Override
	public void tick(int i) {
		if(glfwWindowShouldClose(windowHandle) == GL11.GL_TRUE){
			glfwDestroyWindow(windowHandle);
			Controller.getInstance().setRunning(false);
		} else {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			testObject.draw();
			
			glfwSwapBuffers(windowHandle);
			glfwPollEvents();
		}
	}
	
	public long getWindowHandle(){
		return windowHandle;
	}
	
}
