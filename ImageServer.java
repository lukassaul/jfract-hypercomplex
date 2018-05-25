
//import java.awt.image.*;
import java.awt.Image;
/**
* AN interface that serves up images
*  Only one method required to implement this interface!  You must supply an image!
*
*/
public interface ImageServerInterface {

	/**
	*   A general image request, from a virtual region to an x-y pixel array
	*
	*   top left corner, width, and height from source!
	*   also we need x pixels and y pixels requested..
	*
	*/
	public Image getImage(double x, double y, double width, double height,
					int xPix, int yPix);


}