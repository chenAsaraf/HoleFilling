package holeFilling;

import java.io.File;
import java.util.InputMismatchException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

public class HoleFillingApp {

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

		config.setInputImg(image);
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
			//normalize(image);
			config.setInputImg(convertFromRBGwithMask(image,mask));
			String aftermask = config.getPathToOutputDir() + "/imageWithMask.jpg"; 
			writeImage(aftermask, image);
		}
	}

	private static Mat convertFromRBGwithMask(Mat image, Mat mask) {
		//Mask has intensity of 1 where the hole is
		//Applying the mask to carve out the hole
		image.setTo(new Scalar(-1,-1,-1),mask);
		return image;
	}

	private static Mat loadImage(String file) {
		//Instantiating the Imagecodecs class 
		Imgcodecs imageCodecs = new Imgcodecs(); 
		//Reading the Image from the file  
		Mat image = imageCodecs.imread(file, Imgcodecs.IMREAD_GRAYSCALE);
		return image;
	}

	private static void writeImage(String file, Mat img) {
		Imgcodecs imageCodecs = new Imgcodecs(); 
		imageCodecs.imwrite(file, img);
	}


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

	private static void normalize(Mat img) {
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				float value = (float) img.get(i,j)[0];
				img.put(i, j, (value/255.0));
			}
		}
		//img.convertTo(img, CvType.CV_32FC3, 1.0/255.0);
	}

	public static void main(String[] args) {
		//Loading the OpenCV core library  
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
		//Store the configuration details during parsing:
		Configuration conf = new Configuration();
		ParseArgsToObjects(args,conf);
		//Start to fill and inpaint the hole in the image:
		HoleFilling filler = new HoleFilling(conf.getZ(),conf.getEps(),conf.getInputImg(),conf.getConnectType());
		filler.fill();
		//Write the new image:
		String newfile = conf.getPathToOutputDir() +"/newfile.jpg";
		writeImage(newfile, conf.getInputImg());
	}

}
