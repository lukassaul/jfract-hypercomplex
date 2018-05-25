import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

/**
* Modified from "fraktal.java", thanks very much!
*
*  Amazin, a new fractal in < 2hrs!!!
*
*  L.Saul May 2003
*
*  Moving colors in here to cycle - june '03
*/
public class Tetrabrot extends JFrame implements MouseListener {

	private static int fr_height = 474;
	private static int fr_width = 632;

	BufferedImage fractal;
	WritableRaster raster;
	TetraCalculator calc;
	int mouse_x,mouse_y;

	private Object[] colors;

	double x;   // current startx of fractal x/y plane
	double y;    // current starty of fractal x/y plane
	double width; // the current width of fractal x/y plane
	double height;  // the current height of fractal x/y plane

	Random r;

	public void mouseClicked(MouseEvent e) {
		if (e.getButton()!=1) {
			System.out.println("x "+e.getX());
			System.out.println("y "+e.getY());
			System.out.println("tx "+translateX(e.getX()));
			System.out.println("ty "+translateY(e.getY()));
			System.out.println("rad " +
				Math.sqrt(translateX(e.getX())*translateX(e.getX())+
							translateY(e.getY())*translateY(e.getY())));
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if (e.getButton()==1) {
			mouse_x = e.getX();
			mouse_y = e.getY();
		}
	}

	public void mouseReleased(MouseEvent e) {
		//System.out.println(e.getButton()+"");
		if (e.getButton()==1) {
			double tmpx = translateX(mouse_x);
			double tmpy = translateY(mouse_y);
			double tmpx2  = translateX(e.getX());
			double tmpy2  = translateY(e.getY());

			//start x is lowest x
			if (tmpx<tmpx2) x=tmpx;
			else x=tmpx2;

			//start y is lowest y
			if (tmpy<tmpy2) y=tmpy;
			else y=tmpy2;

			width = Math.abs(tmpx2 - tmpx);
			height = Math.abs(tmpy2 - tmpy);

			while (!calc.isDone()) {
				try { Thread.sleep(1000); } catch (InterruptedException ex) {}
			}

			System.out.println("redrawing: " + x + " -- " + y);
			System.out.println(width + " -- " + height);

			calc = new TetraCalculator(fr_width,fr_height,raster, this,colors);
			setBasis();
			calc.setInsets(x,y,width,height);
			calc.start();
			save("output");
		}
	}

	/**
	* Return the x component in the fractal coords
	*
	*/
	public double translateX(int ix) {
		return x + (((double) ix) / fr_width) * width;
	}

	/**
	* Return the y component in the fractal coords
	*
	*/
	public double translateY(int iy) {
		return y + (((double) iy) / fr_height) * height;
	}

	public Tetrabrot (String args[]) {
		super("Tetrabrot");
		//setSize(640,480);


		String[] st = ImageIO.getWriterFormatNames();
		for (int i=0; i<st.length; i++) {
			System.out.println(st[i]);
		}

		setupColors();

		setSize(fr_width, fr_height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);


		r = new Random();


		Component graphics = new Component() {
			public void paint(Graphics g) {
				g.drawImage(fractal, 0,0,null);
			}
		};


		graphics.addMouseListener(this);
		getContentPane().add(graphics);

		fractal = new BufferedImage(fr_width, fr_height, BufferedImage.TYPE_INT_RGB);
		raster = fractal.getRaster();


		System.out.println("Topleft corner of raster: " + raster.getMinX() + "," + raster.getMinY());
		System.out.println("Size of raster: " + raster.getWidth() + "," + raster.getHeight());

		show();

//		makeMovie();

		// set first boundaries
		//another nice small brot
		//x = -1.81042;
		//y = -.0004;
		//width = .0008;
		//height = .0008;

		// way in there... on x axis still
		//x = -1.8103300974852;
		//y = -.000000000004;
		//width = .000000000008;
		//height = .000000000008;


		// a small 'brot, on axis
		//x = -1.2484485;
		//y = -.000001;
		//width = .000002;
		//height = .000002;

		// a small brot two zooms in - up and right
		//x=.4400;
		//y=-.3782;
		//width=.008;
		//height=.008;

		// way in there - out from 'brot above
		//x=       0.4427045525;
		//y=      -0.3781980985;
		//width=   0.0000000002;
		//height=  0.0000000002;

		// i vs. k zoom point
		//x  =   -.7427288401;
		//y  =   -.0077221352;
		//width  =.0000000004;
		//height =.0000000004;

		// another j vs. k zoom point
		//x  =   -.352554556265386;
		//y  =    .582110872532764;
		//width  =.0000000000004;
		//height =.0000000000004;

		//brot near Q circ. edge?
		//x=.3585;
		//y=-.686;
		//width = .002;
		//height = .002;


		heightenContrast();

		// perfect mandelbrot screen
		//x = -2.25;
		//y = -1.75;
		//width = 3.5;
		//height = 3.5;

		x=-2;
		y=-2;
		width=4.0;
		height=4.0;

		calc = new TetraCalculator(fr_width,fr_height,raster, this,colors);
		calc.setInsets(x,y,width,height);
		//System.out.println("Calculating rectangle: ("+x+","+y+") to (" + (x + width) + "," + (y + height)+")");

		setBasis();

		calc.start();

		//while (!calc.isDone()) {
		//	try { Thread.sleep(1000); } catch (InterruptedException ex) {}
		//}
		//save("test");

	}

	/**
	* Lets make a movie!
	*
	*/
	private void makeMovie() {
		String root = "d:\\jfract\\movie1\\noZoomSins_";

		int numFrames = 200;
		boolean zoom = false;

		// for zooming.. where are we headed?  target coordinates here...
		double tx  =   -.352554556265386;
		double ty  =    .582110872532764;
		double twidth  =.0000000000004;
		double theight =.0000000000004;

		// we need to use the center for zooming
		double txcenter = tx+twidth/2;
		double tycenter = ty+theight/2;

		// initial boundaries
		x=-2.5; // not used w/ zoom
		y=-2; // not used w/ zoom
		width=4;
		height=4;

		// we need some logs for zooming
		double lwidth = Math.log(width);
		double lheight = Math.log(height);
		double ltwidth = Math.log(twidth);
		double ltheight = Math.log(theight);

		// y=mx+b .. we have b (that's log initial)
		double mwidth = (ltwidth-lwidth)/numFrames;
		double mheight = (ltheight-lheight)/numFrames;

		System.out.println("slopes: " +mwidth+" "+mheight);

		// use these to shift through 4d space - set initial config here
		double[] dubs = new double[12];
		for (int i=0; i<12; i++) dubs[i]=0.0;
		dubs[7]=1.0;
		dubs[0]=1.0;

		heightenContrast();
//		heightenContrast();
		heightenContrast();

		// the frame loop
		for (int i=0; i<numFrames; i++) {
			calc = new TetraCalculator(fr_width,fr_height,raster, this, colors);
			calc.type = 2;  // make this a quaternion animation

			if (!zoom)	calc.setInsets(x,y,width,height);
			else {
				calc.setInsets(txcenter-Math.exp(mwidth*(double)i+lwidth)/2,
						   tycenter-Math.exp(mheight*(double)i+lheight)/2,
						   Math.exp(mwidth*i+lwidth),
						   Math.exp(mheight*i+lheight));

				System.out.println("doing: " + (txcenter-Math.exp(mwidth*(double)i+lwidth))/2);
			}

			// set the plane to draw
			calc.setBasis(new Tetron(dubs[0],dubs[1],dubs[2],dubs[3]),
						  new Tetron(dubs[4],dubs[5],dubs[6],dubs[7]),
						  new Tetron(dubs[8],dubs[9],dubs[10],dubs[11]) );

			// cycle the colors
			for (int j=0; j<1; j++) cycleColors();

			dubs[0] = Math.sin(2*i*Math.PI/numFrames);
			//dubs[7] = Math.cos(2*i*Math.PI/numFrames);
			dubs[1] = Math.cos(2*i*Math.PI/numFrames);
			//dubs[6] = Math.sin(2*i*Math.PI/numFrames);


			// set plane for next time
			//dubs[8]= Math.exp(mwidth*i+lwidth)*.2
			//			*Math.sin(24*i*Math.PI/numFrames);
			//dubs[10]=Math.exp(mwidth*i+lwidth)*.05
			//			*Math.sin(48*i*Math.PI/numFrames);
			//dubs[11]=Math.exp(mwidth*i+lwidth)*0.006*Math.sin(64*i*Math.PI/numFrames);
			//dubs[9]=Math.exp(mwidth*i+lwidth)*0.002*Math.sin(128*i*Math.PI/numFrames);

			calc.start();
			save(root+(i+10000));
			System.out.println("done frame: " + i);
		}
	}



	/**
	* This is storage for old shift states.. testing.. not for movie making
	*
	* (move desired setBasis call to end of list)
	*/
	private void setBasis() {

		// this is a cross between a square and a mandlebrot
		calc.setBasis(new Tetron(1.0,0.0,0.0,0.0),
					  new Tetron(0.0,1.0,0.0,1.0),
			  		  new Tetron(0.0,0.0,0.0,0.0));

		// this gives standard mandelbrot shifted by k
		calc.setBasis(new Tetron(1.0,0.0,0.0,0.0),
					  new Tetron(0.0,1.0,0.0,0.0),
					  new Tetron(0.0,0.0,0.0,0.1));

		// this gives vertical flat bottomed mandelbrot!
		calc.setBasis(new Tetron(0.0,1.0,1.0,0.0),
					  new Tetron(1.0,0.0,0.0,0.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// this gives weird truncated sideways mandelbrot
		calc.setBasis(new Tetron(1.0,1.0,1.0,0.0),
					  new Tetron(0.0,0.0,0.0,1.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// perfect vertical mandelbrot
		calc.setBasis(new Tetron(0.0,1.0,1.0,0.0),
					  new Tetron(1.0,0.0,0.0,1.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// Weird rectangle w/ bifurcation like fuzz...
		calc.setBasis(new Tetron(1.0,0.0,0.0,0.0),
					  new Tetron(0.0,0.0,0.0,1.0),
					  new Tetron(0.0,0.1,-0.1,0.0));
		// this gives a random fractal!
		calc.setBasis(new Tetron(nd(),nd(),nd(),nd()),
					  new Tetron(nd(),nd(),nd(),nd()),
					  new Tetron(nd()/10,nd()/10,nd()/10,nd()/10));

		// this gives a square - with fractal borders and stripes
		calc.setBasis(new Tetron(0.0,1.0,0.0,0.0),
					  new Tetron(0.0,0.0,1.0,0.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// Wow!  Distorted mandelbrot...
		calc.setBasis(new Tetron(0.0,1.0,0.0,0.0),
					  new Tetron(1.0,0.0,0.0,0.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// this is a square with a touch of mandlebrot
		calc.setBasis(new Tetron(1.0,0.0,0.0,0.0),
					  new Tetron(0.0,0.99,0.0,0.01),
			  		  new Tetron(0.0,0.0,0.0,0.0));

		// Another distorted square turning 'brot...
		calc.setBasis(new Tetron(1.0,0.1,0.0,0.0),
					  new Tetron(0.0,0.0,0.2,1.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// this gives standard mandelbrot
		calc.setBasis(new Tetron(0.0,1.0,0.0,0.0),
					  new Tetron(0.0,0.0,1.0,0.0),
					  new Tetron(0.0,0.0,0.0,0.0));

		// lets try changing the family...
		calc.type = 0;
		calc.switchType=false;
	}

	/**
	*  Save the image of this fractal to a png file
	*/
	public void save(String fileName) {
		while (!calc.isDone()) {
			try { Thread.sleep(1000); }
			catch (InterruptedException ex) {System.out.println("eh? interrupted geh");}
		}
		try {
			ImageIO.write(fractal,"jpg",new File(fileName+".jpg"));
		}
		catch (Exception e) {
			System.out.println("image io probs in fractal save() : ");
			e.printStackTrace();
		}
		//setVisible(false);
		//try {dispose(); this.finalize();}
		//catch (Throwable e) {e.printStackTrace();}
	}

	/*
	*  Get a BufferedImage of the panel plot
	*/
	public BufferedImage getImage() {
		try {
			//rpl_.draw();

			Robot r = new Robot();

			//Point p = jpane.getLocationOnScreen();

			//Dimension d = new Dimension(jpane.getSize());
			//d.setSize( d.getWidth()+d.getWidth()/2,
			//				d.getHeight()+d.getHeight()/2 );
			//Rectangle rect = new Rectangle(p,d);
			//BufferedImage bi = r.createScreenCapture(rect);

			//ImageIcon i = new ImageIcon(r.createScreenCapture(rect));

			//return r.createScreenCapture(rect);
			return null;
			//setVisible(false);
		//return i.getImage();
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private double nd() {
		return 2*r.nextDouble()-1.0;
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

	public static void main(String args[]) {
		new Tetrabrot(args);
	}
}

