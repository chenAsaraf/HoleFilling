package holeFilling;

public class Pixel {

	//add getters&setters&ttostring
	int row;
	int col;

	Pixel(int r, int c){
		row = r;
		col = c;
		}
	
	 @Override
	   public int hashCode() {
	      return row*col;
	   }



	// Overriding equals() to compare two Complex objects 
	@Override
	public boolean equals(Object o) { 

		// If the object is compared with itself then return true   
		if (o == this) { 
			return true; 
		} 

		/* Check if o is an instance of Pixel or not 
          "null instanceof [type]" also returns false */
		if (!(o instanceof Pixel)) { 
			return false; 
		} 

		// typecast o to Pixel so that we can compare data members  
		Pixel other = (Pixel) o; 

		// Compare the data members and return accordingly  
		return Integer.compare(row, other.row) == 0
				&& Integer.compare(col, other.col) == 0; 
	} 


	public Boolean equals(int other_r, int other_c) {
		return (row == other_r) && (col ==other_c);
	}

}
