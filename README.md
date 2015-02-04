jfract-hypercomplex

by Lukas Saul

This java program images the tetrabrot fractal family. Slices of this four dimensional object are visualized.  

Please acknowlege Lukas Saul if you use this software. 

All contributions are welcome!





The tetrabrot fractals are 4 dimensional objects, which are generated from the recursion relation:

Tn+1=Tn*Tn+T0

The elements T which do not diverge under this iteration are the members of the Tetrabrot set.  For the case where T is a complex number (2D), this is the famous Mandelbrot set, shown to the right.  This same image is the 1 vs. i or j axes in the hypercomplex tetrabrot, or 1 vs. i,j,or k in the quaternion tetrabrot.

There are a few ways to extend complex numbers to higher dimensions, creating "hypercomplex" numbers.  These numbers can be represented as sums of the "basis unit vectors", which have multiplication tables such as shown below:

Complex numbers:
                1    i
            1|  1   i
             i|   i   -1

Quaternions:
                 1    i    j    k
            1|  1   i    j    k
             i|   i  -1   k  -j
             j|   j   -k  -1   i
            k|  k   j    -i  -1    

Hypercomplex A:
                 1    i    j    k
            1|  1   i    j    k
             i|   i  -1  -k   j
             j|   j   -k  -1   i
            k|  k   j     i   1    
Hypercomplex Tetrabrot
i or j vs k axes:
Hypercomplex Tetrabrot:
1 vs k axes:
Note that the videos below are NOT images of Julia sets, or exponent variations, or other fractal manipulations.  Only T^2+T is used here!  


