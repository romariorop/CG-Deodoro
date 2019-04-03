package CG.Deodoro;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import CG.Deodoro.Camera.Direction;

public class FieldCanvas extends GLCanvas implements GLEventListener, KeyListener {
	

	private static final long serialVersionUID = 1L;

	Camera camera;
    float doorA;
    boolean light;
    HashMap<String, Texture> textures = new HashMap<String, Texture>();
	String textureNames[] = {"chair", "curtain", "door", "floor", "inside-roof", "inside-wall", "outside-wall", "p-chair-wood",
			"p-chair", "roof", "soundbox", "stage-sidewall", "stage"};
	public FieldCanvas(int width, int height, GLCapabilities capabilities) {
		super(capabilities);
		setSize(width, height);
		addGLEventListener(this);
		addKeyListener(this);
		doorA = 0;
		light = true;
		camera = new Camera(588, 270 , -255);
	}
	
	public void loadTexture(String file){
		try {	
			File img = new File("src/textures/" + file);
			textures.put(file.split("\\.")[0], TextureIO.newTexture(img, true));
		} catch (Exception e) {
			System.err.println(e);
			System.exit(0);
		}
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		drawable.setGL(new DrawTool(gl, textures));
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glClearColor(52f/255, 73f/255, 94f/255, 0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		for(int i = 0; i < textureNames.length; ++i) {
			loadTexture(textureNames[i] + ".png");
		}
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);

	}
	
	public void display(GLAutoDrawable drawable) {
		
		DrawTool gl = new DrawTool(drawable.getGL().getGL2(), textures);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		camera.setLookAt(GLU.createGLU(gl));
		if(light) {
			gl.glEnable(GL2.GL_LIGHT1);
		} else {
			gl.glDisable(GL2.GL_LIGHT1);
		}
		gl.drawOutside(doorA);
		gl.glFlush();
		
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		drawable.setGL(new DrawTool(gl, textures));
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		GLU glu = GLU.createGLU(gl);
		glu.gluPerspective(45.0, (double) drawable.getSurfaceWidth() / drawable.getSurfaceHeight(), 215, 4500.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		double fraction = 50;
		if(e.getKeyCode() == KeyEvent.VK_W) {
			camera.move(fraction, Direction.UP);
		} else if(e.getKeyCode() == KeyEvent.VK_S) {
			camera.move(fraction, Direction.DOWN);
		} else if(e.getKeyCode() == KeyEvent.VK_A) {
			camera.move(fraction, Direction.LEFT);
		} else if(e.getKeyCode() == KeyEvent.VK_D) {
			camera.move(fraction, Direction.RIGHT);
		} 	else if(e.getKeyCode() == KeyEvent.VK_UP) {
            camera.turn(0.05, Direction.UP);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			camera.turn(0.05, Direction.DOWN);
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            camera.turn(0.05, Direction.LEFT);
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			camera.turn(0.05, Direction.RIGHT);
		} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			camera.fly(fraction, Direction.UP);
		} else if(e.getKeyCode() == KeyEvent.VK_V) {
			camera.fly(fraction, Direction.DOWN);
		} else if(e.getKeyCode() == KeyEvent.VK_O) {
			doorA -= fraction/10;
			doorA = Math.max(doorA, -90);
		} else if(e.getKeyCode() == KeyEvent.VK_P) {
			doorA += fraction/10;
			doorA = Math.min(doorA, 0);
		} else if(e.getKeyCode() == KeyEvent.VK_L) {
			light = !light;
		}
		display();
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}