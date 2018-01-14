import java.math.*;

/**
* This implements two commutative 4d hypercomplex groups..
*  one as defined by Clyde Davenport..
*
*  Not "officially" groups, as there are not always inverses.. i.e. more than one "zero"
*
*  Keeping all variables public here for simplicity for now..
*
*  And making sure to include a static type method for operations that changes first arg
*
*  Note - which "type" (i.e. which multiplication table) doesn't seem to change fractals...
*  L. Saul May 2003
*/
public class Tetron {

	
	public static double phi = (1.0+Math.sqrt(5))/2.0;
	public static double psi = (1.0-Math.sqrt(5))/2.0;
        public static double logphi = Math.log(phi);
	public static double logpsi = Math.log(0.0-psi);
	
	

	/**
	* There are two implementations.. switch between them with this
 	*/
	public int type;

	public static int HYPERCOMPLEX_1 = 0;
	public static int HYPERCOMPLEX_2 = 1;
	public static int QUATERNION = 2;

	/**
	* The four doubles that make up the tetron
	*/
	public double a,b,c,d;

	/**
	* Basis vectors:
	*/
	public static Tetron ONE = new Tetron(1.0,0.0,0.0,0.0);
	public static Tetron I = new Tetron(0.0,1.0,0.0,0.0);
	public static Tetron J = new Tetron(0.0,0.0,1.0,0.0);
	public static Tetron K = new Tetron(0.0,0.0,0.0,1.0);
	public static Tetron ZERO = new Tetron(0.0,0.0,0.0,0.0);

	public Tetron() {
		a=0.0; b=0.0; c=0.0; d=0.0;
		type = 0;
	}

	public Tetron(double _a, double _b, double _c, double _d) {
		a=_a; b=_b; c=_c; d=_d;
		type = 0;
	}

	/**
	* Multiplicatoin - here's the definition of the group
	*
	*/
	public Tetron product(Tetron t) {
		if (t.type==0) return new Tetron(
									a*t.a - b*t.b - c*t.c + d*t.d,
		  						    a*t.b + b*t.a + c*t.d + d*t.c,
								    a*t.c + b*t.d + c*t.a + d*t.b,
								    a*t.d - b*t.c - c*t.b + d*t.a);

		else if (t.type==1) return new Tetron(
							   a*t.a - b*t.b - c*t.c + d*t.d,
		  				       a*t.b + b*t.a - c*t.d - d*t.c,
							   a*t.c - b*t.d + c*t.a - d*t.b,
							   a*t.d + b*t.c + c*t.b + d*t.a);

		else if (t.type==2) return new Tetron(
							   a*t.a - b*t.b - c*t.c - d*t.d,
		  				       a*t.b + b*t.a + c*t.d - d*t.c,
							   a*t.c - b*t.d + c*t.a + d*t.b,
							   a*t.d + b*t.c - c*t.b + d*t.a);

		return null;
		// that wasn't so hard!!
	}

	/**
	* Use this to multiply without creating new object (by reference) (change 1st arg)
	*/
	public static void product(Tetron t, Tetron d) {
		double ta, tb, tc, td; // temp variables
		if (t.type==0) {
			ta = d.a*t.a - d.b*t.b - d.c*t.c + d.d*t.d;
			tb = d.a*t.b + d.b*t.a + d.c*t.d + d.d*t.c;
	    	tc = d.a*t.c + d.b*t.d + d.c*t.a + d.d*t.b;
			td = d.a*t.d - d.b*t.c - d.c*t.b + d.d*t.a;

			t.a=ta; t.b=tb; t.c=tc; t.d=td;
		}
		else  if (t.type==1) {
			ta = d.a*t.a - d.b*t.b - d.c*t.c + d.d*t.d;
			tb = d.a*t.b + d.b*t.a - d.c*t.d - d.d*t.c;
	    	tc = d.a*t.c - d.b*t.d + d.c*t.a - d.d*t.b;
			td = d.a*t.d + d.b*t.c + d.c*t.b + d.d*t.a;

			t.a=ta; t.b=tb; t.c=tc; t.d=td;
		}
		else  if (t.type==2) {
			ta = d.a*t.a - d.b*t.b - d.c*t.c - d.d*t.d;
			tb = d.a*t.b + d.b*t.a + d.c*t.d - d.d*t.c;
	    	tc = d.a*t.c - d.b*t.d + d.c*t.a + d.d*t.b;
			td = d.a*t.d + d.b*t.c - d.c*t.b + d.d*t.a;

			t.a=ta; t.b=tb; t.c=tc; t.d=td;
		}
	}

	/**
	* Of course a sum is easy... (and doesn't depend on type)
	*/
	public Tetron sum(Tetron t) {
		return new Tetron(a+t.a, b+t.b, c+t.c, d+t.d);
	}

	/**
	* sum by reference (change 1st arg)
	*/
	public static void sum(Tetron t, Tetron d) {
		t.a=d.a+t.a;   t.b=d.b+t.b;  t.c=d.c+t.c;  t.d=d.d+t.d;
	}

	/**
	* The additive inverse
	*/
	public Tetron inverse() {
		return new Tetron(-a,-b,-c,-d);
	}

	/**
	* invert a tetron by reference
	*/
	public static void invert(Tetron t) {
		t.a=-t.a; t.b=-t.b; t.c=-t.c;
	}

	/**
	* square a tetron
	*/
	public static void square (Tetron t) {
		product(t,t);
	}

	/**
	* product with a real number (by reference)..
	*/
	public static void product(Tetron t, double d) {
		t.setCoords(t.a*d, t.b*d, t.c*d, t.d*d);
	}

	/**
	* product with a real number
	*/
	public Tetron product(double dd) {
		Tetron t = new Tetron();
		t.setCoords(a*dd, b*dd, c*dd, d*dd);
		return t;
	}

	/**
	* Integer powers are important first...
	*
	*/
	public static Tetron pow (Tetron t, int pow) {
		if (pow==1) return t;
		if (pow==0) return ONE;
		if (pow<0) return null;
		Tetron temp = new Tetron();
		temp.setCoords(t);
		for (int i=1; i<pow; i++) {
			product(temp,t);
		}
		return temp;
	}

	/**
	* Now we can do exponentiation..
	*/
	public static Tetron exp (Tetron t) {
		Tetron temp = new Tetron();
		for (int i=0; i<15; i++) {
			sum( temp, pow(t,i).product(1.0/fact(i)) );
		}
		return temp;
	}

	/**
	* And logarithms..
	*/
	public static Tetron log (Tetron t) {
		Tetron temp = new Tetron();
		Tetron tMinus1 = new Tetron(t.a-1.0, t.b, t.c, t.d);
		double sign = -1.0;
		for (int i=1; i<20; i++) {
			sum( temp, pow(tMinus1,i).product(sign/(double)(i)) );
			sign=sign * -1.0;
			System.out.println("sign,temp"+"\t"+sign+"\t"+temp+"");
		}
		return temp;
	}


	/**
	* Lets do a fibonacci series operation.. find the "Qth" term of the series 
	*/
        public static Tetron fibonacci(Tetron in) {
		
		Tetron tbr = new Tetron();
		Tetron p1, p2, p3;	
		p1 = Tetron.exp(in.product(logphi));
		p2 = Tetron.exp(in.product(logpsi));
		p3 = Tetron.exp(in.product(ONE.inverse()));
		System.out.println("in fib: " + p1.toString() + "\t" + p2.toString());	
		tbr = p1.sum(p2.product(p3).inverse());
		return tbr.product(1.0/Math.sqrt(5.0));
	}
		
		

	/**
	* Magnitude of a tetron
	*/
	public static double mag(Tetron t) {
		return Math.sqrt(t.a*t.a+t.b*t.b+t.c*t.c+t.d*t.d);
	}

	public double mag() {
		return mag(this);
	}

	/**
	* Normalize a tetron
	*/
	public void normalize() {
		double mag = mag();
		a=a/mag; b=b/mag; c=c/mag; d=d/mag;
	}

	public String toString() {
		return new String(a+" "+b+" "+c+" "+d);
	}

	public void setCoords(Tetron t) {
		a=t.a; b=t.b; c=t.c; d=t.d;
	}

	public void setCoords(double d1, double d2, double d3, double d4) {
		a=d1; b=d2; c=d3; d=d4;
	}

	/**
	* The famous recursive factorial..
	*/
	private static int fact(int aa) {
		if (aa==0) return 1;
		else return aa*fact(aa-1);
	}

	/**
	* For testing of course
	*/
	public static final void main(String[] args) {
		System.out.println("IPI = " + I.product(Math.PI));
		System.out.println("IPI^3 = " + pow(I.product(Math.PI),3));
		System.out.println("k^2 = " + pow(K,2));
		System.out.println("j^3 = " + pow(J,3));
		System.out.println("k^3 = " + pow(K,3));
		// test the exp
		System.out.println("e^1 = " + exp(ONE));
		System.out.println("e^iPi = " + exp(I.product(Math.PI)));
		System.out.println("log(1) = " + log(ONE));
		System.out.println("log(-1) = " + log(ONE.inverse()));
		System.out.println("log(E) = " + log(new Tetron(Math.E,0.0,0.0,0.0)));
		System.out.println("logphi,psi: " + logphi + "\t" + logpsi+ "\n");

		// start Fibonacci test
		file f = new file("fibout.txt");  f.initWrite(false);
		Tetron t = Tetron.ONE;
		Tetron res = Tetron.ONE;
		for (double i = 0.0; i<10.0; i=i+0.1) {
			t.a = t.a + 0.1;			
			res = Tetron.fibonacci(t);			
			f.write(t.a + "\t" + Tetron.mag(res) + "\t" + res.a + "\n");
		}
		f.closeWrite();

	}
}



