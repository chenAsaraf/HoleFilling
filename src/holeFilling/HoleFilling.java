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


public class HoleFilling {

	protected WeightFunc w;
	private Mat input;
	private int connectType;
	private Vector<Pixel> H; //change to set also
	//Vector<Pixel> B;
	private Set<Pixel> B;

	
	public HoleFilling(int z, float e, Mat src, int cType) {
		w = new WeightFunc(z, e);
		input = src;
		connectType = cType;
		H = new Vector<>();
		//B = new Vector<>();
		B = new HashSet<>();
	}

	public Pixel findFirstHole() {
		//Iterates over the image:
		for (int i = 0; i < input.rows(); i++) {
			for (int j = 0; j < input.cols(); j++) {
				if(input.get(i, j)[0] == -1.0) {
					return new Pixel(i,j);
				}
			}
		}
		return new Pixel(-1, -1); //no hole in the image
	}

	//	public void findHoleNBoundries() {
	//		for (int i = 0; i < input.rows(); i++) {
	//			for (int j = 0; j < input.cols(); j++) {
	//				if(input.get(i, j)[0] == -1.0) {
	//					Pixel current = new Pixel(i,j);
	//					H.add(current);
	//					for(Pixel n : neighbor(current)) {
	//						B.add(n); 
	//					}
	//				}
	//			}
	//		}
	//	}

	public void findHoleNBoundries() {
		Pixel first = findFirstHole();
		if(first.equals(-1,-1)) throw new InputMismatchException("There is no hole in the image");
		H.add(first);
		Set<Pixel> visited = new HashSet();
		Queue<Pixel> holeQue = new LinkedList<>();
		holeQue.add(first);
		visited.add(first);
		while(!holeQue.isEmpty()) {
			Pixel current = holeQue.poll();
			for(Pixel n : neighbor(current)) {
				if(!visited.contains(n)) {
					if (input.get(n.row, n.col)[0] == -1.0) {
						H.add(n);
						holeQue.add(n);
						visited.add(n);

					}
					else {
						B.add(n); 
						visited.add(n);
					}
				}
			}
		}
	}


	private ArrayList<Pixel> neighbor(Pixel current) {

		ArrayList<Pixel> neig = new ArrayList<>();
		int r = current.row;
		int c = current.col;
		int[][] indexes = {{r+1,c}, {r-1,c}, {r,c+1}, {r,c-1}};
		for (int i = 0; i < indexes.length; i++) {
			int newRowIndex = indexes[i][0];
			int newColIndex = indexes[i][1];
			Pixel borderPix = new Pixel(newRowIndex, newColIndex);
			if(!outOfImageBorders(borderPix)) {
				neig.add(borderPix);
			}
		}

		if(connectType == 8) {//Add corners
			int [][] cornerIdx = {{r+1,c+1}, {r-1,c-1}, {r+1,c-1}, {r-1,c+1}};
			for (int i = 0; i < cornerIdx.length; i++) {
				int newRowIndex = cornerIdx[i][0];
				int newColIndex = cornerIdx[i][1];
				Pixel borderPix = new Pixel(newRowIndex, newColIndex);
				if(!outOfImageBorders(borderPix)) {
					neig.add(borderPix);
				}
			}
		}
		return neig;
	}


	private Boolean outOfImageBorders(Pixel p) {
		int height = input.rows();
		int width = input.cols();
		return ( ((p.row>height-1)) || ((p.col>width-1)) || (p.row < 0) || (p.col < 0) );
	}

	/**
	 * Fills the hole in the image by the following algorithm:
	 * @param input
	 * @param B set of all the boundary pixel coordinates
	 * @param H set of all the hole pixel coordinates 
	 * For each u in H:
	 *  fix the intensity of u to be:
	 *  (sum over v in B: w(u,v)) * I(v) / (sum over v in B: w(u,v))
	 *  where w is a weighting function  
	 */
	public void fill() {
		findHoleNBoundries();
		System.out.println("B size: " + B.size());
		System.out.println("H size: " + H.size());
		for(int i = 0; i < H.size(); ++i) {
			float sumWithValues = 0;
			float sumDistances = 0;
			for(Pixel border: B) {
				//TODO - if the type already float no need in conversion 
				float b_value = (float) input.get(border.row, border.col)[0];
				float w = WeightFunc.weighting(H.get(i),border);
				//TODO - maybe () operator in WeightFunc(h,b)
				sumWithValues += w*b_value;
				sumDistances += w;
			}
			setValue(H.get(i),sumWithValues, sumDistances);
		}
	}



	public void setValue(Pixel h, float sumWithValues, float sumDistances) {
		float data = sumWithValues/ sumDistances;
		input.put(h.row, h.col, data);
	}
	

	// Command line utility:
//	public static void main(String args[]) { 
//		//Steps:
//		//1. check for number of argument. -> if have more then 4, need to convert from RGB
//		//2. convertFromRBGwithMask()
//		//3. normalize() + convert to float
//		//4. fillHole()
//		//5. writeNewImage()
//		
//
//		if (args.length < 4) throw new InputMismatchException("There are not enough arguments");
//		//TODO: add description of the input
//		//TODO: check for exceptions​
//		String file = args[0];
//		int z = Integer.parseInt(args[1]); // 2
//		float e = Float.parseFloat(args[2]); // 0.1
//		int connectivityType = Integer.parseInt(args[3]); // 4
//		//TODO: add condition if exist mask image
//		String mask_file = args[4];
//
//		System.out.println("file: " + file);
//		System.out.println("mask: " + mask_file);
//
//		//Loading the OpenCV core library  
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
//
//		//Instantiating the Imagecodecs class 
//		Imgcodecs imageCodecs = new Imgcodecs(); 
//
//		//Reading the Image from the file   
//		Mat image = imageCodecs.imread(file, Imgcodecs.IMREAD_GRAYSCALE);
//
//		image.convertTo(image, CvType.CV_32FC3, 1.0/255.0); //, 1.0/255.0
//
//		//TODO: add if
//		//Mask has intensity of 1 where the hole is
//		Mat mask = imageCodecs.imread(mask_file, Imgcodecs.IMREAD_GRAYSCALE);
//
//		//Applying the mask to carve out the hole
//		image.setTo(new Scalar(-1,-1,-1),mask);
//		//		for (int i = 0; i < image.rows(); i++) {
//		//			for (int j = 0; j < image.cols(); j++) {
//		//				if(mask.get(i,j)[0] == 1.0) {
//		//					image.put(i, j, -1.0);
//		//					System.out.println("(" + i +"," +j +") = "+ image.get(i, j)[0]);
//		//				}
//		//			}
//		//		}
//
//		String aftermask = "C:/Users/owner/Desktop/imgesForTest/aftermask.jpg"; 
//		imageCodecs.imwrite(aftermask, image);
//
//		HoleFilling h = new HoleFilling(z,e,image, connectivityType);
//		h.fill();
//
//		System.out.println("the image after fill:");
//		for (int i = 0; i < image.rows(); i++) {
//			for (int j = 0; j < image.cols(); j++) {
//				if(mask.get(i,j)[0] == 1.0) {
//					System.out.println("(" + i +"," +j +") = "+ image.get(i, j)[0]);
//				}
//			}
//		}
//
//		String newfile = "C:/Users/owner/Desktop/imgesForTest/newfile.jpg"; 
//		imageCodecs.imwrite(newfile, image);
//
//		System.out.println("Image Loaded");

		//---- test with simple Mat: ----//
		//		int row = 0, col = 0;
		//		int data[] = {  3, -1, 0, -1, -1, -1, 2, 1, 0 };
		//		//allocate Mat before calling put
		//		Mat img = new Mat( 3, 3, CvType.CV_32S );
		//		img.put( row, col, data );
		//		img.convertTo(img, CvType.CV_32FC3, 1.0/255.0);
		//		System.out.println("simple Mat after normalize:");
		//		for (int i = 0; i < img.rows(); i++) {
		//			for (int j = 0; j < img.cols(); j++) {
		//				float value = (float) img.get(i, j)[0];
		//				if(value < 0) img.put(i, j, -1.0);
		//				System.out.println("(" + i +"," +j +") = "+ img.get(i, j)[0]);
		//			}
		//		}
		//		HoleFilling h = new HoleFilling(z,e,img, connectivityType);
		//		h.fill();
		//		
		//		System.out.println("simple Mat after filling:");
		//		for (int i = 0; i < img.rows(); i++) {
		//			for (int j = 0; j < img.cols(); j++) {
		//				System.out.println("(" + i +"," +j +") = "+ img.get(i, j)[0]);
		//			}
		//		}
//	}
	/**
	 * output an image to the specified path, according to the values in *** this.mat ***
	 * @param outputPath absolute path to output the image
	 */


}

