package holeFilling;

import java.util.InputMismatchException;
import org.opencv.core.Mat;

/**
 * Abstract class to represent variation of image hole-filler algorithms.
 * Each filler algorithm must store the source image, a weighting  function and
 * connectivity-type definition, and to implement "fill()" method.  
 * 
 * The class support filling holes in grayscale images, 
 * where each pixel value is a ​float​ in the range ​[0, 1]​,
 * and hole (missing) values which are marked with the value ​-1​.  
 */
public abstract class Filler {
	protected Mat input;
	protected WeightFunc w;
	protected int connectType;

	//Constructor
	protected Filler(int z, float e, Mat src, int cType){
		if(src.channels() > 1)
			throw new InputMismatchException("Image should be in grayscale"); 
		this.input = src.clone();

		this.w = new DeafultWeight(z, e);

		if ((cType!= 4) && (cType!=8))
			throw new InputMismatchException("HoleFiller accept only 4 or 8 connectivity Type");
		connectType = cType;

	}

	//Main class algorithm
	abstract Mat fill();
}
