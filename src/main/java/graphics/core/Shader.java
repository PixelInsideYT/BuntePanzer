package graphics.core;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
	private static List<Integer> allShaderProgramms = new ArrayList<Integer>();
	private static FloatBuffer matrixBuffer4f = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer matrixBuffer3f = BufferUtils.createFloatBuffer(9);

	public int shaderProgram;
	private List<Integer> shaders = new ArrayList<Integer>();
	private Map<String, Integer> uniforms = new HashMap<String, Integer>();

	public Shader(String vertex, String fragment) {
		shaders.add(attachShader(GL_VERTEX_SHADER, vertex));
		shaders.add(attachShader(GL_FRAGMENT_SHADER, fragment));
	}

	private int attachShader(int type, String code) {
		int shader = glCreateShader(type);
		glShaderSource(shader, code);
		glCompileShader(shader);
		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println("Something went wrong for code: "+code+" \n");
			System.out.println(GL20.glGetShaderInfoLog(shader, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);

		}
		return shader;
	}

	public static String loadShaderCode(String name) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(
					Shader.class.getClassLoader().getResourceAsStream("shaders/" + name));
			BufferedReader reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("Could not read file. " + name);
			e.printStackTrace();
			System.exit(-1);
		}
		return shaderSource.toString();
	}

	public Shader combine() {
		shaderProgram = glCreateProgram();
		allShaderProgramms.add(shaderProgram);
		shaders.forEach(i -> glAttachShader(shaderProgram, i));
		glLinkProgram(shaderProgram);
		if (GL20.glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
			System.out.println(GL20.glGetProgramInfoLog(shaderProgram, 500));
			System.err.println("Could not links shaders.");
			System.exit(-1);
		}
		deleteShaders();
		return this;
	}

	public void bind() {
		glUseProgram(shaderProgram);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public Shader bindAtrributs(String... attributes) {
		for (int i = 0; i < attributes.length; i++) {
			bindAttribute(i, attributes[i]);
		}
		return this;
	}

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(shaderProgram, attribute, variableName);
	}

	public Shader connectSampler(String samplerName, int unit) {
		bind();
		GL20.glUniform1i(uniforms.get(samplerName), unit);
		unbind();
		return this;
	}

	public void loadUniforms(String... uniformNames) {
		for (int i = 0; i < uniformNames.length; i++) {
			loadUniform(uniformNames[i]);
		}
	}

	public void loadUniform(String name) {
		int id = GL20.glGetUniformLocation(shaderProgram, name);
		uniforms.put(name, id);
		if (id == -1) {
			System.err.println("Uniform: " + name + " not found!");
		}
	}

	public void loadFloat(String name, float value) {
		GL20.glUniform1f(uniforms.get(name), value);
	}

	public void loadInt(String name, int value) {
		GL20.glUniform1i(uniforms.get(name), value);
	}

	public void load2DVector(String name, Vector2f vector) {
		GL20.glUniform2f(uniforms.get(name), vector.x, vector.y);
	}

	public void load3DVector(String name, Vector3f vector) {
		GL20.glUniform3f(uniforms.get(name), vector.x, vector.y, vector.z);
	}

	public void load4DVector(String name, Vector4f vector) {
		GL20.glUniform4f(uniforms.get(name), vector.x, vector.y, vector.z, vector.w);
	}

	public void loadMatrix(String name, Matrix4f matrix) {
		matrixBuffer4f.clear();
		matrix.get(matrixBuffer4f);
		GL20.glUniformMatrix4fv(uniforms.get(name), false, matrixBuffer4f);
	}

	public void loadMatrix(String name, Matrix3f matrix) {
		matrixBuffer3f.clear();
		matrix.get(matrixBuffer3f);
		GL20.glUniformMatrix3fv(uniforms.get(name), false, matrixBuffer3f);
	}

	private void deleteShaders() {
		unbind();
		shaders.forEach(i -> {
			glDetachShader(shaderProgram, i);
			glDeleteShader(i);
		});
	}

	public static void cleanUpAllShaders() {
		allShaderProgramms.forEach(i -> glDeleteProgram(i));
	}

}
