package holeFilling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import org.opencv.core.Mat;

/***
 * Algorithmic class to find a hole in the image.
 */
public class HoleFinder{

	/**
	 * Initialize the 'hole' and 'borders' pixel's group.
	 * Definitions:
	 * 'hole' : set of all the missing pixels,		
	 * 'borders' : set of all pixels that connected to a hole pixel, but are not in the hole themselves
	 *  
	 * In order to reduce unnecessary pixel scanning in the image,
	 * the method operate in the following way:
	 * By assumption - the image has only a single hole and it connected.
	 * Therefore in the moment we found a missing pixel, we can search only in its surrounding pixels,
	 * to find other missing pixels. In the moments there is no new missing pixel to discover- we can stop.
	 * 
	 * @param input Mat
	 * @param hole Set<Pixel>
	 * @param borders Set<Pixel>
	 * @param connectType int
	 */
	public static void findHoleNBoundries(Mat input, Set<Pixel> hole, Set<Pixel> borders, int connectType) {
		Pixel first = findFirstHole(input);
		if(first.equals(-1,-1)) return; //No need to repaint 
		hole.add(first);
		Set<Pixel> visited = new HashSet(); //All visited pixels to avoid duplicate scanning
		Queue<Pixel> holeQue = new LinkedList<>(); //Pixels that in scanning process 
		holeQue.add(first);
		visited.add(first);
		while(!holeQue.isEmpty()) {
			Pixel current = holeQue.poll();
			for(Pixel n : neighbor(current,connectType,input.rows(), input.cols())) {
				if(!visited.contains(n)) {
					if (input.get(n.row(), n.col())[0] < 0) { //Neighbor is missing pixel 
						hole.add(n);
						holeQue.add(n);
						visited.add(n);
					}
					else { //Neighbor is colored pixel - so its in the border of the hole
						borders.add(n); 
						visited.add(n);
					}
				}
			}
		}
	}


	/**
	 * Initialize the 'hole' and 'inner-border' pixel's group.
	 * Definitions:
	 * 'hole' : set of all missing pixels, that are only surrounded by missing pixels		
	 * 'inner-border' : set of all missing pixels that connected to at least one painted pixel.
	 *  
	 * 
	 * @param input Mat
	 * @param hole Set<Pixel>
	 * @param borders Set<Pixel>
	 * @param connectType int
	 */
	public static void findHoleNInnerBound(Mat input, Set<Pixel> hole, Map<Pixel,List<Pixel>> innerBord, int connectType) {
		for (int i = 0; i < input.rows(); i++) {
			for (int j = 0; j < input.cols(); j++) {
				if(input.get(i, j)[0] < 0) { //if it's a missing pixel
					Pixel current = new Pixel(i,j);
					List<Pixel> colorNeig = colorNeighbor(current,connectType,input);
					if(colorNeig.size()>0) { //Current lying on the inner border
						innerBord.put(current, colorNeig);
					} else {
						hole.add(current);
					}
				}
			}
		}
	}

	/**
	 * Iterates over the image and find the first missing-pixel
	 * @return first found pixel with -1
	 */
	private static Pixel findFirstHole(Mat input) {
		for (int i = 0; i < input.rows(); i++) {
			for (int j = 0; j < input.cols(); j++) {
				if(input.get(i, j)[0] < 0) {
					return new Pixel(i,j);
				}
			}
		}
		return new Pixel(-1, -1); //no hole in the image
	}

	/**
	 * Output the neighborhood of the input pixel,
	 * each pixel neighborhood is exactly as defined by the connectivity type
	 * @param current Pixel
	 * @return neighbor ArrayList<Pixel> 
	 */
	public static ArrayList<Pixel> neighbor(Pixel current, int connectType, int height, int width) {
		ArrayList<Pixel> neig = new ArrayList<>();
		int r = current.row();
		int c = current.col();
		//First for each edge-connected pixel:
		int[][] indexes = {{r+1,c}, {r-1,c}, {r,c+1}, {r,c-1}};
		for (int i = 0; i < indexes.length; i++) {
			int newRowIndex = indexes[i][0];
			int newColIndex = indexes[i][1];
			Pixel borderPix = new Pixel(newRowIndex, newColIndex);
			if(!outOfImageBorders(borderPix, height, width)) {
				neig.add(borderPix);
			}
		}
		//Later if connectivity-type is 8: add the corners-pixels
		if(connectType == 8) {//Add corners
			int [][] cornerIdx = {{r+1,c+1}, {r-1,c-1}, {r+1,c-1}, {r-1,c+1}};
			for (int i = 0; i < cornerIdx.length; i++) {
				int newRowIndex = cornerIdx[i][0];
				int newColIndex = cornerIdx[i][1];
				Pixel borderPix = new Pixel(newRowIndex, newColIndex);
				if(!outOfImageBorders(borderPix, height, width)) {
					neig.add(borderPix);
				}
			}
		}
		return neig;
	}

	/**
	 * Find only colored neighborhood of the input pixel,
	 * @param current
	 * @param connectType
	 * @param input
	 * @return
	 */
	public static ArrayList<Pixel> colorNeighbor(Pixel current, int connectType, Mat input) {
		ArrayList<Pixel> neig = new ArrayList<>();
		int r = current.row();
		int c = current.col();
		int[][] indexes = {{r+1,c}, {r-1,c}, {r,c+1}, {r,c-1}};
		//First for each edge-connected pixel:
		for (int i = 0; i < indexes.length; i++) {
			int newRowIndex = indexes[i][0];
			int newColIndex = indexes[i][1];
			Pixel borderPix = new Pixel(newRowIndex, newColIndex);
			if(!outOfImageBorders(borderPix, input.rows(), input.width())) {
				if(input.get(newRowIndex, newColIndex)[0] > 0) //If the pixel colored
					neig.add(borderPix);
			}
		}
		//Later if connectivity-type is 8: add the corners-pixels
		if(connectType == 8) {
			int [][] cornerIdx = {{r+1,c+1}, {r-1,c-1}, {r+1,c-1}, {r-1,c+1}};
			for (int i = 0; i < cornerIdx.length; i++) {
				int newRowIndex = cornerIdx[i][0];
				int newColIndex = cornerIdx[i][1];
				Pixel borderPix = new Pixel(newRowIndex, newColIndex);
				if(!outOfImageBorders(borderPix, input.rows(), input.width())) {
					if(input.get(newRowIndex, newColIndex)[0] > 0) //If the pixel colored
						neig.add(borderPix);
				}
			}
		}
		return neig;
	}

	/**
	 * Find only missing neighborhood of the input pixel
	 * @param current
	 * @param connectType
	 * @param input
	 * @return
	 */
	public static ArrayList<Pixel> unColorNeighbor(Pixel current, int connectType, Mat input) {
		ArrayList<Pixel> neig = new ArrayList<>();
		int r = current.row();
		int c = current.col();
		int[][] indexes = {{r+1,c}, {r-1,c}, {r,c+1}, {r,c-1}};
		//First for each edge-connected pixel:
		for (int i = 0; i < indexes.length; i++) {
			int newRowIndex = indexes[i][0];
			int newColIndex = indexes[i][1];
			Pixel borderPix = new Pixel(newRowIndex, newColIndex);
			if(!outOfImageBorders(borderPix, input.rows(), input.width())) {
				if(input.get(newRowIndex, newColIndex)[0] < 0) //If the pixel colored
					neig.add(borderPix);
			}
		}
		//Later if connectivity-type is 8: add the corners-pixels
		if(connectType == 8) {
			int [][] cornerIdx = {{r+1,c+1}, {r-1,c-1}, {r+1,c-1}, {r-1,c+1}};
			for (int i = 0; i < cornerIdx.length; i++) {
				int newRowIndex = cornerIdx[i][0];
				int newColIndex = cornerIdx[i][1];
				Pixel borderPix = new Pixel(newRowIndex, newColIndex);
				if(!outOfImageBorders(borderPix, input.rows(), input.width())) {
					if(input.get(newRowIndex, newColIndex)[0] < 0) //If the pixel colored
						neig.add(borderPix);
				}
			}
		}
		return neig;
	}


	/**
	 * Checks whether the pixel index exceeds the image boundaries
	 * @param p Pixel
	 * @return Boolean
	 */
	private static Boolean outOfImageBorders(Pixel p, int height, int width) {
		return ( ((p.row()>height-1)) || ((p.col()>width-1)) || (p.row()<0) || (p.col()<0) );
	}

}
