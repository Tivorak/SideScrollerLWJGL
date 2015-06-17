package de.gymger.sidescroller.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector2f;

import de.gymger.sidescroller.Controller;
import de.gymger.sidescroller.ITickable;
import de.gymger.sidescroller.util.ListOperation;
import de.gymger.sidescroller.util.ListOperation.OperationType;

public class GraphicsManager implements ITickable{

	private GraphicsManager(){}
	
	private static List<IDrawable> drawObjects = new LinkedList<>(), staticDrawObjects = new LinkedList<>(), preloadObjects = new LinkedList<>();
	
	private static List<ListOperation<IDrawable>> dObjectOps = new LinkedList<>(), dStaticObjectOps = new LinkedList<>();
	
	private static int basicShaderProgram;

	static long windowHandle;
	
	private static final int WIDTH = 700, HEIGHT = 700;
	
	private static Vector2f viewPosition = new Vector2f(0, 0);
	
	private static Logger logger = Logger.getLogger(GraphicsManager.class.getName());
	
	private static boolean updateLists = false;
	
	public static void preloadObject(IDrawable d){
		
		preloadObjects.add(d);
		updateLists = true;
	}
	
	public static void addDrawable(IDrawable d){
		dObjectOps.add(new ListOperation<>(d, OperationType.ADD));
		logger.info("Added new Drawable to drawing Queue: " + d);
		updateLists = true;
	}
	
	public static void addStaticDrawable(IDrawable d){
		dStaticObjectOps.add(new ListOperation<>(d, OperationType.ADD));
		logger.info("Added new StaticDrawable to drawing Queue: " + d);
		updateLists = true;
	}
	
	public static void removeDrawable(IDrawable d){
		dObjectOps.add(new ListOperation<>(d, OperationType.REMOVE));
		logger.info("Removed Drawable from Queue: " + d);
		updateLists = true;
	}
	
	public static void removeStaticDrawable(IDrawable d){
		dObjectOps.add(new ListOperation<>(d, OperationType.REMOVE));
		logger.info("Removed StaticDrawable from Queue: " + d);
		updateLists = true;
	}
	
	public static Vector2f getViewPosition(){
		return viewPosition;
	}
	
	public static void init(){
		if(glfwInit() != GL11.GL_TRUE){
			logger.severe("Could not initialize GLFW.");
			System.exit(1);
		} else {
			logger.info("Initialized GLFW Version " + glfwGetVersionString());
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		//glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_SAMPLES, 16);
		glfwWindowHint(GLFW_VISIBLE, GL11.GL_TRUE);


		windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "OpenGLTest", 0, 0);
		glfwMakeContextCurrent(windowHandle);
		GLContext.createFromCurrent();

		

		glfwSwapInterval(1);
		GL11.glClearColor(0, 0, 0, 1);
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		Controller.addTickable(new GraphicsManager());
	}
	
	public static int getBasicShaderProgram(){
		return basicShaderProgram != 0 ? basicShaderProgram : (basicShaderProgram = OpenGLHelper.createShaderProgram("basic.vertex", "basic.fragment"));
	}
	
	public static void draw(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		for(IDrawable d : staticDrawObjects)
			d.draw();
		
		for(IDrawable d : drawObjects)
			d.draw();
		
		GLFW.glfwSwapBuffers(windowHandle);
	}
	
	private static void updateLists(){
		for(ListOperation<IDrawable> l : dObjectOps)
			if(l.getType() == OperationType.ADD)
				drawObjects.add(l.getObject());
			else
				drawObjects.remove(l.getObject());
		
		dObjectOps.clear();
		
		for(ListOperation<IDrawable> l : dStaticObjectOps)
			if(l.getType() == OperationType.ADD)
				staticDrawObjects.add(l.getObject());
			else
				staticDrawObjects.remove(l.getObject());
		
		dStaticObjectOps.clear();
		
		Iterator<IDrawable> it = preloadObjects.iterator();

		IDrawable d;
		try{
			while(it.hasNext()){
				d = it.next();
				
				d.preLoad();
				it.remove();
			}
		} catch(ConcurrentModificationException e){
			logger.severe("I failed you, master");
		}

		updateLists = false;
	}
	
	public static long getWindowHandle() {
		return windowHandle;
	}

	@Override
	public void tick(int i) {
		if(updateLists)
			updateLists();
		
		draw();
	}
}
