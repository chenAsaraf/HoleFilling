package holeFilling;


public class DeafultWeight implements WeightFunc{
	/** 
	 * Holds the default weighting function whereby the algorithm calculates the
	 * missing pixel color.
	 * The values z and epsilon are configurable,
	 * and epsilon is a small float value used to avoid division by zero.
	 */
	protected static int z;
	protected static float epsilon;

	public DeafultWeight() {}

	public DeafultWeight(int z, float e) {
		this.z = z;
		this.epsilon = e;
	}	

	// Assigns a non-negative float weight to a pair of two pixel coordinates
	@Override
	public float func(Pixel u, Pixel v) {
		return (float)(1/(Math.pow(euclideanDistance(u,v), z) + epsilon));	
	}

	private double euclideanDistance(Pixel u, Pixel v) {
		return Math.sqrt(Math.pow(u.col()-v.col(),2) + Math.pow(u.row()-v.row(),2));
	}

	public static int getZ() {
		return z;
	}

	public static void setZ(int z) {
		DeafultWeight.z = z;
	}

	public static float getEpsilon() {
		return epsilon;
	}

	public static void setEpsilon(float epsilon) {
		DeafultWeight.epsilon = epsilon;
	}



}
