/**
* Cross product fractal anyone?
*
*/
public class Vectron {


	/**
	* The three doubles that make up the Vectron
	*/
	public double a,b,c;


	public Vectron() {
		a=0.0; b=0.0; c=0.0;
	}

	public Vectron(double _a, double _b, double _c) {
		a=_a; b=_b; c=_c;
	}

	/**
	* Multiplicatoin - here's the definition of the group
	*
	*/
	public Vectron product(Vectron t) {
		return new Vectron(
			b*t.c - c*t.b,
			c*t.a - a*t.c,
			a*t.b - b*t.a);

		// that wasn't so hard!!
	}

	/**
	* Use this to multiply without creating new object (by reference) (change 1st arg)
	*/
	public static void product(Vectron t, Vectron d) {
		double ta, tb, tc; // temp variables
		ta = d.b*t.c - d.c*t.b;
		tb = d.c*t.a - d.a*t.c;
		tc = d.a*t.b - d.b*t.a;

		t.a=ta; t.b=tb; t.c=tc;
	}

	/**
	* Of course a sum is easy... (and doesn't depend on type)
	*/
	public Vectron sum(Vectron t) {
		return new Vectron(a+t.a, b+t.b, c+t.c);
	}

	/**
	* sum by reference (change 1st arg)
	*/
	public static void sum(Vectron t, Vectron d) {
		t.a=d.a+t.a;   t.b=d.b+t.b;  t.c=d.c+t.c;
	}

	/**
	* The additive inverse
	*/
	public Vectron inverse() {
		return new Vectron(-a,-b,-c);
	}

	/**
	* invert a Vectron by reference
	*/
	public static void invert(Vectron t) {
		t.a=-t.a; t.b=-t.b; t.c=-t.c;
	}

	/**
	* square a Vectron
	*/
	public static void square (Vectron t) {
		product(t,t);
	}

	/**
	* product with a real number (by reference)..
	*/
	public static void product(Vectron t, double d) {
		t.setCoords(t.a*d, t.b*d, t.c*d);
	}

	/**
	* product with a real number
	*/
	public Vectron product(double dd) {
		Vectron t = new Vectron();
		t.setCoords(a*dd, b*dd, c*dd);
		return t;
	}

	/**
	* Magnitude of a Vectron
	*/
	public static double mag(Vectron t) {
		return Math.sqrt(t.a*t.a+t.b*t.b+t.c*t.c);
	}

	public double mag() {
		return mag(this);
	}

	/**
	* Normalize a Vectron
	*/
	public void normalize() {
		double mag = mag();
		a=a/mag; b=b/mag; c=c/mag;
	}

	public String toString() {
		return new String(a+" "+b+" "+c);
	}

	public void setCoords(Vectron t) {
		a=t.a; b=t.b; c=t.c;
	}

	public void setCoords(double d1, double d2, double d3) {
		a=d1; b=d2; c=d3;
	}

}



