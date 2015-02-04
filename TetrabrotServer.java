import java.awt.*;
import java.awt.image.*;

/**
* A thread that does the iteration and calculations for us
*
*/
public class TetrabrotServer implements ImageServerInterface {
	WritableRaster raster;


	int[] black;
	int[] red;
	public Object[] colors;
	boolean done;

	/**
	* The basis vectors for rendering the 2D image...
	*/
	private Tetron bX, bY, offset, z;

	/**
	* Which group are we using
	*/
	private int type;

	private boolean switchType;

	/**
	* Set up colors in this constructor...
	*/
	public TetraBrotServer() {
		switchType=false;
		setupColors()



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

	private void setupColors() {
		colors = new Object[200];
		int c = 255;
		for (int i = 0 ; i < 50 ; i++) {
			int[] color = new int[3];
			color[0] = 0; color[1] = 255 - c; color[2] = c;
			colors[i] = color;
			c -= 5;
		}


		c = 255;
		for (int i = 50 ; i < 100 ; i++) {
			int[] color = new int[3];
			color[0] = 255 - c; color[1] = 255; color[2] = 0;
			colors[i] = color;
			c -= 5;
		}

		c = 255;
		for (int i = 100 ; i < 150 ; i++) {
			int[] color = new int[3];
			color[0] = c; color[1] = c; color[2] = 255 - c;
			colors[i] = color;
			c -= 5;
		}

		c = 255;
		for (int i = 150 ; i < 200 ; i++) {
			int[] color = new int[3];
			color[0] = 255 - c; color[1] = 0; color[2] = 255;
			colors[i] = color;
			c -= 5;
		}
	}

	/**
	* Only take every 4th color
	*
	*/
	private void heightenContrast() {
		for (int i=0; i<50; i++) {
			colors[i]=colors[i*4];
		}
		for (int i=50; i<100; i++) {
			colors[i]=colors[i-50];
		}
		for (int i=100; i<200; i++) {
			colors[i]=colors[i-100];
		}
	}

	/**
	* OK, lets cycle the colors
	* for now just simple cycle by one
	*/
	public void cycleColors() {
		int[] temp = (int[])colors[colors.length-1];
		for (int i=colors.length-1; i>0; i--) {
			colors[i]=colors[i-1];
		}
		colors[0]=temp;
	}

	public void setInsets(double sx, double sy, double w, double h) {
		startx = sx; starty = sy;
		width = w; height = h;
	}



}