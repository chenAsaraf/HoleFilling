package holeFilling;


import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class test {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		//Loading the OpenCV core library  
//		
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
//		
//		//Instantiating the imagecodecs class 
//		Imgcodecs imageCodecs = new Imgcodecs();
//
//		String file = "C:/Users/owner/Desktop/imgesForTest/img.jpg"; 
//		
//		Mat src = Imgcodecs.imread(file, Imgcodecs.IMREAD_GRAYSCALE);
//
//		Mat mask = new Mat(src.rows(), src.cols(), CvType.CV_8U, Scalar.all(0));
//		//mask.setTo( new Scalar( 1 ) );
//		// Drawing a Rectangle
//		Imgproc.rectangle (
//				mask,
//                new Point( src.rows() / 2, src.cols() / 2 ),
//                new Point( src.rows() / 4, src.cols() / 4 ),
//                new Scalar( 255, 255, 255 ),
//                -1  );
//		Mat cropped = new Mat();
//		
//		
//		src.copyTo( cropped, mask );
//		src.setTo(new Scalar(255,255,255),cropped);
//		String fileMask = "C:/Users/owner/Desktop/imgesForTest/mask.jpg"; 
//		String fileAfterMask = "C:/Users/owner/Desktop/imgesForTest/afterMask.jpg";
//		//Writing the image 
//		imageCodecs.imwrite(fileMask, mask); 
//		imageCodecs.imwrite(fileAfterMask, src);
//		System.out.println("Image Saved ............"); 
//		
//		System.out.println(src.type());
//	}

}
