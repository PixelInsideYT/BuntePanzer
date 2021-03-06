package graphics.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Vao {

	private static List<Vao> allVaos = new ArrayList<>();

	int vaoId, indiciesLength;
	List<Integer> vbos = new ArrayList<Integer>();
	List<Integer> attribNumbers = new ArrayList<Integer>();

	public Vao() {
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		allVaos.add(this);
	}

	public int getIndiciesLength() {
		return indiciesLength;
	}

	public Vao addIndicies(int[] indicies) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indicies);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		vbos.add(vboID);
		indiciesLength = indicies.length;
		return this;
	}

	public void bind() {
		GL30.glBindVertexArray(vaoId);
		attribNumbers.forEach(i -> GL20.glEnableVertexAttribArray(i));
	}

	public void unbind() {
		attribNumbers.forEach(i -> GL20.glDisableVertexAttribArray(i));
		GL30.glBindVertexArray(0);
	}

	public void cleanUp() {
		GL30.glDeleteVertexArrays(vaoId);
		vbos.forEach(i -> GL15.glDeleteBuffers(i));
	}

	public static void cleanUpAllVaos() {
		allVaos.forEach(vao -> vao.cleanUp());
	}

	public Vao addDataAttributes(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		vbos.add(vboID);
		attribNumbers.add(attributeNumber);
		return this;
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
