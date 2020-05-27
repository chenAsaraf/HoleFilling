package holeFilling;

import java.util.HashSet;
import java.util.Set;
import org.opencv.core.Mat;  

/**
 * Performs the naive-algorithm operations to fill the hole in the image
 * 
 * The class support filling holes in grayscale images, 
 * where each pixel value is a ​float​ in the range ​[0, 1]​,
 * and hole (missing) values which are marked with the value ​-1​. 
 * 
 * User manual:
 * 1. Initialize HoleFiller object with details:
 *    Filler filler = new HoleFiller(z, epsilon, input, connectivityType);
 * 2. Activate the filler, store the result in OpenCV::Mat to allow further
 *    image processing:
 *    Mat result = filler.fill();
 */
public class HoleFillerNaive extends Filler {

	private Set<Pixel> hole; 
	private Set<Pixel> borders;

	//Constructor
	public HoleFillerNaive(int z, float e, Mat src, int cType) {
		super(z,e,src,cType);
		hole = new HashSet<>();
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
	@Override
	public Mat fill() {
		HoleFinder.findHoleNBoundries(input, hole, borders,connectType);
		for(Pixel missing : hole) {
			float sumWithValues = 0;
			float sumDistances = 0;
			for(Pixel border: borders) {
				float b_value = (float) input.get(border.row(), border.col())[0];
				float weight = w.func(missing,border);
				sumWithValues += weight*b_value;
				sumDistances += weight;
			}
			setValue(missing,sumWithValues, sumDistances);
		}
		return input;
	}


	/**
	 * set new value to hole pixel in the image
	 * @param h hole Pixel
	 * @param sumWithValues
	 * @param sumDistances
	 */
	private void setValue(Pixel h, float sumWithValues, float sumDistances) {
		float data = sumWithValues/ sumDistances;
		input.put(h.row(), h.col(), data);
	}

}

