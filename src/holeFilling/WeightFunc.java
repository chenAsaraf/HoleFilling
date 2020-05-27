package holeFilling;

public interface WeightFunc {
	/**
	 * Represent weighting function which assigns a non-negative float weight
	 * to a pair of two pixels coordinates in the image.  
	 * Easily accessed to see how change of function
	 * impacts the reconstruction of the image.
	*/
	
	public float func(Pixel u, Pixel v);
}
