package de.gymger.sidescroller.graphics;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import de.gymger.sidescroller.util.AssetManager;

public class OpenGLHelper {

	private static final Logger logger = Logger.getLogger(OpenGLHelper.class.getName());
	
	public static int createShaderProgram(String vertexShaderLoc, String fragmentShaderLoc){
		
		if(glfwInit() != GL_TRUE){
			logger.severe("GLFW could not be initialized!");
			return -1;
		}
		
		if(glfwGetCurrentContext() == 0){
			logger.severe("No OpenGL Context current!");
			return -1;
		}
		
		int vertexShaderID, fragmentShaderID;
		
		vertexShaderLoc = vertexShaderLoc.replace('.', File.separatorChar) + ".glsl";
		fragmentShaderLoc = fragmentShaderLoc.replace('.', File.separatorChar) + ".glsl";
		
		try {
			StringBuilder sb = new StringBuilder();
			String shaderBasePath = AssetManager.getBasePath() + File.separatorChar + ".." + File.separatorChar + "shader" + File.separatorChar;
			for(String s : Files.readAllLines(new File(shaderBasePath + vertexShaderLoc).toPath()))
				sb.append(s).append('\n');
			
			vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
			GL20.glShaderSource(vertexShaderID, sb.toString());
			GL20.glCompileShader(vertexShaderID);

	        if (GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
	        	logger.severe("Could not compile vertex shader: " + GL20.glGetShaderInfoLog(vertexShaderID));
	            return -1;
	        }
			
	        
	        sb = new StringBuilder();
			for(String s : Files.readAllLines(new File(shaderBasePath + fragmentShaderLoc).toPath()))
				sb.append(s).append('\n');
			
			fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
			GL20.glShaderSource(fragmentShaderID, sb.toString());
			GL20.glCompileShader(fragmentShaderID);

	        if (GL20.glGetShaderi(fragmentShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
	            logger.severe("Could not compile fragment shader: " + GL20.glGetShaderInfoLog(fragmentShaderID));
	            return -1;
	        }
			
	        int shaderProgram = GL20.glCreateProgram();
	        
	        GL20.glAttachShader(shaderProgram, vertexShaderID);
	        GL20.glAttachShader(shaderProgram, fragmentShaderID);

	        GL20.glLinkProgram(shaderProgram);
	        if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
	            logger.severe("Shader linking failed: " + GL20.glGetProgramInfoLog(shaderProgram));
	            return -1;
	        }
	   
	        GL20.glValidateProgram(shaderProgram);
	        GL20.glDeleteShader(vertexShaderID);
	        GL20.glDeleteShader(fragmentShaderID);
	        
	        return shaderProgram;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
