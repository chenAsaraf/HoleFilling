package holeFilling;

import java.io.File;
import java.util.InputMismatchException;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *  Command Line Utility:
 *  
 *  Accepts the input in the following order:
 *  
 *  1. Input grayscale image, with -1 marking a missing color
 *     or RGB image with the addition of the fifth argument to mark the hole
 *      
 *  2. "Z" configuration - an integer
 *  
 *  3. "Epsilon" configuration -  a small float number
 *  
 *  4. Connectivity type - Integer, 4 or 8
 *  
 *  5. Optional - extra RGB mask-image to defines the hole. 
 *     Required in case of input which is RGB image
 *     The missing pixels in the image are the pixels marked as 1 in the mask.
 */

public class HoleFillingApp {
	
	public static void main(String[] args) {
		//Loading the OpenCV core library  
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
		//Store the configuration details during parsing:
		Configuration conf = new Configuration();
		ParseArgsToObjects(args,conf);
		System.out.println("arguments loaded");
		//Start to fill and inpaint the hole in the image:
		HoleFiller filler = new HoleFiller(conf.getZ(),conf.getEps(),conf.getInput(),conf.getConnectType());
		Mat result = filler.fill();
		//Write the new image:
		String newfile = conf.getPathToOutputDir() +"/newfile.jpg";
		writeImage(newfile, result);
		System.out.println("image's hole repainted");
	}

	

	/**
	 * Auxiliary function to parse command line arguments and store the information 
	 * for use next 
	 * @param args
	 * @param config - structure holds the information of the inputs
	 */
	public static void ParseArgsToObjects(String[] args, Configuration config) {
		if (args.length < 4) throw new InputMismatchException("There are not enough arguments");
		//TODO: add description of the input
		//TODO: check for exceptions
		String file = args[0];
		int z = Integer.parseInt(args[1]); 
		float e = Float.parseFloat(args[2]); 
		int connectivityType = Integer.parseInt(args[3]); 

		//Set configuration:
		config.setPathToOutputDir(createOutputFolder(file));

		Mat image = loadImage(file);
		config.setInput(image);
		config.setZ(z);
		config.setEps(e);
		config.setConnectType(connectivityType);

		//May accept (and write to) RGB image formats, and perform the required conversions:
		if (args.length == 5) {
			String maskFile = args[4];
			Mat mask = loadImage(maskFile);
			//Convert to grayscale format with hole value -1
			image.convertTo(image, CvType.CV_32FC3);
			//Normalize to range [0 1]
			Core.normalize(image, image, 0.0, 255.0, Core.NORM_MINMAX);
			config.setInput(convertFromRBGwithMask(image,mask,config));
		}
	}

	/**
	 * Output a Mat object contains intensity of -1 in the hole pixels,
	 * applying the mask to carve the hole
	 * @param image - original image already in grayscale format and in Mat structure
	 * @param mask - image containing 1 where pixels are missing (hole)
	 * @return image
	 */
	private static Mat convertFromRBGwithMask(Mat image, Mat mask, Configuration config) {
		image.setTo(new Scalar(-1.0),mask);
		String aftermask = config.getPathToOutputDir() + "/imageWithMask.jpg"; 
		writeImage(aftermask, image);
		return image;
	}

	/**
	 * Read image from path and store it in OpenCv::Mat Object 
	 * @param file
	 * @return image Mat
	 */
	private static Mat loadImage(String file) {
		//Instantiating the Imagecodecs class 
		Imgcodecs imageCodecs = new Imgcodecs(); 
		//Reading the Image from the file  
		Mat image = imageCodecs.imread(file, Imgcodecs.IMREAD_GRAYSCALE);
		return image;
	}

	/**
	 * Write the image into path
	 * @param file
	 * @param img
	 */
	private static void writeImage(String file, Mat img) {
		Imgcodecs imageCodecs = new Imgcodecs(); 
		imageCodecs.imwrite(file, img);
	}

	
	//Create output folder to examine the results
	private static String createOutputFolder(String path) {
		String[] parts = path.split("/");
		String pathToDir = parts[0];
		for (int i = 1; i < parts.length-1; i++) {
			pathToDir += "/" +parts[i];
		}
		pathToDir += "/Output";
		File dir = new File(pathToDir );
		if (!dir.exists()) {
			dir.mkdir();
		}
		return pathToDir;
	}

}
