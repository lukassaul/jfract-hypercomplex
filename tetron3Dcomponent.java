

import idx3d.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import java.io.FileInputStream;
import java.util.Vector;
import java.net.URL;
//import java.applet.Applet;

/**
*  This is a Jpanel, does the 3d display business for us
*
*  Keep tetron calculations out of this class..  just 3D engine interface here.
*  Now a JPanel to support SwingSet integration
*/
public class Tetron3Dcomponent extends JPanel implements Runnable, ActionListener {
	private Thread idx_Thread;
	idx3d_Scene scene;
	boolean initialized=false;
	boolean antialias=false;

	int oldx=0;
	int oldy=0;
	boolean autorotation=false;

	private Tetron3D theMain;

	//private SphereChess theMain;
	//private JPopupMenu popup;

	// for action determination
	//private idx3d_Object lastObject;
	// now delegated to SphereChess

	//private boolean pieceSelected;
	//private idx3d_Object selectedObject;
	//private idx3d_Object selectedSquareObject;

	public Tetron3Dcomponent(Tetron3D sc)	{
		// pointers rock
		theMain = sc;
		//pieceSelected=false;
		//lastObject=null;
		//final JFrame gui = theMain.gui;

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}
 			public void mouseEntered(MouseEvent e) {
          		//	Invoked when the mouse enters a component.
			}
 			public void mouseExited(MouseEvent e) {
 	         	//	Invoked when the mouse exits a component.
			}
 			public void mousePressed(MouseEvent e) {
          		//	Invoked when a mouse button has been pressed on a component.
			}
 			public void mouseReleased(MouseEvent e) {
          		//	Invoked when a mouse button has been released on a component.
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int x=e.getX(); int y=e.getY();
				//theMain.resetSelected();
				autorotation=false;
				float dx=(float)(y-oldy)/100;
				float dy=(float)(oldx-x)/100;
				scene.rotate(dx,dy,0);
				oldx=x;
				oldy=y;
			}
			public void mouseMoved(MouseEvent e) {
			}
		});

	}



	/**
	* This is because key events are coming into the JFRame, JSphereChess
	*
	*  (Swing protocol here)
	*/
	public void processKeyEvent(KeyEvent e) {
		int key = e.getKeyCode();
		int kch = e.getKeyChar();
		if (key==32) { System.out.println(scene.getFPS()+""); }
		if (key==KeyEvent.VK_PAGE_UP) {scene.defaultCamera.shift(0f,0f,0.2f); }
		if (key==KeyEvent.VK_PAGE_DOWN) {scene.defaultCamera.shift(0f,0f,-0.2f); }
		if (key==KeyEvent.VK_UP) {scene.defaultCamera.shift(0f,0.2f,0f); }
		if (key==KeyEvent.VK_DOWN) {scene.defaultCamera.shift(0f,-0.2f,0f);}
		if (key==KeyEvent.VK_LEFT) {scene.defaultCamera.shift(-0.2f,0f,0f); }
		if (key==KeyEvent.VK_RIGHT) {scene.defaultCamera.shift(0.2f,0f,0f); }
		if (kch=='+') {scene.scale(1.2f); }
		if (kch=='-') {scene.scale(0.8f);  }
		if (kch=='.') {scene.defaultCamera.roll(0.2f); }
		if (kch==',') {scene.defaultCamera.roll(-0.2f);  }
		if (kch=='a') {antialias=!antialias; scene.setAntialias(antialias); }

		//if (kch=='e') {export(); }
		//if (kch=='i') {idx3d.debug.Inspector.inspect(scene); }
	}

	public void init()	{
		//setNormalCursor();

		//resize(300,200);

		// BUILD SCENE
		scene=new idx3d_Scene(300,200);
		//scene.useIdBuffer(true);

		theMain.buildAll(scene);
		System.out.println("finished building scene.. now rebuilding");
		scene.rebuild();
		System.out.println("done rebuilding");

		scene.scale(0.8f);
		scene.scale(0.8f);


		System.out.println("set materials to objects..");

		idx_Thread = new Thread(this);
		idx_Thread.start();

		initialized=true;
	}



	public synchronized void paint(Graphics g) {
		repaint();
	}


	public void run() {
		while(true)	{
			repaint();
			try	{
				idx_Thread.sleep(10);
			}
			catch (InterruptedException e)	{
				System.out.println("idx://interrupted");
			}
		}
	}

	public synchronized void update(Graphics g)	{
		if (!initialized) return;

		//idx3d_Object obj=scene.identifyObjectAt(oldx,oldy);
		//System.out.println("updating: " + obj.name);

		//if (obj!=null) {
		//	lastObject = obj;
			//theMain.objectAction(obj);
		//}

		scene.render();
		g.drawImage(scene.getImage(),0,0,this);
	}

	public boolean imageUpdate(Image image, int a, int b, int c, int d, int e)	{
   	     return true;
   	}

	public void reshape(int x, int y, int w, int h)	{
		super.reshape(x,y,w,h);
		if (!initialized) init();
		scene.resize(w,h);
	}

	public synchronized void repaint()	{
		if (getGraphics() != null) update(getGraphics());
	}

	private idx3d_Object identifyObject(int x, int y) {
		idx3d_Object tbr;
		//update();
		//scene.normalize()
		tbr = scene.identifyObjectAt(x,y);
		if (tbr!=null) {
			System.out.println("found idx3dObj first try!");
			return tbr;
		}
		int index = 1;
		for (int i=-5; i<6; i++) {
			for (int j=-5; j<6; j++) {
				//scene.normalize();
				tbr=scene.identifyObjectAt(x+i,y+j);
				index++;
				if (tbr!=null) {
					System.out.println("found obj after " + index + " tries");
					return tbr;
					//System.out.println("tried again: " + (x+i) + " " + (y+j));
				}
			}
		}
		return null;
	}

	/**
	*   Here's where all the button presses and actions go...
	*/
	public void actionPerformed(ActionEvent e) {
		Object source = e.getActionCommand();
		String src = (String)source;

		if (src.length()==2) {
			//theMain.assign(src,selectedSquareObject);
		}

		else if (src.equals("Empty")) {
			//theMain.vacate(selectedSquareObject);
		}
	}
	//System.out.println(e+"");
}





