

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.event.*;
import idx3d.*;
import java.net.URL;


/**
*    This class is the main GUI -
*      for application running aparently
*      because of fucked up JApplet status bullshit
*   Lukas Saul   Jan 2003
*
*/
public class Tetron3D extends JFrame implements ActionListener{
	// add new variables to bottom of list
	public String user_dir = System.getProperty("user.dir");
	public String file_sep = System.getProperty("file.separator");
	public String save_dir = user_dir; // for now...
	public String CRLF = System.getProperty("line.separator");
	public String backupFileName = ".backup";
	public URL aurl;


	// GUI Objects...
	private Container contentPane;
	private JPanel buttonPanel, goodsPanel;
	private JButton newButton, serverButton, undoButton, piecesButton;
	private JLabel statusLabel;
	private JPanel idx3dPanel;
	private Tetron3Dcomponent t3DComponent;
	private idx3d_Object theObject;

	private Tetron z;

	//

	/**
	* the constructor for the GUI.  Constructing this object starts the program.
	*
	*/
	public Tetron3D () {
		if (user_dir == null) user_dir = "";
		if (save_dir == null) save_dir = "";

		o(save_dir);

		try {
			aurl = new URL("file://"+user_dir);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("Tetron3D");
		contentPane = getContentPane();


		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				//saveToFile();
				System.exit(0);
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				t3DComponent.processKeyEvent(e);

			}
		});

		z=new Tetron();

		buildObject();  // ah, delegation..  get to work lackeys!

		t3DComponent=new Tetron3Dcomponent(this);

		contentPane.add(t3DComponent,"Center");

		setSize(500,500);

		show();
	}

	/**
	*   Here's where all the button presses and actions go...
	*/
	public void actionPerformed(ActionEvent e) {
		Object source = e.getActionCommand();

	}


	/**
	*  OK, this is for the GUI who comes looking for something to display
	*   Redraw everything.
	*/
	public void buildAll(idx3d_Scene scene) {
		//this.scene = scene;
		System.out.println("about to add everything to the scene");
		try {
			scene.environment.setBackground(
							idx3d_TextureFactory.CHECKERBOARD(160,120,2,0x000000,0x999999));
			//scene.addLight("Light1",new idx3d_Light(new idx3d_Vector(0.2f,0.2f,1f),0xFFFFFF,320,80));
			//scene.addLight("Light2",new idx3d_Light(new idx3d_Vector(-1f,-1f,1f),0xFFCC99,100,40));
			scene.addLight("Light3",new idx3d_Light(new idx3d_Vector(5f,5f,5f),0xFFFFFF,320,80));
			//scene.addLight("Light4",new idx3d_Light(new idx3d_Vector(-5f,-5f,5f),0xFFFFFF,320,80));
			//scene.addLight("Light5",new idx3d_Light(new idx3d_Vector(-5f,5f,-5f),0xFFFFFF,320,80));
			//scene.addLight("Light6",new idx3d_Light(new idx3d_Vector(-5f,-5f,-5f),0xFFFFFF,320,80));

			o("about to add idx3d_material");
			scene.addMaterial("Chrome",new idx3d_Material(aurl,"chrome.material"));
			//scene.addMaterial("Chrome",new idx3d_Material("d:\\jfract\\chrome.material"));
			//scene.addMaterial("Chrome",new idx3d_Material(1));
			o("scene material and lights added.. now for the object");
			scene.addObject("the object",theObject);
			for (int i=0; i<scene.objects;i++)
					idx3d_TextureProjector.projectFrontal(scene.object[i]);

			scene.object("the object").setMaterial(scene.material("Chrome"));

			//scene.normalize();
		}
		catch (Exception e) {e.printStackTrace();}
	}

	private void buildObject() {
		theObject = new idx3d_Object();



		// first a test.. lets build a single layer i.e. mandelbrot..
		int slices = 10;
		int sliceHeight = 20;




		// OK - build the object now bitch..
		// first lets set up the full vertex array
		Vector allVertices = new Vector();
		o("building vertex array .. tetrabrot computation");
		for (int i=0; i<slices; i++) {
			System.out.println("doing slice " + i);
			allVertices.addElement( getVertices(-2.0f,-1.0f,4.0f,2.0f,   500,500,
					new Tetron(1.0,0.0,0.0,0.0),
					new Tetron(0.0,1.0-((double)i/(double)slices),0.0,0.0+((double)i/(double)slices)),
					new Tetron(0.0,0.0,0.0,0.0),120,200) );
		}

		// OK, we now have a vector of arrays.. each of 2D integer vertexes..
		// we have to build up 3D arrays first..
		o("built 2D arrays..  " + allVertices.size());

		int[] sliceSizes = new int[slices];
		int maxSize = 0;
		for (int i=0; i<allVertices.size(); i++) {
			sliceSizes[i]=((int[])allVertices.elementAt(i)).length/2;
			if (sliceSizes[i]>maxSize) maxSize = sliceSizes[i];
		}

		for (int i=0; i<sliceSizes.length; i++) {
			o("" + sliceSizes[i]);
		}

		// finally we can cast away our vector
		int maxSize3D = maxSize*3;
		int[][] allVerts = new int[slices][maxSize3D];

		// fill it with zeros
		for (int i=0; i<slices; i++) {
			for (int j=0; j<maxSize3D; j++) {
				allVerts[i][j] = 0;
			}
		}

		// now put in the data adding 3rd dimension
		for (int i=0; i<slices; i++) {
			int[] temp = (int[])allVertices.elementAt(i);
			for (int j=0; j<(temp.length/2); j++) { // looping through # of vertices
				allVerts[i][3*j]    = temp[2*j];
				allVerts[i][3*j+1]  = temp[2*j+1];
				allVerts[i][3*j+2]  = sliceHeight*i;
			}
		}

		// list 'em
		/*for (int i=0; i<slices; i++) {
			for (int j=0; j<sliceSizes[i]; j++) { // looping through # of vertices
				o("vertex: "+allVerts[i][3*j]+" "+allVerts[i][3*j+1]+" "+allVerts[i][3*j+2]);
			}
		}*/

		o("added third dimension.. about to start triangle searches");
		//okay, supposedly we have our 3d array of vertices...
		// but perhaps we should build triangles as we add 3dvertobjects..
		int vIndex = 0;
		for (int i=0; i<slices-1; i++) {
			for (int j=0; j<sliceSizes[i]; j++) {
				theObject.addVertex((float)allVerts[i][3*j],
									(float)allVerts[i][3*j+1],
				 					(float)allVerts[i][3*j+2]);
				vIndex++;

				int[] nextVert = getNearestVertex3D(allVerts[i][3*j],
						allVerts[i][3*j+1], allVerts[i][3*j+2], allVerts[i], sliceSizes[i]);

				theObject.addVertex((float)nextVert[0],(float)nextVert[1],(float)nextVert[2]);
				vIndex++;

				nextVert = getNearestVertex3D(allVerts[i][3*j],
						allVerts[i][3*j+1], allVerts[i][3*j+2], allVerts[i+1], sliceSizes[i+1]);


				theObject.addVertex((float)nextVert[0],(float)nextVert[1],(float)nextVert[2]);
				vIndex++;

				theObject.addTriangle(vIndex-1, vIndex-2, vIndex-3);
				theObject.addTriangle(vIndex-1, vIndex-3, vIndex-2);

/*

				nextVert = getSecondNearestVertex3D(allVerts[i][3*j],
						allVerts[i][3*j+1], allVerts[i][3*j+2], allVerts[i], sliceSizes[i]);

				theObject.addVertex((float)nextVert[0],(float)nextVert[1],(float)nextVert[2]);
				vIndex++;

				nextVert = getSecondNearestVertex3D(allVerts[i][3*j],
						allVerts[i][3*j+1], allVerts[i][3*j+2], allVerts[i+1], sliceSizes[i+1]);

				theObject.addVertex((float)nextVert[0],(float)nextVert[1],(float)nextVert[2]);
				vIndex++;

				theObject.addTriangle(vIndex-1, vIndex-2, vIndex-5);
				theObject.addTriangle(vIndex-1, vIndex-5, vIndex-2);

				theObject.addTriangle(vIndex-1, vIndex-3, vIndex-5);
				theObject.addTriangle(vIndex-1, vIndex-5, vIndex-3);
*/

				//theObject.meshSmooth();
			}
			o("computed triangles in slice " + i);
		}

		//theObject.setMaterial(new idx3d_Material("chrome.material"));
	}


	private int[] getNearestVertex3D(int x, int y, int z, int[] v, int lSize) {
		int[] tbr = new int[3];
		// we seek nearest vertex to x,y,z in level v of size lSize
		boolean found = false;
		int min = 10000000;
		for (int i=0; i<lSize; i++) {
			int tt = (x-v[i*3])*(x-v[i*3])+(y-v[i*3+1])*(y-v[i*3+1])+(z-v[i*3+2])*(z-v[i*3+2]);
			if (tt!=0 & tt < min) {
				tbr[0]=v[i*3]; tbr[1]=v[i*3+1]; tbr[2]=v[i*3+2];
				min = tt;
				found = true;
			}
		}
		//o("get nearest test: input: " + x + " " + y + " " + z);
		//o("lSize: " + lSize + " found one? " + found);
		//o("result: " + tbr[0] + " " + tbr[1] + " " + tbr[2]);
		return tbr;
	}

	private int[] getSecondNearestVertex3D(int x, int y, int z, int[] v, int lSize) {
		//int[] tbr = new int[3];
		int[] tbr2 = new int[3];

		// we seek nearest vertex to x,y,z in level v of size lSize
		boolean found = false;
		int min = 10000000;
		int min2 = 10000000;
		for (int i=0; i<lSize; i++) {
			int tt = (x-v[i*3])*(x-v[i*3])+(y-v[i*3+1])*(y-v[i*3+1])+(z-v[i*3+2])*(z-v[i*3+2]);
			if (tt!=0 & tt < min) {
				//tbr[0]=v[i*3]; tbr[1]=v[i*3+1]; tbr[2]=v[i*3+2];
				min = tt;
				found = true;
			}
			else if (tt!=0 & tt<min2 & tt>min) {
				tbr2[0]=v[i*3]; tbr2[1]=v[i*3+1]; tbr2[2]=v[i*3+2];
				min2 = tt;
			}
		}
		//o("get nearest test: input: " + x + " " + y + " " + z);
		//o("lSize: " + lSize + " found one? " + found);
		//o("result: " + tbr[0] + " " + tbr[1] + " " + tbr[2]);
		return tbr2;
	}

	public static void main(String[] args) {
		Tetron3D t = new Tetron3D();
	}

	/**
	* Check to see if a tetron is in the set!  Heart of program here..
	*  Modified Oct. 2003 - input max iterations -
	*   note: returns -1 for still converged
	*/
	public int tetrabrotTest(Tetron t, int max) {
		double atmp, btmp;
		int number = 0;
		z.setCoords(t);
		z.type=t.type;
		//Tetron ztemp = z;

		while ( (number != max) && (z.mag() < 2)) {
			number++;
			Tetron.square(z);
			//Tetron.square(z);
			Tetron.sum(z,t);
			//Tetron.invert(z);
		}

		if (number == max) return -1;
		else return number;
	}


	/**
	* for 2D near vertex checking..
	*/
	public boolean nearVertex(int x,int y, Vector vertices, int minToVertex) {
		int[] verts = new int[vertices.size()];
		for (int i=0; i<vertices.size(); i++) {
			verts[i]=((Integer)vertices.elementAt(i)).intValue();
		}

		int num = verts.length/2;
		for (int i=0; i<num; i++) {
			if ( (x-verts[2*i])*(x-verts[2*i])+
				 (y-verts[2*i+1])*(y-verts[2*i+1]) < minToVertex) return true;
		}
		return false;
	}

	/**
	* Use this to get an array of vertices on a "surface" of a tetrabrot
	*		// define tetrabrot 2D center and radius (3 floats)  (DOMAIN)
	*		// # of pixels in radiant (1 ints)
	*       // tetrabrot offsets and basis axes (3 4vectors)
	*		// number of longitude lines (points on slice) to compute (int)
	*       // max iterations (int)
	*/
	public int[] getVertices (double startx, double starty, double radius,
				int pointsPerRadiant,  Tetron bX, Tetron bY, Tetron offset,
				int pointsPerSlice, int max)  {

		float[] vertices = new float[numPoints];
		int vertIndex = 0;

		// build the center Tetron from startx, starty, and basis vectors...
		Tetron t = new Tetron(startx*bX.a + starty*bY.a + offset.a,
		                      startx*bX.b + starty*bY.b + offset.b,
		                      startx*bX.c + starty*bY.c + offset.c,
		                      startx*bX.d + starty*bY.d + offset.d);

		Tetron ztemp = new Tetron();
		//System.out.println("Calculating...");
		//System.out.println("startx: " + startx + " starty: " + starty);
		//System.out.println("width: " + width + " height: " + height);

		float dr = radius/pointsPerRadiant;

		// instead of doing each pixel we are going to emerge from the center with polar coords..
		for (double theta=0; theta<=2.0*Math.PI; theta+= 2.0*Math.PI/pointsPerSlice) {
			// at each theta we move out a radiant
			// Actually, moving IN is more efficient.. less recursions necessary!
			int lastVertIndex = vertIndex;
			for (double r=radius; r>=0.0; r-=dr) {
				// decrement "r", but theta is constant
				// we should have ztemp at end of radiant to start out
				double x = r*Math.cos(theta));
				double y = r*Math.sin(theta));

				ztemp.setCoords(t); // start out at center, and add the radiant
				ztemp.a += y*bY.a + x*bX.a;
				ztemp.b += y*bY.b + x*bX.b;
				ztemp.c += y*bY.c + x*bX.c;
				ztemp.d += y*bY.d + x*bX.d;

				int it = 0;
				if ((it=tetrabrotTest(ztemp,max)) == -1) {
					// OK we have passed the surface..
					// save the vertex in cartesian 2D coordinates..
					vertices[vertIndex]=(float)(startx+x);
					vertIndex++;
					vertices[vertIndex]=(float)(starty+y);
					vertIndex++;
				}										// not in the tetrabrot set.
			}
			if (lastVertIndex==vertIndex) {
				// we didn't find a vertex on the last radiant!
				o("reached center without finding set!");
				if (vertIndex!=0) {
					vertices[vertIndex]=vertices[vertIndex-2];
					vertices[vertIndex+1]=vertices[vertIndex-1];
					vertIndex++; vertIndex++;
				}
				else {
					vertices[vertIndex]=0.0f; vertIndex++;
					vertices[vertIndex]=0.0f; vertIndex++;
				}
			}
		}

		System.out.println("found vertices in this level: " + vertices.length/2);
		for (int i=0; i<verts.length/2; i++) {
		//	o("Vertex: " + verts[2*i] + " " + verts[2*i+1]);
		}
		return vertices;
	}



	private void o(String s) {
		System.out.println(s);
	}
}




/*
    OLD ROUTINE..



	/**
	* Use this to get an array of vertices on a plane of a tetrabrot
	*		// define tetrabrot 2D plane edges (4 floats)
	*		// define # of pixels in width and height (2 ints)
	*       // define tetrabrot offsets and basis axes (3 4vectors)
	*		// define minimum distance squared twixt vertices (int)
	*		// define minimum distance from brot to vertex (int - iterations to exit)
	public int[] getVertices (float startx, float starty, float width, float height,
					int sizex, int sizey,
				Tetron bX, Tetron bY, Tetron offset, int minToVertex, int minToSet)  {

		Vector vertices = new Vector();
		int[] vA ={0,0};
		int[] vB ={0,0};
		int[] vC ={0,0};
		int[] vD ={0,0};
		int[] vE ={0,0};
		int[] vF ={0,0};

		// cache it up bro
		boolean firstVertex = true;

		double dx = width / sizex;
		double dy = height / sizey;

		// build the first Tetron from startx, starty, and basis vectors...
		Tetron t = new Tetron(startx*bX.a + starty*bY.a + offset.a,
		                      startx*bX.b + starty*bY.b + offset.b,
		                      startx*bX.c + starty*bY.c + offset.c,
		                      startx*bX.d + starty*bY.d + offset.d);

		Tetron ztemp = new Tetron();
		//System.out.println("Calculating...");
		//System.out.println("startx: " + startx + " starty: " + starty);
		//System.out.println("width: " + width + " height: " + height);
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
							firstVertex=false;
							vA[0]=x; vA[1]=y;
							vB[0]=x; vB[1]=y;
							vC[0]=x; vC[1]=y;
							vD[0]=x; vD[1]=y;
							vE[0]=x; vE[1]=y;
							vF[0]=x; vF[1]=y;
							vertices.add(new Integer(x));
							vertices.add(new Integer(y));
						}
						else if ( (x-vA[0])*(x-vA[0])+(y-vA[1])*(y-vA[1]) > minToVertex &
						(x-vB[0])*(x-vB[0])+(y-vB[1])*(y-vB[1]) > minToVertex &
						(x-vC[0])*(x-vC[0])+(y-vC[1])*(y-vC[1]) > minToVertex  &
						(x-vD[0])*(x-vD[0])+(y-vD[1])*(y-vD[1]) > minToVertex  &
						(x-vE[0])*(x-vE[0])+(y-vE[1])*(y-vE[1]) > minToVertex  &
						(x-vF[0])*(x-vF[0])+(y-vF[1])*(y-vF[1]) > minToVertex ) {

							// cantidate second vertex .. check all now
							if (!nearVertex(x,y,vertices,minToVertex)) {
								// redo cache
								vA[0]=vB[0]; vA[1]=vB[1];
								vB[0]=vC[0]; vB[1]=vC[1];
								vC[0]=vD[0]; vC[1]=vD[1];
								vE[0]=vE[0]; vD[1]=vE[1];
								vE[0]=vF[0]; vE[1]=vF[1];
								vF[0]=x; vF[1]=y;

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
		System.out.println("found vertices in this level: " + verts.length/2);
		for (int i=0; i<verts.length/2; i++) {
		//	o("Vertex: " + verts[2*i] + " " + verts[2*i+1]);
		}
		return verts;
	}
*/