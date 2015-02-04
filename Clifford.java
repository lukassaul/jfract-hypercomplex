/**
*  Let's do this fractal shit with eight dimensional beasts
*
*
*
*
*  L. Saul June 2007
*/
public class Clifford {

	/**
	* There are two implementations.. switch between them with this
 	*/
	public int type;

	public static int HYPERCOMPLEX_1 = 0;
	public static int HYPERCOMPLEX_2 = 1;
	public static int QUATERNION = 2;

	/**
	* The four doubles that make up the Clifford
	*/
	public double a,b,c,d;

	/**
	* Basis vectors:
	*/
	public static Clifford ONE = new Clifford(1.0,0.0,0.0,0.0, 0.0,0.0,0.0,0.0);
	public static Clifford I = new Clifford(0.0,1.0,0.0,0.0, 0.0,0.0,0.0,0.0);
	public static Clifford J = new Clifford(0.0,0.0,1.0,0.0, 0.0,0.0,0.0,0.0);
	public static Clifford K = new Clifford(0.0,0.0,0.0,1.0, 0.0,0.0,0.0,0.0);
	public static Clifford L = new Clifford(0.0,1.0,0.0,0.0, 0.0,0.0,0.0,0.0);
	public static Clifford M = new Clifford(0.0,0.0,1.0,0.0, 0.0,0.0,0.0,0.0);
	public static Clifford N = new Clifford(0.0,0.0,0.0,1.0, 0.0,0.0,0.0,0.0);
	public static Clifford ZERO = new Clifford(0.0,0.0,0.0,0.0, 0.0,0.0,0.0,0.0);

	public Clifford() {
		a=0.0; b=0.0; c=0.0; d=0.0;
		type = 0;
	}

	public Clifford(double _a, double _b, double _c, double _d) {
		a=_a; b=_b; c=_c; d=_d;
		type = 0;
	}

	/**
	* Multiplicatoin - here's the definition of the group
	*
	*/
	public Clifford product(Clifford t) {
		if (t.type==0) return new Clifford(
									a*t.a - b*t.b - c*t.c + d*t.d,
		  						    a*t.b + b*t.a + c*t.d + d*t.c,
								    a*t.c + b*t.d + c*t.a + d*t.b,
								    a*t.d - b*t.c - c*t.b + d*t.a);

		else if (t.type==1) return new Clifford(
							   a*t.a - b*t.b - c*t.c + d*t.d,
		  				       a*t.b + b*t.a - c*t.d - d*t.c,
							   a*t.c - b*t.d + c*t.a - d*t.b,
							   a*t.d + b*t.c + c*t.b + d*t.a);

		else if (t.type==2) return new Clifford(
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
	public static void product(Clifford t, Clifford d) {
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
	public Clifford sum(Clifford t) {
		return new Clifford(a+t.a, b+t.b, c+t.c, d+t.d);
	}

	/**
	* sum by reference (change 1st arg)
	*/
	public static void sum(Clifford t, Clifford d) {
		t.a=d.a+t.a;   t.b=d.b+t.b;  t.c=d.c+t.c;  t.d=d.d+t.d;
	}

	/**
	* The additive inverse
	*/
	public Clifford inverse() {
		return new Clifford(-a,-b,-c,-d);
	}

	/**
	* invert a Clifford by reference
	*/
	public static void invert(Clifford t) {
		t.a=-t.a; t.b=-t.b; t.c=-t.c;
	}

	/**
	* square a Clifford
	*/
	public static void square (Clifford t) {
		product(t,t);
	}

	/**
	* product with a real number (by reference)..
	*/
	public static void product(Clifford t, double d) {
		t.setCoords(t.a*d, t.b*d, t.c*d, t.d*d);
	}

	/**
	* product with a real number
	*/
	public Clifford product(double dd) {
		Clifford t = new Clifford();
		t.setCoords(a*dd, b*dd, c*dd, d*dd);
		return t;
	}

	/**
	* Integer powers are important first...
	*
	*/
	public static Clifford pow (Clifford t, int pow) {
		if (pow==1) return t;
		if (pow==0) return ONE;
		if (pow<0) return null;
		Clifford temp = new Clifford();
		temp.setCoords(t);
		for (int i=1; i<pow; i++) {
			product(temp,t);
		}
		return temp;
	}

	/**
	* Now we can do exponentiation..
	*/
	public static Clifford exp (Clifford t) {
		Clifford temp = new Clifford();
		for (int i=0; i<15; i++) {
			sum( temp, pow(t,i).product(1.0/fact(i)) );
		}
		return temp;
	}

	/**
	* And logarithms..
	*/
	public static Clifford log (Clifford t) {
		Clifford temp = new Clifford();
		Clifford tMinus1 = new Clifford(t.a-1.0, t.b, t.c, t.d);
		double sign = 1;
		for (int i=1; i<20; i++) {
			sum( temp, pow(tMinus1,i).product(sign/(double)i) );
			sign*=-1.0;
			System.out.println(temp+"");
		}
		return temp;
	}

	/**
	* Magnitude of a Clifford
	*/
	public static double mag(Clifford t) {
		return Math.sqrt(t.a*t.a+t.b*t.b+t.c*t.c+t.d*t.d);
	}

	public double mag() {
		return mag(this);
	}

	/**
	* Normalize a Clifford
	*/
	public void normalize() {
		double mag = mag();
		a=a/mag; b=b/mag; c=c/mag; d=d/mag;
	}

	public String toString() {
		return new String(a+" "+b+" "+c+" "+d);
	}

	public void setCoords(Clifford t) {
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
		System.out.println("log(E) = " + log(new Clifford(Math.E,0.0,0.0,0.0)));

	}
}

/*

a*b
	b.e 	b.e1 	b.e2 	b.e3 	b.e12 	b.e31 	b.e23 	b.e123
a.e 	1 	e1 	e2 	e3 	e12 	e31 	e23 	e123
a.e1 	e1 	1 	e12 	-e31 	e2 	-e3 	e123 	e23
a.e2 	e2 	-e12 	1 	e23 	-e1 	e123 	e3 	e31
a.e3 	e3 	e31 	-e23 	1 	e123 	e1 	-e2 	e12
a.e12 	e12 	-e2 	e1 	e123 	-1 	e23 	-e31 	-e3
a.e31 	e31 	e3 	e123 	-e1 	-e23 	-1 	e12 	-e2
a.e23 	e23 	e123 	-e3 	e2 	e31 	-e12 	-1 	-e1
a.e123 	e123 	e23 	e31 	e12 	-e3 	-e2 	-e1 	-1

*/