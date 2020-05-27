package holeFilling;


public class WeightFunc {
	public static int z;
	public static float epsilon;
	
	public WeightFunc() {}
	
	public WeightFunc(int z, float e) {
		this.z = z;
		this.epsilon = e;
	}
	
	
	
	// Assigns a non-negative float weight to a pair of two pixel coordinates
	public static float w(Pixel u, Pixel v) {
		return (float)(1/(Math.pow(euclideanDistance(u,v), z) + epsilon));
	}
	
	private static double euclideanDistance(Pixel u, Pixel v) {
		return Math.sqrt(Math.pow(u.col()-v.col(),2) + Math.pow(u.row()-v.row(),2));
	}
	
	 
}
