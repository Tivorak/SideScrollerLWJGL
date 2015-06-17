package de.gymger.sidescroller.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.gymger.sidescroller.util.AssetManager;

public class TestObject implements IDrawable{

	static int VBO, EBO, uOffset, texIDWall, texIDSmiley;
	
	//static FloatBuffer vertices;
	//static ByteBuffer indices;
	
	float texCoords[] = {
		0f, 0f, 
		1f, 0f, 
		0.5f, 1f
	};
	
	double scalarX, scalarY;
	
	Vector2f off;
	
	private static final boolean WIREFRAME = false;
	
	public TestObject(double x, double y){
		
		GraphicsManager.preloadObject(this);
		
		Random r = new Random();

		off = new Vector2f((r.nextFloat() - 0.5f) / 2f, (r.nextFloat() - 0.5f) / 2f);
		scalarX = x;
		scalarY = y;
	}
	
	@Override
	public void preLoad(){
		GraphicsManager.preloadObject(this);
		if(VBO == 0 && EBO == 0){
			VBO = GL15.glGenBuffers();
			EBO = GL15.glGenBuffers();

			texIDWall = AssetManager.loadTexture("wall");
			texIDSmiley = AssetManager.loadTexture("awesomeface");

			FloatBuffer vertices = BufferUtils.createByteBuffer(4 * 8 * Float.BYTES).asFloatBuffer();

			vertices.put(new float[]{
					//POSITION		     //COLOR       //TEXTURE COORDS
					-0.5f,  0.5f, 1f,   1f, 0f, 0f,   0f, 1f,  //0:TOP LEFT
					 0.5f,  0.5f, 1f,   0f, 1f, 0f,   1f, 1f,  //1:TOP RIGHT
					-0.5f, -0.5f, 1f,   0f, 0f, 1f,   0f, 0f,  //2:BOT LEFT
					 0.5f, -0.5f, 1f,   0f, 0f, 0f,   1f, 0f   //3:BOT RIGHT

			}).flip();

			ByteBuffer indices = BufferUtils.createByteBuffer(2 * 3);

			indices.put(new byte[]{
					0, 1, 2,
					1, 2, 3
			}).flip();


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

			uOffset = GL20.glGetUniformLocation(GraphicsManager.getBasicShaderProgram(), "offset");

		}
	}
	
	public void draw(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		
		if(WIREFRAME)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		boolean output = false;
		
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
		
		Matrix4f m4f = new Matrix4f();
		
		if(output)
			System.out.println(m4f);
		
		m4f.translate(new Vector2f((float)(Math.sin(GLFW.glfwGetTime() * scalarX) * 0.5f) + off.getX(), (float)(Math.cos(GLFW.glfwGetTime() * scalarY) * 0.5f) + off.getY()));
		
		m4f.rotate(-(float)(GLFW.glfwGetTime() * Math.PI), new Vector3f(0, 0, 1));
		
		m4f.scale(new Vector3f((float)(Math.sin(GLFW.glfwGetTime()) + 1) / 3f, (float)(Math.sin(GLFW.glfwGetTime()) + 1) / 3f, 0));
		
		if(output)
			System.out.println(m4f);
		
		m4f.storeTranspose(matrixBuffer);
		
		matrixBuffer.flip();
		
		if(output){
			int i = 0;

			System.out.println("Buffer Contents:");
			
			while(matrixBuffer.hasRemaining()){
				System.out.print(matrixBuffer.get() + ((i == 3)?"\r\n":"\t"));
				i++;
				if(i > 3)
					i = 0;
			}

			System.out.println();
			matrixBuffer.flip();
		}
		
		
		GL20.glUseProgram(GraphicsManager.getBasicShaderProgram());
		
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(GraphicsManager.getBasicShaderProgram(), "transform"), false, matrixBuffer);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIDWall);
		GL20.glUniform1i(GL20.glGetUniformLocation(GraphicsManager.getBasicShaderProgram(), "wallTex"), 0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIDSmiley);
		GL20.glUniform1i(GL20.glGetUniformLocation(GraphicsManager.getBasicShaderProgram(), "smileyTex"), 1);
		

		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 0);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		{
			GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL20.glUseProgram(0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		if(WIREFRAME)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(EBO);
		GL15.glDeleteBuffers(VBO);
	}
}
