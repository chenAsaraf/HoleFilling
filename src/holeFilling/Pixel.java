package holeFilling;

public class Pixel {
	/**
	 * Holds 2 integer to indicate coordinate in 2D image-space
	 */
	private int row;
	private int col;

	Pixel(int r, int c){
		row = r;
		col = c;
	}

	@Override
	public int hashCode() {
		return row*col;
	}

	@Override
	public String toString() {
		return "[ " + row + ", " + col + "]";
	}


	// Overriding equals() to compare two Pixels objects 
	@Override
	public boolean equals(Object o) { 
		if (o == this) { 
			return true; 
		}
		
		if (!(o instanceof Pixel)) { 
			return false; 
		} 
		
		Pixel other = (Pixel) o; 
		// Compare the data members and return accordingly  
		return Integer.compare(row, other.row) == 0
				&& Integer.compare(col, other.col) == 0; 
	} 


	public Boolean equals(int other_r, int other_c) {
		return (row == other_r) && (col ==other_c);
	}


	public int row() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int col() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}


}
