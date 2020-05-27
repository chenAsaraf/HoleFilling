package holeFilling;

import org.opencv.core.Mat;

public class Configuration {
	/** Structure that store the HoleFilling library settings
	     during the argument parsing, used by Command Line utility **/
	
	private String PathToOutputDir;
	private int z;
	private float eps;
	private Mat input;
	private int connectType;

	public int getConnectType() {
		return connectType;
	}

	public void setConnectType(int connectType) {
		this.connectType = connectType;
	}

	public Mat getInput() {
		return input;
	}

	public void setInput(Mat input) {
		this.input = input;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public float getEps() {
		return eps;
	}

	public void setEps(float eps) {
		this.eps = eps;
	}

	public String getPathToOutputDir() {
		return PathToOutputDir;
	}

	public void setPathToOutputDir(String pathToOutputDir) {
		PathToOutputDir = pathToOutputDir;
	}
	
}
