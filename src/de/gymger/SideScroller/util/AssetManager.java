package de.gymger.sidescroller.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class AssetManager {
	
	private AssetManager(){};

	private static String basePath = ClassLoader.getSystemClassLoader().getResource("").getFile().replace('/', File.separatorChar).substring(1) + ".." + File.separatorChar + "assets";

	private static final int BYTES_PER_PIXEL = 4;
	public static int loadTexture(String name){

		final BufferedImage image;

		int textureID = 0;
		
		try {
			image = ImageIO.read(new File(basePath + File.separatorChar + name.replace('.', File.separatorChar) + ".png"));

			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

			for(int y = image.getHeight() - 1; y >= 0; y--){
				for(int x = image.getWidth() - 1; x >= 0; x--){
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
					buffer.put((byte) (pixel & 0xFF));               // Blue component
					buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
				}
			}

			buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
			
			// You now have a ByteBuffer filled with the color data of each pixel.
			// Now just create a texture ID and bind it. Then you can load it using 
			// whatever OpenGL method you want, for example:

			textureID = GL11.glGenTextures(); //Generate texture ID
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID

			//Setup wrap mode
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
			

			//Setup texture scaling filterings
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);


			//Send texel data to OpenGL
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Return the texture ID so we can bind it later again
		return textureID;
	}
	
	public static void unloadTexture(int id){
		GL11.glDeleteTextures(id);
	}
	
	public static String getBasePath(){
		return basePath;
	}
}
