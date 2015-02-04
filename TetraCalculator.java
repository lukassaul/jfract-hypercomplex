import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
* A thread that does the iteration and calculations for us
*
*/
public class TetraCalculator extends Thread {
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
	public Tetron bX, bY, offset, z;

	/**
	* Which group are we using
	*/
	public int type;

	public boolean switchType;

	/**
	* Set up colors in this constructor...
	*/
	public TetraCalculator(int sx, int sy, WritableRaster r, JFrame p, Object[] _colors) {
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
		z = new Tetron();
	}

	public void setBasis(Tetron t, Tetron d, Tetron o) {
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
	* Check to see if a tetron is in the set!  Heart of program here...
	*/
	public int tetrabrotTest(Tetron t) {
		double atmp, btmp;
		int number = 0;
		z.setCoords(t);
		z.type=type;
		//Tetron ztemp = z;

		while ( (number != 400) && (z.mag() < 2)) {
			number++;
			Tetron.square(z);
			//Tetron.square(z);
			Tetron.sum(z,t);
			//Tetron.invert(z);
			if (switchType) switchType(z);
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

	private void switchType(Tetron tt) {
		//if (tt.type==1) tt.type=0;
		//if (tt.type==0) tt.type=1;
		if (tt.type==2) tt.type=1;
		else tt.type=2;
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
	*   Compute the color for each tetron, corresponding to each pixel.
	*/
	public void run() {
		done = false;

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
				ztemp.d += dy*bY.d;
			}
			if ( (x%10) == 0) {
				parent.repaint();

			}
			//increment "x"
			t.a += dx*bX.a;
			t.b += dx*bX.b;
			t.c += dx*bX.c;
			t.d += dx*bX.d;
		}
		parent.repaint();

		done = true;
		//System.out.println("Done!");
	}
}