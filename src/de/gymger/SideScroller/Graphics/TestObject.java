package de.gymger.SideScroller.Graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.gymger.SideScroller.util.AssetManager;

public class TestObject {

	int VAO, VBO, EBO, uOffset, texIDWall, texIDSmiley;
	
	int basicShaderProgram = OpenGLHelper.createShaderProgram("vertex.glsl", "fragment.glsl");
	
	FloatBuffer vertices;
	ByteBuffer indices;
	
	ByteBuffer texture = null;
	
	float texCoords[] = {
		0f, 0f, 
		1f, 0f, 
		0.5f, 1f
	};
	
	double scalarX, scalarY;
	
	private static final boolean WIREFRAME = false;
	
	public TestObject(double x, double y){
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();
		EBO = GL15.glGenBuffers();
		
		vertices = BufferUtils.createByteBuffer(4 * 8 * Float.BYTES).asFloatBuffer();

		texIDWall = AssetManager.loadTexture("wall");
		texIDSmiley = AssetManager.loadTexture("awesomeface");
		
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
		scalarX = x;
		scalarY = y;
	}
	
	
	public void draw(){
		if(WIREFRAME)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		Matrix4f m4f = new Matrix4f();
		
		m4f.rotate((float) Math.toRadians(90), new Vector3f(0, 0, 1));
		
		m4f.translate(new Vector2f((float)Math.sin(GLFW.glfwGetTime() * scalarX) * 0.5f, (float)Math.cos(GLFW.glfwGetTime() * scalarY) * 0.5f));
		
		
		FloatBuffer buf = BufferUtils.createFloatBuffer(4 * 4);
		
		m4f.store(buf);
		
		buf.flip();
		
		GL20.glUseProgram(basicShaderProgram);
		
		GL20.glUniform3f(uOffset, (float)Math.sin(GLFW.glfwGetTime() * scalarX) * 0.5f, (float)Math.cos(GLFW.glfwGetTime() * scalarY) * 0.5f, 0);
		
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(basicShaderProgram, "transform"), false, buf);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIDWall);
		GL20.glUniform1i(GL20.glGetUniformLocation(basicShaderProgram, "myTexture1"), 0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIDSmiley);
		GL20.glUniform1i(GL20.glGetUniformLocation(basicShaderProgram, "myTexture2"), 1);
		
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
