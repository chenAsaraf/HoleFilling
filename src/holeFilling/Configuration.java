package holeFilling;

import org.opencv.core.Mat;

public class Configuration {
	/** Structure that store the HoleFilling library settings
	     during the argument parsing in command line utility **/
	private String PathToOutputDir;
	private int z;
	private float eps;
	private Mat inputImg;
	private int connectType;

	public int getConnectType() {
		return connectType;
	}

	public void setConnectType(int connectType) {
		this.connectType = connectType;
	}

	public Mat getInputImg() {
		return inputImg;
	}

	public void setInputImg(Mat inputImg) {
		this.inputImg = inputImg;
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
