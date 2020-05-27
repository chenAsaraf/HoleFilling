package holeFilling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;  
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;           

/**
 * Class to perform the algorithmic operations of filling the hole in the image
 * The class support filling holes in grayscale images, 
 * where each pixel value is a ​float​ in the range ​[0, 1]​,
 * and hole (missing) values which are marked with the value ​-1​. 
 * 
 * User manual:
 * 1. Initialize HoleFiller object with details:
 *    HoleFiller filler = new HoleFiller(z, epsilon, input, connectivityType);
 * 2. Activate the filler, store the result in OpenCv::Mat to allow further
 *    image processing:
 *    Mat result = filler.fill();
 */

public class HoleFiller {

	protected WeightFunc w;
	private Mat input;
	private int connectType;
	private Vector<Pixel> hole; //change to set also
	private Set<Pixel> borders;

	//Constructor
	public HoleFiller(int z, float e, Mat src, int cType) {
		if(src.channels() > 1)
			throw new InputMismatchException("Image should be in grayscale");
		Imgcodecs imageCodecs = new Imgcodecs(); 
		//Reading the Image from the file  
		input = src;

		w = new DeafultWeight(z, e);


		if ((cType!= 4) && (cType!=8))
			throw new InputMismatchException("HoleFiller accept only 4 or 8 connectivity Type");
		connectType = cType;

		hole = new Vector<>();
		borders = new HashSet<>();
	}
	
	
	/**
	 * Main method:
	 * Fills the hole in the image by the followi0ng algorithm:
	 * For each pixel u in the hole (missing pixels:
	 *     fix the intensity of u to be:
	 *       (sum over pixel v in borders group: w(u,v)) * I(v) / (sum over v in borders: w(u,v))
	 *        w is a weighting function  
	 * @return the image after painting the hole, in Mat (opencv) format
	 */
	public Mat fill() {
		HoleFinder.findHoleNBoundries(input, hole, borders,connectType);
		for(int i = 0; i < hole.size(); ++i) {
			float sumWithValues = 0;
			float sumDistances = 0;
			for(Pixel border: borders) {
				float b_value = (float) input.get(border.row(), border.col())[0];
				float weight = w.func(hole.get(i),border);
				sumWithValues += weight*b_value;
				sumDistances += weight;
			}
			setValue(hole.get(i),sumWithValues, sumDistances);
		}
		return input;
	}
	


	
	/**
	 * set new value to hole pixel in the image
	 * @param h hole Pixel
	 * @param sumWithValues
	 * @param sumDistances
	 */
	public void setValue(Pixel h, float sumWithValues, float sumDistances) {
		float data = sumWithValues/ sumDistances;
		input.put(h.row(), h.col(), data);
	}
	
}

