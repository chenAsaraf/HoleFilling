package holeFilling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opencv.core.Mat;
/**
 * Performs  an approximation-algorithm operations to fill the hole in the image
 * 
 * The class support filling holes in grayscale images, 
 * where each pixel value is a ​float​ in the range ​[0, 1]​,
 * and hole (missing) values which are marked with the value ​-1​. 
 * 
 * User manual:
 * 1. Initialize HoleFiller object with details:
 *    Filler filler = new HoleFillerAprox(z, epsilon, input, connectivityType);
 * 2. Activate the filler, store the result in OpenCV::Mat to allow further
 *    image processing:
 *    Mat result = filler.fill();
 */ 
public class HoleFillerAprox extends Filler{

	private Map<Pixel,List<Pixel>> innerBorder; 
	private Set<Pixel> hole;

	//Constructor
	public HoleFillerAprox(int z, float e, Mat src, int cType) {
		super(z,e,src,cType);
		hole = new HashSet<>();
		innerBorder = new HashMap<>();
	}

	/**
	 * Initialize the hole and the inner-border.
	 * While hole is not empty: 
	 * 	Fill the innerBorder pixels using only the pixels in its neighbors list  
	 * 	Clean innerBorder
	 * 	for each pixel p in the  hole:
	 * 		Create a list of all p neighbors that are not missing
	 * 		If neighborhood size > 0:
	 * 			innerBorder add(p,neighborhood)
	 * 			remove p from the hole
	 */
	@Override
	public Mat fill() {
		init();
		while(!hole.isEmpty()) {
			for(Pixel current : innerBorder.keySet()) {
				float sumWithValues = 0;
				float sumDistances = 0;
				for(Pixel neigh: innerBorder.get(current)) { // O(connectivity type)
					float b_value = (float) input.get(neigh.row(), neigh.col())[0];
					float weight = w.func(current,neigh);
					sumWithValues += weight*b_value;
					sumDistances += weight;
				}
				setValue(current,sumWithValues, sumDistances);
			}
			findNewInnerBorder();
		}
		return input;
	}

	/**
	 * Find the missing pixels in the original image.
	 * Store the inner boundaries of the hole (missing pixels themselves) separately inside
	 * a HashMap, with a list of the colored-surrounding-pixels as a value.
	 */
	private void init() {
		HoleFinder.findHoleNInnerBound(input,hole,innerBorder,connectType);
	}

	/**
	 * Find new inner-border set. The new pixels in the border will be
	 * in the neighborhood of new painted pixels (last inner-border set).
	 * Iterates over the last inner-border set and select the missing neighbor pixels.
	 */
	private void findNewInnerBorder() {
		Set<Pixel> visited = new HashSet();
		Map<Pixel,List<Pixel>> newInnerBorder = new HashMap<>();
		Iterator<Pixel> iter = innerBorder.keySet().iterator();
		while (iter.hasNext()) {
			Pixel colored = iter.next();
			List<Pixel> missingNeig = HoleFinder.unColorNeighbor(colored,connectType,input);
			for (Pixel missing : missingNeig) {
				if(!visited.contains(missing)) {
					List<Pixel> colorNeig = HoleFinder.colorNeighbor(missing,connectType,input);
					newInnerBorder.put(missing, colorNeig);
					hole.remove(missing);
					visited.add(missing);
				}
			}
		}
		innerBorder = newInnerBorder;
	}


	/**
	 * Set new value to hole pixel in the image
	 * @param h hole Pixel
	 * @param sumWithValues
	 * @param sumDistances
	 */
	private void setValue(Pixel h, float sumWithValues, float sumDistances) {
		float data = sumWithValues/ sumDistances;
		input.put(h.row(), h.col(), data);
	}



}

