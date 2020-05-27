# HoleFilling

## To Run The Program:
- Make sure that the OpenCV library is installed
- Run the "HoleFillingApp" for command line utiliy, with the following inputs: 

      1. Input grayscale image, with -1 marking a missing color
         or RGB image with the addition of the fifth argument to mark the hole
         
      2. "Z" configuration - an integer
      
      3. "Epsilon" configuration -  a small float number  
      
      4. Connectivity type - Integer, 4 or 8 
      
      5. Optional - extra RGB mask-image to defines the hole.
         Required in case of input which is RGB image
         The missing pixels in the image are the pixels marked as 1 in the mask.
         
