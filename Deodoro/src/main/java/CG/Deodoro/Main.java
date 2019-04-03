package CG.Deodoro;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

public class Main {
    public static void main( String[] args ) {
    	
    	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        int width = 1200;
        int height = 1000;
        
       //Gl
       GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
       final FieldCanvas canvas = new FieldCanvas(width, height, capabilities);
       
       // window frames
       final JFrame jframe = new JFrame("Teatro Deodoro");
       jframe.setResizable(false);

       //actions
       
       jframe.addWindowListener(new WindowAdapter() {
    	   public void windowClosing(WindowEvent windowevent) {
    		   jframe.dispose();
    		   System.exit(0);
    	   }
       });


       
       //screen
       jframe.setLocation(10, 10);
       jframe.pack();
       jframe.setSize(width, height);
       jframe.getContentPane().add(canvas);
       jframe.setVisible(true);
      
    }
}