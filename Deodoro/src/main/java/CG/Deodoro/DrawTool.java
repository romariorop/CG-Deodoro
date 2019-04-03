package CG.Deodoro;

import java.nio.FloatBuffer;
import java.util.HashMap;

import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class DrawTool extends DebugGL2{
	
	float deodoroW = 1200, deodoroH = 800, deodoroD = 1500;
	float doorW = 150, doorH = 200;
	float windowW = 100, windowH = 100;
	float leftDoorRotation = 0;
	float backStageSize = 100, stageSize = 200;
	Texture currentTexture;
	GLU glu;
	HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private GLUquadric IDquadric;
	public DrawTool(GL2 downstream, HashMap<String, Texture> textures) {
		super(downstream);
		GLU glu = GLU.createGLU(this);
		IDquadric = glu.gluNewQuadric();
		glu.gluQuadricNormals(IDquadric, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(IDquadric, true);
		this.textures = textures;
	}
	
	public void drawOutside(float doorRotation) {
		
		//global lighting
		float ambientLight[] = {0.5f, 0.5f, 0.5f, 0.5f};// 0.5
		float diffuseLight[] = {0.55f, 0.55f, 0.55f, 0.15f};// 0.8
		float specularLight[] = {0.0055f, 0.0055f, 0.0055f, 0.1f};// 0.3
		float lightPos[] = {deodoroW/2, 1000f, -deodoroD/2, 1.0f};
		
		glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, FloatBuffer.wrap(ambientLight));
		glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, FloatBuffer.wrap(diffuseLight));
		glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, FloatBuffer.wrap(specularLight));
		glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, FloatBuffer.wrap(lightPos));
		setChandelierLighting();
		leftDoorRotation = doorRotation;
		drawFront();
		drawBack();
		drawSides();
		drawBottom();
		drawTop();
		drawStage();
		drawPilars();
	}
	
	public void drawPilars() {
		glu = GLU.createGLU(this);
		float dx[] = {140, 140, 140, 140, deodoroW-140, deodoroW-140, deodoroW-140, deodoroW-140};
		float dz[] = {300, 600, 900, 1200, 300, 600, 900, 1200};
		textures.get("stage").bind(this);
		textures.get("stage").enable(this);
		for(int i = 0; i < 8; ++i) {
			
			glPushMatrix();
				glColor3f(0,0,0);
				glTranslatef(dx[i], 0, -dz[i]);
				glRotatef(-90, 1, 0, 0);
				glu.gluCylinder(this.IDquadric,5,5,deodoroH,20,20);
			glPopMatrix();
		}
		textures.get("stage").disable(this);
		
	}
	
	public void drawStage() {
		this.glPushMatrix();
			this.glTranslatef(0, 0, -deodoroD + backStageSize + stageSize);
			drawWall3d((int)doorW, deodoroH/10, deodoroW-2*doorW, stageSize, "stage");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(0, 0, -deodoroD);
			drawWall3d(0, deodoroH/10, doorW, deodoroD, "stage-sidewall");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(0, deodoroH*0.3f, -deodoroD);
			drawWall3d(0, deodoroH/10, doorW, deodoroD, "stage-sidewall");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(0, deodoroH*0.6f, -deodoroD);
			drawWall3d(0, deodoroH/10, doorW, deodoroD, "stage-sidewall");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(deodoroW-doorW, 0, -deodoroD);
			drawWall3d(0, deodoroH/10, doorW, deodoroD, "stage-sidewall");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(deodoroW-doorW, deodoroH*0.3f, -deodoroD);
			drawWall3d(0, deodoroH/10, doorW, deodoroD, "stage-sidewall");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(deodoroW-doorW, deodoroH*0.6f, -deodoroD);
			drawWall3d(0, deodoroH/10, doorW, deodoroD, "stage-sidewall");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(deodoroW, 0, -deodoroD + backStageSize);
			drawWall(0, deodoroH, deodoroW - 2*deodoroW, "curtain", 1, true);
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(0, deodoroH/10, -deodoroD + backStageSize + stageSize);
			drawWall3d((int)doorW, deodoroH/10, deodoroH/10, deodoroH/10, "soundbox");
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(deodoroW-doorW*2, deodoroH/10, -deodoroD + backStageSize + stageSize);
			drawWall3d(0, deodoroH/10, deodoroH/10, deodoroH/10, "soundbox");
		this.glPopMatrix();
		for(int j = 100; j < deodoroD - backStageSize - stageSize-200; j += 50) {
			for(int i = 300; i < deodoroW - 300; i += 50) {
				this.glPushMatrix();
					this.glTranslatef(0, 0, -j);
					drawChair(i, 40, 40, 5);
				this.glPopMatrix();
			}
		}
		float dH[] = {deodoroH/10 - 5, deodoroH/10 - 5 + deodoroH*0.3f, deodoroH/10 - 5 + deodoroH*0.6f,
				deodoroH/10 - 5, deodoroH/10 - 5 + deodoroH*0.3f, deodoroH/10 - 5 + deodoroH*0.6f};
		float dT[] = {-90, -90, -90, 90, 90, 90};
		float dx[] = {80, 80, 80, deodoroW-90, deodoroW-90, deodoroW-90};
		glPushMatrix();
			for(int i = 0; i < 6; ++i) {
				for(int j = 100; j < deodoroD - backStageSize - stageSize; j += 50) {
						this.glPushMatrix();
							this.glTranslatef(dx[i], dH[i], -j);
							glPushMatrix();
								glRotatef(dT[i], 0 , 1, 0);
								drawChair(10, 40, 40, 5);
							glPopMatrix();
						this.glPopMatrix();
				}
			}
		glPopMatrix();
	}
	
	public void drawSemiCircle(float radius, float direction, float z) { // L = 0 , U = 1, R = 2, D = 3
		direction *= (Math.PI/2);
		double twoPI = 2 * Math.PI;
		this.glBegin(GL_TRIANGLE_FAN);
	    	for (double i = Math.PI + direction; i <= twoPI + direction; i += 0.001) {
	    		this.glVertex3d((Math.sin(i)*radius), (Math.cos(i)*radius), z);
	    	}
		this.glEnd();
	    this.glFlush();
	}
	
	public void drawChair(int x, float h, float w, float d) {
		glPushMatrix();
			glTranslatef(0,h/2, 0);
			drawWall3d(x, h, w, d, "p-chair");
			drawWall3d(x, d, w, -h, "p-chair");
			glPushMatrix();
				glTranslatef(0, -h/2+d, d);
				drawWall3d(x-d, h/2, d, -h*3/4-d, "p-chair-wood");
				drawWall3d(x+w, h/2, d, -h*3/4-d, "p-chair-wood");
			glPopMatrix();
		glPopMatrix();
	}
	
	public void drawBack() {
		this.glPushMatrix();
			this.glTranslatef(0, 0, -deodoroD);
			drawWall(0, deodoroH, deodoroW, "outside-wall", 1, true);
		this.glPopMatrix();
	}
	
	public void drawSides() {
		this.glPushMatrix(); // left side
			this.glRotatef(90f, 0, 1, 0);
			drawWall(0, deodoroH, deodoroD, "outside-wall", 1, false);
		this.glPopMatrix();
		this.glPushMatrix(); // right side
			this.glRotatef(90f, 0, 1, 0);
			this.glTranslatef(0, 0, deodoroW);
			drawWall(0, deodoroH, deodoroD, "outside-wall", 1, false);
		this.glPopMatrix();
	}
	
	public void drawBottom() {
		this.glPushMatrix(); // right side
			this.glRotatef(-90f, 1, 0, 0);
			drawWall(0, deodoroD, deodoroW, "floor", 10, true);
		this.glPopMatrix();
	}
	
	public void drawTop() {
		this.glPushMatrix(); // right side
			this.glRotatef(-90f, 1, 0, 0);
			this.glTranslatef(0, 0, deodoroH);
			drawWall(0, deodoroD, deodoroW, "inside-roof", 10, false);
		this.glPopMatrix();
	}
	
	public void drawFront() {
		drawDoors();
		drawWindows();
		drawWalls();
	}
	
	public void drawWalls() {
		float windowLimit = doorH*2 + windowH;
		float innerWall = deodoroW/24;
		float outerWall = deodoroW/48;
		float middle = deodoroW/2;
		float middleL = middle - doorW - innerWall;
		float middleR = middle + doorW + innerWall;
		float leftWallLimit = (deodoroW/8) - windowW/2;
		
		
		this.glPushMatrix(); // topW
			this.glTranslatef(0, windowLimit, 0);
			drawWall(0, deodoroH - windowLimit, deodoroW, "outside-wall", 1, true);
		this.glPopMatrix();
		
		this.glPushMatrix(); // middle
			this.glTranslatef(0, doorH, 0);
			drawWall(0, doorH , deodoroW, "outside-wall", 1, true);
		this.glPopMatrix();
		
		this.glPushMatrix(); // left
			drawWall(0, deodoroH , leftWallLimit, "outside-wall", 1, true);
		this.glPopMatrix();
		
		this.glPushMatrix(); // right
			this.glTranslatef(deodoroW - leftWallLimit, 0, 0);
			drawWall(0, deodoroH , leftWallLimit, "outside-wall", 1, true);
		this.glPopMatrix();
		
		this.glPushMatrix(); // TopCenter
			this.glTranslatef(0, doorH*2, 0);
			drawWall(middle + windowW/2, windowH , (deodoroW/6), "outside-wall", 1, true);
			drawWall(middle - windowW/2 - windowH, windowH , (deodoroW/6), "outside-wall", 1, true);
		this.glPopMatrix();
		
		this.glPushMatrix(); // BottomCenter
			drawWall(middle + doorW/2, doorH , innerWall, "outside-wall", 1, true);
			drawWall(middle - doorW/2 - innerWall, doorH , innerWall, "outside-wall", 1, true);
		this.glPopMatrix();
		
		this.glPushMatrix(); // doorSides
			drawWall(middleL  - doorW/2 - (leftWallLimit + outerWall), doorH , leftWallLimit + outerWall, "outside-wall", 1, true);
			drawWall(middleR + doorW/2 , doorH , leftWallLimit+ outerWall, "outside-wall", 1, true);
		this.glPopMatrix();
		
		middleL = (deodoroW/4)/2;
		middleR = middleL + (deodoroW)*3/4;
		this.glPushMatrix(); // windowsBase
			drawWall(middleL  - windowW/2, windowH , windowW, "outside-wall", 1, true);
			drawWall(middleR  - windowW/2 , windowH , windowW, "outside-wall", 1, true);
		this.glPopMatrix();
		float diff = (middle - doorW - innerWall - windowW/2) - (middleL+windowW/2); 
		this.glPushMatrix(); // TopWindowsSides
			this.glTranslatef(0, doorH*2, 0);
			drawWall(middleL  + windowW/2  , windowH , windowW + diff, "outside-wall", 1, true);
			drawWall(middleR  - windowW/2 - (windowW + diff), windowH , windowW + diff, "outside-wall", 1, true);
		this.glPopMatrix();
		
	}
	
	public void drawWindows() {
		drawBottomWindows();
		drawTopWindows();
	}
	
	public void drawTopWindows() {
		float middle = deodoroW/2;
		float middleL = (deodoroW/4)/2;
		float middleR = middleL + (deodoroW)*3/4;
		this.glPushMatrix();
			this.glTranslatef(0, doorH*2, 0);
			drawWall(middleL - windowW/2,windowH,windowW, "door", 1, true);
			drawWall(middle - (deodoroW/6) - windowW/2,windowH,windowW, "door", 1, true);
			drawWall(middle - windowW/2,windowH,windowW, "door", 1, true);
			drawWall(middle + (deodoroW/6) - windowW/2,windowH,windowW, "door", 1, true);
			drawWall(middleR - windowW/2,windowH,windowW, "door", 1, true);
			
		this.glPopMatrix();
	}
	
	public void drawBottomWindows() {
		float middleL = (deodoroW/4)/2;
		float middleR = middleL + (deodoroW)*3/4;
		this.glPushMatrix();
			this.glTranslatef(0, windowH, 0);
			drawWall(middleL - windowW/2,windowH,windowW, "door",1, true);
			drawWall(middleR - windowW/2,windowH,windowW, "door",1, true);
		this.glPopMatrix();
	}
	
	public void drawDoors() {
		float middle = deodoroW/2;
		float innerWall = deodoroW/24;
			
		this.glPushMatrix();
			this.glTranslatef(middle - doorW*1.5f - innerWall, 0, 0);	
			this.glRotatef(leftDoorRotation, 0, 1, 0);
			this.drawWall(0, doorH, doorW/2, "door",1, true);
		this.glPopMatrix();
		this.glPushMatrix();
			this.glTranslatef(middle - doorW*0.5f - innerWall, 0, 0);	
			this.glRotatef(180 - leftDoorRotation, 0, 1, 0);
			this.drawWall(0, doorH, doorW/2, "door",1, false);
		this.glPopMatrix();
		
		this.glPushMatrix();
			this.glTranslatef(middle - doorW/2, 0, 0);
			this.glRotatef(leftDoorRotation, 0, 1, 0);
			this.drawWall(0, doorH, doorW/2, "door",1, true);
		this.glPopMatrix();
		this.glPushMatrix();
		this.glTranslatef(middle + doorW/2, 0, 0);	
			this.glRotatef(180 - leftDoorRotation, 0, 1, 0);
			this.drawWall(0, doorH, doorW/2, "door",1, false);
		this.glPopMatrix();
		
		this.glPushMatrix();
			this.glTranslatef(middle + doorW/2 + innerWall, 0, 0);
			this.glRotatef(leftDoorRotation, 0, 1, 0);
			this.drawWall(0, doorH, doorW/2, "door",1, true);
		this.glPopMatrix();
		
		this.glPushMatrix();
			this.glTranslatef(middle + doorW*1.5f + innerWall, 0, 0);	
			this.glRotatef(180 - leftDoorRotation, 0, 1, 0);
			this.drawWall(0, doorH, doorW/2, "door", 1, false);
		this.glPopMatrix();
	}
	
	public void drawWall(float x, float height, float width, String texName, float repeater, boolean faceInside) {
		textures.get(texName).bind(this);
		textures.get(texName).enable(this);
		glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		this.glBegin(GL2.GL_QUADS);
		int normal = 1;
		if(faceInside)normal*=-1;
		this.glTexCoord2f(0.0f, 0.0f); glNormal3d(0, 0, normal);this.glVertex3f(x, 0, 0);
		this.glTexCoord2f(repeater, 0.0f); glNormal3d(0, 0, normal);this.glVertex3f(x, height, 0);
		this.glTexCoord2f(repeater, repeater); glNormal3d(0, 0, normal);this.glVertex3f(x+ width, height, 0);
		this.glTexCoord2f(0.0f, repeater); glNormal3d(0, 0, normal);this.glVertex3f(x+ width, 0, 0);
		this.glEnd();
		textures.get(texName).disable(this);
	}
	
	public void drawWall3d(float x, float h, float w, float d, String texName) {
		textures.get(texName).bind(this);
		textures.get(texName).enable(this);
//		this.glBindTexture(GL2.GL_TEXTURE_2D, textures.get(texName).getTextureObject());
		glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		this.glPushMatrix();
			this.glBegin(GL2.GL_QUADS);
				this.glTexCoord2f(0.0f, 0.0f);glNormal3d(0, 0, -1); this.glVertex3f(x, 0, 0);
				this.glTexCoord2f(1.0f, 0.0f);glNormal3d(0, 0, -1); this.glVertex3f(x, h, 0);
				this.glTexCoord2f(1.0f, 1.0f);glNormal3d(0, 0, -1); this.glVertex3f(x+w, h, 0);
				this.glTexCoord2f(0.0f, 1.0f);glNormal3d(0, 0, -1); this.glVertex3f(x+w, 0, 0);
			this.glEnd();
			this.glBegin(GL2.GL_QUADS);
				this.glTexCoord2f(0.0f, 0.0f);glNormal3d(0, 0, 1); this.glVertex3f(x, 0, d);
				this.glTexCoord2f(1.0f, 0.0f);glNormal3d(0, 0, 1); this.glVertex3f(x, h, d);
				this.glTexCoord2f(1.0f, 1.0f);glNormal3d(0, 0, 1); this.glVertex3f(x+w, h, d);
				this.glTexCoord2f(0.0f, 1.0f);glNormal3d(0, 0, 1); this.glVertex3f(x+w, 0, d);
			this.glEnd();
			this.glBegin(GL2.GL_QUADS);
				this.glTexCoord2f(0.0f, 0.0f);glNormal3d(1, 0, 0); this.glVertex3f(x, 0, 0);
				this.glTexCoord2f(1.0f, 0.0f);glNormal3d(1, 0, 0); this.glVertex3f(x, h, 0);
				this.glTexCoord2f(1.0f, 1.0f);glNormal3d(1, 0, 0); this.glVertex3f(x, h, d);
				this.glTexCoord2f(0.0f, 1.0f);glNormal3d(1, 0, 0); this.glVertex3f(x, 0, d);
			this.glEnd();
			this.glBegin(GL2.GL_QUADS);
				this.glTexCoord2f(0.0f, 0.0f);glNormal3d(1, 0, 0); this.glVertex3f(x+w, 0, 0);
				this.glTexCoord2f(1.0f, 0.0f);glNormal3d(1, 0, 0); this.glVertex3f(x+w, h, 0);
				this.glTexCoord2f(1.0f, 1.0f);glNormal3d(1, 0, 0); this.glVertex3f(x+w, h, d);
				this.glTexCoord2f(0.0f, 1.0f);glNormal3d(1, 0, 0); this.glVertex3f(x+w, 0, d);
			this.glEnd();
			this.glBegin(GL2.GL_QUADS);
				this.glTexCoord2f(0.0f, 0.0f);glNormal3d(0, -1, 0); this.glVertex3f(x, 0, 0);
				this.glTexCoord2f(1.0f, 0.0f);glNormal3d(0, -1, 0); this.glVertex3f(x, 0, d);
				this.glTexCoord2f(1.0f, 1.0f);glNormal3d(0, -1, 0); this.glVertex3f(x+w, 0, d);
				this.glTexCoord2f(0.0f, 1.0f);glNormal3d(0, -1, 0); this.glVertex3f(x+w, 0, 0);
			this.glEnd();
			this.glBegin(GL2.GL_QUADS);
				this.glTexCoord2f(0.0f, 0.0f);glNormal3d(0, 1, 0); this.glVertex3f(x, h, 0);
				this.glTexCoord2f(1.0f, 0.0f);glNormal3d(0, 1, 0); this.glVertex3f(x, h, d);
				this.glTexCoord2f(1.0f, 1.0f);glNormal3d(0, 1, 0); this.glVertex3f(x+w, h, d);
				this.glTexCoord2f(0.0f, 1.0f);glNormal3d(0, 1, 0);this.glVertex3f(x+w, h, 0);
			this.glEnd();
		this.glPopMatrix();
		textures.get(texName).disable(this);
	}
	
	private void setChandelierLighting() {
		float ambientLight[] = {0.30f, 0.30f, 0.30f, 0.2f};
		float diffuseLight[] = {0.6f, 0.6f, 0.30f, 0.5f};
		float specularLight[] = {0.15f, 0.15f, 0.15f, 1.0f};
		float lightPos[] = {deodoroW/2-25, deodoroH-25, -deodoroD/2};
		GLUT glut = new GLUT();
		textures.get("stage-sidewall").bind(this);
		textures.get("stage-sidewall").enable(this);
		glPushMatrix();
			glColor4f(1, 1, 1, 0.5f);
			glTranslatef(deodoroW/2, deodoroH-50, -deodoroD/2);
			glRotatef(45, 1, 0, 1);
			glut.glutSolidCube(60);
		glPopMatrix();
		textures.get("stage-sidewall").disable(this);
		glLightfv(GL_LIGHT1, GL_AMBIENT, FloatBuffer.wrap(ambientLight));
		glLightfv(GL_LIGHT1, GL_DIFFUSE, FloatBuffer.wrap(diffuseLight));
		glLightfv(GL_LIGHT1, GL_SPECULAR, FloatBuffer.wrap(specularLight));
		glLightf(GL_LIGHT1, GL_CONSTANT_ATTENUATION, 0.1f);
		glLightf(GL_LIGHT1, GL_LINEAR_ATTENUATION, .03f);
		glLightfv(GL_LIGHT1, GL_POSITION, FloatBuffer.wrap(lightPos));
	}
	
}