import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
* A thread that does the iteration and calculations for us
*
*/
public class VectraCalculator extends Thread {
	WritableRaster raster;
	int sizex,sizey;

	double startx, starty, width, height;
	int[] black;
	int[] red;
	public Object[] colors;
	JFrame parent;
	boolean done;

	/**
	* The basis vectors for rendering a 2D image...
	*/
	public Vectron bX, bY, offset, z;

	/**
	* Which group are we using
	*/
	public int type;

	public boolean switchType;

	/**
	* Set up colors in this constructor...
	*/
	public VectraCalculator(int sx, int sy, WritableRaster r, JFrame p, Object[] _colors) {
		type = 0;
		switchType=false;

		colors = _colors;

		parent = p;
		raster = r;
		sizex = sx;
		sizey = sy;

		black = new int[3];
		black[0] = 0;black[1] = 0;black[2] = 0;
		red = new int[3];
		red[0] = 255;red[1] = 0;red[2] = 0;

		done = false;
		z = new Vectron();
	}

	public void setBasis(Vectron t, Vectron d, Vectron o) {
		bX=t; bY=d; offset = o;
		bX.normalize(); bY.normalize();
		//System.out.println("set basis: " + t.getString() +
		//			" - " + d.getString() +" - "+o.getString());
	}

	public void setInsets(double sx, double sy, double w, double h) {
		startx = sx; starty = sy;
		width = w; height = h;
	}

	/**
	* Check to see if a Vectron is in the set!  Heart of program here...
	*/
	public int tetrabrotTest(Vectron t) {
		double atmp, btmp;
		int number = 0;
		z.setCoords(t);
		Vectron ztemp = new Vectron(1.0,0.0,0.0);

		while ( (number != 400) && (z.mag() < 4)) {
			number++;
			Vectron.product(z,ztemp);
			//Vectron.square(z);
			Vectron.sum(z,ztemp);
			//Vectron.invert(z);
			ztemp=z;
		}

		if (number == 400) {
			return -1;
		} else {
			return number;
		}
	}

	public boolean isDone() {
		return done;
	}

	/**
	* Use this to set done false before starting a run..
	*  otherwise some thread issues come up...
	*/
	public void setDone(boolean b) {
		done = b;
	}

	/**
	* This computes the fractal,
	*   at this point we have specified boundaries, axes, and offsets
	*   Compute the color for each Vectron, corresponding to each pixel.
	*/
	public void run() {
		done = false;

		double dx = width / sizex;
		double dy = height / sizey;

		// build the first Vectron from startx, starty, and basis vectors...
		Vectron t = new Vectron(startx*bX.a + starty*bY.a + offset.a,
		                      startx*bX.b + starty*bY.b + offset.b,
		                      startx*bX.c + starty*bY.c + offset.c);

		Vectron ztemp = new Vectron();
		//System.out.println("Calculating...");
		//System.out.println("startx: " + startx + " starty: " + starty);
		//System.out.println("width: " + width + " height: " + height);
		for (int x = 0 ; x < sizex ; x++) {
			// set "y" shift to zero in z Vectron
			//z.a -= starty;

			int it;
			ztemp.setCoords(t);
			for (int y = 0 ; y < sizey ; y++) {
				if ((it =tetrabrotTest(ztemp)) != -1) {
					// in the tetrabrot set.
					raster.setPixel(x,y,(int[]) colors[it/2]);
				} else {
					// not the tetrabrot set
					raster.setPixel(x,y,black);
				}
				// increment "y"
				ztemp.a += dy*bY.a;
				ztemp.b += dy*bY.b;
				ztemp.c += dy*bY.c;
			}
			if ( (x%10) == 0) {
				parent.repaint();

			}
			//increment "x"
			t.a += dx*bX.a;
			t.b += dx*bX.b;
			t.c += dx*bX.c;
		}
		parent.repaint();

		done = true;
		//System.out.println("Done!");
	}
}