import java.awt.*;
import java.applet.*;
import java.net.*;
import java.awt.image.*;
import java.util.Vector;
import a3dAPI.*;


/**
*  Lets try to use ansy3d to make a slice of tetrabrot visible
*
*/
public class Tetron3DApplet extends Applet implements Runnable {

	anfy3dAPI engine;							// An instance of Anfy3dAPI's engine 3d.

	Thread Tkicker;								// The applet thread.

	float vertex[] = {-3,00,00,00,03,00,03,00,00};		// Object vertices (format: x,y,z).
	int vertexI[] = {0,2,1};						// Object links vertices (format: v1,v2,v3).

	Tetron z = new Tetron();

	/**
	*	Method used to initialise the applet.	//
	*/
	public void init()	{

		setLayout(null);						// We don't need layout.

 		engine = new a3dCORE("software");				// Make a a3dSWH class object to use as our scene.

		Component tmp = engine.getComponent();				// Now anfy3dAPI returns also the Component you need...
		add(tmp);							// ...add to current applet...
		tmp.resize(size().width,size().height);				// ...resize it.

		int aspect[] = new int[2];					// Make an array where will put the width and height of the image to load.
		int backgroundPic[]=loadImage("data/spherical.jpg",aspect);	// Make an array where put the image loaded with loadImage().
		engine.setBackgroundImage(backgroundPic,aspect[0],aspect[1]);	// Set as background the loaded image.

		z=new Tetron();
		// OK - build the object now bitch..
		// first lets set up the full vertex array
		Vector allVertices = new Vector();
		for (int i=0; i<100; i++) {
			System.out.println("doing slice " + i);
			allVertices.addElement( getVertices(-2.0f,-1.0f,4.0f,2.0f,   500,500,
					new Tetron(),new Tetron(), new Tetron(),4,200) );
		}

		int obj=engine.createMesh(vertex,vertexI,null,null,null,null,null,null,null,null);	// Make the mesh for the object (a triangle).

		int mat=engine.createMaterial();				// Make a material...
		engine.setMeshMaterial(obj,mat);				// ...link it to the mesh...
		engine.setMaterialTransparency(mat,0.6f);			// ...set the transparency for the material...
		engine.setMaterialDiffuse(mat,1,0,0);				// ...and a colour (red: r=1,g=0,b=0).

		int sce=engine.createTransform();				// Make a transform matrix id for the scene.
		int cam=engine.createCamera();					// Make a camera.

		engine.addChild(sce,cam);					// Add, as child node, the camera to the scene.
		engine.addChild(sce,obj);					// ...and add to the scene a node for the object.

		engine.setSceneRoot(sce);					// Set the root node.
		engine.setSceneCamera(cam);					// Set the camera node.
	}


	/**
	* Use this to get an array of vertices on a plane of a tetrabrot
	*		// define tetrabrot 2D plane edges"
	*		// define # of pixels in width and height
	*       // define tetrabrot offsets and basis axes
	*		// define minimum distance twixt vertices
	*		// define minimum distance from brot to vertex (in iterations to exit)
	*/
	public int[] getVertices (float startx, float starty, float width, float height,
					int sizex, int sizey,
				Tetron bX, Tetron bY, Tetron offset, float minToVertex, int minToSet)  {

		Vector vertices = new Vector();
		int[] vA ={0,0};
		boolean firstVertex = true;

		double dx = width / sizex;
		double dy = height / sizey;

		// build the first Tetron from startx, starty, and basis vectors...
		Tetron t = new Tetron(startx*bX.a + starty*bY.a + offset.a,
		                      startx*bX.b + starty*bY.b + offset.b,
		                      startx*bX.c + starty*bY.c + offset.c,
		                      startx*bX.d + starty*bY.d + offset.d);

		Tetron ztemp = new Tetron();
		System.out.println("Calculating...");
		System.out.println("startx: " + startx + " starty: " + starty);
		System.out.println("width: " + width + " height: " + height);
		for (int x = 0 ; x < sizex ; x++) {
			// set "y" shift to zero in z tetron
			int it;
			ztemp.setCoords(t);
			for (int y = 0 ; y < sizey ; y++) {
				if ((it =tetrabrotTest(ztemp)) != -1) {
					// not in the tetrabrot set.
					if (it > minToSet) {
						// we have a cantidate vertex...
						if (firstVertex) {
							vA[0]=x; vA[1]=y;
							vertices.add(new Integer(x));
							vertices.add(new Integer(y));
						}
						else if ( (x-vA[0])*(x-vA[0])+(y-vA[1])*(y-vA[1]) > minToVertex ) {
							// cantidate second vertex .. check all now
							if (!nearVertex(x,y,vertices,minToVertex)) {
								vA[0]=x; vA[1]=y;
								vertices.add(new Integer(x));
								vertices.add(new Integer(y));
							}
						}
					}
				} else {
					// in tetrabrot set
					//raster.setPixel(x,y,black);
				}
				// increment "y"
				ztemp.a += dy*bY.a;
				ztemp.b += dy*bY.b;
				ztemp.c += dy*bY.c;
				ztemp.d += dy*bY.d;
			}
			//increment "x"
			t.a += dx*bX.a;
			t.b += dx*bX.b;
			t.c += dx*bX.c;
			t.d += dx*bX.d;
		}

		int[] verts = new int[vertices.size()];
		for (int i=0; i<vertices.size(); i++) {
			verts[i]=((Integer)vertices.elementAt(i)).intValue();
		}
		return verts;
	}

	/**
	* Check to see if a tetron is in the set!  Heart of program here...
	*/
	public int tetrabrotTest(Tetron t) {
		double atmp, btmp;
		int number = 0;
		z.setCoords(t);
		z.type=t.type;
		//Tetron ztemp = z;

		while ( (number != 400) && (z.mag() < 2)) {
			number++;
			Tetron.square(z);
			//Tetron.square(z);
			Tetron.sum(z,t);
			//Tetron.invert(z);
		}

		if (number == 400) {
			return -1;
		} else {
			return number;
		}
	}


	public boolean nearVertex(int x,int y, Vector vertices, float minToVertex) {
		int[] verts = new int[vertices.size()];
		for (int i=0; i<vertices.size(); i++) {
			verts[i]=((Integer)vertices.elementAt(i)).intValue();
		}

		int num = verts.length/2;
		for (int i=0; i<num; i++) {
			if ( (x-verts[2*i])*(x-verts[2*i])+
				 (y-verts[2*i+1])*(y-verts[2*i+1]) > minToVertex) return true;
		}
		return false;
	}














//////////////////////////////////////////////////
//						//
//	Method used when the applet start.	//
//						//
//////////////////////////////////////////////////

	public void start()
	{
	    Tkicker = new Thread(this);						// Make a thread...
	    Tkicker.start();							// ...start it...
	}


//////////////////////////////////////////////////
//						//
//	Method used when the applet stop.	//
//						//
//////////////////////////////////////////////////

	public void stop()
	{
	    Tkicker.stop();							// stop the thread.
	}


//////////////////////////////////////////////////////////
//							//
//	Method used by the applet to load images.	//
//							//
//////////////////////////////////////////////////////////

	private int[] loadImage(String imgfileN, int aspect[])
	{
		try								// Try catch protection code.
		{
			URL name = new URL(getDocumentBase(),imgfileN);		// Make a URL object with the path and picture name...
			return a3dUTIL.imgLoad(name,aspect,this);		// ...and store the image into an array.
		}
		catch(Exception e)						// Is there a problem?...
		{
			e.printStackTrace();					// ...print the stack...
			return null;						// ...and return null value...
		}
	}


//////////////////////////
//			//
//	Run Method.	//
//			//
//////////////////////////

	public void run()
	{
		while(Tkicker!=null)						// Main loop, it will goes on for all the duration of thread.
		{
			engine.beginScene();					// Method used to init the scene.
			engine.render();					// Method used to render the scene.
			engine.endScene();					// Method used to end the scene.

			try							// Try catch protection code.
            		{
	        	    Tkicker.sleep(1);					// Go to sleep the thread.
	        	}
	        	catch(Exception e)					// Problems? I don't think :)
	        	{
	        	}
		}
	}

}
