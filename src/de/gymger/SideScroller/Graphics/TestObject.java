package de.gymger.SideScroller.Graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.gymger.SideScroller.util.AssetManager;

public class TestObject {

	int VAO, VBO, EBO, uOffset, texID;
	
	int basicShaderProgram = OpenGLHelper.createShaderProgram("vertex.glsl", "fragment.glsl");
	
	FloatBuffer vertices;
	ByteBuffer indices;
	
	ByteBuffer texture = null;
	
	float texCoords[] = {
		0f, 0f, 
		1f, 0f, 
		0.5f, 1f
	};
	
	private static final boolean WIREFRAME = false;
	
	public TestObject(){
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();
		EBO = GL15.glGenBuffers();
		
		vertices = BufferUtils.createByteBuffer(4 * 8 * Float.BYTES).asFloatBuffer();

		texID = AssetManager.loadTexture("wall");
		
		vertices.put(new float[]{
				//POSITION		   //COLOR       //TEXTURE COORDS
				-0.5f, 0.5f, 0f,   1f, 0f, 0f,   0f, 1f,  //0:TOP LEFT
				 0.5f, 0.5f, 0f,   0f, 1f, 0f,   1f, 1f,  //1:TOP RIGHT
				-0.5f,-0.5f, 0f,   0f, 0f, 1f,   0f, 0f,  //2:BOT LEFT
				 0.5f,-0.5f, 0f,   0f, 0f, 0f,   1f, 0f   //3:BOT RIGHT

		}).flip();
		
		indices = BufferUtils.createByteBuffer(2 * 3);
		
		indices.put(new byte[]{
				0, 1, 2,
				1, 2, 3
		}).flip();
		
		GL30.glBindVertexArray(VAO);
		{
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 0);
			GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
			GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		}
		GL30.glBindVertexArray(0);
		
		uOffset = GL20.glGetUniformLocation(basicShaderProgram, "offset");
	}
	
	
	public void draw(){
		if(WIREFRAME)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		
		GL20.glUseProgram(basicShaderProgram);
		
		GL20.glUniform3f(uOffset, (float)Math.sin(GLFW.glfwGetTime()) * 0.5f, (float)Math.cos(GLFW.glfwGetTime()) * 0.5f, 0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		
		GL30.glBindVertexArray(VAO);
		{
			GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		}
		GL30.glBindVertexArray(0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		if(WIREFRAME)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public void cleanup(){
		GL15.glDeleteBuffers(EBO);
		GL15.glDeleteBuffers(VBO);
		GL30.glDeleteVertexArrays(VAO);
		
		GL20.glDeleteProgram(basicShaderProgram);
	}
}
