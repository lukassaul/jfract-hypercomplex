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

	
	public static double phi = (1.0+Math.sqrt(5.0))/2.0;
        public static double logphi = Math.log(phi);
	
	

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
	* t = t+d   
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
	/*public static Tetron pow (Tetron t, int pow) {
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
	*/

	/**
	* Now we can do exponentiation..
	*/
	public static Tetron exp (Tetron t) {
		Tetron base = new Tetron();
		double factoid = 1.0;
		base.setCoords(ONE);  // this is going to change each iteration  
		Tetron tbr = new Tetron();
		tbr.setCoords(ZERO);
		for (double i=0.0; i<150.0; i=i+1.0) {
			sum( tbr, base.product(1.0/factoid) );
			product(base,t);            // prepare the base for next loop
			factoid = factoid*(i+1.0);  // prepare the factorial for next loop
		}
		return tbr;
	}

	/**
	* Now we can do exponentiation..  or can we?  lets try it with doubles
	*/
	public static double exp (double t) {
		double base = 1.0;
		double factoid = 1.0; 
		double tbr = 0.0;

		for (double i=0.0; i<150.0; i=i+1.0) {
			tbr = tbr + base/factoid;
			base = base*t;            // prepare the base for next loop
			factoid = factoid*(i+1.0);  // prepare the factorial for next loop
		}
		return tbr;
	}
	/**
	* And logarithms..
	*/
	/*public static Tetron log (Tetron t) {
		Tetron temp = new Tetron();
		Tetron tMinus1 = new Tetron(t.a-1.0, t.b, t.c, t.d);
		double sign = -1.0;
		for (int i=1; i<20; i++) {
			sum( temp, pow(tMinus1,i).product(sign/(double)(i)) );
			sign=sign * -1.0;
			System.out.println("sign,temp"+"\t"+sign+"\t"+temp+"");
		}
		return temp;
	}*/

	/**
	*In fact this is a multivalued function but we choose a branch log(-1)=i*pi
	*/
	public static Tetron minusOneToTheN(Tetron n) {
		//Tetron t = ONE.inverse();
		//Tetron t1 = I.product(Math.PI);
		Tetron ponent = new Tetron();
		ponent.a = 0.0-Math.PI*n.b;
		ponent.b = Math.PI*n.a;
		ponent.c = 0.0 - Math.PI*n.d;
		ponent.d = Math.PI*n.c;
		//System.out.println("t1 : " + t1.toString() + " n " + n.toString());
		//t1 = t1.product(1.0/Math.sqrt(2));
		return exp(ponent);
	}

		
	/**
	* Lets do a fibonacci series operation.. find the "Qth" term of the series 
	*/
        public static Tetron fibonacci(Tetron in) {
		
		Tetron tbr = new Tetron();
		Tetron p1, p2, p3;	
		p1 = Tetron.exp(in.product(logphi));
		p2 = minusOneToTheN(in);
		p3 = Tetron.exp(in.inverse().product(logphi));		
		//System.out.println("in fib: " + p1.toString() + "\t" + p2.toString() + "\t" + p3.toString());	
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
		//System.out.println("IPI^3 = " + pow(I.product(Math.PI),3));
		//System.out.println("k^2 = " + pow(K,2));
		//System.out.println("j^3 = " + pow(J,3));
		//System.out.println("k^3 = " + pow(K,3));
		System.out.println("fact(0) = " + fact(0));
		System.out.println("fact(2) = " + fact(2));
		System.out.println("fact(3) = " + fact(3));
		// test the exp
		System.out.println("e^1 = " + Tetron.exp(ONE));
		Tetron tempT = new Tetron();
		tempT.setCoords(1.0,0.0,0.0,0.0);
		System.out.println("e^1 = " + Tetron.exp(tempT));
		System.out.println("e^-1 = " + Tetron.exp(ONE.inverse()));
		System.out.println("e^0 = " + Tetron.exp(ZERO));
		System.out.println("e^2 = " + Tetron.exp(ONE.sum(ONE)));
		System.out.println("e^iPi = " + exp(I.product(Math.PI)));
		System.out.println("e^jPi = " + exp(J.product(Math.PI)));
		System.out.println("e^kPi = " + exp(K.product(Math.PI)));
		System.out.println("ONE.product(1.0) = " + ONE.product(1.0));
		System.out.println("J.product(2.1) = " + J.product(2.1));
		System.out.println("-1^1 = " + minusOneToTheN(ONE.product(1.0)));
		System.out.println("mag -1^1 = " + mag(minusOneToTheN(ONE.product(1.0))));
		System.out.println("-1^i = " + minusOneToTheN(I));
		System.out.println("mag -1^j = " + mag(minusOneToTheN(J)));
		System.out.println("-1^j = " + minusOneToTheN(J));
		System.out.println("mag -1^k = " + mag(minusOneToTheN(K)));
		System.out.println("-1^k = " + minusOneToTheN(K));
		System.out.println("mag -1^i = " + mag(minusOneToTheN(I)));
		System.out.println("-1^3 = " + minusOneToTheN(ONE.product(3.0)));
		System.out.println("mag -1^3 = " + mag(minusOneToTheN(ONE.product(3.0))));
		System.out.println("-1^4 = " + minusOneToTheN(ONE.product(4.0)));
		System.out.println("mag -1^4 = " + mag(minusOneToTheN(ONE.product(4.0))));
		System.out.println("Fib ONE= " + fibonacci(ONE));
		System.out.println("Fib TWO= " + fibonacci(ONE.product(2.0)));
		System.out.println("Fib THREE= " + fibonacci(ONE.product(3.0)));
		System.out.println("Fib 4= " + fibonacci(ONE.product(4.0)));
		System.out.println("Fib 5= " + fibonacci(ONE.product(5.0)));
		System.out.println("Fib 6= " + fibonacci(ONE.product(6.0)));
		System.out.println("Fib i= " + fibonacci(I));
		System.out.println("Fib j= " + fibonacci(J));
		System.out.println("Fib k= " + fibonacci(K));
		System.out.println("ratio 1 = " + fibonacci(ONE.product(10.0)).a/fibonacci(ONE.product(9.0)).a);
		System.out.println("ratio 1 = " + fibonacci(ONE.product(10.0)).b/fibonacci(ONE.product(9.0)).b);
		System.out.println("ratio 1 = " + fibonacci(ONE.product(10.0)).c/fibonacci(ONE.product(9.0)).c);
		System.out.println("ratio 1 = " + fibonacci(ONE.product(10.0)).d/fibonacci(ONE.product(9.0)).d);
		System.out.println("ratio 1 = " + fibonacci(I.product(10.0)).a/fibonacci(I.product(9.0)).a);
		System.out.println("ratio 1 = " + fibonacci(I.product(10.0)).b/fibonacci(I.product(9.0)).b);
		System.out.println("ratio 1 = " + fibonacci(I.product(10.0)).c/fibonacci(I.product(9.0)).c);
		System.out.println("ratio 1 = " + fibonacci(I.product(10.0)).d/fibonacci(I.product(9.0)).d);
		System.out.println("ratio 1 = " + fibonacci(J.product(10.0)).a/fibonacci(J.product(9.0)).a);
		System.out.println("ratio 1 = " + fibonacci(J.product(10.0)).b/fibonacci(J.product(9.0)).b);
		System.out.println("ratio 1 = " + fibonacci(J.product(10.0)).c/fibonacci(J.product(9.0)).c);
		System.out.println("ratio 1 = " + fibonacci(J.product(10.0)).d/fibonacci(J.product(9.0)).d);
		System.out.println("ratio 1 = " + fibonacci(K.product(10.0)).a/fibonacci(K.product(9.0)).a);
		System.out.println("ratio 1 = " + fibonacci(K.product(10.0)).b/fibonacci(K.product(9.0)).b);
		System.out.println("ratio 1 = " + fibonacci(K.product(10.0)).c/fibonacci(K.product(9.0)).c);
		System.out.println("ratio 1 = " + fibonacci(K.product(10.0)).d/fibonacci(K.product(9.0)).d);


		System.out.println("ratio ib = " + fibonacci(I.product(11.0)).b/fibonacci(I.product(10.0)).b);
		System.out.println("ratio 1b = " + fibonacci(I.product(12.0)).b/fibonacci(I.product(11.0)).b);
		System.out.println("ratio 1b = " + fibonacci(I.product(13.0)).b/fibonacci(I.product(12.0)).b);


		System.out.println("-ONE product 10 = " + ONE.inverse().product(ONE.product(10)));

	//	System.out.println("log(1) = " + log(ONE));
//		System.out.println("log(-1) = " + log(ONE.inverse()));
//		System.out.println("log(E) = " + log(new Tetron(Math.E,0.0,0.0,0.0)));
//		System.out.println("logphi,psi: " + logphi + "\t" + logpsi+ "\n");
// start exp test
	
	

/*		file fx = new file("expout.txt");  fx.initWrite(false);
		Tetron tx = new Tetron();
		Tetron resx = new Tetron();
		for (double i = -3.0; i<3.0; i=i+0.1) {
			tx.setCoords(i,0.0,0.0,0.0);		
			resx = Tetron.exp(tx);			
			fx.write(tx.a  + "\t" + resx + "\n");
		}
		fx.closeWrite();

*/
/*
		fx = new file("expout2.txt");  fx.initWrite(false);
		double resres = 1;
		for (double i = -3.0; i<3.0; i=i+0.01) {			
			resres = exp(i);			
			fx.write(i + "\t" + resres + "\n");
		}
		fx.closeWrite();
*/


		file f = new file("fibout.txt");  f.initWrite(false);
		Tetron t = new Tetron();
		Tetron res = new Tetron();
		for (double i = 0.0; i<2.0*Math.PI; i=i+0.01) {
			t.setCoords(0.0,Math.cos(i),Math.sin(i),0.0);			
			res = fibonacci(t);			
			f.write(i  + "\t" + res.a + "\t" + res.b + "\t" + res.c + "\t" + res.d + "\t" + mag(res) + "\n");
		}
		f.closeWrite();

		
		// LETS MEAKE A 3DD THINGY
/*
		file f = new file("fibout3d.txt");  f.initWrite(false);
		Tetron t = new Tetron();
		Tetron res = new Tetron();
		for (double a = -5.0; a<5.0; a=a+0.1) {
			for (double k = -5.0; k<5.0; k+=0.1) {
				t.setCoords(0.0,a,0.0,k);			
				res = fibonacci(t);			
				f.write(a  + "\t" + k+ "\t" + res.a + "\t" + res.b + "\t" + res.c + "\t" + res.d + "\t" + mag(res) + "\n");
				
			}
		}
		f.closeWrite();
*/




		// start Fibonacci test
/*		file f = new file("fibout.txt");  f.initWrite(false);
		Tetron t = Tetron.ZERO;
		Tetron res = Tetron.ONE;
		for (double i = 0.0; i<10.0; i=i+0.1) {
			t.a = t.a + 0.1;			
			res = Tetron.fibonacci(t);			
			f.write(t.a + "\t" + Tetron.mag(res) + "\t" + res.a + "\n");
		}
		f.closeWrite();
*/

	}
}



