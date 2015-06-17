package de.gymger.sidescroller.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import de.gymger.sidescroller.gameplay.Actor;

public class SampleGraphicsObject implements IDrawable{

	Actor actor;
	
	private int VBO, EBO, shaderProgramID;
	
	public SampleGraphicsObject(Actor a) {
		actor = a;
		GraphicsManager.preloadObject(this);
	}
	
	@Override
	public void preLoad(){
		VBO = GL15.glGenBuffers();
		EBO = GL15.glGenBuffers();
		FloatBuffer vert = BufferUtils.createFloatBuffer(3 * 5);
		
		vert.put(new float[]{
				0, 0, 1,   1, 0,
				0, 1, 1,   0, 1,
				1, 1, 0,   1, 0
		}).flip();
		
		ByteBuffer ind = BufferUtils.createByteBuffer(3);
		
		ind.put(new byte[]{
				0, 1, 2
		}).flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vert, GL15.GL_STATIC_DRAW);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ind, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shaderProgramID = OpenGLHelper.createShaderProgram("debug.passColor.vertex", "debug.passColor.fragment");
	}
	
	@Override
	public void draw() {
		if(VBO == 0 && EBO == 0)
			preLoad();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		
		GL20.glUseProgram(shaderProgramID);
		
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4 * 4);
		
		Matrix4f mat4f = new Matrix4f();
		
		mat4f.translate(actor.getPositionOnScreen());
		
		mat4f.storeTranspose(matBuffer);
		matBuffer.flip();
		
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(shaderProgramID, "transform"), false, matBuffer);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_BYTE, 0);
		
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		
		GL20.glUseProgram(0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(VBO);
		GL20.glDeleteProgram(shaderProgramID);
	}

}
