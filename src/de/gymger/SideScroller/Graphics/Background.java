package de.gymger.sidescroller.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import de.gymger.sidescroller.util.AssetManager;

public class Background implements IDrawable{

	private int backTexID, VBO, EBO, shaderProgram;
	
	public Background(){
		
		GraphicsManager.preloadObject(this);
		
	}
	
	@Override
	public void preLoad(){
		VBO = GL15.glGenBuffers();
		EBO = GL15.glGenBuffers();
		shaderProgram  = OpenGLHelper.createShaderProgram("background.vertex", "background.fragment");

		FloatBuffer vert = BufferUtils.createFloatBuffer(4 * 4);

		vert.put(new float[]{
				-1f, -1f, 0f, 0f, //BOT LEFT
				1f, -1f, 1f, 0f, //BOT RIGHT
				-1f,  1f, 0f, 1f, //TOP LEFT
				1f,  1f, 1f, 1f  //TOP RIGHT
		});

		vert.flip();

		ByteBuffer ind  = BufferUtils.createByteBuffer(2 * 3);

		ind.put(new byte[]{
				0, 1, 2, 
				1, 2, 3
		});

		ind.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vert, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ind, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		backTexID = AssetManager.loadTexture("background");
	}
	
	@Override
	public void init(){
		GraphicsManager.addStaticDrawable(this);
	}
	
	@Override
	public void draw() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, backTexID);
		
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL20.glUseProgram(shaderProgram);
		GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "myTexture"), 0);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		
		GL20.glUseProgram(0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(EBO);
		GL15.glDeleteBuffers(VBO);
		GL20.glDeleteProgram(shaderProgram);
		
		GraphicsManager.removeStaticDrawable(this);
	}

}
