# HoleFilling

## To Run The Program:
- Make sure that the OpenCV library is installed
- Run the "CMDUtils" for command line utility, with the following inputs: 

      1. Input grayscale image, with -1 marking a missing color
         or RGB image with the addition of the fifth argument to mark the hole
         
      2. "Z" configuration - an integer
      
      3. "Epsilon" configuration -  a small float number  
      
      4. Connectivity type - Integer, 4 or 8 
      
      5. Optional - extra RGB mask-image to defines the hole.
         Required in case of input which is RGB image
         The missing pixels in the image are the pixels marked as 1 in the mask.
         
## About the implementation:

### Filler:
Abstract class to represent variation of image hole-filler algorithms.
Each filler algorithm must store the source image, a weighting  function and
connectivity-type definition, and to implement "fill()" method. 

### HoleFillerNaive
Extends Filler. Implement the basic algorithm.

### HoleFillerAprox
Extends Filler. Implement the approximation algorithm.

### WeightFunc
Interface to support difference weight function.

## DefaultWeight
Implement WeightFunc and store the default weighting function.

### HoleFinder
Algorithmic class that stores methods to find a hole in given image.  
